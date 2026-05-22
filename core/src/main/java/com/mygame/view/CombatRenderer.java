package com.mygame.view;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.model.Card;
import com.mygame.model.Enemy;
import com.mygame.model.Hero;
import com.mygame.model.LevelData;
import com.mygame.model.PlayerProgress;

import java.util.List;

public class CombatRenderer {

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

    private static final float REWARD_TEXT_X = 102f;
    private static final float REWARD_TEXT_Y = 300f; 
    private static final float REWARD_TEXT_W = 174.1f;
    private static final float REWARD_TEXT_H = 48.7f;

    private static final float VICTORY_PILL_X = 75.2f;
    private static final float VICTORY_PILL_Y = 360f; 
    private static final float VICTORY_PILL_W = 113.8f;
    private static final float VICTORY_PILL_H = 113.8f;

    private final BitmapFont font;
    private final GlyphLayout layout;
    private final Texture pixelTexture;

    public CombatRenderer(BitmapFont font, GlyphLayout layout) {
        this.font = font;
        this.layout = layout;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.pixelTexture = new Texture(pixmap);
        pixmap.dispose();
    }

        public void render(SpriteBatch batch, float delta,
                       Texture background, Texture menuBtn, Texture syringe,
                       Texture pill, Texture textBg,
                       Texture victoryOverlay, boolean showVictory,
                       Texture defeatOverlay, boolean showDefeat,
                       Texture menuButtonTex, Texture continueButtonTex,
                       Texture victoryPillIcon, int rewardAmount,
                       Texture[][] heroTextures, Hero[] heroes,
                       Texture[] enemyTextures, List<Enemy> enemies,
                       Texture[][] cardTextures, List<Card> hand,
                       int selectedCardIndex, Card.CardType[] cardTypes,
                       int[] cardHeroIndices, LevelData levelData,
                       int draggedCardIndex, float draggedCardX, float draggedCardY,
                       int hoveredEnemyIndex, int showCardInfoIndex,
                       int[] attackCounters,
                       Texture[][] attackAnimationTextures,
                       Texture[][] enemyAttackAnimationTextures,
                       Texture pot1Texture, Texture pot2Texture,
                       AnimationManager animationManager) {

        drawBackground(batch, background);
        drawMenuButton(batch, menuBtn);
        drawSyringe(batch, syringe, levelData);
        drawCurrency(batch, pill, textBg);
        drawHeroes(batch, heroTextures, heroes, attackCounters, animationManager);
        drawEnemies(batch, enemyTextures, enemies, hoveredEnemyIndex, animationManager);
        drawAttackAnimations(batch, attackAnimationTextures, pot1Texture, pot2Texture,
                            animationManager, heroes, enemies);
        drawEnemyAttackAnimations(batch, enemyAttackAnimationTextures,
                                 animationManager, heroes, enemies);
        drawCards(batch, cardTextures, hand, selectedCardIndex, cardTypes, cardHeroIndices,
                 draggedCardIndex, draggedCardX, draggedCardY);

        if (showCardInfoIndex >= 0 && showCardInfoIndex < hand.size()) {
            drawCardInfo(batch, hand.get(showCardInfoIndex));
        }

        if (showVictory) {
            drawResultOverlay(batch, victoryOverlay, menuButtonTex, continueButtonTex,
                victoryPillIcon, rewardAmount, true, levelData);
        } else if (showDefeat) {
            drawResultOverlay(batch, defeatOverlay, menuButtonTex, null,
                null, 0, false, levelData);
        }
    }

    private void drawBackground(SpriteBatch batch, Texture background) {
        float bgY = CombatConfig.CANVAS_HEIGHT - CombatConfig.BG_Y_CANVAS - CombatConfig.BG_H;
        batch.draw(background, CombatConfig.BG_X, bgY, CombatConfig.BG_W, CombatConfig.BG_H);
    }

    private void drawMenuButton(SpriteBatch batch, Texture menuBtn) {
        float menuY = CombatConfig.CANVAS_HEIGHT - CombatConfig.MENU_BTN_Y_CANVAS - CombatConfig.MENU_BTN_H;
        batch.draw(menuBtn, CombatConfig.MENU_BTN_X, menuY, CombatConfig.MENU_BTN_W, CombatConfig.MENU_BTN_H);
    }

