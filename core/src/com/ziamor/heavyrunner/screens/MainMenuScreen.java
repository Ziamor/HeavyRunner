package com.ziamor.heavyrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    Texture front, back;

    float time = 0;

    public MainMenuScreen(final Runner runner) {
        this.runner = runner;
        this.assetManager = runner.assetManager;

        skin = assetManager.get("trSkin.json", Skin.class);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        back = assetManager.get("Title_back.png", Texture.class);
        front = assetManager.get("Title_front.png", Texture.class);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.row().pad(10, 0, 10, 0);

        final TextButton btnStart = new TextButton("Start", skin, "default");

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runner.selectSound.play(0.75f);
                if (runner.batch.isDrawing())
                    runner.batch.end();
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
                runner.selectSound.play(0.25f);
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
        time += delta;
        float scale = 2f;
        float size = 0.5f * (float) Math.sin(time) / 2 + 1 - 0.25f;
        Gdx.gl.glClearColor(48f / 255f, 36f / 255f, 66f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        runner.batch.begin();
        float x = (Gdx.graphics.getWidth() - front.getWidth() * scale) / 2;
        float y = 300;

        runner.batch.draw(back, x, y, front.getWidth() * scale, front.getHeight() * scale);
        runner.batch.draw(front, x + Math.abs(front.getWidth()* scale - front.getWidth() * size* scale) / 2, y + Math.abs(front.getHeight()* scale - front.getHeight() * size* scale) / 2, front.getWidth() * size * scale, front.getHeight() * size * scale);
        runner.batch.end();
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
