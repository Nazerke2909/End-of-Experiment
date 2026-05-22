package com.mygame.view;
public class AttackAnimation {
    public enum AnimationType {
        DEER_ATTACK,
        HORSE_ATTACK,
        RABBIT_ATTACK,
        PIG_ATTACK,
        RAT_ATTACK,
        CROCO_ATTACK
    }

    private final AnimationType type;
    private final int heroIndex;
    private final int targetEnemyIndex;
    private final int attackerEnemyIndex;
    private final int targetHeroIndex;
    private float elapsed = 0f;
    private boolean finished = false;

    private static final float ATTACK_FRAME1_DURATION = 0.3f;
    private static final float ATTACK_FRAME2_DURATION = 0.3f;
    private static final float PROJECTILE_DURATION = 0.6f;
    private static final float EXPLOSION_DURATION = 0.2f;

    public AttackAnimation(AnimationType type, int heroIndex, int targetEnemyIndex, boolean isHeroAttack) {
        this.type = type;
        this.heroIndex = heroIndex;
        this.targetEnemyIndex = targetEnemyIndex;
        this.attackerEnemyIndex = -1;
        this.targetHeroIndex = -1;
    }

    public AttackAnimation(AnimationType type, int attackerEnemyIndex, int targetHeroIndex, boolean isHeroAttack, boolean dummy) {
        this.type = type;
        this.heroIndex = -1;
        this.targetEnemyIndex = -1;
        this.attackerEnemyIndex = attackerEnemyIndex;
        this.targetHeroIndex = targetHeroIndex;
    }

    public boolean isEnemyAttack() {
        return attackerEnemyIndex >= 0;
    }

    public void update(float delta) {
        if (finished) return;
        elapsed += delta;

        switch (type) {
            case DEER_ATTACK:
            case HORSE_ATTACK:
            case PIG_ATTACK:
            case RAT_ATTACK:
            case CROCO_ATTACK:
                if (elapsed >= ATTACK_FRAME1_DURATION + ATTACK_FRAME2_DURATION) {
                    finished = true;
                }
                break;
            case RABBIT_ATTACK:
                if (elapsed >= ATTACK_FRAME1_DURATION + ATTACK_FRAME2_DURATION + PROJECTILE_DURATION + EXPLOSION_DURATION) {
                    finished = true;
                }
                break;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public AnimationType getType() {
        return type;
    }

    public int getHeroIndex() {
        return heroIndex;
    }

    public int getTargetEnemyIndex() {
        return targetEnemyIndex;
    }

    public int getAttackerEnemyIndex() {
        return attackerEnemyIndex;
    }

    public int getTargetHeroIndex() {
        return targetHeroIndex;
    }

    public float getElapsed() {
        return elapsed;
    }

    public int getAnimationFrame() {
        if (elapsed < ATTACK_FRAME1_DURATION) {
            return 0;
        } else if (elapsed < ATTACK_FRAME1_DURATION + ATTACK_FRAME2_DURATION) {
            return 1;
        }
        return 1; 
    }

    public float getMovementProgress() {
        if (type == AnimationType.RABBIT_ATTACK) return 0f;
        float progress = Math.min(1f, elapsed / (ATTACK_FRAME1_DURATION + ATTACK_FRAME2_DURATION));
        return Math.min(1f, progress * 1.5f); 
    }

    public float getProjectileProgress() {
        if (type != AnimationType.RABBIT_ATTACK) return 0f;
        float projectileStart = ATTACK_FRAME1_DURATION + ATTACK_FRAME2_DURATION;
        if (elapsed < projectileStart) return 0f;
        float projectileElapsed = elapsed - projectileStart;
        return Math.min(1f, projectileElapsed / PROJECTILE_DURATION);
    }

    public boolean shouldShowProjectile() {
        if (type != AnimationType.RABBIT_ATTACK) return false;
        float projectileStart = ATTACK_FRAME1_DURATION + ATTACK_FRAME2_DURATION;
        return elapsed >= projectileStart && elapsed < projectileStart + PROJECTILE_DURATION;
    }

    public boolean shouldShowExplosion() {
        if (type != AnimationType.RABBIT_ATTACK) return false;
        float explosionStart = ATTACK_FRAME1_DURATION + ATTACK_FRAME2_DURATION + PROJECTILE_DURATION;
        return elapsed >= explosionStart && elapsed < explosionStart + EXPLOSION_DURATION;
    }
}
