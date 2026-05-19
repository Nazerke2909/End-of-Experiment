package com.mygame.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.Hero;
import com.mygame.model.Enemy;
import com.mygame.model.LevelData;
import com.mygame.controller.CombatManager;
import com.mygame.view.HubInputHandler.ClickResult;
import com.mygame.view.HubInputHandler.ClickType;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private static final float CANVAS_WIDTH = 1253f;
    private static final float CANVAS_HEIGHT = 752f;

    private SpriteBatch batch;
    private Music bgMusic;
    private BitmapFont font;
    private GlyphLayout layout;
    private OrthographicCamera camera;
    private Viewport viewport;

    private ScreenState state = ScreenState.HUB;
    private int openedHeroIndex = -1;
    private Hero[] heroes;

    private HubRenderer hubRenderer;
    private HeroProfileUI heroProfileUI;
    private HubInputHandler inputHandler;
    private HubInputHandler.NavigationListener navListener;

    private boolean initialized = false;

    private void ensureInitialized() {
        if (initialized) return;
        initialized = true;

        batch = new SpriteBatch();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
        layout = new GlyphLayout();

        // Heroes
        heroes = new Hero[]{
            new Hero(0, Hero.getNames()[0]),
            new Hero(1, Hero.getNames()[1]),
            new Hero(2, Hero.getNames()[2])
        };

        // Camera & viewport
        camera = new OrthographicCamera();
        viewport = new StretchViewport(CANVAS_WIDTH, CANVAS_HEIGHT, camera);
        viewport.apply();

        // HubRenderer
        hubRenderer = new HubRenderer(font, layout);
        hubRenderer.create();

        // HeroProfileUI
        heroProfileUI = new HeroProfileUI();
        heroProfileUI.create();

        // Navigation
        navListener = new HubInputHandler.NavigationListener() {
            @Override
            public void goToStartScreen() {
                bgMusic.stop();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
            }

            @Override
            public void goToLevel(int levelNumber) {
                bgMusic.stop();

                Hero[] combatHeroes = new Hero[]{
                    new Hero(0, Hero.getNames()[0]),
                    new Hero(1, Hero.getNames()[1]),
                    new Hero(2, Hero.getNames()[2])
                };

                for (int i = 0; i < combatHeroes.length; i++) {
                    int currentLevel = heroes[i].getLevel();
                    for (int lvl = 1; lvl < currentLevel; lvl++) {
                        combatHeroes[i].upgrade();
                    }
                }

                LevelData levelData = new LevelData(levelNumber);
                List<Enemy> combatEnemies = new ArrayList<>();

                String[] enemyFiles = levelData.getEnemyTextures();
                int[] enemyHps = levelData.getEnemyHp();
                int[] enemyDmgs = levelData.getEnemyDmg();

                for(int e = 0; e < enemyFiles.length; e++) {
                    String eName = enemyFiles[e].replace(".png", "").replace(".PNG", "");
                    combatEnemies.add(new Enemy(eName, enemyHps[e], enemyDmgs[e], 10));
                }

                CombatManager combatManager = new CombatManager(combatHeroes, combatEnemies);

                ((Game) Gdx.app.getApplicationListener()).setScreen(
                    new CombatScreen(combatManager, new CombatScreen.CombatEndListener() {
                        @Override
                        public void onVictory(int reward) {
                            System.out.println("Victory! You earned " + reward + " pills.");
                            com.mygame.model.PlayerProgress.getInstance().addCurrency(reward);
                            ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreen.this);
                        }

                        @Override
                        public void onDefeat() {
                            System.out.println("Defeat!");
                            ((Game) Gdx.app.getApplicationListener()).setScreen(GameScreen.this);
                        }
                    }, levelData)
                );
            }
        };

        inputHandler = new HubInputHandler(navListener, heroes, viewport);

        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mainusic.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.5f);
    }

    @Override
    public void show() {
        ensureInitialized();
        if (!bgMusic.isPlaying()) {
            bgMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        handleInput();

        batch.begin();
        hubRenderer.draw(batch);

        if (state == ScreenState.PROFILE && openedHeroIndex != -1) {
            heroProfileUI.draw(batch, heroes[openedHeroIndex], openedHeroIndex);
        }

        batch.end();
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) return;

        if (state == ScreenState.PROFILE) {
            HeroProfileUI.UpgradeRect rect = heroProfileUI.getUpgradeRect();
            if (HubInputHandler.isUpgradeClicked(viewport, rect.x, rect.y, rect.width, rect.height)) {
                Hero hero = heroes[openedHeroIndex];
                boolean success = com.mygame.controller.UpgradeController.tryUpgrade(hero);
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

            state = ScreenState.HUB;
            openedHeroIndex = -1;
            return;
        }

        ClickResult result = inputHandler.handleTouch();

        switch (result.type) {
            case HERO:
                state = ScreenState.PROFILE;
                openedHeroIndex = result.value;
                break;
            case LEVEL:
                navListener.goToLevel(result.value);
                break;
            case BACK:
                navListener.goToStartScreen();
                break;
            default:
                break;
        }
    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    @Override
    public void pause() {
        if (bgMusic != null && bgMusic.isPlaying()) bgMusic.pause();
    }

    @Override
    public void resume() {
        if (bgMusic != null) bgMusic.play();
    }

    @Override
    public void hide() {
        if (bgMusic != null) bgMusic.stop();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (hubRenderer != null) hubRenderer.dispose();
        if (heroProfileUI != null) heroProfileUI.dispose();
        if (font != null) font.dispose();
        if (bgMusic != null) bgMusic.dispose();
    }
}