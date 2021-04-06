public class Item {
    Difficulty difficulty = new Difficulty();

    /*
    itemID: <item type>.<item rarity>
    item type: 0=null, 1=armor, 2=sword, 3=shield, 4=potion,
    item rarity: 0=base, 1=common(+1), 2=uncommon(+2), 3=rare(+3), 4=very rare(+4), 5=legendary(+5)
    */

    // GETTERS
    public String getGearTypeName(int itemType) {
        if (itemType == 1) {
            return "suit of Plate";
        } else if (itemType == 2) {
            return "Longsword";
        } else {
            return "Shield";
        }
    }
    public String getGearBonusName(double bonus) {
        if (bonus == 0.1) {
            return " +1";
        } else if (bonus == 0.2) {
            return " +2";
        } else if (bonus == 0.3) {
            return " +3";
        } else if (bonus == 0.4) {
            return " +4";
        } else if (bonus == 0.5) {
            return " +5";
        } else return null;
    }
    public String getItemName(double itemID) {
        String name;
        String bonus = null;
        // itemID processing block
        int i = (int)itemID;
        double d  = itemID - i;
        d = Math.round(d * 10);
        d = d / 10;
        // assign name based on item type and bonus
        if (i == 4) {
            name = getPotionName(itemID);
        } else {
            name = getGearTypeName(i);
            bonus = getGearBonusName(d);
        }
        if (bonus == null) {
            return name;
        } else {
            return name + bonus;
        }
    }
    public String getPotionName(double itemID) {
        // itemID processing block
        int i = (int)itemID;
        double d  = itemID - i;
        d = Math.round(d * 10);
        d = d / 10;
        // potion name assignment
        if (d == 0.3) {
            return "Greater Healing Potion";
        } else if (d == 0.4) {
            return "Superior Healing Potion";
        } else if (d == 0.5) {
            return "Very Supreme Healing Potion";
        } else {
            return "Healing Potion";
        }
    }

    // OTHER METHODS
    public int sellItem(double itemID) {
        // itemID processing block
        int i = (int)itemID;
        double d = itemID - i;
        d = Math.round(d * 10);
        d = d / 10;
        // reduce value by determined amounts and return
        if (d == 0.1) {
            return difficulty.getCommonPrice() / 10;
        } else if (d == 0.2) {
            return difficulty.getUncommonPrice() / 7;
        } else if (d == 0.3) {
            return difficulty.getRarePrice() / 5;
        } else if (d == 0.4) {
            return difficulty.getVRarePrice() / 3;
        } else if (d == 0.5) {
            return difficulty.getLegendaryPrice() / 2;
        } else return 1;
    }

    public int rollForMoneyByLevel(int lvl) {
        // generate random amount of money, max based on character level
        int i = (int)(Math.random() * (2 * lvl)) + 1;
        i *= lvl;
        return i;
    }

    public double createRandomItem(int lvl) {
        int n = (int)(Math.random() * 20) + 1; // random 1-20 is chance of magic item
        int i = (int) (Math.random() * 3) + 1; // random 1-3 is type of magic item
        double d; // 1-5 is rarity of magic item
        if (n == 20) {
            d = (int)(Math.random() * 5) + 3; // 0.3 - 0.5 - high magic bonus
            d = d / 10;
        } else if (n < 20 && n >= 15) {
            d = (int)(Math.random() * 3) + 1; // 0.1 - 0.3
            d = d / 10;
        } else if (n < 15 && n >= 10) {
            i = 4; // potions
            d = (int)(Math.random() * 3); // 0.0 - 0.2
            d = d / 10;
        } else {
            // no item - gold
            i = 0;
            d = 0.0;
        }
        // adjust item rarity by character level
        if (lvl > 9) {
            d += 0.3;
        } else if (lvl >= 7) {
            d += 0.1;
        } else if (lvl >= 4) {
            d -= 0.1;
        } else if (lvl == 1) {
            d -= 0.3;
        }
        // test for out of bounds bonuses
        if (d > 0.5) {
            d = 0.5;
        }
        if (d < 0.0) {
            d = 0.0;
        }
        // create and return itemID
        return i + d;
    }

    public int rollHealPotOne() { return (int)(Math.random() * 7) + 4; } // 2d4+2
    public int rollHealPotTwo() { return (int)(Math.random() * 17) + 8; } // 4d4+4
    public int rollHealPotThree() { return (int)(Math.random() * 25) + 16; } // 8d4+8
    public int rollHealPotFour() { return (int)(Math.random() * 31) + 30; } // 10d4+20
}