    private void drawSyringe(SpriteBatch batch, Texture syringe, LevelData levelData) {
        float syringeY = CombatConfig.CANVAS_HEIGHT - CombatConfig.SYRINGE_Y_CANVAS - CombatConfig.SYRINGE_H;
        batch.draw(syringe, CombatConfig.SYRINGE_X, syringeY, CombatConfig.SYRINGE_W, CombatConfig.SYRINGE_H);

        String levelText = "LEVEL " + levelData.getLevelNumber();
        layout.setText(font, levelText);
        float textX = CombatConfig.SYRINGE_X + (CombatConfig.SYRINGE_W - layout.width) / 2f;
        float textY = syringeY + (CombatConfig.SYRINGE_H + layout.height) / 2f;
        font.setColor(Color.BLACK);
        font.draw(batch, levelText, textX, textY);
        font.setColor(Color.WHITE);
    }

    private void drawCurrency(SpriteBatch batch, Texture pill, Texture textBg) {
        float pillY = CombatConfig.CANVAS_HEIGHT - CombatConfig.PILL_Y_CANVAS - CombatConfig.PILL_H;
        batch.draw(pill, CombatConfig.PILL_X, pillY, CombatConfig.PILL_W, CombatConfig.PILL_H);

        int currency = PlayerProgress.getInstance().getCurrency();
        font.getData().setScale(2.2f);
        layout.setText(font, String.valueOf(currency));

        float textX = CombatConfig.PILL_X - layout.width - CombatConfig.CURRENCY_GAP;
        float textY = CombatConfig.CANVAS_HEIGHT - CombatConfig.CURRENCY_Y_CANVAS;
        float padding = 8f;

        batch.draw(textBg, textX - padding, textY - layout.height - padding,
            layout.width + (padding * 2), layout.height + (padding * 2));
        font.draw(batch, String.valueOf(currency), textX, textY);
        font.getData().setScale(1.5f);
    }

        private void drawHeroes(SpriteBatch batch, Texture[][] heroTextures, Hero[] heroes, int[] attackCounters,
                               AnimationManager animationManager) {
            for (int i = 0; i < heroes.length; i++) {
                Hero hero = heroes[i];
                if (!hero.isAlive()) continue;

                if (animationManager != null && animationManager.hasAnimationForHero(i)) continue;

                float heroY = CombatConfig.CANVAS_HEIGHT - CombatConfig.HERO_Y_CANVAS[i] - CombatConfig.HERO_H[i];
                float heroCenterX = CombatConfig.HERO_X[i] + CombatConfig.HERO_W[i] / 2f;
                float heroBaseX = CombatConfig.HERO_X[i] + hero.getShakeOffset();
                float heroBaseY = heroY;

                    if (hero.isDamageFlashing()) {
                        batch.setColor(1f, 0.2f, 0.2f, 1f);
                    } else {
                        batch.setColor(1f, 1f, 1f, 1f);
                    }

                Texture heroTex = heroTextures[i][0];
                batch.draw(heroTex, heroBaseX, heroBaseY,
                    CombatConfig.HERO_W[i], CombatConfig.HERO_H[i]);

                batch.setColor(1f, 1f, 1f, 1f);

                float barY = heroY + CombatConfig.HERO_H[i] + 5f;
                drawHpBar(batch, CombatConfig.HERO_X[i], barY,
                         CombatConfig.HERO_W[i], hero.getHp(), hero.getMaxHp());

                if (hero.getShield() > 0) {
                    drawShieldBar(batch, CombatConfig.HERO_X[i], barY - 3f,
                                 CombatConfig.HERO_W[i], hero.getShield());
                }

                if (attackCounters != null && i < attackCounters.length) {
                    drawAPBar(batch, CombatConfig.HERO_X[i], barY + 12f,
                             CombatConfig.HERO_W[i], attackCounters[i], CombatConfig.ULTIMATE_TRIGGER_COUNT);
                }

                font.getData().setScale(0.9f);
                String hpText = hero.getHp() + "/" + hero.getMaxHp();
                layout.setText(font, hpText);
                font.draw(batch, hpText,
                    heroCenterX - layout.width / 2f,
                    heroY + CombatConfig.HERO_H[i] + 25f);
                font.getData().setScale(1.5f);
            }
        }

