package com.mygame.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.controller.CardManager;
import com.mygame.controller.CombatManager;
import com.mygame.controller.TurnManager;
import com.mygame.model.Card;
import com.mygame.model.Enemy;
import com.mygame.model.Hero;
import com.mygame.model.LevelData;
import com.mygame.model.PlayerProgress;

import java.util.List;

public class CombatScreen implements Screen {

    private static final float OVERLAY_W = 1304.6f;
    private static final float OVERLAY_H = 752.3f;
    private static final float OVERLAY_X = -51.6f;
    private static final float OVERLAY_Y = 0f;

    private static final float MENU_BTN_X_OVERLAY = 848.8f;
    private static final float MENU_BTN_Y_OVERLAY = 262.4f;
    private static final float MENU_BTN_W_OVERLAY = 329f;
    private static final float MENU_BTN_H_OVERLAY = 101.5f;

    private static final float CONTINUE_BTN_X_OVERLAY = 848.8f;
    private static final float CONTINUE_BTN_Y_OVERLAY = 403.4f;
    private static final float CONTINUE_BTN_W_OVERLAY = 329f;
    private static final float CONTINUE_BTN_H_OVERLAY = 100.3f;

    public interface CombatEndListener {
        void onVictory(int reward);
        void onDefeat();
    }

    private final CombatManager combatManager;
    private final CombatEndListener listener;
    private final LevelData levelData;

    private CombatResourceManager resources;
    private CombatRenderer renderer;
    private CombatInputHandler inputHandler;
    private AnimationManager animationManager;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private List<Card> currentHand;
    private int[] cardHeroIndices;
    private Card.CardType[] cardTypes;

    private boolean showVictory = false;
    private boolean showDefeat = false;
    private boolean resultProcessed = false;
    private boolean battleEndedPending = false; 

