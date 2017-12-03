package com.ziamor.heavyrunner.screens;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ziamor.heavyrunner.Runner;
import com.ziamor.heavyrunner.components.*;
import com.ziamor.heavyrunner.systems.*;

public class GamePlayScreen implements Screen {
    public enum GameState {
        ACTIVE,
        PAUSED
    }

    GameState gameState;

    final Runner runner;
    SpriteBatch batch;
    ShapeRenderer shape;
    AssetManager assetManager;

    Stage stage;
    Table table;
    Skin skin;

    World world;
    WorldConfiguration config;

    InputMultiplexer inputMultiplexer;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;
    ComponentMapper<cPlayerController> playerControllerComponentMapper;
    ComponentMapper<cVelocity> velocityComponentMapper;
    ComponentMapper<cIgnoreGravity> ignoreGravityComponentMapper;
    ComponentMapper<cObstacle> obstacleComponentMapper;
    ComponentMapper<cSize> sizeComponentMapper;
    ComponentMapper<cAABB> aabbComponentMapper;
    ComponentMapper<cGroundCollider> groundColliderComponentMapper;
    ComponentMapper<cTimeSave> timeSaveComponentMapper;
    ComponentMapper<cParallaxBG> parallaxBGComponentMapper;

    public GamePlayScreen(final Runner runner) {
        this.runner = runner;
        this.batch = runner.batch;
        this.shape = runner.shape;
        this.assetManager = runner.assetManager;

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        config = new WorldConfigurationBuilder()
                .with(
                        new TagManager(),
                        // Render
                        new sRender(),
                        new sDirector(),
                        //Input
                        new sPlayerController(),
                        // Update position
                        new sParallaxBG(),
                        new sAABBUpdate(),
                        new sGroundColliderUpdate(),
                        new sCollisionDetection(),
                        new sGravity(),
                        new sObstacleController(),
                        new sPlayerMovement(),
                        // Time Stuff
                        new sTimeCreateSavePoints(),
                        new sRewindTime(),
                        // Util
                        new sDrawAABB(),
                        new sObstacleCleaner()
                        // new sObstacleDebug()
                )
                .build()
                .register(batch)
                .register(shape)
                .register(assetManager);

        world = new World(config);

        inputMultiplexer.addProcessor(world.getSystem(sPlayerController.class));

        positionComponentMapper = world.getMapper(cPosition.class);
        textureComponentMapper = world.getMapper(cTexture.class);
        playerControllerComponentMapper = world.getMapper(cPlayerController.class);
        velocityComponentMapper = world.getMapper(cVelocity.class);
        ignoreGravityComponentMapper = world.getMapper(cIgnoreGravity.class);
        obstacleComponentMapper = world.getMapper(cObstacle.class);
        sizeComponentMapper = world.getMapper(cSize.class);
        aabbComponentMapper = world.getMapper(cAABB.class);
        groundColliderComponentMapper = world.getMapper(cGroundCollider.class);
        timeSaveComponentMapper = world.getMapper(cTimeSave.class);
        parallaxBGComponentMapper = world.getMapper(cParallaxBG.class);

        int player = world.create();
        world.getSystem(TagManager.class).register("player", player);

        cPosition playerPos = positionComponentMapper.create(player);
        cTexture playerTexture = textureComponentMapper.create(player);
        cSize playerSize = sizeComponentMapper.create(player);
        cTimeSave timeSave = timeSaveComponentMapper.create(player);
        cVelocity playerVel = velocityComponentMapper.create(player);

        playerControllerComponentMapper.create(player);
        aabbComponentMapper.create(player);
        groundColliderComponentMapper.create(player);
        playerPos.x = 0;
        playerPos.y = Gdx.graphics.getHeight() / 2 + 32;
        playerPos.z = 6;
        playerTexture.texture = assetManager.get("player.png", Texture.class);
        playerSize.width = playerTexture.texture.getWidth();
        playerSize.height = playerTexture.texture.getHeight();

        timeSave.init(240, playerPos, playerVel);

        createParallax(0);
        createParallax(assetManager.get("clouds1.png", Texture.class).getWidth());
        createParallax(assetManager.get("clouds1.png", Texture.class).getWidth() * -1f);

        skin = assetManager.get("skin.json", Skin.class);
        stage = new Stage();
        inputMultiplexer.addProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final TextButton btnStart = new TextButton("Continue", skin, "default");

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameState = GameState.ACTIVE;
            }
        });

        table.add(btnStart).width(250).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);

        final TextButton btnMainMenu = new TextButton("Main Menu", skin, "default");

        btnMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runner.setScreen(new MainMenuScreen(runner));
                dispose();
            }
        });

        table.add(btnMainMenu).width(250).fillX().uniformX();

        gameState = GameState.ACTIVE;
    }

    private void createParallax(float offset) {
        int clouds1 = world.create();
        positionComponentMapper.create(clouds1).set(offset, 0, 0);
        textureComponentMapper.create(clouds1).texture = assetManager.get("clouds1.png", Texture.class);
        parallaxBGComponentMapper.create(clouds1).scrollFactor = 0.2f;

        int mountains = world.create();
        positionComponentMapper.create(mountains).set(offset, 0, 1);
        textureComponentMapper.create(mountains).texture = assetManager.get("mountains.png", Texture.class);
        parallaxBGComponentMapper.create(mountains).scrollFactor = 0.4f;

        int clouds2 = world.create();
        positionComponentMapper.create(clouds2).set(offset, 0, 2);
        textureComponentMapper.create(clouds2).texture = assetManager.get("clouds2.png", Texture.class);
        parallaxBGComponentMapper.create(clouds2).scrollFactor = 0.6f;

        int grass = world.create();
        positionComponentMapper.create(grass).set(offset, 0, 3);
        textureComponentMapper.create(grass).texture = assetManager.get("grass.png", Texture.class);
        parallaxBGComponentMapper.create(grass).scrollFactor = 0.7f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameState == GameState.ACTIVE) {
            gameState = GameState.PAUSED;
        }

        if (gameState == GameState.ACTIVE) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            world.setDelta(delta);
            world.process();
        } else if (gameState == GameState.PAUSED) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
