package com.ziamor.heavyrunner;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ziamor.heavyrunner.screens.GamePlayScreen;
import com.ziamor.heavyrunner.screens.MainMenuScreen;

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
        assetManager.load("skin.json", Skin.class);
        assetManager.load("trSkin.json", Skin.class);
        assetManager.load("player.png", Texture.class);
        assetManager.load("pl1.png", Texture.class);
        assetManager.load("pr1.png", Texture.class);
        assetManager.load("pl2.png", Texture.class);
        assetManager.load("pr2.png", Texture.class);
        assetManager.load("pm1.png", Texture.class);
        assetManager.load("pm2.png", Texture.class);
        assetManager.load("pm3.png", Texture.class);
        assetManager.load("pm4.png", Texture.class);
        //assetManager.load("clouds1.png", Texture.class);
        //assetManager.load("clouds2.png", Texture.class);
        //assetManager.load("grass.png", Texture.class);
        //assetManager.load("mountains.png", Texture.class);
        assetManager.load("farTrees_BG.png", Texture.class);
        assetManager.load("closeTrees_BG.png", Texture.class);

        assetManager.finishLoading();
        this.setScreen(new MainMenuScreen(this));
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