        private void drawEnemies(SpriteBatch batch, Texture[] enemyTextures, List<Enemy> enemies, int hoveredEnemyIndex,
                            AnimationManager animationManager) {
        if (enemies.size() == 3) {
            int[] order = {0, 2, 1};
            for (int idx : order) {
                drawSingleEnemy(batch, enemyTextures, enemies, idx, hoveredEnemyIndex, animationManager);
            }
        } else {
            for (int i = 0; i < enemies.size(); i++) {
                drawSingleEnemy(batch, enemyTextures, enemies, i, hoveredEnemyIndex, animationManager);
            }
        }
    }

    private void drawSingleEnemy(SpriteBatch batch, Texture[] enemyTextures, List<Enemy> enemies, int i, int hoveredEnemyIndex,
                                 AnimationManager animationManager) {
        Enemy enemy = enemies.get(i);
        if (!enemy.isAlive()) return;

        if (animationManager != null && animationManager.hasAnimationForEnemy(i)) return;

        int texIndex = i % enemyTextures.length;
        float enemyY = CombatConfig.CANVAS_HEIGHT - CombatConfig.ENEMY_Y_CANVAS[i] - CombatConfig.ENEMY_H[i];
        float enemyCenterX = CombatConfig.ENEMY_X[i] + CombatConfig.ENEMY_W[i] / 2f;
        float enemyBaseX = CombatConfig.ENEMY_X[i] + enemy.getShakeOffset();

        Texture tex = enemyTextures[texIndex];

        batch.setColor(1f, 1f, 1f, 1f);

        if (hoveredEnemyIndex == i) {
            batch.setColor(1.2f, 1.2f, 1.2f, 1f);
        }
        if (enemy.isDamageFlashing()) {
            batch.setColor(1f, 0.2f, 0.2f, 1f);
        }

        batch.draw(tex,
            enemyBaseX, enemyY,
            CombatConfig.ENEMY_W[i], CombatConfig.ENEMY_H[i],
            0, 0, tex.getWidth(), tex.getHeight(),
            true, false);

        batch.setColor(1f, 1f, 1f, 1f);

        drawHpBar(batch, CombatConfig.ENEMY_X[i], enemyY + CombatConfig.ENEMY_H[i] + 5f,
                 CombatConfig.ENEMY_W[i], enemy.getHp(), enemy.getMaxHp());

        font.getData().setScale(0.9f);
        String hpText = enemy.getHp() + "/" + enemy.getMaxHp();
        layout.setText(font, hpText);
        font.draw(batch, hpText,
            enemyCenterX - layout.width / 2f,
            enemyY + CombatConfig.ENEMY_H[i] + 25f);
        font.getData().setScale(1.5f);
    }

    private void drawAttackAnimations(SpriteBatch batch, Texture[][] attackAnimationTextures,
                                      Texture pot1Texture, Texture pot2Texture,
                                      AnimationManager animationManager,
                                      Hero[] heroes, List<Enemy> enemies) {
        if (animationManager == null || !animationManager.hasActiveAnimations()) {
            return;
        }

        for (AttackAnimation anim : animationManager.getActiveAnimations()) {
            int heroIdx = anim.getHeroIndex();
            int targetEnemyIdx = anim.getTargetEnemyIndex();

            if (heroIdx < 0 || heroIdx >= heroes.length) continue;
            if (targetEnemyIdx < 0 || targetEnemyIdx >= enemies.size()) continue;

                        Hero hero = heroes[heroIdx];
            Enemy targetEnemy = enemies.get(targetEnemyIdx);

            switch (anim.getType()) {
                case DEER_ATTACK:
                    drawDeerAttackAnimation(batch, anim, heroIdx, targetEnemyIdx,
                                          attackAnimationTextures);
                    break;
                case HORSE_ATTACK:
                    drawHorseAttackAnimation(batch, anim, heroIdx, targetEnemyIdx,
                                           attackAnimationTextures);
                    break;
                case RABBIT_ATTACK:
                    drawRabbitAttackAnimation(batch, anim, heroIdx, targetEnemyIdx,
                                            attackAnimationTextures, pot1Texture, pot2Texture,
                                            heroes, enemies);
                    break;
            }
        }
    }

