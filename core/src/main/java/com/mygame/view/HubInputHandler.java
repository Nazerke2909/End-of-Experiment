package com.mygame.view;
import  com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.controller.UpgradeController;
import com.mygame.model.Hero;
import com.mygame.model.PlayerProgress;

public class HubInputHandler {

    public interface NavigationListener {
        void goToStartScreen();
        void goToLevel(int levelNumber);
    }

    private static final float CANVAS_HEIGHT = 752f;

    private static final float[] AVATAR_W = {467.6f, 465.7f, 459.7f};
    private static final float[] AVATAR_H = {280.4f, 279.2f, 275.6f};
    private static final float[] AVATAR_X = {-1.9f, 0f, 0f};
    private static final float[] AVATAR_Y_CANVA = {22.8f, 197.1f, 368.8f};

    private static final float[] FLASK_W = {246.3f, 239.8f, 239.8f};
    private static final float[] FLASK_H = {134.1f, 130.6f, 130.6f};
    private static final float[] FLASK_X = {534.8f, 538.0f, 538.0f};
    private static final float[] FLASK_Y_CANVA = {49.6f, 183.8f, 310.7f};

    private static final float BACK_BTN_WIDTH = 120f;
    private static final float BACK_BTN_HEIGHT = 45f;
    private static final float BACK_BTN_X = 20f;
    private static final float BACK_BTN_Y = 20f;

    private static final int LEVELS_COUNT = 3;

    private NavigationListener navListener;
    private Hero[] heroes;
    private Viewport viewport;

    public HubInputHandler(NavigationListener navListener, Hero[] heroes, Viewport viewport) {
        this.navListener = navListener;
        this.heroes = heroes;
        this.viewport = viewport;
    }

    public ClickResult handleTouch() {
        if (!Gdx.input.justTouched()) return new ClickResult(ClickType.NONE, 0);

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touch);

        if (touch.x >= BACK_BTN_X && touch.x <= BACK_BTN_X + BACK_BTN_WIDTH &&
            touch.y >= BACK_BTN_Y && touch.y <= BACK_BTN_Y + BACK_BTN_HEIGHT) {
            return new ClickResult(ClickType.BACK, 0);
        }

        for (int i = 0; i < 3; i++) {
            float y = CANVAS_HEIGHT - AVATAR_Y_CANVA[i] - AVATAR_H[i];
            if (touch.x >= AVATAR_X[i] && touch.x <= AVATAR_X[i] + AVATAR_W[i] &&
                touch.y >= y && touch.y <= y + AVATAR_H[i]) {
                return new ClickResult(ClickType.HERO, i);
            }
        }

        PlayerProgress progress = PlayerProgress.getInstance();
        for (int i = 0; i < LEVELS_COUNT; i++) {
            float y = CANVAS_HEIGHT - FLASK_Y_CANVA[i] - FLASK_H[i];
            float x1 = FLASK_X[i];
            float x2 = FLASK_X[i] + FLASK_W[i];
            float y1 = y;
            float y2 = y + FLASK_H[i];
            boolean inX = touch.x >= x1 && touch.x <= x2;
            boolean inY = touch.y >= y1 && touch.y <= y2;
            if (inX && inY) {
                int levelNumber = i + 1;
                if (!progress.isLevelUnlocked(levelNumber)) {
                    return new ClickResult(ClickType.NONE, 0);
                }
                return new ClickResult(ClickType.LEVEL, levelNumber);
            }
        }

        return new ClickResult(ClickType.NONE, 0);
    }

    public static boolean isUpgradeClicked(Viewport viewport, float upgradeX, float upgradeY, float upgradeW, float upgradeH) {
        if (!Gdx.input.justTouched()) return false;
        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touch);
        return touch.x >= upgradeX && touch.x <= upgradeX + upgradeW &&
            touch.y >= upgradeY && touch.y <= upgradeY + upgradeH;
    }

    public enum ClickType { NONE, HERO, LEVEL, BACK }

    public static class ClickResult {
        public final ClickType type;
        public final int value;
        public ClickResult(ClickType type, int value) {
            this.type = type;
            this.value = value;
        }
    }
}