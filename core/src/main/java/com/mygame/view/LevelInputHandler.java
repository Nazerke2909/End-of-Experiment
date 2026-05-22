package com.mygame.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LevelInputHandler {

    public enum ClickTarget {
        NONE,
        MENU_BUTTON
    }

    private final Viewport viewport;

    public LevelInputHandler(Viewport viewport) {
        this.viewport = viewport;
    }

    public ClickTarget handleTouch() {
        if (!Gdx.input.justTouched()) {
            return ClickTarget.NONE;
        }

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touch);

        float menuY = CombatConfig.CANVAS_HEIGHT - CombatConfig.MENU_BTN_Y_CANVAS - CombatConfig.MENU_BTN_H;
        if (touch.x >= CombatConfig.MENU_BTN_X && touch.x <= CombatConfig.MENU_BTN_X + CombatConfig.MENU_BTN_W &&
            touch.y >= menuY && touch.y <= menuY + CombatConfig.MENU_BTN_H) {
            return ClickTarget.MENU_BUTTON;
        }

        return ClickTarget.NONE;
    }
}
