public class Player extends Monster {

    // OBJECTS
    Difficulty difficulty = new Difficulty(); // Used to access difficulty options in stat calculations.
    Item item = new Item(); // Used to access itemID manipulation methods in item.

    // MEMBER VARIABLES:
    private int level; // player level
    private int race; // 1=human, 2=orc, 3=dwarf, 4=elf
    private int experience; // player experience tracker
    private int money; // total money
    private int posX; // east/west
    private int posY; // north/south
    // posZ in Monster

    // Bonuses based on stats.
    private int raceStrBonus;
    private int raceDexBonus;
    private int raceConBonus;

    // MEMBER VARIABLES: ITEMS - Reference itemID design in Items class.
    private int healPotionsOne; // number of Healing Potions, id=4.0, 4.1, 4.2
    private int healPotionsTwo; // number of Greater Healing Potions, id=4.3
    private int healPotionsThree; // number of Superior Healing Potions, id=4.4
    private int healPotionsFour; // number of Supreme Healing Potions, id=4.5
    private int totalPotions; // total number of healing potions

    // These hold the id number representing equipped gear.
    private double equippedArmor;
    private double equippedShield;
    private double equippedSword;

    // These hold stats used for calculating combat.
    // weaponBase comes from Monster
    private int armorBaseAC;
    private int shieldBaseAC;
    private int armorBonus;
    private int shieldBonus;
    private int weaponBonus;

    // DUNGEONS CLEARED: These are true if a dungeon has been cleared.
    private boolean plainsDungeonClear;
    private boolean coldDungeonClear;
    private boolean mountainDungeonClear;
    private boolean shoresDungeonClear;
    private boolean tropicalDungeonClear;

    // TOWER CLEARED: These are true if a tower boss has been killed.
    private boolean towerOneClear;
    private boolean towerTwoClear;

    // METHODS:
    // This is used instead of a default constructor to reset character data on a soft reload.
    public void newCharacter() {
        level = 1; // value > 1 is debug
        experience = 0; // value > 0 is debug
        money = 0; // value > 0 is debug
        posX = 0;
        posY = 0;
        posZ = 0;

        hpBase = difficulty.getPlayerBaseHP();

        equippedArmor = 1.0;
        equippedShield = 3.0;
        equippedSword = 2.0;

        weaponMin = 1;
        weaponMax = 8;
        shieldBaseAC = 2;
        armorBaseAC = 18;

        shieldBonus = 0;
        armorBonus = 0;
        weaponBonus = 0;

        healPotionsOne = 0;
        healPotionsTwo = 0;
        healPotionsThree = 0;
        healPotionsFour = 0;
        totalPotions = 0;

        plainsDungeonClear = false;
        coldDungeonClear = false;
        mountainDungeonClear = false;
        shoresDungeonClear = false;
        tropicalDungeonClear = false;

        towerOneClear = false;
        towerTwoClear = false;
    }

    // SETTERS:
    // These are basic player stats.
    public void setLevel(int lvl) { level = lvl; }
    public void setRace(int race) {
        this.race = race;
        setRaceBonus(race);
    }
    public void setRaceBonus(int race) {
        if (race == 1) {
            raceStrBonus += 1;
            raceDexBonus += 1;
            raceConBonus += 1;
        } else if (race == 2) {
            raceStrBonus += 2;
            raceConBonus += 1;
        } else if (race == 3) {
            raceDexBonus += 1;
            raceConBonus += 2;
        } else {
            raceStrBonus += 1;
            raceDexBonus += 2;
        }
    }
    public void setExperience(int exp) { experience = exp; }

    // This accepts positive or negative ints and adjusts money.
    public void setMoney(int money) {
        this.money += money;
        if (this.money < 0) {
            this.money = 0;
        }
    }

    // These set co-ordinates.
    public void setPosX(int x) { posX = x; }
    public void setPosY(int y) { posY = y; }

    // These set stats as part of character creation.
    public void setStrength(int n) { strength = n; }
    public void setConstitution(int n) { constitution = n; }
    public void setDexterity(int n) { dexterity = n; }

