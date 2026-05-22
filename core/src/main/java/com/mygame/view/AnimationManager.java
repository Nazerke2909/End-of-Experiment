package com.mygame.view;
import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private final List<AttackAnimation> activeAnimations = new ArrayList<>();

    public void addAnimation(AttackAnimation animation) {
        activeAnimations.add(animation);
    }

    public void update(float delta) {
        for (AttackAnimation animation : activeAnimations) {
            animation.update(delta);
        }
        activeAnimations.removeIf(AttackAnimation::isFinished);
    }

    public List<AttackAnimation> getActiveAnimations() {
        return activeAnimations;
    }

    public void clear() {
        activeAnimations.clear();
    }

    public boolean hasActiveAnimations() {
        return !activeAnimations.isEmpty();
    }

    public boolean hasAnimationForHero(int heroIndex) {
        for (AttackAnimation anim : activeAnimations) {
            if (!anim.isEnemyAttack() && anim.getHeroIndex() == heroIndex) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnimationForEnemy(int enemyIndex) {
        for (AttackAnimation anim : activeAnimations) {
            if (anim.isEnemyAttack() && anim.getAttackerEnemyIndex() == enemyIndex) {
                return true;
            }
        }
        return false;
    }
}
