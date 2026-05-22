package com.mygame.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.controller.UpgradeController;
import com.mygame.model.Hero;
import com.mygame.model.LevelData;

public class LevelTransitionScreen implements Screen {

    public interface TransitionListener {
        void onNextLevel();
        void onReturnToHub();
    }

    private static final float CANVAS_WIDTH = 1253f;
    private static final float CANVAS_HEIGHT = 752f;

    private TransitionListener listener;
    private LevelData levelData;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private HubRenderer hubRenderer;
    private HeroProfileUI heroProfileUI;
    private Hero[] heroes;
    private int openedHeroIndex = -1;
    private boolean showProfile = false;

    private Texture levelBackground;
    private Texture deerStanding;
    private Texture deerWalk1;
    private Texture deerWalk2;
    private Texture portal;

    private float deerX = 141.4f;
    private float deerY = 248.5f;
    private static final float DEER_WIDTH = 152.6f;
    private static final float DEER_HEIGHT = 255.1f;
    private static final float DEER_SPEED = 200f;
    private boolean deerFacingRight = true;

    private static final float PORTAL_X = 939.8f;
    private static final float PORTAL_Y = 150.3f;
    private static final float PORTAL_WIDTH = 379.3f;
    private static final float PORTAL_HEIGHT = 379.3f;
    private static final float PORTAL_ROTATION = -5.6f;

    private float animationTimer = 0f;
    private static final float ANIMATION_SPEED = 0.3f;
    private boolean isWalking = false;

    private static final float TOP_MARGIN = 55f;

    private static final float[] AVATAR_W = {467.6f, 465.7f, 459.7f};
    private static final float[] AVATAR_H = {280.4f, 279.2f, 275.6f};
    private static final float[] AVATAR_X = {-1.9f, 0f, 0f};
    private static final float[] AVATAR_Y_CANVA = {22.8f, 197.1f, 368.8f};

    private HeroProfileUI.UpgradeRect upgradeRect = null;

    private BitmapFont font;
    private GlyphLayout layout;

