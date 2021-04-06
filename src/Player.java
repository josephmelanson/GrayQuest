import java.util.*;

public class Player extends Monster {

    // OBJECTS
    Place worldMap = new Place(); // Used to access map boundaries.
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

    // DEFAULT CONSTRUCTOR
    public Player() {
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

    // ZERO-ARG CONSTRUCTOR
    // This is used to reset character data on a soft reload.
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
            raceConBonus += 2;
        } else {
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
    public void setPosY(int y) { posY = y;}

    // These set stats as part of character creation.
    public void setStrength(int strength) { this.strength = strength; }
    public void setConstitution(int constitution) { this.constitution = constitution; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

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
    public void setEquippedSword(double equippedSword) { this.equippedSword = equippedSword; }
    public void setEquippedShield(double equippedShield) { this.equippedShield = equippedShield; }
    public void setEquippedArmor(double equippedArmor) { this.equippedArmor = equippedArmor; }

    // These set combat math stats.
    public void setFinalAC() { this.finalAC = armorBaseAC + shieldBaseAC + armorBonus + shieldBonus; }
    public void setFinalDamageBonus() { finalDamageBonus = strengthMod + weaponBonus ; }
    public void setFinalToHit() { this.finalToHit = dexterityMod + weaponBonus; }
    public void setMaxHP() { maxHP = (hpBase + constitutionMod) * level; }
    public void setMaxHPLoad(int maxHP) { this.maxHP = maxHP; }

    // This references a monsterID to determine, set, and display a dungeon completion.
    public void setDungeonClear(double monsterID) {
        int i = (int)monsterID;
        double d = monsterID - i;
        d = (double) Math.round(d * 10) / 10;
        if (i == 0) {
            if (d == 0.1) {
                setColdDungeonClear(true);
                System.out.println("You have cleared the frozen dungeon!");
            } else if (d == 0.2) {
                setTropicalDungeonClear(true);
                System.out.println("You have cleared the jungle dungeon!");
            } else if (d == 0.3) {
                setPlainsDungeonClear(true);
                System.out.println("You have cleared the plains dungeon!");
            } else if (d == 0.4) {
                setShoresDungeonClear(true);
                System.out.println("You have cleared the shores dungeon!");
            } else {
                setMountainDungeonClear(true);
                System.out.println("You have cleared the mountain dungeon!");
            }
        }
    }

    // These set player dungeon completion stats.
    public void setPlainsDungeonClear(boolean plainsDungeonClear) { this.plainsDungeonClear = plainsDungeonClear; }
    public void setColdDungeonClear(boolean coldDungeonClear) { this.coldDungeonClear = coldDungeonClear; }
    public void setMountainDungeonClear(boolean mountainDungeonClear) { this.mountainDungeonClear = mountainDungeonClear; }
    public void setShoresDungeonClear(boolean shoresDungeonClear) { this.shoresDungeonClear = shoresDungeonClear; }
    public void setTropicalDungeonClear(boolean tropicalDungeonClear) { this.tropicalDungeonClear = tropicalDungeonClear; }

    // These set player tower completion stats.
    public void setTowerOneClear(boolean towerOneClear) { this.towerOneClear = towerOneClear; }
    public void setTowerTwoClear(boolean towerTwoClear) { this.towerTwoClear = towerTwoClear; }

    // Called at the end of movement.
    public void setBiome() {
        if (posY > 5) {
            posZ = 1;
        } else if (posY < -5) {
            posZ = 2;
        } else if (posY == 0 && posX == 0) {
            posZ = 0;
        } else if (posX == -3) {
            posZ = 4;
        } else if (posX == 3) {
            posZ = 5;
        } else {
            posZ = 3;
        }
    }

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
    public void getCharacterSummary() {
        System.out.println("-----STATS-----");
        System.out.println("Character name: " + name);
        System.out.println("Character level: " + level);
        System.out.println("Character race: " + getRaceString(race));
        System.out.println("Character experience: " + experience);
        System.out.println("Character money: " + money);
        System.out.println("Character current HP: " + currentHP);
        System.out.println("Character max HP: " + maxHP);
        System.out.println("Character strength: " + strength);
        System.out.println("Character dexterity: " + dexterity);
        System.out.println("Character constitution: " + constitution + "\n");
        if (totalPotions > 0) {
            System.out.println("-----POTIONS-----");
            if (healPotionsOne > 0) {
                System.out.println("Healing Potions: " + healPotionsOne);
            }
            if (healPotionsTwo > 0) {
                System.out.println("Greater Healing Potions: " + healPotionsTwo);
            }
            if (healPotionsThree > 0) {
                System.out.println("Superior Healing Potions: " + healPotionsThree);
            }
            if (healPotionsFour > 0) {
                System.out.println("Very Supreme Healing Potion: " + healPotionsFour + "\n");
            }
        }
        System.out.println("-----GEAR-----");
        System.out.println("Equipped Armor: A " + item.getItemName(equippedArmor));
        System.out.println("Equipped Shield: " + item.getItemName(equippedShield));
        System.out.println("Equipped Sword: " + item.getItemName(equippedSword) + "\n");
        System.out.println("-----DUNGEONS-----");
        if (!plainsDungeonClear) {
            System.out.println("Plains dungeon not cleared.");
        } else {
            System.out.println("Plains dungeon cleared.");
        }
        if (!coldDungeonClear) {
            System.out.println("Cold dungeon not cleared.");
        } else {
            System.out.println("Cold dungeon cleared.");
        }
        if (!tropicalDungeonClear) {
            System.out.println("Tropical dungeon not cleared.");
        } else {
            System.out.println("Tropical dungeon cleared.");
        }
        if (!shoresDungeonClear) {
            System.out.println("Shores dungeon not cleared.");
        } else {
            System.out.println("Shores dungeon cleared.");
        }
        if (!mountainDungeonClear) {
            System.out.println("Mountain dungeon not cleared.");
        } else {
            System.out.println("Mountain dungeon cleared.");
        }
    }

    // IS/CHECKS
    // This returns true if the player has enough experience to level up.
    public boolean checkForLevelUp() {
        if (level >= difficulty.getMaxLevel()) {
            return false;
        } else {
            return experience >= difficulty.getXpForLevel(level);
        }
    }

    // This returns true if the player can afford a supplied price item.
    public boolean checkMoney(int price) {
        return !(price > money);
    }

    // This equips gear if the supplied itemID is better than what is equipped, then discards unused gear.
    public void checkForEquipUpgrade(double itemID) {
        int i = (int)itemID;
        double d = itemID - i;
        d = Math.round(d * 10);
        d = d / 10;
        if (i == 1) {
            if (d == getEquippedArmorBonus()) {
                System.out.print("The " + item.getItemName(getEquippedArmorID()) + " you're using is better than the " + item.getItemName(itemID) + ".\n");
                System.out.print("You discard the " + item.getItemName(itemID) + ".\n");
            } else if (d > getEquippedArmorBonus()) {
                System.out.print("The " + item.getItemName(itemID) + " is better than the " + item.getItemName(getEquippedArmorID()) + " you're using.\n");
                System.out.print("You equip the " + item.getItemName(itemID) + " and discard the " + item.getItemName(getEquippedArmorID()) + ".\n");
                setEquippedArmor(itemID);
            } else {
                System.out.print("The " + item.getItemName(getEquippedArmorID()) + " you're using is better than the " + item.getItemName(itemID) + ".\n");
                System.out.print("You discard the " + item.getItemName(itemID) + ".\n");
            }
        } else if (i == 2) {
            if (d == getEquippedSwordBonus()) {
                System.out.print("The " + item.getItemName(getEquippedSwordID()) + " you're using is better than the " + item.getItemName(itemID) + ".\n");
                System.out.print("You discard the " + item.getItemName(itemID) + ".\n");
            } else if (d > getEquippedSwordBonus()) {
                System.out.print("The " + item.getItemName(itemID) + " is better than the " + item.getItemName(getEquippedSwordID()) + " you're using.\n");
                System.out.print("You equip the " + item.getItemName(itemID) + " and discard the " + item.getItemName(getEquippedSwordID()) + ".\n");
                setEquippedSword(itemID);
            } else {
                System.out.print("The " + item.getItemName(getEquippedSwordID()) + " you're using is better than the " + item.getItemName(itemID) + ".\n");
                System.out.print("You discard the " + item.getItemName(itemID) + ".\n");
            }
        } else if (i == 3) {
            if (d == getEquippedShieldBonus()) {
                System.out.print("The "  + item.getItemName(getEquippedShieldID()) + " you're using is better than the " + item.getItemName(itemID) + ".\n");
                System.out.print("You discard the " + item.getItemName(itemID) + ".\n");
            } else if (d > getEquippedShieldBonus()) {
                System.out.print("The " + item.getItemName(itemID) + " is better than the " + item.getItemName(getEquippedShieldID()) + " you're using.\n");
                System.out.print("You equip the " + item.getItemName(itemID) + " and discard your " + item.getItemName(getEquippedShieldID()) + ".\n");
                setEquippedShield(itemID);
            } else {
                System.out.print("The " + item.getItemName(getEquippedShieldID()) + "you're using is better than the " + item.getItemName(itemID) + ".\n");
                System.out.print("You discard the " + item.getItemName(itemID) + ".\n");
            }
        }
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

    // MOVEMENT METHODS
    // This offers the player movement options depending on current location.
    public void moveCharacterPrompt() {
        char c = 0;
        // south, east, or west only
        boolean swe;
        swe = posY == worldMap.getNorthLimit() && posY > worldMap.getSouthLimit() && posX < worldMap.getEastLimit() && posX > worldMap.getWestLimit();
        while (swe) {
            System.out.print("\nTowering glaciers block your path to the north.");
            System.out.print("\nWhich direction would you like to go?\nEast, South or West. (e/s/w): ");
            c = cleanChar();
            while (c != 'e' && c != 'w' && c != 's') {
                System.out.print("You must chose East, South or West. (e/s/w): ");
                c = cleanChar();
            }
            swe = false;
        }
        // south and west only
        boolean sw;
        sw = posY == worldMap.getNorthLimit() && posY > worldMap.getSouthLimit() && posX == worldMap.getEastLimit() && posX > worldMap.getWestLimit();
        while (sw) {
            System.out.print("\nTowering glaciers block your path to the north, and giant mountains block your path to the east.");
            System.out.print("\nWhich direction would you like to go?\nSouth or West. (s/w): ");
            c = cleanChar();
            while (c != 'w' && c != 's') {
                System.out.print("You must chose South or West. (s/w): ");
                c = cleanChar();
            }
            sw = false;
        }
        // south and east only
        boolean se;
        se = posY == worldMap.getNorthLimit() && posY > worldMap.getSouthLimit() && posX < worldMap.getEastLimit() && posX == worldMap.getWestLimit();
        while (se) {
            System.out.print("\nTowering glaciers block your path to the north, and a vast ocean blocks your path to the west.");
            System.out.print("\nWhich direction would you like to go?\nSouth or East. (s/e): ");
            c = cleanChar();
            while (c != 'e' && c != 's') {
                System.out.print("You must chose South or East. (s/e): ");
                c = cleanChar();
            }
            se = false;
        }
        // north, east, or west only
        boolean nwe;
        nwe = posY < worldMap.getNorthLimit() && posY == worldMap.getSouthLimit() && posX < worldMap.getWestLimit() && posX > worldMap.getWestLimit();
        while (nwe) {
            System.out.print("\nThe jungle ends in a steep cliff which blocks your path to the south.");
            System.out.print("\nWhich direction would you like to go?\nNorth, West or East. (n/w/e): ");
            c = cleanChar();
            while (c != 'n' && c != 'w' && c != 'e') {
                System.out.print("You must chose North, West or East. (n/w/e): ");
                c = cleanChar();
            }
            nwe = false;
        }
        // north and west only
        boolean nw;
        nw = posY < worldMap.getNorthLimit() && posY == worldMap.getSouthLimit() && posX == worldMap.getEastLimit() && posX > worldMap.getWestLimit();
        while (nw) {
            System.out.print("\nThe jungle ends in a steep cliff which blocks your path to the south, and giant mountains block your path to the east.");
            System.out.print("\nWhich direction would you like to go?\nNorth or West. (n/w): ");
            c = cleanChar();
            while (c != 'w' && c != 'n') {
                System.out.print("You must chose North or West. (n/w): ");
                c = cleanChar();
            }
            nw = false;
        }
        // north and east only
        boolean ne;
        ne = posY < worldMap.getNorthLimit() && posY == worldMap.getSouthLimit() && posX < worldMap.getEastLimit() && posX == worldMap.getWestLimit();
        while (ne) {
            System.out.print("\nThe jungle ends in a steep cliff which blocks your path to the south, and a vast ocean blocks your path to the west.");
            System.out.print("\nWhich direction would you like to go?\nNorth or East. (n/e): ");
            c = cleanChar();
            while (c != 'n' && c != 'e') {
                System.out.print("You must chose North or East. (n/e): ");
                c = cleanChar();
            }
            ne = false;
        }
        // north, south, and west only
        boolean nsw;
        nsw = posY < worldMap.getNorthLimit() && posY > worldMap.getSouthLimit() && posX == worldMap.getEastLimit() && posX > worldMap.getWestLimit();
        while (nsw) {
            System.out.println("\nGiant mountains block your path to the east.");
            System.out.print("\nWhich direction would you like to go?\nNorth, West or South. (n/w/s): ");
            c = cleanChar();
            while (c != 'n' && c != 'w' && c != 's') {
                System.out.print("You must chose North, West or South. (n/w/s): ");
                c = cleanChar();
            }
            nsw = false;
        }
        // north, south, and east only nse
        boolean nse;
        nse = posY < worldMap.getNorthLimit() && posY > worldMap.getSouthLimit() && posX < worldMap.getEastLimit() && posX == worldMap.getWestLimit();
        while (nse) {
            System.out.println("\nA vast ocean blocks your path to the west.");
            System.out.print("\nWhich direction would you like to go?\nNorth, East or South. (n/e/s): ");
            c = cleanChar();
            while (c != 'n' && c != 'e' && c != 's') {
                System.out.print("You must chose North, East or South. (n/e/s): ");
                c = cleanChar();
            }
            nse = false;
        }
        // all directions nsew
        boolean nsew;
        nsew = posY < worldMap.getNorthLimit() && posY > worldMap.getSouthLimit() && posX < worldMap.getEastLimit() && posX > worldMap.getWestLimit();
        while (nsew) {
            System.out.print("\nWhich direction would you like to go?\nNorth, East, South or West. (n/e/s/w): ");
            c = cleanChar();
            while (c != 'n' && c != 'e' && c != 'w' && c != 's') {
                System.out.print("You must chose North, East, South or West. (n/e/s/w): ");
                c = cleanChar();
            }
            nsew = false;
        }
        moveCharacter(c);
        setBiome();
    }
    // This take in character movement selection, checks for out of bounds movement, the applies movement if able.
    public void moveCharacter(char input) {
        // check for map limits after moving
        if (input == 'n') {
            posY = posY + 1;
            if (posY > worldMap.getNorthLimit()) {
                System.out.println("Towering glaciers block your path, you can go no farther north");
                posY = worldMap.getNorthLimit();
            }
        } else if (input == 's') {
            posY = posY - 1;
            if (posY < worldMap.getSouthLimit()) {
                System.out.println("The jungle ends in a steep cliff, you can go no farther south.");
                posY = worldMap.getSouthLimit();
            }
        } else if (input == 'e') {
            posX = posX + 1;
            if (posX > worldMap.getEastLimit()) {
                System.out.println("The shore ends at a vast ocean, you can go no farther west.");
                posX = worldMap.getEastLimit();
            }
        } else if (input == 'w') {
            posX = posX - 1;
            if (posX < worldMap.getWestLimit()) {
                System.out.println("The giant mountains are impassable, you can go no farther east.");
                posX = worldMap.getWestLimit();
            }
        }
    }
    // Sets posX, posY, and posZ to town.
    public void returnToTown() {
        posY = 0;
        posX = 0;
        setBiome();
    }

    // OTHER METHODS:
    // This performs the level up adjustments to the character.
    public void levelUp() {
        level++;
        // This isn't used to increase player HP, it's just for displaying to the console.
        int hpIncrease = hpBase + constitutionMod;
        setSecondaryStats();
        System.out.print("\nYou are now level " + level + ".\n");
        System.out.print("You have gained " + hpIncrease + " HP, bringing your max HP to " + maxHP + ".");
    }

    // This accepts a single char (but doesn't prompt) and cleans the input to lowercase.
    private char cleanChar() {
        Scanner s = new Scanner(System.in);
        char c  = s.next().charAt(0);
        c = Character.toLowerCase(c);
        return c;
    }

    // Prints player stats to console.
    public void printPlayerStats() {
        System.out.println("\nCharacter Name: " + name + "");
        getStats();
    }
}