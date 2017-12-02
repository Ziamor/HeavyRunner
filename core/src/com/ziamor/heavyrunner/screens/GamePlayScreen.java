package com.ziamor.heavyrunner.screens;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziamor.heavyrunner.Runner;
import com.ziamor.heavyrunner.components.cPosition;
import com.ziamor.heavyrunner.components.cTexture;
import com.ziamor.heavyrunner.systems.RenderSystem;

public class GamePlayScreen implements Screen {
    Runner runner;
    SpriteBatch batch;
    AssetManager assetManager;

    World world;
    WorldConfiguration config;

    ComponentMapper<cPosition> positionComponentMapper;
    ComponentMapper<cTexture> textureComponentMapper;

    public GamePlayScreen(Runner runner) {
        this.runner = runner;
        this.batch = runner.batch;
        this.assetManager = runner.assetManager;

        config = new WorldConfigurationBuilder()
                .with(new RenderSystem())
                .build()
                .register(batch);
        world = new World(config);

        positionComponentMapper = world.getMapper(cPosition.class);
        textureComponentMapper = world.getMapper(cTexture.class);

        int player = world.create();
        cPosition playerPos = positionComponentMapper.create(player);
        cTexture playerTexture = textureComponentMapper.create(player);

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
