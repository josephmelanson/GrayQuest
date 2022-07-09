public class Difficulty {
        // DIFFICULTY OPTIONS
        // GETTERS
        public int getMaxLevel() { return 20; } // Maximum level players can achieve.
        public int getXpForLevel(int lvl) {
        // Monsters required for level up = xpForLevel * playerLevel.
                int xpForLevel = 2;
                return xpForLevel * lvl; }
        public int getDungeonChance() { return 5; } // 1=100%, 2=50%, 3=33%, 4=25%, etc.
        public int getMonsterChance() { return 2; } // 1=100%, 2=50%, 3=33%, 4=25%, etc.
        public int getPlayerBaseHP() { return 10; } // 10 is D&D standard, 100 is debugging.
        public int getRunChance() { return 2; } // 1=100%, 2=50%, 3=33%, 4=25%, etc.

        public int getCommonPrice() { return 10; }
        public int getUncommonPrice() { return 50; }
        public int getRarePrice() { return 100; }
        public int getVRarePrice() { return 200; }
        public int getLegendaryPrice() { return 500; }
}