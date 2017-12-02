package com.ziamor.heavyrunner;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ziamor.heavyrunner.screens.GamePlayScreen;

public class Runner extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shape;
    public AssetManager assetManager;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        assetManager = new AssetManager();
        assetManager.load("player.png", Texture.class);
        assetManager.load("wall.png", Texture.class);
        assetManager.finishLoading();
        this.setScreen(new GamePlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        assetManager.dispose();
    }
}
