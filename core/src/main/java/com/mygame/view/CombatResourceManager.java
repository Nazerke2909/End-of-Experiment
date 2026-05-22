package com.mygame.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mygame.model.LevelData;

public class CombatResourceManager {

    private Texture background;
    private Texture backBtnTexture;
    private Texture syringeTexture;
    private Texture pillTexture;
    private Texture textBgTexture;
    private Texture victoryOverlay;
    private Texture defeatOverlay;
    private Texture menuButtonTexture;
    private Texture continueButtonTexture;
    private Texture victoryPillIcon;
    private Texture[][] heroTextures;
    private Texture[] enemyTextures;

    private Texture[][] cardTextures;

    private Texture[][] attackAnimationTextures;

    private Texture[][] enemyAttackAnimationTextures;

    private Texture pot2Texture;
    private Texture pot1Texture;

    private Music battleMusic;
    private BitmapFont font;
    private GlyphLayout layout;

    private LevelData levelData;

    public void load(LevelData levelData) {
        this.levelData = levelData;
        background = new Texture(levelData.getBackgroundPath());

        backBtnTexture = new Texture("ui/menu.png");
        syringeTexture = new Texture("ui/syringe.png");
        pillTexture = new Texture("ui/pill.png");
        victoryOverlay = new Texture("ui/win.png");
        defeatOverlay = new Texture("ui/lose.png");
        menuButtonTexture = new Texture("ui/menuButton.png");
        continueButtonTexture = new Texture("ui/continueButton.png");
        victoryPillIcon = new Texture("ui/pill.png");

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(new Color(0f, 0f, 0f, 0.5f));
        pix.fill();
        textBgTexture = new Texture(pix);
        pix.dispose();

        heroTextures = new Texture[][]{
            {new Texture("characters/rabbit.png")},
            {new Texture("characters/horse.png")},
            {new Texture("characters/deer.png")}
        };

        attackAnimationTextures = new Texture[][]{
            {new Texture("characters/rabbit1.PNG"), new Texture("characters/rabbit2.PNG")},
            {new Texture("characters/horse1.PNG"), new Texture("characters/horse2.PNG")},
            {new Texture("characters/deer1.PNG"), new Texture("characters/deer2.PNG")}
        };

        pot2Texture = new Texture("characters/pot2.PNG");
        pot1Texture = new Texture("characters/pot1.PNG");

        enemyAttackAnimationTextures = new Texture[][]{
            {new Texture("enemies/pig1.PNG"), new Texture("enemies/pig2.PNG")},
            {new Texture("enemies/rat1.PNG"), new Texture("enemies/rat2.PNG")},
            {new Texture("enemies/croco1.PNG"), new Texture("enemies/croco2.PNG")}
        };

        cardTextures = new Texture[][]{
            {new Texture("cards/rabbitA.PNG"), new Texture("cards/rabbitUlt.PNG")},
            {new Texture("cards/horseA.PNG"),   new Texture("cards/horseUlt.png")},
            {new Texture("cards/deerA.PNG"),    new Texture("cards/deerUlt.PNG")}
        };

        String[] enemyFiles = levelData.getEnemyTextures();
        enemyTextures = new Texture[enemyFiles.length];
        for (int i = 0; i < enemyFiles.length; i++) {
            enemyTextures[i] = new Texture("enemies/" + enemyFiles[i]);
        }

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        layout = new GlyphLayout();

        battleMusic = Gdx.audio.newMusic(Gdx.files.internal("music/battlemusic.mp3"));
        battleMusic.setLooping(true);
        battleMusic.setVolume(0.5f);
    }

    public LevelData getLevelData() { return levelData; }
    public void playMusic() { if (battleMusic != null) battleMusic.play(); }
    public void stopMusic() { if (battleMusic != null) battleMusic.stop(); }
    public void pauseMusic() { if (battleMusic != null && battleMusic.isPlaying()) battleMusic.pause(); }

    public Texture getBackground() { return background; }
    public Texture getBackBtnTexture() { return backBtnTexture; }
    public Texture getSyringeTexture() { return syringeTexture; }
    public Texture getPillTexture() { return pillTexture; }
    public Texture getTextBgTexture() { return textBgTexture; }
    public Texture getVictoryOverlay() { return victoryOverlay; }
    public Texture getDefeatOverlay() { return defeatOverlay; }
    public Texture getMenuButtonTexture() { return menuButtonTexture; }
    public Texture getContinueButtonTexture() { return continueButtonTexture; }
    public Texture getVictoryPillIcon() { return victoryPillIcon; }
    public Texture[][] getHeroTextures() { return heroTextures; }
    public Texture[] getEnemyTextures() { return enemyTextures; }
    public Texture[][] getCardTextures() { return cardTextures; }
        public Texture[][] getAttackAnimationTextures() { return attackAnimationTextures; }
    public Texture[][] getEnemyAttackAnimationTextures() { return enemyAttackAnimationTextures; }
    public Texture getPot2Texture() { return pot2Texture; }
    public Texture getPot1Texture() { return pot1Texture; }
    public BitmapFont getFont() { return font; }
    public GlyphLayout getLayout() { return layout; }

    public void dispose() {
        if (background != null) background.dispose();
        if (backBtnTexture != null) backBtnTexture.dispose();
        if (syringeTexture != null) syringeTexture.dispose();
        if (pillTexture != null) pillTexture.dispose();
        if (textBgTexture != null) textBgTexture.dispose();
        if (victoryOverlay != null) victoryOverlay.dispose();
        if (defeatOverlay != null) defeatOverlay.dispose();
        if (menuButtonTexture != null) menuButtonTexture.dispose();
        if (continueButtonTexture != null) continueButtonTexture.dispose();
        if (victoryPillIcon != null) victoryPillIcon.dispose();
        if (heroTextures != null) {
            for (Texture[] arr : heroTextures) {
                for (Texture t : arr) if (t != null) t.dispose();
            }
        }
        if (attackAnimationTextures != null) {
            for (Texture[] arr : attackAnimationTextures) {
                for (Texture t : arr) if (t != null) t.dispose();
            }
        }
                if (pot2Texture != null) pot2Texture.dispose();
        if (pot1Texture != null) pot1Texture.dispose();
        if (enemyAttackAnimationTextures != null) {
            for (Texture[] arr : enemyAttackAnimationTextures) {
                for (Texture t : arr) if (t != null) t.dispose();
            }
        }
        if (cardTextures != null) {
            for (Texture[] arr : cardTextures) {
                for (Texture t : arr) if (t != null) t.dispose();
            }
        }
        if (enemyTextures != null) {
            for (Texture t : enemyTextures) if (t != null) t.dispose();
        }
        if (battleMusic != null) battleMusic.dispose();
        if (font != null) font.dispose();
    }
}