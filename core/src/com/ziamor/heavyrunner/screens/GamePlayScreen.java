package com.ziamor.heavyrunner.screens;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ziamor.heavyrunner.Runner;
import com.ziamor.heavyrunner.components.*;
import com.ziamor.heavyrunner.systems.*;

public class GamePlayScreen implements Screen {
    Runner runner;
    SpriteBatch batch;
    ShapeRenderer shape;
    AssetManager assetManager;

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

    public GamePlayScreen(Runner runner) {
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
                        new sAABBUpdate(),
                        new sGroundColliderUpdate(),
                        new sCollisionDetection(),
                        new sGravity(),
                        new sObstacleController(),
                        new sMovement(),
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
        playerPos.y = 0;
        playerTexture.texture = assetManager.get("player.png", Texture.class);
        playerSize.width = playerTexture.texture.getWidth();
        playerSize.height = playerTexture.texture.getHeight();

        timeSave.init(240,playerPos, playerVel);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.setDelta(delta);
        world.process();
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