    private void drawDeerAttackAnimation(SpriteBatch batch, AttackAnimation anim,
                                        int heroIdx, int targetEnemyIdx,
                                        Texture[][] attackAnimationTextures) {
        Texture[] deerFrames = attackAnimationTextures[2]; 
        int frameIdx = anim.getAnimationFrame();
        Texture currentFrame = deerFrames[frameIdx];

        float moveProgress = anim.getMovementProgress();

        float heroBaseX = CombatConfig.HERO_X[heroIdx];
        float heroBaseY = CombatConfig.CANVAS_HEIGHT - CombatConfig.HERO_Y_CANVAS[heroIdx] - CombatConfig.HERO_H[heroIdx];

        float targetEnemyX = CombatConfig.ENEMY_X[targetEnemyIdx];
        float targetEnemyY = CombatConfig.CANVAS_HEIGHT - CombatConfig.ENEMY_Y_CANVAS[targetEnemyIdx] - CombatConfig.ENEMY_H[targetEnemyIdx];

        float dirX = targetEnemyX - heroBaseX;
        float dirY = targetEnemyY - heroBaseY;
        float dirLen = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (dirLen > 0) {
            dirX = dirX / dirLen;
            dirY = dirY / dirLen;
        }

        float maxMoveDistance = dirLen * 0.7f; 
        float moveDistance = maxMoveDistance * Math.min(1f, moveProgress * 1.5f);

        float animX = heroBaseX + dirX * moveDistance;
        float animY = heroBaseY + dirY * moveDistance;

        float frameW = 155.8f; 
        if (frameIdx == 1) {
            frameW = 156.1f; 
        }
        float frameH = 261.1f; 
        if (frameIdx == 1) {
            frameH = 261.6f; 
        }

        batch.draw(currentFrame, animX, animY, frameW, frameH);
    }

    private void drawHorseAttackAnimation(SpriteBatch batch, AttackAnimation anim,
                                         int heroIdx, int targetEnemyIdx,
                                         Texture[][] attackAnimationTextures) {
        Texture[] horseFrames = attackAnimationTextures[1]; 
        int frameIdx = anim.getAnimationFrame();
        Texture currentFrame = horseFrames[frameIdx];

        float moveProgress = anim.getMovementProgress();

        float heroBaseX = CombatConfig.HERO_X[heroIdx];
        float heroBaseY = CombatConfig.CANVAS_HEIGHT - CombatConfig.HERO_Y_CANVAS[heroIdx] - CombatConfig.HERO_H[heroIdx];

        float targetEnemyX = CombatConfig.ENEMY_X[targetEnemyIdx];
        float targetEnemyY = CombatConfig.CANVAS_HEIGHT - CombatConfig.ENEMY_Y_CANVAS[targetEnemyIdx] - CombatConfig.ENEMY_H[targetEnemyIdx];

        float dirX = targetEnemyX - heroBaseX;
        float dirY = targetEnemyY - heroBaseY;
        float dirLen = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (dirLen > 0) {
            dirX = dirX / dirLen;
            dirY = dirY / dirLen;
        }

        float maxMoveDistance = dirLen * 0.7f; 
        float moveDistance = maxMoveDistance * Math.min(1f, moveProgress * 1.5f);

        float animX = heroBaseX + dirX * moveDistance;
        float animY = heroBaseY + dirY * moveDistance;

        float frameW = 247.2f; 
        if (frameIdx == 1) {
            frameW = 274f; 
        }
        float frameH = 297.9f; 
        if (frameIdx == 1) {
            frameH = 293f; 
        }

        batch.draw(currentFrame, animX, animY, frameW, frameH);
    }

