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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

    public final Runner runner;
    SpriteBatch batch;
    ShapeRenderer shape;
    AssetManager assetManager;

    Stage pauseStage;
    Table pauseTable;
    Stage uiStage;
    Table uiTable;
    ProgressBar timeProgressBar, timeDeSyncProgressBar;
    Label lblScore;

    Skin skin;

    World world;
    WorldConfiguration config;

    InputMultiplexer inputMultiplexer;

    sDrawAABB drawAABB;

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
    ComponentMapper<cAcceleration> accelerationComponentMapper;
    ComponentMapper<cTextureRegion> textureRegionComponentMapper;
    ComponentMapper<cPlayerAnimation> playerAnimationComponentMapper;
    ComponentMapper<cStaticShader> staticShaderComponentMapper;

    public boolean gameover = false;
    public float finalScore = 0;

    public boolean rev = false;

    public GamePlayScreen(final Runner runner) {
        this.runner = runner;
        this.batch = runner.batch;
        this.shape = runner.shape;
        this.assetManager = runner.assetManager;

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);


        skin = assetManager.get("skin.json", Skin.class);

        createPauseUI();
        createUI();

        config = new WorldConfigurationBuilder()
                .with(
                        new TagManager(),
                        new sDead(this),
                        // Render
                        new sPlayerAnimationController(),
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
                        new sRewindTime(timeProgressBar, timeDeSyncProgressBar, lblScore),
                        // Util
                        new sDrawAABB(),
                        new sObstacleCleaner()
                        // new sObstacleDebug()
                )
                .build()
                .register(batch)
                .register(shape)
                .register(assetManager)
                .register(runner);

        world = new World(config);

        drawAABB = world.getSystem(sDrawAABB.class);
        drawAABB.setEnabled(false);

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
        accelerationComponentMapper = world.getMapper(cAcceleration.class);
        textureRegionComponentMapper = world.getMapper(cTextureRegion.class);
        playerAnimationComponentMapper = world.getMapper(cPlayerAnimation.class);
        staticShaderComponentMapper = world.getMapper(cStaticShader.class);

        int player = world.create();
        world.getSystem(TagManager.class).register("player", player);

        cPosition playerPos = positionComponentMapper.create(player);
        cTextureRegion playerTexture = textureRegionComponentMapper.create(player);
        cSize playerSize = sizeComponentMapper.create(player);
        cTimeSave timeSave = timeSaveComponentMapper.create(player);
        cVelocity playerVel = velocityComponentMapper.create(player);

        //staticShaderComponentMapper.create(player);
        accelerationComponentMapper.create(player);
        playerControllerComponentMapper.create(player);
        aabbComponentMapper.create(player);
        groundColliderComponentMapper.create(player);
        playerAnimationComponentMapper.create(player);

        playerPos.x = 0;
        playerPos.y = Gdx.graphics.getHeight() / 2 + 32;
        playerPos.z = 6;
        playerTexture.textureRegion = new TextureRegion(assetManager.get("player.png", Texture.class));
        playerSize.width = 32;
        playerSize.height = 64;

        timeSave.init(240, playerPos, playerVel);

        createParallax(0);
        createParallax(assetManager.get("farTrees_BG.png", Texture.class).getWidth());
        createParallax(assetManager.get("farTrees_BG.png", Texture.class).getWidth() * -1f);

        gameState = GameState.ACTIVE;
    }

    private void createParallax(float offset) {
        int mountains = world.create();
        positionComponentMapper.create(mountains).set(offset, 0, 1);
        textureComponentMapper.create(mountains).texture = assetManager.get("farTrees_BG.png", Texture.class);
        parallaxBGComponentMapper.create(mountains).scrollFactor = 0.4f;

        int clouds2 = world.create();
        positionComponentMapper.create(clouds2).set(offset, 0, 2);
        textureComponentMapper.create(clouds2).texture = assetManager.get("closeTrees_BG.png", Texture.class);
        parallaxBGComponentMapper.create(clouds2).scrollFactor = 0.6f;
    }

    protected void createUI() {
        uiStage = new Stage();
        inputMultiplexer.addProcessor(uiStage);

        uiTable = new Table();
        uiTable.setFillParent(true);
        uiTable.top();
        uiTable.left();
        uiStage.addActor(uiTable);

        timeProgressBar = new ProgressBar(0, 1f, 0.01f, false, skin);
        uiTable.add(new Label("Time Bank:", skin));
        uiTable.add(timeProgressBar);

        lblScore = new Label("Score: 0", skin);
        uiTable.add().expandX();
        uiTable.add(lblScore).right();

        uiTable.row();

        timeDeSyncProgressBar = new ProgressBar(0, 1f, 0.01f, false, skin);
        uiTable.add(new Label("Time DeSync:", skin));
        uiTable.add(timeDeSyncProgressBar);
    }

    protected void createPauseUI() {
        pauseStage = new Stage();
        inputMultiplexer.addProcessor(pauseStage);

        pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseStage.addActor(pauseTable);

        final TextButton btnStart = new TextButton("Continue", skin, "default");

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runner.selectSound.play(0.75f);
                gameState = GameState.ACTIVE;
            }
        });

        pauseTable.add(btnStart).width(250).fillX().uniformX();
        pauseTable.row().pad(10, 0, 10, 0);

        final TextButton btnMainMenu = new TextButton("Main Menu", skin, "default");

        btnMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runner.selectSound.play(0.75f);
                runner.setScreen(new MainMenuScreen(runner));
                dispose();
            }
        });

        pauseTable.add(btnMainMenu).width(250).fillX().uniformX();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (rev && runner.enableMusic) {
            if (!Gdx.input.isKeyPressed(Input.Keys.Q)) {
                rev = false;
                this.runner.bgMusicRev.pause();
                this.runner.bgMusic.setPosition(this.runner.bgMusicRev.getPosition());
                this.runner.bgMusic.play();
            }
        } else if (runner.enableMusic && Gdx.input.isKeyPressed(Input.Keys.Q)) {
            rev = true;
            this.runner.bgMusic.pause();
            this.runner.bgMusicRev.setPosition(this.runner.bgMusic.getPosition());
            this.runner.bgMusicRev.play();
        }

        if (gameover) {
            runner.setScreen(new GameOverScreen(runner, finalScore));
            dispose();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameState == GameState.ACTIVE) {
                gameState = GameState.PAUSED;
            }

            if (gameState == GameState.ACTIVE) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH))
                    drawAABB.setEnabled(!drawAABB.isEnabled());
                Gdx.gl.glClearColor(186f / 255f, 230f / 255f, 214f / 255f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                world.setDelta(delta);
                world.process();
                uiStage.act(delta);
                uiStage.draw();
            } else if (gameState == GameState.PAUSED) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                pauseStage.act(delta);
                pauseStage.draw();
            }
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