    public LevelTransitionScreen(TransitionListener listener, LevelData levelData, Hero[] heroes) {
        this.listener = listener;
        this.levelData = levelData;
        this.heroes = heroes;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(CANVAS_WIDTH, CANVAS_HEIGHT, camera);
        viewport.apply();

        font = new BitmapFont();
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        font.getData().setScale(1.5f);
        layout = new GlyphLayout();

        hubRenderer = new HubRenderer(font, layout);
        hubRenderer.create();

        heroProfileUI = new HeroProfileUI();
        heroProfileUI.create();

        levelBackground = new Texture(levelData.getBackgroundPath());

        deerStanding = new Texture("characters/deer.png");
        deerWalk1 = new Texture("characters/walk1.PNG");
        deerWalk2 = new Texture("characters/walk2.PNG");
        portal = new Texture("ui/portal.PNG");

        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (openedHeroIndex < 0 || openedHeroIndex >= heroes.length) {
            showProfile = false;
            openedHeroIndex = -1;
        }

        if (showProfile) {
            handleProfileInput();

            if (!showProfile) {
            } else {
                batch.begin();
                batch.draw(levelBackground, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                heroProfileUI.draw(batch, heroes[openedHeroIndex], openedHeroIndex);
                batch.end();
                return; 
            }
        }
        if (!showProfile) {
            handleWalkingInput();
            updateDeerPosition(delta);
            checkPortalCollision();
            handleHubClick();

            batch.begin();
            batch.draw(levelBackground, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            drawPortal();
            drawDeer();
            hubRenderer.drawAvatarsOnly(batch); 
            batch.end();
        }
    }

    private void drawPortal() {
        batch.draw(portal, PORTAL_X, PORTAL_Y, PORTAL_WIDTH / 2f, PORTAL_HEIGHT / 2f,
                   PORTAL_WIDTH, PORTAL_HEIGHT, 1, 1, PORTAL_ROTATION, 0, 0,
                   portal.getWidth(), portal.getHeight(), false, true);
    }

    private void drawDeer() {
        Texture currentTexture;
        if (isWalking) {
            animationTimer += Gdx.graphics.getDeltaTime();
            if (animationTimer > ANIMATION_SPEED) {
                animationTimer -= ANIMATION_SPEED;
            }
            currentTexture = (animationTimer < ANIMATION_SPEED / 2f) ? deerWalk1 : deerWalk2;
        } else {
            currentTexture = deerStanding;
            animationTimer = 0f;
        }

        float originX = DEER_WIDTH / 2f;
        float originY = 0;
        float scaleX = deerFacingRight ? 1f : -1f;

        batch.draw(currentTexture,
                   deerX + DEER_WIDTH / 2f, deerY,
                   originX, originY,
                   DEER_WIDTH, DEER_HEIGHT,
                   scaleX, 1,
                   0, 0, 0,
                   currentTexture.getWidth(), currentTexture.getHeight(),
                   false, false);
    }

    private void handleWalkingInput() {
        isWalking = false;

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            deerY += DEER_SPEED * Gdx.graphics.getDeltaTime();
            isWalking = true;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            deerX -= DEER_SPEED * Gdx.graphics.getDeltaTime();
            deerFacingRight = false;
            isWalking = true;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            deerY -= DEER_SPEED * Gdx.graphics.getDeltaTime();
            isWalking = true;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            deerX += DEER_SPEED * Gdx.graphics.getDeltaTime();
            deerFacingRight = true;
            isWalking = true;
        }
    }

    private void updateDeerPosition(float delta) {
        if (deerY + DEER_HEIGHT > CANVAS_HEIGHT - TOP_MARGIN) {
            deerY = CANVAS_HEIGHT - DEER_HEIGHT - TOP_MARGIN;
        }
        if (deerX < 0) deerX = 0;
        if (deerX + DEER_WIDTH > CANVAS_WIDTH) deerX = CANVAS_WIDTH - DEER_WIDTH;
        if (deerY < 0) deerY = 0;
    }

    private void checkPortalCollision() {
        Rectangle deerRect = new Rectangle(deerX, deerY, DEER_WIDTH, DEER_HEIGHT);
        Rectangle portalRect = new Rectangle(PORTAL_X, PORTAL_Y, PORTAL_WIDTH, PORTAL_HEIGHT);

        if (deerRect.overlaps(portalRect)) {
            listener.onNextLevel();
        }
    }

    private void handleHubClick() {
        if (!Gdx.input.justTouched()) return;

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touch);

        for (int i = 0; i < 3; i++) {
            float y = CANVAS_HEIGHT - AVATAR_Y_CANVA[i] - AVATAR_H[i];
            if (touch.x >= AVATAR_X[i] && touch.x <= AVATAR_X[i] + AVATAR_W[i] &&
                touch.y >= y && touch.y <= y + AVATAR_H[i]) {
                openedHeroIndex = i;
                showProfile = true;
                upgradeRect = heroProfileUI.getUpgradeRect();
                return;
            }
        }

        float BACK_BTN_WIDTH = 120f;
        float BACK_BTN_HEIGHT = 45f;
        float BACK_BTN_X = 20f;
        float BACK_BTN_Y = 20f;
        if (touch.x >= BACK_BTN_X && touch.x <= BACK_BTN_X + BACK_BTN_WIDTH &&
            touch.y >= BACK_BTN_Y && touch.y <= BACK_BTN_Y + BACK_BTN_HEIGHT) {
            listener.onReturnToHub();
            return;
        }
    }

    private void handleProfileInput() {
        if (!Gdx.input.justTouched()) return;

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touch);

        if (upgradeRect != null) {
            if (touch.x >= upgradeRect.x && touch.x <= upgradeRect.x + upgradeRect.width &&
                touch.y >= upgradeRect.y && touch.y <= upgradeRect.y + upgradeRect.height) {
                Hero hero = heroes[openedHeroIndex];
                boolean success = UpgradeController.tryUpgrade(hero);
                if (success) {
                    System.out.println("Upgraded hero " + hero.getName() + " to level " + hero.getLevel() + "!");
                } else {
                    if (hero.isMaxLevel()) {
                        System.out.println("Hero " + hero.getName() + " is already at max level!");
                    } else {
                        System.out.println("Not enough pills!");
                    }
                }
                return;
            }
        }

        showProfile = false;
        openedHeroIndex = -1;
        upgradeRect = null;
    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        if (hubRenderer != null) hubRenderer.dispose();
        if (heroProfileUI != null) heroProfileUI.dispose();
        if (levelBackground != null) levelBackground.dispose();
        deerStanding.dispose();
        deerWalk1.dispose();
        deerWalk2.dispose();
        portal.dispose();
        if (font != null) font.dispose();
    }
}