    private void drawRabbitAttackAnimation(SpriteBatch batch, AttackAnimation anim,
                                         int heroIdx, int targetEnemyIdx,
                                         Texture[][] attackAnimationTextures,
                                         Texture pot1Texture, Texture pot2Texture,
                                         Hero[] heroes, List<Enemy> enemies) {
        Texture[] rabbitFrames = attackAnimationTextures[0]; 
        int frameIdx = anim.getAnimationFrame();
        Texture currentFrame = rabbitFrames[frameIdx];

        float rabbitX = CombatConfig.HERO_X[heroIdx];
        float rabbitY = CombatConfig.CANVAS_HEIGHT - CombatConfig.HERO_Y_CANVAS[heroIdx] - CombatConfig.HERO_H[heroIdx];

        float frameW = 194.7f; 
        if (frameIdx == 1) {
            frameW = 211.1f; 
        }
        float frameH = 253.5f; 
        if (frameIdx == 1) {
            frameH = 266.7f; 
        }

        batch.draw(currentFrame, rabbitX, rabbitY, frameW, frameH);

        if (anim.shouldShowProjectile()) {
            float projectileProgress = anim.getProjectileProgress();

            float startX = rabbitX + frameW / 2f;
            float startY = rabbitY + frameH / 2f;

            float enemyX = CombatConfig.ENEMY_X[targetEnemyIdx];
            float enemyY = CombatConfig.CANVAS_HEIGHT - CombatConfig.ENEMY_Y_CANVAS[targetEnemyIdx] - CombatConfig.ENEMY_H[targetEnemyIdx];
            float enemyW = CombatConfig.ENEMY_W[targetEnemyIdx];
            float enemyH = CombatConfig.ENEMY_H[targetEnemyIdx];

            float targetX = enemyX + enemyW / 2f;
            float targetY = enemyY + enemyH / 2f;

            float projectileX = startX + (targetX - startX) * projectileProgress;
            float projectileY = startY + (targetY - startY) * projectileProgress;

            float pot2W = 101.7f;
            float pot2H = 114.1f;

            batch.draw(pot2Texture, projectileX - pot2W / 2f, projectileY - pot2H / 2f, pot2W, pot2H);
        }

        if (anim.shouldShowExplosion()) {
            float enemyX = CombatConfig.ENEMY_X[targetEnemyIdx];
            float enemyY = CombatConfig.CANVAS_HEIGHT - CombatConfig.ENEMY_Y_CANVAS[targetEnemyIdx] - CombatConfig.ENEMY_H[targetEnemyIdx];
            float enemyW = CombatConfig.ENEMY_W[targetEnemyIdx];
            float enemyH = CombatConfig.ENEMY_H[targetEnemyIdx];

            float pot1W = 204.8f;
            float pot1H = 238.7f;

            float centerX = enemyX + enemyW / 2f;
            float explosionY = enemyY + enemyH / 2f;

            batch.draw(pot1Texture, centerX - pot1W / 2f, explosionY - pot1H / 2f, pot1W, pot1H);
        }
    }

