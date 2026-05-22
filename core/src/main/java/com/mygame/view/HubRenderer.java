package com.mygame.view;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.model.PlayerProgress;

public class HubRenderer {

    private static final float CANVAS_WIDTH = 1253f;
    private static final float CANVAS_HEIGHT = 752f;

        private static final float[] FLASK_W = {246.3f, 239.8f, 239.8f};
    private static final float[] FLASK_H = {134.1f, 130.6f, 130.6f};
    private static final float[] FLASK_X = {534.8f, 538.0f, 538.0f};
    private static final float[] FLASK_Y_CANVA = {49.6f, 183.8f, 310.7f};

    private static final float[] LEVEL_TEXT_X = {657.9f, 657.5f, 656.6f};
    private static final float[] LEVEL_TEXT_Y_CANVA = {161.5f, 293.6f, 418.1f};
    private static final float TEXT_SHIFT_Y = -10f;

    private static final float PILL_W = 116.7f;
    private static final float PILL_H = 116.7f;
    private static final float PILL_X = 1119.5f;
    private static final float PILL_Y_CANVA = 16.9f;
    private static final float CURRENCY_Y_CANVA = 59.5f;
    private static final float CURRENCY_GAP = 10f;

    private static final float[] AVATAR_W = {467.6f, 465.7f, 459.7f};
    private static final float[] AVATAR_H = {280.4f, 279.2f, 275.6f};
    private static final float[] AVATAR_X = {-1.9f, 0f, 0f};
    private static final float[] AVATAR_Y_CANVA = {22.8f, 197.1f, 368.8f};

    private static final String[] HERO_NAMES = {"Sakura", "Gojo", "Yuki"};
    private static final float HERO_NAME_CENTER_X = 245.3f;
    private static final float[] HERO_NAME_Y_CANVA = {214.8f, 391.0f, 567.3f};

    private static final float BACK_BTN_WIDTH = 120f;
    private static final float BACK_BTN_HEIGHT = 45f;
    private static final float BACK_BTN_X = 20f;
    private static final float BACK_BTN_Y = 20f;

    private static final int LEVELS_COUNT = 3;

    private Texture background;
    private Texture pillIcon;
    private Texture levelButtonActive;
    private Texture levelButtonInactive;
    private Texture[] avatarIcons;
    private Texture backBtnWhiteBox;
    private BitmapFont font;
    private GlyphLayout layout;

    public HubRenderer(BitmapFont font, GlyphLayout layout) {
        this.font = font;
        this.layout = layout;
    }

    public void create() {
        background = new Texture("backgrounds/mainroom.PNG");
        pillIcon = new Texture("ui/pill.png");
        levelButtonActive = new Texture("ui/levelbutton.PNG");
        levelButtonInactive = new Texture("ui/greylvl.png");

        Texture rabbitIcon = new Texture("ui/rabbitP.png");
        Texture horseIcon = new Texture("ui/horseP.png");
        Texture deerIcon = new Texture("ui/deerP.png");
        avatarIcons = new Texture[]{rabbitIcon, horseIcon, deerIcon};

        Pixmap pixWhite = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixWhite.setColor(new Color(1f, 1f, 1f, 0.4f));
        pixWhite.fill();
        backBtnWhiteBox = new Texture(pixWhite);
        pixWhite.dispose();
    }

