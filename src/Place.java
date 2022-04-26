public class Place {

    Difficulty difficulty = new Difficulty();

    // generates a monsterID based on the characters level
    // takes in biome number as z
    public double rollForMonsterByLevel(double z, int lvl) {
        int i = (int)(Math.random() * 3) + 1;
        // id.biome
        if (lvl < 2) {
            i -= 1;
            if (i < 1) {
                i = 1;
            }
        } else if (lvl > 2 && lvl <= 5) {
            i += 1;
        } else if (lvl > 5 && lvl <= 8) {
            i += 2;
        } else if (lvl > 8 && lvl <= 11) {
            i += 3;
        } else if (lvl > 11 && lvl <= 14) {
            i += 4;
        } else if (lvl > 14 && lvl <= 17) {
            i += 5;
        } else if (lvl > 17) {
            i += 6;
        }
        z = z / 10;
        return i + z;
    }

    // GETTERS
    // print biome description
    public void getBiome(int posZ) {
        if (posZ == 0) {
            System.out.println("\nYou are in town.");
        } else if (posZ == 1) {
            System.out.println("\nYou are in the frigid north.");
        } else if (posZ == 2) {
            System.out.println("\nYou are in the tropical south.");
        } else if (posZ == 3) {
            System.out.println("\nYou are in the central plains.");
        } else if (posZ == 4) {
            System.out.println("\nYou are on the western shore.");
        } else if (posZ == 5) {
            System.out.println("\nYou are in the eastern mountains.");
        }
    }

    public int getNorthLimit() { // WORLD MAP BORDERS
        return 9; }
    public int getEastLimit() {
        return 3; }
    public int getSouthLimit() {
        return -9; }
    public int getWestLimit() {
        return -3; }

    // chance to encounter a dungeon
    public boolean rollForDungeon(int z, int lvl) {
        int i;
        if (z == 3 && lvl > 16) {
            i = (int) (Math.random() * difficulty.getDungeonChance()) + 1;
            return i == 1;
        } else if (z == 2 && lvl > 1) {
            i = (int) (Math.random() * difficulty.getDungeonChance()) + 1;
            return i == 1;
        } else if (z == 1 && lvl > 12) {
            i = (int) (Math.random() * difficulty.getDungeonChance()) + 1;
            return i == 1;
        } else if (z == 4 && lvl > 4) {
            i = (int) (Math.random() * difficulty.getDungeonChance()) + 1;
            return i == 1;
        } else if (z == 5 && lvl > 8) {
            i = (int) (Math.random() * difficulty.getDungeonChance()) + 1;
            return i == 1;
        } else {
            //System.out.println("-----DEBUG----- No dungeon.");
            return false;
        }
    }

    // chance to encounter a monster
    public boolean rollForMonster() {
        int i = (int) (Math.random() * difficulty.getMonsterChance()) + 1;
        return i == 1;
    }
}