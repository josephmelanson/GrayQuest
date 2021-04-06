public class Difficulty {

        // DIFFICULTY OPTIONS
        private final int maxLevel = 20; // Maximum level players can achieve.
        private final int xpForLevel = 2; // Monsters required for level up = xpForLevel * playerLevel.
        private final int dungeonChance = 5; // 1=100%, 2=50%, 3=33%, 4=25%, etc.
        private final int monsterChance = 2; // 1=100%, 2=50%, 3=33%, 4=25%, etc.
        private final int playerBaseHP = 100; // 10 is D&D standard, 100 is debugging.
        private final int runChance = 2; // 1=100%, 2=50%, 3=33%, 4=25%, etc.

        private final int commonPrice = 10;
        private final int uncommonPrice = 50;
        private final int rarePrice = 100;
        private final int vRarePrice = 200;
        private final int legendaryPrice = 500;

        // GETTERS
        public int getMaxLevel() { return maxLevel; }
        public int getXpForLevel(int lvl) { return xpForLevel * lvl; }
        public int getDungeonChance() { return dungeonChance; }
        public int getMonsterChance() { return monsterChance; }
        public int getPlayerBaseHP() { return playerBaseHP; }
        public int getRunChance() { return runChance; }

        public int getCommonPrice() { return commonPrice; }
        public int getUncommonPrice() { return uncommonPrice; }
        public int getRarePrice() { return rarePrice; }
        public int getVRarePrice() { return vRarePrice; }
        public int getLegendaryPrice() { return legendaryPrice; }
}
