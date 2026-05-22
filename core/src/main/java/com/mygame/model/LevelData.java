package com.mygame.model;
public class LevelData {
    private int levelNumber;
    private String backgroundPath;
    private String[] enemyTextures;
    private int[] enemyHp;
    private int[] enemyDmg;
    private int rewardPills;

    public LevelData(int levelNumber) {
        this.levelNumber = levelNumber;
        loadData();
    }

    public static class Builder {
        private int levelNumber;
        private String backgroundPath;
        private String[] enemyTextures;
        private int[] enemyHp;
        private int[] enemyDmg;
        private int rewardPills;

        public Builder levelNumber(int levelNumber) { this.levelNumber = levelNumber; return this; }
        public Builder backgroundPath(String path) { this.backgroundPath = path; return this; }
        public Builder enemyTextures(String... textures) { this.enemyTextures = textures; return this; }
        public Builder enemyHp(int... hp) { this.enemyHp = hp; return this; }
        public Builder enemyDmg(int... dmg) { this.enemyDmg = dmg; return this; }
        public Builder rewardPills(int reward) { this.rewardPills = reward; return this; }

        public LevelData build() {
            LevelData data = new LevelData(levelNumber);
            data.backgroundPath = this.backgroundPath;
            data.enemyTextures = this.enemyTextures;
            data.enemyHp = this.enemyHp;
            data.enemyDmg = this.enemyDmg;
            data.rewardPills = this.rewardPills;
            return data;
        }
    }

    private void loadData() {
        switch (levelNumber) {
            case 1:
                backgroundPath = "backgrounds/lvl1.png";
                enemyTextures = new String[]{"enemyPig.PNG"};
                enemyHp = new int[]{100};
                enemyDmg = new int[]{19};
                rewardPills = 150;
                break;
            case 2:
                backgroundPath = "backgrounds/lvl2.png";
                enemyTextures = new String[]{"enemyPig.PNG", "enemyRat.PNG"};
                enemyHp = new int[]{80, 90};
                enemyDmg = new int[]{22, 38};
                rewardPills = 360;
                break;
            case 3:
                backgroundPath = "backgrounds/lvl3.png";
                enemyTextures = new String[]{"enemyPig.PNG", "enemyCroco.PNG", "enemyRat.PNG"};
                enemyHp = new int[]{170, 150, 160};
                enemyDmg = new int[]{37, 50, 48};
                rewardPills = 1000;
                break;
            default:
                backgroundPath = "backgrounds/lvl1.png";
                enemyTextures = new String[]{};
                enemyHp = new int[]{};
                enemyDmg = new int[]{};
                rewardPills = 0;
        }
    }

    public int getLevelNumber() { return levelNumber; }
    public String getBackgroundPath() { return backgroundPath; }
    public String[] getEnemyTextures() { return enemyTextures; }
    public int[] getEnemyHp() { return enemyHp; }
    public int[] getEnemyDmg() { return enemyDmg; }
    public int getRewardPills() { return rewardPills; }
}