    // These set healing potion values.
    public void setHealPotionsOne(int healPotionsOne) {
        this.healPotionsOne += healPotionsOne;
        if (healPotionsOne < 0) {
            this.healPotionsOne = 0;
        }
    }
    public void setHealPotionsTwo(int healPotionsTwo) {
        this.healPotionsTwo += healPotionsTwo;
        if (healPotionsTwo < 0) {
            this.healPotionsTwo = 0;
        }
    }
    public void setHealPotionsThree(int healPotionsThree) {
        this.healPotionsThree += healPotionsThree;
        if (healPotionsThree < 0) {
            this.healPotionsThree = 0;
        }
    }
    public void setHealPotionsFour(int healPotionsFour) {
        this.healPotionsFour += healPotionsFour;
        if (healPotionsFour < 0) {
            this.healPotionsFour = 0;
        }
    }
    public void setTotalPotions() { totalPotions = healPotionsOne + healPotionsTwo + healPotionsThree + healPotionsFour; }

    // These set equipment based on supplied itemIDs.
    public void setEquippedSword(double itemID) { equippedSword = itemID; }
    public void setEquippedShield(double itemID) { equippedShield = itemID; }
    public void setEquippedArmor(double itemID) { equippedArmor = itemID; }

    // These set combat math stats.
    public void setFinalAC() { this.finalAC = armorBaseAC + shieldBaseAC + armorBonus + shieldBonus; }
    public void setFinalDamageBonus() { finalDamageBonus = strengthMod + weaponBonus ; }
    public void setFinalToHit() { this.finalToHit = dexterityMod + weaponBonus; }
    public void setMaxHP() { maxHP = (hpBase + constitutionMod) * level; }
    public void setMaxHPLoad(int maxHP) { this.maxHP = maxHP; }

    // This references a monsterID to determine, set, and display a dungeon completion.
    public String setDungeonClear(double monsterID) {
        int i = (int)monsterID;
        double d = monsterID - i;
        d = (double) Math.round(d * 10) / 10;
        String s = "";
        if (i == 0) {
            if (d == 0.1) {
                setColdDungeonClear(true);
                s = "You have cleared the frozen dungeon!";
            } else if (d == 0.2) {
                setTropicalDungeonClear(true);
                s = "You have cleared the jungle dungeon!";
            } else if (d == 0.3) {
                setPlainsDungeonClear(true);
                s = "You have cleared the plains dungeon!";
            } else if (d == 0.4) {
                setShoresDungeonClear(true);
                s = "You have cleared the shores dungeon!";
            } else {
                setMountainDungeonClear(true);
                s = "You have cleared the mountain dungeon!";
            }
        }
        return s;
    }

    // These set player dungeon completion stats.
    public void setPlainsDungeonClear(boolean b) { plainsDungeonClear = b; }
    public void setColdDungeonClear(boolean b) { coldDungeonClear = b; }
    public void setMountainDungeonClear(boolean b) { mountainDungeonClear = b; }
    public void setShoresDungeonClear(boolean b) { shoresDungeonClear = b; }
    public void setTropicalDungeonClear(boolean b) { tropicalDungeonClear = b; }

    // These set player tower completion stats.
    public void setTowerOneClear(boolean b) { towerOneClear = b; }
    public void setTowerTwoClear(boolean b) { towerTwoClear = b; }

    // This should only be called during character creation.
    public void setMainStats() {
        strength = rollStats() + raceStrBonus;
        dexterity = rollStats()+ raceDexBonus;
        constitution = rollStats()+ raceConBonus;
        setSecondaryStats();
    }

    // This will be called anytime there is a chance which impacts character stats, equipping/un-equipping, leveling, etc
    public void setSecondaryStats() {
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        setFinalDamageBonus();
        setFinalToHit();
        setFinalAC();
        setMaxHP();
        currentHP = maxHP;
    }

    // GETTERS:
    public int getLevel() { return level; }

    // This returns character race as an int.
    public int getRaceInt() { return race; }
    // This returns character race as a String/description.
    public String getRaceString(int race) {
        if (race == 1) {
            return "human";
        } else if (race == 2) {
            return "orc";
        } else if (race == 3) {
            return "dwarf";
        } else { return "elf"; }
    }

