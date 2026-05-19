package com.mygame.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.model.Hero;

public class HeroProfileUI {

    private static final float CANVAS_WIDTH = 1253f;
    private static final float CANVAS_HEIGHT = 752f;

    private static final float BOOK_W = 1135.1f;
    private static final float BOOK_H = 680.6f;
    private static final float BOOK_X = 88.3f;
    private static final float BOOK_Y_CANVA = -3.6f;

    private static final float[] CHAR_W = {234.9f, 159.7f, 184.6f};
    private static final float[] CHAR_H = {299.5f, 313.0f, 308.5f};
    private static final float[] CHAR_X = {655.9f, 698.0f, 704.2f};
    private static final float[] CHAR_Y_CANVA = {160.8f, 157.9f, 167.8f};

    private static final float UPGRADE_W = 236.1f;
    private static final float UPGRADE_H = 141.6f;
    private static final float UPGRADE_X = 547.5f;
    private static final float UPGRADE_Y_CANVA = 497.5f;

    private static final float STAT_LEVEL_X = 481.7f;
    private static final float STAT_LEVEL_Y_CANVA = 187.1f;
    private static final float STAT_NAME_X = 459.7f;
    private static final float STAT_NAME_Y_CANVA = 231.9f;
    private static final float STAT_VAL_X = 571.1f;
    private static final float STAT_VAL_Y_CANVA = 234.3f;
    private static final float STAT_SPACING = 40f;

    private Texture bookTexture;
    private Texture[] fullCharacters;
    private Texture upgradeBtnTexture;
    private Texture solidBlackDimOverlay;
    private BitmapFont font;
    private GlyphLayout layout;

    public void create() {
        bookTexture = new Texture("backgrounds/book.png");
        upgradeBtnTexture = new Texture("ui/upgradeB.png");

        fullCharacters = new Texture[]{
            new Texture("characters/rabbit.png"),
            new Texture("characters/horse.png"),
            new Texture("characters/deer.png")
        };

        Pixmap pixBlack = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixBlack.setColor(Color.BLACK);
        pixBlack.fill();
        solidBlackDimOverlay = new Texture(pixBlack);
        pixBlack.dispose();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        layout = new GlyphLayout();
    }

    public void draw(SpriteBatch batch, Hero hero, int heroIndex) {
        batch.setColor(1, 1, 1, 0.7f);
        batch.draw(solidBlackDimOverlay, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        batch.setColor(Color.WHITE);

        float libgdxBookY = CANVAS_HEIGHT - BOOK_Y_CANVA - BOOK_H;
        batch.draw(bookTexture, BOOK_X, libgdxBookY, BOOK_W, BOOK_H);

        Texture currentHero = fullCharacters[heroIndex];
        float libgdxCharY = CANVAS_HEIGHT - CHAR_Y_CANVA[heroIndex] - CHAR_H[heroIndex];
        batch.draw(currentHero, CHAR_X[heroIndex], libgdxCharY, CHAR_W[heroIndex], CHAR_H[heroIndex]);

        if (!hero.isMaxLevel()) {
            float libgdxUpgradeY = CANVAS_HEIGHT - UPGRADE_Y_CANVA - UPGRADE_H;
            batch.draw(upgradeBtnTexture, UPGRADE_X, libgdxUpgradeY, UPGRADE_W, UPGRADE_H);
        }

        font.getData().setScale(1.2f);
        font.setColor(Color.BLACK);

        font.draw(batch, "Level: " + hero.getLevel() + " / 3", STAT_LEVEL_X, CANVAS_HEIGHT - STAT_LEVEL_Y_CANVA);

        font.draw(batch, "Health:", STAT_NAME_X, CANVAS_HEIGHT - STAT_NAME_Y_CANVA);
        font.draw(batch, String.valueOf(hero.getMaxHp()), STAT_VAL_X, CANVAS_HEIGHT - STAT_VAL_Y_CANVA);

        font.draw(batch, "Damage:", STAT_NAME_X, CANVAS_HEIGHT - (STAT_NAME_Y_CANVA + STAT_SPACING));
        font.draw(batch, String.valueOf(hero.getDamage()), STAT_VAL_X, CANVAS_HEIGHT - (STAT_VAL_Y_CANVA + STAT_SPACING));

        font.draw(batch, "Ultimate:", STAT_NAME_X, CANVAS_HEIGHT - (STAT_NAME_Y_CANVA + STAT_SPACING * 2));
        font.draw(batch, String.valueOf(hero.getUltimateDamage()), STAT_VAL_X, CANVAS_HEIGHT - (STAT_VAL_Y_CANVA + STAT_SPACING * 2));

        font.draw(batch, "Cost:", STAT_NAME_X, CANVAS_HEIGHT - (STAT_NAME_Y_CANVA + STAT_SPACING * 3));
        int cost = hero.getUpgradeCost();
        font.draw(batch, (cost != -1) ? cost + " pills" : "MAX", STAT_VAL_X, CANVAS_HEIGHT - (STAT_VAL_Y_CANVA + STAT_SPACING * 3));

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
    }

    public UpgradeRect getUpgradeRect() {
        float y = CANVAS_HEIGHT - UPGRADE_Y_CANVA - UPGRADE_H;
        return new UpgradeRect(UPGRADE_X, y, UPGRADE_W, UPGRADE_H);
    }

    public static class UpgradeRect {
        public float x, y, width, height;
        public UpgradeRect(float x, float y, float w, float h) {
            this.x = x; this.y = y; this.width = w; this.height = h;
        }
    }

    public void dispose() {
        bookTexture.dispose();
        upgradeBtnTexture.dispose();
        solidBlackDimOverlay.dispose();
        for (Texture t : fullCharacters) t.dispose();
        font.dispose();
    }
}
