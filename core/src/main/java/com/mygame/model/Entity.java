package com.mygame.model;
public abstract class Entity {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int damage;
    protected int shield = 0;

    private static final float DAMAGE_FLASH_DURATION = 0.1f;
    private float damageFlashTime = 0f;
    private float shakeOffset = 0f;

    private static final float ANIMATION_FRAME_DURATION = 0.3f;
    private float animationTime = 0f;
    private int currentAnimationFrame = 0;

    public Entity(String name, int hp, int damage) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.damage = damage;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getDamage() { return damage; }
    public int getShield() { return shield; }
    public void setShield(int amount) { this.shield = Math.max(0, amount); }
    public void addShield(int amount) { this.shield += amount; }

    public void takeDamage(int amount) {
        int damageToShield = Math.min(shield, amount);
        shield -= damageToShield;
        int remainingDamage = amount - damageToShield;
        hp = Math.max(0, hp - remainingDamage);
        damageFlashTime = DAMAGE_FLASH_DURATION;
    }

    public boolean isAlive() { return hp > 0; }
    public void heal(int amount) { hp = Math.min(maxHp, hp + amount); }

    public final void performAttack(Entity target) {
        beforeAttack();
        int dmg = calculateDamage();
        if (dmg > 0) {
            target.takeDamage(dmg);
            System.out.println(getName() + " attacks " + target.getName() + " for " + dmg + " damage!");
        }
        afterAttack(target, dmg);
    }

    protected void beforeAttack() {
    }

    protected int calculateDamage() {
        return damage;
    }

    protected void afterAttack(Entity target, int damageDealt) {
    }

    public void update(float delta) {
        if (damageFlashTime > 0) {
            damageFlashTime -= delta;
            shakeOffset = (float) Math.sin(damageFlashTime * 50f) * 3f;
        } else {
            shakeOffset = 0f;
        }

        animationTime += delta;
        if (animationTime >= ANIMATION_FRAME_DURATION) {
            animationTime -= ANIMATION_FRAME_DURATION;
            currentAnimationFrame = (currentAnimationFrame + 1) % 2;
        }
    }

    public boolean isDamageFlashing() { return damageFlashTime > 0; }

    public float getDamageFlashAlpha() {
        if (damageFlashTime <= 0) return 0f;
        return damageFlashTime / DAMAGE_FLASH_DURATION;
    }

    public float getShakeOffset() { return shakeOffset; }
    public int getAnimationFrame() { return currentAnimationFrame; }
}