    public int getExperience() { return experience; }
    public int getMoney() { return money; }

    // These return player co-ordinates.
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    // getPosZ from Monster.

    // These return healing potion amounts.
    public int getHealPotionsOne() { return healPotionsOne; }
    public int getHealPotionsTwo() { return healPotionsTwo; }
    public int getHealPotionsThree() { return healPotionsThree; }
    public int getHealPotionsFour() { return healPotionsFour; }
    public int getTotalPotions() {
        setTotalPotions();
        return totalPotions;
    }

    // These return itemIDs for equipped gear.
    public double getEquippedArmorID() { return equippedArmor; }
    public double getEquippedShieldID() { return equippedShield; }
    public double getEquippedSwordID() { return equippedSword; }
    // These return double gear bonus values based on supplied itemIDs.
    public double getEquippedArmorBonus() {
        int i = (int)equippedArmor;
        double d  = equippedArmor - i;
        d = Math.round(d * 10);
        d = d / 10;
        return d;
    }
    public double getEquippedShieldBonus() {
        int i = (int)equippedShield;
        double d  = equippedShield - i;
        d = Math.round(d * 10);
        d = d / 10;
        return d;
    }
    public double getEquippedSwordBonus() {
        int i = (int)equippedSword;
        double d = equippedSword - i;
        d = Math.round(d * 10);
        d = d / 10;
        return d;
    }

    // This is used to display a formatted summary of all character data after load.
    public String getCharacterSummary() {
        String s;
        s = "-----STATS-----\n\nName: " + name + "\nLevel: " + level + "\nRace: " + getRaceString(race) + "\nCharacter experience: " + experience + "\nMoney: " + money +  "\nMax HP: " + maxHP + "\nCurrent HP: " + currentHP + "\nStrength: " + strength + "\nDexterity: " + dexterity + "\nConstitution: " + constitution + "\n\n-----GEAR-----\n\nEquipped Armor: A " + item.getItemName(equippedArmor) + "\nEquipped Shield: " + item.getItemName(equippedShield) + "\nEquipped Sword: " + item.getItemName(equippedSword) + "\n\n";
        if (totalPotions > 0) {
            s = s + "-----POTIONS-----";
            if (healPotionsOne > 0) {
                s = s + "Healing Potions: " + healPotionsOne + "\n";
            }
            if (healPotionsTwo > 0) {
                s = s + "Greater Healing Potions: " + healPotionsTwo + "\n";
            }
            if (healPotionsThree > 0) {
                s = s + "Superior Healing Potions: " + healPotionsThree + "\n";
            }
            if (healPotionsFour > 0) {
                s = s + "Pristine Healing Potion: " + healPotionsFour + "\n";
            }
            s = s + "\n";
        }
        s = s + "-----DUNGEONS-----\n\n";
        if (!plainsDungeonClear) {
            s = s + "Plains dungeon not cleared.";
        } else {
            s = s + "Plains dungeon cleared.";
        }
        if (!coldDungeonClear) {
            s = s + "Cold dungeon not cleared.";
        } else {
            s = s + "Cold dungeon cleared.";
        }
        if (!tropicalDungeonClear) {
            s = s + "Tropical dungeon not cleared.";
        } else {
            s = s + "Tropical dungeon cleared.";
        }
        if (!shoresDungeonClear) {
            s = s + "Shores dungeon not cleared.";
        } else {
            s = s + "Shores dungeon cleared.";
        }
        if (!mountainDungeonClear) {
            s = s + "Mountain dungeon not cleared.";
        } else {
            s = s + "Mountain dungeon cleared.";
        }
        return s;
    }

    // IS/CHECKS
    // Returns true if the player has cleared all the dungeons.
    public boolean checkForTowerReady() { return plainsDungeonClear && coldDungeonClear && mountainDungeonClear && shoresDungeonClear && tropicalDungeonClear; }

    // Returns true if the player has enough experience to level up.
    public boolean checkForLevelUp() {
        if (level >= difficulty.getMaxLevel()) {
            return false;
        } else {
            return experience >= difficulty.getXpForLevel(level);
        }
    }

    // Returns true if the player can afford a supplied price item.
    public boolean checkMoney(int price) {
        return !(price > money);
    }

