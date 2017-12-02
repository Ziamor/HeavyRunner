package com.ziamor.heavyrunner.screens;

import com.artemis.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziamor.heavyrunner.Runner;
import com.ziamor.heavyrunner.components.*;
import com.ziamor.heavyrunner.systems.*;

public class GamePlayScreen implements Screen {
    Runner runner;
    SpriteBatch batch;
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

    public GamePlayScreen(Runner runner) {
        this.runner = runner;
        this.batch = runner.batch;
        this.assetManager = runner.assetManager;

        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        config = new WorldConfigurationBuilder()
                .with(
                        new sRender(),
                        new sDirector(),
                        new sPlayerController(),
                        new sGravity(),
                        new sObstacleController(),
                        new sMovement()
                )
                .build()
                .register(batch)
                .register(assetManager);

        world = new World(config);

        inputMultiplexer.addProcessor(world.getSystem(sPlayerController.class));

        positionComponentMapper = world.getMapper(cPosition.class);
        textureComponentMapper = world.getMapper(cTexture.class);
        playerControllerComponentMapper = world.getMapper(cPlayerController.class);
        velocityComponentMapper = world.getMapper(cVelocity.class);
        ignoreGravityComponentMapper = world.getMapper(cIgnoreGravity.class);
        obstacleComponentMapper = world.getMapper(cObstacle.class);

        int player = world.create();
        cPosition playerPos = positionComponentMapper.create(player);
        cTexture playerTexture = textureComponentMapper.create(player);
        velocityComponentMapper.create(player);
        playerControllerComponentMapper.create(player);

        playerPos.x = 0;
        playerPos.y = 0;
        playerTexture.texture = assetManager.get("player.png", Texture.class);
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
