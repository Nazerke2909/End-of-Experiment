package com.mygame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class FirstScreen implements Screen {
    private SpriteBatch backgroundBatch;
    private Texture background;

    private Stage stage;
    private Texture playTexture;
    private Texture playHoverTexture;

    private static final float BUTTON_WIDTH = 225f;
    private static final float BUTTON_HEIGHT = 112f;

    private static final float WORLD_WIDTH = 800f;
    private static final float WORLD_HEIGHT = 600f;

    @Override
    public void show() {
        backgroundBatch = new SpriteBatch();
        background = new Texture("backgrounds/startroom.png");

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        playTexture = new Texture("ui/play.png");
        playHoverTexture = new Texture("ui/mouseoverplay.PNG");

        TextureRegionDrawable playUp = new TextureRegionDrawable(new TextureRegion(playTexture));
        TextureRegionDrawable playOver = new TextureRegionDrawable(new TextureRegion(playHoverTexture));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = playUp;
        style.imageOver = playOver;

        ImageButton playButton = new ImageButton(style);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(playButton).size(BUTTON_WIDTH, BUTTON_HEIGHT);

        stage.addActor(table);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        backgroundBatch.begin();
        backgroundBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundBatch.end();

        stage.getViewport().apply();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;

        Matrix4 matrix = new Matrix4();
        matrix.setToOrtho2D(0, 0, width, height);
        backgroundBatch.setProjectionMatrix(matrix);

        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        backgroundBatch.dispose();
        background.dispose();
        stage.dispose();
        playTexture.dispose();
        playHoverTexture.dispose();
    }
}
