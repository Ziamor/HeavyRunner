package com.ziamor.heavyrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ziamor.heavyrunner.Runner;

public class MainMenuScreen implements Screen {
    final Runner runner;

    AssetManager assetManager;

    Stage stage;
    Table table;
    Skin skin;

    public MainMenuScreen(final Runner runner) {
        this.runner = runner;
        this.assetManager = runner.assetManager;

        skin = assetManager.get("skin.json", Skin.class);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final TextButton btnStart = new TextButton("Start", skin, "default");

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runner.setScreen(new GamePlayScreen(runner));
                dispose();
            }
        });

        table.add(btnStart).width(250).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        final TextButton btnExit = new TextButton("Exit", skin, "default");

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                dispose();
            }
        });

        table.add(btnExit).fillX().uniformX();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
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