    private void drawCards(SpriteBatch batch, Texture[][] cardTextures,
                           List<Card> hand, int selectedCardIndex,
                           Card.CardType[] cardTypes, int[] cardHeroIndices,
                           int draggedCardIndex, float draggedCardX, float draggedCardY) {

        if (hand == null || hand.isEmpty()) {
            return;
        }

        for (int i = 0; i < hand.size() && i < CombatConfig.CARDS_SLOTS; i++) {
            if (i == draggedCardIndex) continue;

            float x = CombatConfig.CARD_X[i];
            float y = CombatConfig.CARDS_Y;

            int heroIdx = (cardHeroIndices != null && i < cardHeroIndices.length)
                ? cardHeroIndices[i] : 0;
            if (heroIdx < 0 || heroIdx >= cardTextures.length) {
                heroIdx = 0;
            }
            int texType = (cardTypes != null && i < cardTypes.length
                && cardTypes[i] == Card.CardType.ULTIMATE) ? 1 : 0;

            Texture cardTex = cardTextures[heroIdx][texType];
            batch.draw(cardTex, x, y, CombatConfig.CARD_W, CombatConfig.CARD_H);

            font.getData().setScale(2.5f);
            if (i == selectedCardIndex) {
                font.setColor(Color.YELLOW);
            } else {
                font.setColor(Color.WHITE);
            }
            String label = hand.get(i).getDamage() + " dmg";
            layout.setText(font, label);
            font.draw(batch, label, x + (CombatConfig.CARD_W - layout.width) / 2f, y - 20f);
            font.setColor(Color.WHITE);
            font.getData().setScale(1.5f);
        }

        if (draggedCardIndex >= 0 && draggedCardIndex < hand.size()) {
            int heroIdx = (cardHeroIndices != null && draggedCardIndex < cardHeroIndices.length)
                ? cardHeroIndices[draggedCardIndex] : 0;
            if (heroIdx < 0 || heroIdx >= cardTextures.length) {
                heroIdx = 0;
            }
            int texType = (cardTypes != null && draggedCardIndex < cardTypes.length
                && cardTypes[draggedCardIndex] == Card.CardType.ULTIMATE) ? 1 : 0;

            Texture cardTex = cardTextures[heroIdx][texType];
            float cardY = draggedCardY - CombatConfig.CARD_H / 2f;
            batch.setColor(1f, 1f, 1f, 0.9f);
            batch.draw(cardTex, draggedCardX - CombatConfig.CARD_W / 2f, cardY,
                      CombatConfig.CARD_W, CombatConfig.CARD_H);
            batch.setColor(1f, 1f, 1f, 1f);

            font.getData().setScale(2.5f);
            font.setColor(Color.YELLOW);
            String label = hand.get(draggedCardIndex).getDamage() + " dmg";
            layout.setText(font, label);
            font.draw(batch, label, draggedCardX - CombatConfig.CARD_W / 2f + (CombatConfig.CARD_W - layout.width) / 2f,
                     cardY - 20f);
            font.setColor(Color.WHITE);
            font.getData().setScale(1.5f);
        }
    }

    private void drawHpBar(SpriteBatch batch, float x, float y, float width, int currentHp, int maxHp) {
        float barHeight = 10f;
        float hpPercent = Math.max(0f, Math.min(1f, (float) currentHp / maxHp));

        drawFilledRect(batch, x, y, width, barHeight, 0.8f, 0.1f, 0.1f);

        if (hpPercent > 0) {
            drawFilledRect(batch, x, y, width * hpPercent, barHeight, 0.1f, 0.8f, 0.1f);
        }
    }

    private void drawShieldBar(SpriteBatch batch, float x, float y, float width, int shield) {
        float barHeight = 3f;
        drawFilledRect(batch, x, y, width, barHeight, 1f, 1f, 1f);
    }

    private void drawAPBar(SpriteBatch batch, float x, float y, float width, int currentAP, int maxAP) {
        float barHeight = 6f;
        float apPercent = Math.max(0f, Math.min(1f, (float) currentAP / maxAP));

        drawFilledRect(batch, x, y, width, barHeight, 0.3f, 0.1f, 0.5f);

        if (apPercent > 0) {
            drawFilledRect(batch, x, y, width * apPercent, barHeight, 0.2f, 0.8f, 1f);
        }
    }