    public void draw(SpriteBatch batch) {
        PlayerProgress progress = PlayerProgress.getInstance();
        batch.draw(background, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        float libgdxPillY = CANVAS_HEIGHT - PILL_Y_CANVA - PILL_H;
        batch.draw(pillIcon, PILL_X, libgdxPillY, PILL_W, PILL_H);
        layout.setText(font, String.valueOf(progress.getCurrency()));
        font.draw(batch, String.valueOf(progress.getCurrency()), PILL_X - layout.width - CURRENCY_GAP, CANVAS_HEIGHT - CURRENCY_Y_CANVA);

        for (int i = 0; i < 3; i++) {
            float avatarY = CANVAS_HEIGHT - AVATAR_Y_CANVA[i] - AVATAR_H[i];
            batch.draw(avatarIcons[i], AVATAR_X[i], avatarY, AVATAR_W[i], AVATAR_H[i]);
            layout.setText(font, HERO_NAMES[i]);
            font.draw(batch, HERO_NAMES[i], HERO_NAME_CENTER_X - (layout.width / 2f), CANVAS_HEIGHT - HERO_NAME_Y_CANVA[i]);
        }

        for (int i = 0; i < LEVELS_COUNT; i++) {
            Texture tex = progress.isLevelUnlocked(i + 1) ? levelButtonActive : levelButtonInactive;
            float y = CANVAS_HEIGHT - FLASK_Y_CANVA[i] - FLASK_H[i];
            batch.draw(tex, FLASK_X[i], y, FLASK_W[i], FLASK_H[i]);
            font.draw(batch, String.valueOf(i + 1), LEVEL_TEXT_X[i], CANVAS_HEIGHT - LEVEL_TEXT_Y_CANVA[i] + TEXT_SHIFT_Y);
        }

        batch.draw(backBtnWhiteBox, BACK_BTN_X, BACK_BTN_Y, BACK_BTN_WIDTH, BACK_BTN_HEIGHT);
        layout.setText(font, "<- BACK");
        font.draw(batch, "<- BACK", BACK_BTN_X + (BACK_BTN_WIDTH - layout.width) / 2f, BACK_BTN_Y + (BACK_BTN_HEIGHT + layout.height) / 2f);
    }

    public void drawHeroIconsOnly(SpriteBatch batch) {
        PlayerProgress progress = PlayerProgress.getInstance();

        float libgdxPillY = CANVAS_HEIGHT - PILL_Y_CANVA - PILL_H;
        batch.draw(pillIcon, PILL_X, libgdxPillY, PILL_W, PILL_H);
        layout.setText(font, String.valueOf(progress.getCurrency()));
        font.draw(batch, String.valueOf(progress.getCurrency()), PILL_X - layout.width - CURRENCY_GAP, CANVAS_HEIGHT - CURRENCY_Y_CANVA);

        for (int i = 0; i < 3; i++) {
            float avatarY = CANVAS_HEIGHT - AVATAR_Y_CANVA[i] - AVATAR_H[i];
            batch.draw(avatarIcons[i], AVATAR_X[i], avatarY, AVATAR_W[i], AVATAR_H[i]);
            layout.setText(font, HERO_NAMES[i]);
            font.draw(batch, HERO_NAMES[i], HERO_NAME_CENTER_X - (layout.width / 2f), CANVAS_HEIGHT - HERO_NAME_Y_CANVA[i]);
        }

        batch.draw(backBtnWhiteBox, BACK_BTN_X, BACK_BTN_Y, BACK_BTN_WIDTH, BACK_BTN_HEIGHT);
        layout.setText(font, "<- BACK");
        font.draw(batch, "<- BACK", BACK_BTN_X + (BACK_BTN_WIDTH - layout.width) / 2f, BACK_BTN_Y + (BACK_BTN_HEIGHT + layout.height) / 2f);
    }

    public void drawAvatarsOnly(SpriteBatch batch) {
        PlayerProgress progress = PlayerProgress.getInstance();

        float libgdxPillY = CANVAS_HEIGHT - PILL_Y_CANVA - PILL_H;
        batch.draw(pillIcon, PILL_X, libgdxPillY, PILL_W, PILL_H);
        layout.setText(font, String.valueOf(progress.getCurrency()));
        font.draw(batch, String.valueOf(progress.getCurrency()), PILL_X - layout.width - CURRENCY_GAP, CANVAS_HEIGHT - CURRENCY_Y_CANVA);

        for (int i = 0; i < 3; i++) {
            float avatarY = CANVAS_HEIGHT - AVATAR_Y_CANVA[i] - AVATAR_H[i];
            batch.draw(avatarIcons[i], AVATAR_X[i], avatarY, AVATAR_W[i], AVATAR_H[i]);
            layout.setText(font, HERO_NAMES[i]);
            font.draw(batch, HERO_NAMES[i], HERO_NAME_CENTER_X - (layout.width / 2f), CANVAS_HEIGHT - HERO_NAME_Y_CANVA[i]);
        }

        batch.draw(backBtnWhiteBox, BACK_BTN_X, BACK_BTN_Y, BACK_BTN_WIDTH, BACK_BTN_HEIGHT);
        layout.setText(font, "<- BACK");
        font.draw(batch, "<- BACK", BACK_BTN_X + (BACK_BTN_WIDTH - layout.width) / 2f, BACK_BTN_Y + (BACK_BTN_HEIGHT + layout.height) / 2f);
    }

    public void dispose() {
        background.dispose();
        pillIcon.dispose();
        levelButtonActive.dispose();
        levelButtonInactive.dispose();
        for (Texture t : avatarIcons) t.dispose();
        backBtnWhiteBox.dispose();
    }
}
