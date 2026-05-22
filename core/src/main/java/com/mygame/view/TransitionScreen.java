package com.mygame.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.controller.MovementController;
import com.mygame.model.Hero;

public class TransitionScreen implements Screen {

    public interface TransitionListener {
        void onTransitionComplete();
    }

    private static final float CANVAS_WIDTH = 1253f;
    private static final float CANVAS_HEIGHT = 752f;

    private static final float PORTAL_W = 200f;
    private static final float PORTAL_H = 300f;
    private static final float PORTAL_X = CANVAS_WIDTH - 300f;
    private static final float PORTAL_Y = CANVAS_HEIGHT / 2f - PORTAL_H / 2f;

    private static final float HERO_W = 150f;
    private static final float HERO_H = 200f;

    private TransitionListener listener;
    private Hero hero;
    private boolean nearPortal = false;
    private float portalTimer = 0f;
    private static final float PORTAL_DELAY = 1.5f;

    private SpriteBatch batch;
    private Texture background;
    private Texture heroTexture;
    private Texture portalTexture;
    private BitmapFont font;
    private GlyphLayout layout;
    private OrthographicCamera camera;
    private Viewport viewport;
    private MovementController movement;

    public TransitionScreen(Hero hero, Texture heroTexture, TransitionListener listener) {
        this.hero = hero;
        this.heroTexture = heroTexture;
        this.listener = listener;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("backgrounds/mainroom.PNG");
        portalTexture = new Texture("ui/portal.png");

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        layout = new GlyphLayout();

        movement = new MovementController(100f, CANVAS_HEIGHT / 2f - HERO_H / 2f);

        camera = new OrthographicCamera();
        viewport = new StretchViewport(CANVAS_WIDTH, CANVAS_HEIGHT, camera);
        viewport.apply();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        movement.update(delta);

        float heroCenterX = movement.getX() + HERO_W / 2f;
        float portalCenterX = PORTAL_X + PORTAL_W / 2f;
        float distance = Math.abs(heroCenterX - portalCenterX);

        if (distance < 100f) {
            if (!nearPortal) {
                nearPortal = true;
                portalTimer = 0f;
            }
            portalTimer += delta;
            if (portalTimer >= PORTAL_DELAY) {
                listener.onTransitionComplete();
                return;
            }
        } else {
            nearPortal = false;
            portalTimer = 0f;
        }

        batch.begin();
        batch.draw(background, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        batch.draw(portalTexture, PORTAL_X, PORTAL_Y, PORTAL_W, PORTAL_H);
        batch.draw(heroTexture, movement.getX(), movement.getY(), HERO_W, HERO_H);

        if (nearPortal) {
            String hint = "Entering...";
            layout.setText(font, hint);
            font.draw(batch, hint, (CANVAS_WIDTH - layout.width) / 2f, CANVAS_HEIGHT - 50f);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        portalTexture.dispose();
        font.dispose();
    }
}