    private void drawFilledRect(SpriteBatch batch, float x, float y, float width, float height,
                                float r, float g, float b) {
        batch.setColor(r, g, b, 1f);
        batch.draw(pixelTexture, x, y, width, height);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private void drawResultOverlay(SpriteBatch batch, Texture overlayImage,
                                   Texture menuButtonTex, Texture continueButtonTex,
                                   Texture pillIcon, int rewardAmount,
                                   boolean isVictory, LevelData levelData) {
        float screenY = CombatConfig.CANVAS_HEIGHT - OVERLAY_Y - OVERLAY_H;
        batch.draw(overlayImage, OVERLAY_X, screenY, OVERLAY_W, OVERLAY_H);

        float menuBtnY = CombatConfig.CANVAS_HEIGHT - MENU_BTN_Y_OVERLAY - MENU_BTN_H_OVERLAY;
        batch.draw(menuButtonTex, MENU_BTN_X_OVERLAY, menuBtnY, MENU_BTN_W_OVERLAY, MENU_BTN_H_OVERLAY);

        if (isVictory) {
            if (levelData.getLevelNumber() < 3 && continueButtonTex != null) {
                float continueBtnY = CombatConfig.CANVAS_HEIGHT - CONTINUE_BTN_Y_OVERLAY - CONTINUE_BTN_H_OVERLAY;
                batch.draw(continueButtonTex, CONTINUE_BTN_X_OVERLAY, continueBtnY, CONTINUE_BTN_W_OVERLAY, CONTINUE_BTN_H_OVERLAY);
            }

            font.getData().setScale(2.0f);
            font.setColor(Color.WHITE);
            String rewardStr = String.valueOf(rewardAmount);
            layout.setText(font, rewardStr);
            float rewardTextY = CombatConfig.CANVAS_HEIGHT - REWARD_TEXT_Y - REWARD_TEXT_H;
            float textX = REWARD_TEXT_X + (REWARD_TEXT_W - layout.width) / 2f;
            float textY = rewardTextY + (REWARD_TEXT_H + layout.height) / 2f;
            font.draw(batch, rewardStr, textX, textY);

            float pillY = CombatConfig.CANVAS_HEIGHT - VICTORY_PILL_Y - VICTORY_PILL_H;
            batch.draw(pillIcon, VICTORY_PILL_X, pillY, VICTORY_PILL_W, VICTORY_PILL_H);

            font.getData().setScale(1.5f);
            font.setColor(Color.WHITE);
        }
    }

    private void drawEnemyAttackAnimations(SpriteBatch batch, Texture[][] enemyAttackAnimationTextures,
                                           AnimationManager animationManager,
                                           Hero[] heroes, List<Enemy> enemies) {
        if (animationManager == null || !animationManager.hasActiveAnimations()) {
            return;
        }

        for (AttackAnimation anim : animationManager.getActiveAnimations()) {
            if (!anim.isEnemyAttack()) continue;

            int enemyIdx = anim.getAttackerEnemyIndex();
            int targetHeroIdx = anim.getTargetHeroIndex();

            if (enemyIdx < 0 || enemyIdx >= enemies.size()) continue;
            if (targetHeroIdx < 0 || targetHeroIdx >= heroes.length) continue;

            Enemy enemy = enemies.get(enemyIdx);

            switch (anim.getType()) {
                case PIG_ATTACK:
                    drawEnemyAttackFrame(batch, anim, enemyIdx, targetHeroIdx,
                                        enemyAttackAnimationTextures, 0, 
                                        CombatConfig.ENEMY_W[enemyIdx], CombatConfig.ENEMY_H[enemyIdx],
                                        235f, 317f, 229.4f, 301.1f);
                    break;
                case RAT_ATTACK:
                    drawEnemyAttackFrame(batch, anim, enemyIdx, targetHeroIdx,
                                        enemyAttackAnimationTextures, 1, 
                                        CombatConfig.ENEMY_W[enemyIdx], CombatConfig.ENEMY_H[enemyIdx],
                                        283.5f, 273.3f, 308.4f, 310.1f);
                    break;
                case CROCO_ATTACK:
                    drawEnemyAttackFrame(batch, anim, enemyIdx, targetHeroIdx,
                                        enemyAttackAnimationTextures, 2, 
                                        CombatConfig.ENEMY_W[enemyIdx], CombatConfig.ENEMY_H[enemyIdx],
                                        280.2f, 373f, 277.2f, 322.8f);
                    break;
                default:
                    break;
            }
        }
    }

    private void drawEnemyAttackFrame(SpriteBatch batch, AttackAnimation anim,
                                      int enemyIdx, int targetHeroIdx,
                                      Texture[][] enemyAttackAnimationTextures,
                                      int textureIndex,
                                      float origW, float origH,
                                      float frame1W, float frame1H,
                                      float frame2W, float frame2H) {
        int frameIdx = anim.getAnimationFrame();
        Texture currentFrame = enemyAttackAnimationTextures[textureIndex][frameIdx];

        float enemyBaseX = CombatConfig.ENEMY_X[enemyIdx];
        float enemyY = CombatConfig.CANVAS_HEIGHT - CombatConfig.ENEMY_Y_CANVAS[enemyIdx] - CombatConfig.ENEMY_H[enemyIdx];

        float targetHeroX = CombatConfig.HERO_X[targetHeroIdx];
        float targetHeroY = CombatConfig.CANVAS_HEIGHT - CombatConfig.HERO_Y_CANVAS[targetHeroIdx] - CombatConfig.HERO_H[targetHeroIdx];

        float moveProgress = anim.getMovementProgress();
        float dirX = targetHeroX - enemyBaseX;
        float dirY = targetHeroY - enemyY;
        float dirLen = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        if (dirLen > 0) {
            dirX = dirX / dirLen;
            dirY = dirY / dirLen;
        }

        float maxMoveDistance = dirLen * 0.7f;
        float moveDistance = maxMoveDistance * Math.min(1f, moveProgress * 1.5f);

        float animX = enemyBaseX + dirX * moveDistance;
        float animY = enemyY + dirY * moveDistance;

        float frameW = (frameIdx == 0) ? frame1W : frame2W;
        float frameH = (frameIdx == 0) ? frame1H : frame2H;

        batch.draw(currentFrame, animX, animY, frameW, frameH,
            0, 0, currentFrame.getWidth(), currentFrame.getHeight(),
            true, false);
    }

    public void dispose() {
        if (pixelTexture != null) {
            pixelTexture.dispose();
        }
    }

    private void drawCardInfo(SpriteBatch batch, Card card) {
        float windowX = CombatConfig.CANVAS_WIDTH / 2f - 200f;
        float windowY = CombatConfig.CANVAS_HEIGHT / 2f - 100f;
        float windowW = 400f;
        float windowH = 200f;

        batch.setColor(0f, 0f, 0f, 0.8f);
        batch.draw(pixelTexture, windowX, windowY, windowW, windowH);
        batch.setColor(1f, 1f, 1f, 1f);

        drawRect(batch, windowX, windowY, windowW, windowH, 1f, 1f, 1f);

        font.getData().setScale(2f);
        font.setColor(Color.WHITE);
        layout.setText(font, card.getName());
        font.draw(batch, card.getName(), windowX + 20f, windowY + windowH - 30f);

        font.getData().setScale(1.5f);
        font.setColor(Color.RED);
        String damageText = "Damage: " + card.getDamage();
        layout.setText(font, damageText);
        font.draw(batch, damageText, windowX + 20f, windowY + windowH - 70f);

        font.getData().setScale(1.2f);
        font.setColor(Color.YELLOW);
        String featureText = "Feature: " + card.getDescription();
        layout.setText(font, featureText);

        float maxWidth = windowW - 40f;
        if (layout.width > maxWidth) {
            String[] words = featureText.split(" ");
            String line1 = "";
            String line2 = "";
            for (String word : words) {
                String testLine = (line1.isEmpty() ? "" : line1 + " ") + word;
                layout.setText(font, testLine);
                if (layout.width <= maxWidth) {
                    line1 = testLine;
                } else {
                    line2 = (line2.isEmpty() ? "" : line2 + " ") + word;
                }
            }
            font.draw(batch, line1, windowX + 20f, windowY + windowH - 110f);
            if (!line2.isEmpty()) {
                font.draw(batch, line2, windowX + 20f, windowY + windowH - 140f);
            }
        } else {
            font.draw(batch, featureText, windowX + 20f, windowY + windowH - 110f);
        }

        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);
    }

    private void drawRect(SpriteBatch batch, float x, float y, float width, float height,
                         float r, float g, float b) {
        batch.setColor(r, g, b, 1f);
        batch.draw(pixelTexture, x, y, width, 2f);
        batch.draw(pixelTexture, x, y, 2f, height);
        batch.draw(pixelTexture, x + width - 2f, y, 2f, height);
        batch.draw(pixelTexture, x, y + height - 2f, width, 2f);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}