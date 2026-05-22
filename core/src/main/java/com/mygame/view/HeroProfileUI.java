package com.mygame.view;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.model.Hero;
import com.mygame.controller.CardManager;

public class HeroProfileUI {

    private static final float CANVAS_WIDTH = 1253f;
    private static final float CANVAS_HEIGHT = 752f;

    private static final float BOOK_W = 1180f;
    private static final float BOOK_H = 710f;
    private static final float BOOK_X = 45f;
    private static final float BOOK_Y_CANVA = -15f;

    private static final float[] CHAR_W = {234.9f, 159.7f, 184.6f};
    private static final float[] CHAR_H = {299.5f, 313.0f, 308.5f};
    private static final float[] CHAR_X = {655.9f, 698.0f, 704.2f};
    private static final float[] CHAR_Y_CANVA = {160.8f, 157.9f, 167.8f};

    private static final float UPGRADE_W = 236.1f;
    private static final float UPGRADE_H = 141.6f;
    private static final float UPGRADE_X = 547.5f;
    private static final float UPGRADE_Y_CANVA = 540f;

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

        font.draw(batch, "Ability:", STAT_NAME_X, CANVAS_HEIGHT - (STAT_NAME_Y_CANVA + STAT_SPACING * 2));
        String abilityDesc = CardManager.getHeroDescription(hero.getHeroIndex());
        font.getData().setScale(0.9f);
        String[] lines = formatTextBy4Words(abilityDesc);
        float descY = CANVAS_HEIGHT - (STAT_NAME_Y_CANVA + STAT_SPACING * 2 + 30);
        for (String line : lines) {
            font.draw(batch, line, STAT_NAME_X, descY);
            descY -= 25f;
        }

        font.getData().setScale(1.2f);
        String ultLabel = "Ultimate:";
        String ultVal = String.valueOf(hero.getUltimateDamage());
        float ultY = CANVAS_HEIGHT - (STAT_NAME_Y_CANVA + STAT_SPACING * 5);
        float ultValY = CANVAS_HEIGHT - (STAT_VAL_Y_CANVA + STAT_SPACING * 5);
        float offset = 1.5f;
        font.setColor(new Color(0.6f, 0.4f, 0f, 1f));
        for (float dx = -offset; dx <= offset; dx += offset) {
            for (float dy = -offset; dy <= offset; dy += offset) {
                if (dx == 0 && dy == 0) continue;
                font.draw(batch, ultLabel, STAT_NAME_X + dx, ultY + dy);
                font.draw(batch, ultVal, STAT_VAL_X + dx, ultValY + dy);
            }
        }
        font.setColor(new Color(1f, 0.84f, 0f, 1f));
        font.draw(batch, ultLabel, STAT_NAME_X, ultY);
        font.draw(batch, ultVal, STAT_VAL_X, ultValY);

        int cost = hero.getUpgradeCost();
        String costText = (cost != -1) ? "Cost: " + cost + " pills" : "Cost: MAX";
        float costCenterX = UPGRADE_X + UPGRADE_W / 2f;
        float costY = CANVAS_HEIGHT - (UPGRADE_Y_CANVA + UPGRADE_H + 20f); 
        layout.setText(font, costText);
        float textX = costCenterX - layout.width / 2f;
        float greenOffset = 1.5f;
        font.setColor(new Color(0f, 0.5f, 0f, 1f));
        for (float dx = -greenOffset; dx <= greenOffset; dx += greenOffset) {
            for (float dy = -greenOffset; dy <= greenOffset; dy += greenOffset) {
                if (dx == 0 && dy == 0) continue;
                font.draw(batch, costText, textX + dx, costY + dy);
            }
        }
        font.setColor(new Color(0f, 0.9f, 0.2f, 1f));
        font.draw(batch, costText, textX, costY);

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
    }
    private String[] formatTextBy4Words(String text) {
        String[] words = text.split("\\s+");
        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        int wordCount = 0;

        for (String word : words) {
            if (wordCount == 4) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
                wordCount = 0;
            }
            if (currentLine.length() > 0) currentLine.append(" ");
            currentLine.append(word);
            wordCount++;
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines.toArray(new String[0]);
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