package com.ziamor.heavyrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziamor.heavyrunner.Runner;

public class GamePlayScreen implements Screen {
    Runner runner;
    SpriteBatch batch;
    AssetManager assetManager;
    Texture test;

    public GamePlayScreen(Runner runner) {
        this.runner = runner;
        this.batch = runner.batch;
        this.assetManager = runner.assetManager;

        test = assetManager.get("badlogic.jpg", Texture.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(test, 0, 0,100,100);
        batch.end();
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
