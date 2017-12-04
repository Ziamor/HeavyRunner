package com.ziamor.heavyrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ziamor.heavyrunner.Runner;

import java.text.DecimalFormat;

public class GameOverScreen implements Screen {
    final Runner runner;

    AssetManager assetManager;

    Stage stage;
    Table table;
    Skin skin;

    public GameOverScreen(final Runner runner, float score) {
        this.runner = runner;
        this.assetManager = runner.assetManager;

        skin = assetManager.get("trSkin.json", Skin.class);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        final Label lblScore = new Label("Time Survived: " + df.format(score) + "s", skin);
        lblScore.setAlignment(Align.center);
        table.add(lblScore).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);

        final TextButton btnStart = new TextButton("New Game", skin, "default");

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runner.selectSound.play(0.75f);
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
                runner.selectSound.play(0.75f);
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
        Gdx.gl.glClearColor(48f/255f, 36f/255f, 66f/255f, 1);
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