    // This equips gear if the supplied itemID is better than what is equipped, then discards unused gear.
    public String checkForEquipUpgrade(double itemID) {
        String s = "";
        int i = (int)itemID;
        double d = itemID - i;
        d = Math.round(d * 10);
        d = d / 10;
        if (i == 1) {
            if (d == getEquippedArmorBonus()) {
                s = "The " + item.getItemName(getEquippedArmorID()) + " you're using is better than the " + item.getItemName(itemID) + ".\nYou discard the " + item.getItemName(itemID) + ".\n";
            } else if (d > getEquippedArmorBonus()) {
                s = "The " + item.getItemName(itemID) + " is better than the " + item.getItemName(getEquippedArmorID()) + " you're using.\nYou equip the " + item.getItemName(itemID) + " and discard the " + item.getItemName(getEquippedArmorID()) + ".\n";
                setEquippedArmor(itemID);
            } else {
                s = "The " + item.getItemName(getEquippedArmorID()) + " you're using is better than the " + item.getItemName(itemID) + ".\nYou discard the " + item.getItemName(itemID) + ".\n";
            }
        } else if (i == 2) {
            if (d == getEquippedSwordBonus()) {
                s = "The " + item.getItemName(getEquippedSwordID()) + " you're using is better than the " + item.getItemName(itemID) + ".\nYou discard the " + item.getItemName(itemID) + ".\n";
            } else if (d > getEquippedSwordBonus()) {
                s = "The " + item.getItemName(itemID) + " is better than the " + item.getItemName(getEquippedSwordID()) + " you're using.\nYou equip the " + item.getItemName(itemID) + " and discard the " + item.getItemName(getEquippedSwordID()) + ".\n";
                setEquippedSword(itemID);
            } else {
                s = "The " + item.getItemName(getEquippedSwordID()) + " you're using is better than the " + item.getItemName(itemID) + ".\nYou discard the " + item.getItemName(itemID) + ".\n";
            }
        } else if (i == 3) {
            if (d == getEquippedShieldBonus()) {
                s = "The "  + item.getItemName(getEquippedShieldID()) + " you're using is better than the " + item.getItemName(itemID) + ".\nYou discard the " + item.getItemName(itemID) + ".\n";
            } else if (d > getEquippedShieldBonus()) {
                s = "The " + item.getItemName(itemID) + " is better than the " + item.getItemName(getEquippedShieldID()) + " you're using.\nYou equip the " + item.getItemName(itemID) + " and discard your " + item.getItemName(getEquippedShieldID()) + ".\n";
                setEquippedShield(itemID);
            } else {
                s = "The " + item.getItemName(getEquippedShieldID()) + "you're using is better than the " + item.getItemName(itemID) + ".\nYou discard the " + item.getItemName(itemID) + ".\n";
            }
        }
        return s;
    }

    // These return the (true/false) status of a given dungeon from the players stats.
    public boolean isPlainsDungeonClear() { return plainsDungeonClear; }
    public boolean isColdDungeonClear() { return coldDungeonClear; }
    public boolean isMountainDungeonClear() { return mountainDungeonClear; }
    public boolean isShoresDungeonClear() { return shoresDungeonClear; }
    public boolean isTropicalDungeonClear() { return tropicalDungeonClear; }

    // These return the (true/false) status of a given tower level from the players stats.
    public boolean isTowerOneClear() { return towerOneClear; }
    public boolean isTowerTwoClear() { return towerTwoClear; }

    // ADDERS:
    public void addExperience() { experience ++; }
    // This adds the appropriate potion to the players inventory based on the supplied itemID.
    public void addPotion(double itemID) {
        int i = (int)itemID;
        double d  = itemID - i;
        d = Math.round(d * 10);
        d = d / 10;
        if (d == 0.5) {
            healPotionsFour++;
        } else if (d == 0.4) {
            healPotionsThree++;
        } else if (d == 0.3) {
            healPotionsTwo++;
        } else {
            healPotionsOne++;
        }
        setTotalPotions();
    }

    // OTHER METHODS:
    // This performs the level up adjustments to the character.
    public void levelUp() {
        level++;
        setSecondaryStats();
    }

}