    public CombatScreen(CombatManager combatManager, CombatEndListener listener, LevelData levelData) {
        this.combatManager = combatManager;
        this.listener = listener;
        this.levelData = levelData;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(CombatConfig.CANVAS_WIDTH, CombatConfig.CANVAS_HEIGHT, camera);
        viewport.apply();

        batch = new SpriteBatch();

        resources = new CombatResourceManager();
        resources.load(levelData);

        renderer = new CombatRenderer(resources.getFont(), resources.getLayout());
        inputHandler = new CombatInputHandler(viewport);
        animationManager = new AnimationManager();

        CardManager cardManager = combatManager.getCardManager();
        currentHand = cardManager.generateHand();
        cardTypes = cardManager.getLastCardTypes();
        cardHeroIndices = cardManager.getLastCardHeroIndices();

        resources.playMusic();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        updateEntities(delta);
        animationManager.update(delta);

        if (battleEndedPending) {
            if (!animationManager.hasActiveAnimations()) {
                finishBattle();
            }
        }

        if (showVictory || showDefeat) {
            handleOverlayInput();

            batch.begin();
            int reward = showVictory ? levelData.getRewardPills() : 0;
            int[] attackCounters = combatManager.getCardManager().getAttackCounters();
            renderer.render(
                batch, delta,
                resources.getBackground(), resources.getBackBtnTexture(),
                resources.getSyringeTexture(), resources.getPillTexture(),
                resources.getTextBgTexture(),
                resources.getVictoryOverlay(), showVictory,
                resources.getDefeatOverlay(), showDefeat,
                resources.getMenuButtonTexture(), resources.getContinueButtonTexture(),
                resources.getVictoryPillIcon(), reward,
                resources.getHeroTextures(), combatManager.getHeroes(),
                resources.getEnemyTextures(), combatManager.getEnemies(),
                resources.getCardTextures(), currentHand,
                inputHandler.getSelectedCardIndex(), cardTypes, cardHeroIndices,
                levelData,
                inputHandler.getDraggedCardIndex(), inputHandler.getDraggedCardX(), inputHandler.getDraggedCardY(),
                inputHandler.getHoveredEnemyIndex(), inputHandler.getShowCardInfoIndex(),
                attackCounters,
                                resources.getAttackAnimationTextures(), resources.getEnemyAttackAnimationTextures(),
                resources.getPot1Texture(), resources.getPot2Texture(),
                animationManager
            );
            batch.end();
            return;
        }

                if (!combatManager.isPlayerTurn() && !combatManager.isBattleEnded() && !battleEndedPending) {
                    TurnManager turnManager = combatManager.getTurnManager();
                    if (turnManager.updateEnemyTimer(delta)) {
                        combatManager.processEnemyAttack();

                        int attackingEnemyIdx = combatManager.getLastAttackerEnemyIndex();
                        int targetHeroIdx = combatManager.getLastTargetHeroIndex();
                        if (attackingEnemyIdx >= 0 && targetHeroIdx >= 0) {
                            createEnemyAttackAnimation(attackingEnemyIdx, targetHeroIdx);
                        }

                        if (combatManager.isBattleEnded()) {
                            if (!animationManager.hasActiveAnimations()) {
                                finishBattle();
                            } else {
                                battleEndedPending = true;
                            }
                        } else {
                            CardManager cardManager = combatManager.getCardManager();
                            currentHand = cardManager.generateHand();
                            cardTypes = cardManager.getLastCardTypes();
                            cardHeroIndices = cardManager.getLastCardHeroIndices();
                            inputHandler.clearSelectedCard();
                        }
                    }
                }

        if (combatManager.isBattleEnded() && !battleEndedPending && !showVictory && !showDefeat) {
            finishBattle();
        }

        if (!battleEndedPending && !showVictory && !showDefeat && combatManager.isPlayerTurn()) {
            CombatInputHandler.ClickResult click = inputHandler.handleTouch(
                currentHand.size(), combatManager.getEnemies());

            switch (click.target) {
                case MENU_BUTTON:
                    resources.stopMusic();
                    listener.onDefeat();
                    return;
                case CARD:
                    inputHandler.setShowCardInfoIndex(click.index);
                    break;
                case ENEMY:
                    performPlayerAttack(click.selectedCardIdx, click.index);
                    inputHandler.clearCardInfo();
                    if (combatManager.isBattleEnded()) {
                        if (!animationManager.hasActiveAnimations()) {
                            finishBattle();
                        } else {
                            battleEndedPending = true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        batch.begin();
        int[] attackCounters = combatManager.getCardManager().getAttackCounters();
        renderer.render(
            batch, delta,
            resources.getBackground(), resources.getBackBtnTexture(),
            resources.getSyringeTexture(), resources.getPillTexture(),
            resources.getTextBgTexture(),
            resources.getVictoryOverlay(), false,
            resources.getDefeatOverlay(), false,
            resources.getMenuButtonTexture(), resources.getContinueButtonTexture(),
            resources.getVictoryPillIcon(), 0,
            resources.getHeroTextures(), combatManager.getHeroes(),
            resources.getEnemyTextures(), combatManager.getEnemies(),
            resources.getCardTextures(), currentHand,
            inputHandler.getSelectedCardIndex(), cardTypes, cardHeroIndices,
            levelData,
            inputHandler.getDraggedCardIndex(), inputHandler.getDraggedCardX(), inputHandler.getDraggedCardY(),
            inputHandler.getHoveredEnemyIndex(), inputHandler.getShowCardInfoIndex(),
            attackCounters,
            resources.getAttackAnimationTextures(), resources.getEnemyAttackAnimationTextures(),
            resources.getPot1Texture(), resources.getPot2Texture(),
            animationManager
        );
        batch.end();
    }

    private void updateEntities(float delta) {
        for (Hero hero : combatManager.getHeroes()) {
            hero.update(delta);
        }
        for (Enemy enemy : combatManager.getEnemies()) {
            enemy.update(delta);
        }
    }

        private void performPlayerAttack(int cardIndex, int enemyIndex) {
        if (cardIndex < 0 || enemyIndex < 0) return;
        if (currentHand == null || cardIndex >= currentHand.size()) return;

        if (combatManager.playerAttack(cardIndex, enemyIndex, currentHand)) {
            int heroIndex = combatManager.getLastAttackerHeroIndex();
            Card.HeroType heroType = combatManager.getLastAttackerHeroType();

            AttackAnimation.AnimationType animType = AttackAnimation.AnimationType.DEER_ATTACK;
            if (heroType == Card.HeroType.RABBIT) {
                animType = AttackAnimation.AnimationType.RABBIT_ATTACK;
            } else if (heroType == Card.HeroType.HORSE) {
                animType = AttackAnimation.AnimationType.HORSE_ATTACK;
            }

                        AttackAnimation animation = new AttackAnimation(animType, heroIndex, enemyIndex, true);
            animationManager.addAnimation(animation);
        }
    }

        private void createEnemyAttackAnimation(int enemyIndex, int targetHeroIndex) {
        AttackAnimation.AnimationType animType = AttackAnimation.AnimationType.PIG_ATTACK; 

        if (enemyIndex >= 0 && enemyIndex < combatManager.getEnemies().size()) {
            String enemyName = combatManager.getEnemies().get(enemyIndex).getName().toLowerCase();
            if (enemyName.contains("rat")) {
                animType = AttackAnimation.AnimationType.RAT_ATTACK;
            } else if (enemyName.contains("croco")) {
                animType = AttackAnimation.AnimationType.CROCO_ATTACK;
            } else {
                animType = AttackAnimation.AnimationType.PIG_ATTACK;
            }
        }

        AttackAnimation animation = new AttackAnimation(animType, enemyIndex, targetHeroIndex, false, false);
        animationManager.addAnimation(animation);
    }

    private void finishBattle() {
        showVictory = false;
        showDefeat = false;
        battleEndedPending = false;
        resources.stopMusic();
        if (combatManager.isVictory()) {
            showVictory = true;
            PlayerProgress.getInstance().addCurrency(levelData.getRewardPills());
            PlayerProgress.getInstance().unlockNextLevel();
        } else {
            showDefeat = true;
        }
        resultProcessed = false;
    }

    private void handleOverlayInput() {
        if (!Gdx.input.justTouched()) return;

        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touch);

        float menuBtnY = CombatConfig.CANVAS_HEIGHT - MENU_BTN_Y_OVERLAY - MENU_BTN_H_OVERLAY;
        float continueBtnY = CombatConfig.CANVAS_HEIGHT - CONTINUE_BTN_Y_OVERLAY - CONTINUE_BTN_H_OVERLAY;

        if (touch.x >= MENU_BTN_X_OVERLAY && touch.x <= MENU_BTN_X_OVERLAY + MENU_BTN_W_OVERLAY &&
            touch.y >= menuBtnY && touch.y <= menuBtnY + MENU_BTN_H_OVERLAY) {
            if (!resultProcessed) {
                resultProcessed = true;
                listener.onDefeat();
            }
            return;
        }

        if (showVictory && levelData.getLevelNumber() < 3 &&
            touch.x >= CONTINUE_BTN_X_OVERLAY && touch.x <= CONTINUE_BTN_X_OVERLAY + CONTINUE_BTN_W_OVERLAY &&
            touch.y >= continueBtnY && touch.y <= continueBtnY + CONTINUE_BTN_H_OVERLAY) {
            if (!resultProcessed) {
                resultProcessed = true;
                listener.onVictory(levelData.getRewardPills());
            }
            return;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        resources.pauseMusic();
    }

    @Override
    public void resume() {
        resources.playMusic();
    }

    @Override
    public void hide() {
        resources.stopMusic();
    }

    @Override
    public void dispose() {
        batch.dispose();
        renderer.dispose();
        resources.dispose();
    }
}