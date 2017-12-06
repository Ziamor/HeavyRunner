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

public class ControlScreen implements Screen {
    final Runner runner;

    AssetManager assetManager;

    Stage stage;
    Table table;
    Skin skin;

    public ControlScreen(final Runner runner) {
        this.runner = runner;

        this.assetManager = runner.assetManager;

        skin = assetManager.get("trSkin.json", Skin.class);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final Label lblTip = new Label("The controls are simple. Uses WADS to move around and press Q to rewind time. But be careful, " +
                "\nrewinding time increases your desync causing the world around you to speed up.", skin);
        lblTip.setWidth(100);
        lblTip.setAlignment(Align.center);
        table.add(lblTip).fillX().uniformX();
        table.row().pad(50, 0, 50, 0);

        final TextButton btnStart = new TextButton("Main Menu", skin, "default");

        btnStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runner.selectSound.play(0.75f);
                runner.setScreen(new MainMenuScreen(runner));
                dispose();
            }
        });

        table.add(btnStart).width(150).fillX().uniformX();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(48f / 255f, 36f / 255f, 66f / 255f, 1);
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
