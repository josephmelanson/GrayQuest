import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Game {

    // OBJECTS:
    private Player player = new Player(); // Used to access Player class for stat and combat calculations.
    private Monster monster = new Monster(); // Used to access monster class for stat and combat calculations.
    private Place place = new Place(); // Used to access map boundaries.
    private Item item = new Item(); // Used to access itemID manipulation methods in item.
    private final Text text = new Text(); // Used to access stored blocks of text.
    private final Difficulty difficulty = new Difficulty(); // Used to access difficulty options in stat and combat calculations.

    // METHODS:
    // GAME METHODS:
    // This is the main method which runs/loops the game.
    public void runGame() {
        // START game loop
        // declare variables
        boolean preGame = true;
        while (preGame) {
            System.out.println(text.getIntroText());
            preGame();
            boolean gameLoop = true;
            while (gameLoop) {
                if (checkTown() && player.getCurrentHP() > 0) {
                    townMenu();
                } else if (!checkTown() && player.getCurrentHP() > 0) {
                    mapMenu();
                } else {
                    gameLoop = false;
                }
            }
            System.out.print("\nGame over. Choose Yes to reload or No to quit. (y/n): ");
            char c = cleanChar();
            while (c != 'y' && c != 'n') {
                System.out.print("\nYou must choose Yes to reload, or No to quit. (y/n): ");
                c = cleanChar();
            }
            preGame = c == 'y';
        }
    }
    // This method handles character loading and creation.
    private void preGame() {
        player.newCharacter(); // this resets character data in the event of a soft reload
        File f = new File("savedata.txt");
        char c;
        if (f.exists()) {
            System.out.print("Will you Load a game or start a New one? (l/n): ");
            c = cleanChar();
            while (c != 'l' && c != 'n') {
                System.out.print("\nYou must Load a game or start a New one. (l/n): ");
                c = cleanChar();
            }
            if (c == 'l') {
                System.out.print("\n-----LOAD-----\n");
                readLoadData();
                player.setSecondaryStats();
                player.getCharacterSummary();
            } else {
                characterCreation();
            }
        } else characterCreation();
    }

    private void characterCreation() {
        Scanner s = new Scanner(System.in);
        System.out.print("\nEnter your character's name: ");
        String characterName = s.next();
        player.setName(characterName);
        System.out.print(text.getRacePrompt());
        char c = cleanChar();
        while (c != 'h' && c != 'o' && c != 'd' && c != 'e') {
            System.out.print("\nYou must choose Human, Orc, Dwarf or Elf. (h/o/d/e): ");
            c = cleanChar();
        }
        if (c == 'h') {
            player.setRace(1);
        } else if (c == 'o') {
            player.setRace(2);
        } else if (c == 'd') {
            player.setRace(3);
        } else {
            player.setRace(4);
        }
        boolean statLoop = true;
        while (statLoop) {
            player.setMainStats();
            player.printPlayerStats();
            System.out.print("\n\nThese are your stats. Accept? (y/n): ");
            c = cleanChar();
            while (c != 'n' && c != 'y') {
                System.out.print("\n\nYou must choose Yes to accept or No to re-roll. (y/n): ");
                c = cleanChar();
            }
            statLoop = c != 'y';
        }
    }

    // TOWN MENU METHODS:
    // This is the main town menu.
    private void townMenu() {
        System.out.print("\n-----TOWN MENU-----\n\n");
        System.out.print(getTownTextWithHint(player.getLevel()));
        if (checkBoss()) {
            System.out.print("\n\nWill you Rest, Save, visit the Market, return the Produce or Leave? (r/s/m/r/l): ");
        } else {
            System.out.print("\n\nWill you Rest, Save, visit the Market or Leave? (r/s/m/l): ");
        }
        char c = cleanChar();
        while (c != 'r' && c != 'l' && c != 's' && c != 'm' && c != 'p') {
            if (checkBoss()) {
                System.out.print("\n\nWill you Rest, Save, visit the Market, return the Produce or Leave? (r/s/m/r/l): ");
            } else {
                System.out.print("\n\nWill you Rest, Save, visit the Market or Leave? (r/s/m/l): ");
            }
            c = cleanChar();
        }
        if (c == 'r') {
            System.out.println("\nYou rest and recover all your hp.");
            // checks for level and if so levels
            if (player.checkForLevelUp()) {
                player.levelUp();
            }
            // restores player hp
            player.setCurrentHP(player.getMaxHP());
        } else if (c == 'l') {
            System.out.println("\nYou leave town.");
            player.moveCharacterPrompt();
        } else if (c == 's') {
            writeSaveData();
        } else if (c == 'm') {
            shopMenu();
        } else {
            if (checkBoss()) {
                towerMenu();
            } else {
                System.out.print("\nYou have not gathered all the glass produce.\n"); //Will you Rest, Save, visit the Market or Leave? (r/s/m/l):
            }
        }
    }
    // These are the shop submenus.
    private void shopMenu() {
        boolean shopLoop = true;
        while (shopLoop) {
            System.out.println("\n-----MARKET MENU-----");
            System.out.print("\nYou have " + player.getMoney() + " gold.\n\nChoose Armor, Weapons, Shields, Potions, or Exit. (a/w/s/p/e): ");
            char c = cleanChar();
            while (c != 'a' && c != 'w' && c != 's' && c != 'p' && c != 'e') {
                System.out.println("You must choose Armor, Weapons, Shields, Potions, or Exit. (a/w/s/p/e): ");
                c = cleanChar();
            }
            if (c == 'a') {
                armorMenu();
            } else if (c == 'w') {
                weaponMenu();
            } else if (c == 's') {
                shieldMenu();
            } else if (c == 'p') {
                potionMenu();
            } else shopLoop = false;
        }
    }
    private void armorMenu() {
        System.out.print("\n-----ARMOR SHOP-----\n");
        boolean armorLoop = true;
        while (armorLoop) {
            System.out.print("\nYou are wearing " + item.getItemName(player.getEquippedArmorID()) + ".\n");
            System.out.print("\nYou have " + player.getMoney() + " gold.\n\nChoose your plate:\n\nCommon (+1): " + difficulty.getCommonPrice() + " GP\nUncommon (+2): " + difficulty.getUncommonPrice() + " GP\nRare (+3): " + difficulty.getRarePrice() + " GP\nVery Rare (+4): " + difficulty.getVRarePrice() + " GP\n\nor Exit (c/u/r/v/e): ");
            char c = cleanChar();
            while (c != 'c' && c != 'u' && c != 'r' && c != 'v' && c != 'e') {
                System.out.print("You must choose (c/u/r/v/e): ");
                c = cleanChar();
            }
            if (c == 'c') {
                if (player.checkMoney(difficulty.getCommonPrice())) {
                    if (player.getEquippedArmorBonus() >= 0.1) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedArmorID()));
                        System.out.println("\nYou sell your " + item.getItemName(player.getEquippedArmorID()) + " for " + item.sellItem(player.getEquippedArmorID()) + " GP.");
                        player.setEquippedArmor(1.1);
                        System.out.println("You buy the " + item.getItemName(1.1) + " for " + difficulty.getCommonPrice() + " GP.");
                        player.setMoney(-difficulty.getCommonPrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'u') {
                if (player.checkMoney(difficulty.getUncommonPrice())) {
                    if (player.getEquippedArmorBonus() >= 0.2) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedArmorID()));
                        player.setEquippedArmor(1.2);
                        player.setMoney(-difficulty.getUncommonPrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'r') {
                if (player.checkMoney(difficulty.getRarePrice())) {
                    if (player.getEquippedArmorBonus() >= 0.3) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedArmorID()));
                        player.setEquippedArmor(1.3);
                        player.setMoney(-difficulty.getRarePrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'v') {
                if (player.checkMoney(difficulty.getVRarePrice())) {
                    if (player.getEquippedArmorBonus() >= 0.4) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedArmorID()));
                        player.setEquippedArmor(1.4);
                        player.setMoney(-difficulty.getVRarePrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else armorLoop = false;
        }
    }
    private void weaponMenu() {
        System.out.print("\n-----WEAPON SHOP-----\n");
        boolean weaponLoop = true;
        while (weaponLoop) {
            System.out.print("\nYou are wearing a " + item.getItemName(player.getEquippedSwordID()) + ".\n");
            System.out.print("\nYou have " + player.getMoney() + " gold.\n\nChoose your longsword:\n\nCommon (+1): " + difficulty.getCommonPrice() + " GP\nUncommon (+2): " + difficulty.getUncommonPrice() + " GP\nRare (+3): " + difficulty.getRarePrice() + " GP\nVery Rare (+4): " + difficulty.getVRarePrice() + " GP\n\nor Exit (c/u/r/v/e): ");
            char c = cleanChar();
            while (c != 'c' && c != 'u' && c != 'r' && c != 'v' && c != 'e') {
                System.out.print("You must choose (c/u/r/v/e): ");
                c = cleanChar();
            }
            if (c == 'c') {
                if (player.checkMoney(difficulty.getCommonPrice())) {
                    if (player.getEquippedSwordBonus() >= 0.1) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedSwordID()));
                        player.setEquippedSword(2.1);
                        player.setMoney(-difficulty.getCommonPrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'u') {
                if (player.checkMoney(difficulty.getUncommonPrice())) {
                    if (player.getEquippedSwordBonus() >= 0.2) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedSwordID()));
                        player.setEquippedSword(2.2);
                        player.setMoney(-difficulty.getUncommonPrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'r') {
                if (player.checkMoney(difficulty.getRarePrice())) {
                    if (player.getEquippedSwordBonus() >= 0.3) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedSwordID()));
                        player.setEquippedSword(2.3);
                        player.setMoney(-difficulty.getRarePrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'v') {
                if (player.checkMoney(difficulty.getVRarePrice())) {
                    if (player.getEquippedSwordBonus() >= 0.4) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedSwordID()));
                        player.setEquippedSword(2.4);
                        player.setMoney(-difficulty.getVRarePrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else weaponLoop = false;
        }
    }
    private void shieldMenu() {
        System.out.print("\n-----SHIELD SHOP-----\n");
        boolean shieldLoop = true;
        while (shieldLoop) {
            System.out.print("\nYou are wearing a " + item.getItemName(player.getEquippedShieldID()) + ".\n");
            System.out.print("\nYou have " + player.getMoney() + " gold.\n\nChoose your shield:\n\nCommon (+1): " + difficulty.getCommonPrice() + " GP\nUncommon (+2): " + difficulty.getUncommonPrice() + " GP\nRare (+3): " + difficulty.getRarePrice() + " GP\nVery Rare (+4): " + difficulty.getVRarePrice() + " GP\n\nor Exit (c/u/r/v/e): ");
            char c = cleanChar();
            while (c != 'c' && c != 'u' && c != 'r' && c != 'v' && c != 'e') {
                System.out.print("You must choose (c/u/r/v/e): ");
                c = cleanChar();
            }
            if (c == 'c') {
                if (player.checkMoney(difficulty.getCommonPrice())) {
                    if (player.getEquippedShieldBonus() >= 0.1) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedShieldID()));
                        player.setEquippedShield(3.1);
                        player.setMoney(-difficulty.getCommonPrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'u') {
                if (player.checkMoney(difficulty.getUncommonPrice())) {
                    if (player.getEquippedShieldBonus() >= 0.2) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedShieldID()));
                        player.setEquippedShield(3.2);
                        player.setMoney(-difficulty.getUncommonPrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'r') {
                if (player.checkMoney(difficulty.getRarePrice())) {
                    if (player.getEquippedShieldBonus() >= 0.3) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedShieldID()));
                        player.setEquippedShield(3.3);
                        player.setMoney(-difficulty.getRarePrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'v') {
                if (player.checkMoney(difficulty.getVRarePrice())) {
                    if (player.getEquippedShieldBonus() >= 0.4) {
                        System.out.println("You're already wearing better. No sale.");
                    } else {
                        player.setMoney(item.sellItem(player.getEquippedShieldID()));
                        player.setEquippedShield(3.4);
                        player.setMoney(-difficulty.getVRarePrice());
                    }
                } else {
                    System.out.println("You can't afford that.");
                }
            } else shieldLoop = false;
        }
    }
    private void potionMenu() {
        System.out.print("\n-----POTION SHOP-----\n");
        boolean potionLoop = true;
        while (potionLoop) {
            System.out.print("\nYou have " + player.getHealPotionsOne() + " Healing Potions, " + player.getHealPotionsTwo() + " Greater Healing Potions, " + player.getHealPotionsThree() + " Superior Healing Potions and " + player.getHealPotionsFour() + " Very Supreme Healing Potions.\n");
            System.out.print("\nYou have " + player.getMoney() + " gold.\n\nChoose your potion:\n\nHealing Potion (2d4+2): " + difficulty.getCommonPrice() + " GP\nGreater Healing (4d4+4): " + difficulty.getUncommonPrice() + " GP\nSuperior Healing Potion (8d4+8): " + difficulty.getRarePrice() + " GP\nVery Supreme healing (10d4+20): " + difficulty.getVRarePrice() + " GP\n\nor Exit? (h/g/s/v/e): ");
            char c = cleanChar();
            while (c != 'h' && c != 'g' && c != 's' && c != 'v' && c != 'e') {
                System.out.print("You must choose (h/g/s/v/e): ");
                c = cleanChar();
            }
            if (c == 'h') {
                if (player.checkMoney(difficulty.getCommonPrice())) {
                    player.setHealPotionsOne(1);
                    player.setMoney(-difficulty.getCommonPrice());
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'g') {
                if (player.checkMoney(difficulty.getUncommonPrice())) {
                    player.setHealPotionsTwo(1);
                    player.setMoney(-difficulty.getUncommonPrice());
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 's') {
                if (player.checkMoney(difficulty.getRarePrice())) {
                    player.setHealPotionsThree(1);
                    player.setMoney(-difficulty.getRarePrice());
                } else {
                    System.out.println("You can't afford that.");
                }
            } else if (c == 'v') {
                if (player.checkMoney(difficulty.getVRarePrice())) {
                    player.setHealPotionsFour(1);
                    player.setMoney(-difficulty.getVRarePrice());
                } else {
                    System.out.println("You can't afford that.");
                }
            } else potionLoop = false;
        }
    }
    // This uses a random number and player level to determine town gossip with dungeons hints.
    public String getTownTextWithHint(int lvl) {
        int i = (int) (Math.random() * 2) + 1;
        if (lvl > 1 && !player.isTropicalDungeonClear()) {
            if (i == 1) {
                return text.getJungleHint();
            } return text.getRandomTownText();
        } else if (lvl > 4 && !player.isShoresDungeonClear()) {
            if (i == 1) {
                return text.getShoreHint();
            } return text.getRandomTownText();
        } else if (lvl > 8 && !player.isMountainDungeonClear()) {
            if (i == 1) {
                return text.getMountainHint();
            } return text.getRandomTownText();
        } else if (lvl > 12 && !player.isColdDungeonClear()) {
            if (i == 1) {
                return text.getColdHint();
            } return text.getRandomTownText();
        } else if (lvl > 16 && !player.isPlainsDungeonClear()) {
            if (i == 1) {
                return text.getPlainsHint();
            } return text.getRandomTownText();
        } else return text.getRandomTownText();
    }

    // MAP MENU:
    private void mapMenu() {
        System.out.print("\n-----MAP MENU-----");
        char c = 'x';
        double d = 0.0;
        boolean mapLoop = true;
        while (mapLoop) {
            System.out.print("\n");
            place.getBiome(player.posZ);
            if (player.getTotalPotions() <= 0) {
                System.out.print("\nWill you Teleport to town, Search the area or Leave? (t/s/l): ");
                c = cleanChar();
                while (c != 't' && c != 's' && c != 'l') {
                    System.out.print("You must Teleport to town, Search the area or Leave. (t/s/l): ");
                    c = cleanChar();
                }
            } else if (player.getTotalPotions() > 0) {
                System.out.print("\nWill you Teleport to town, Search the area, use a Potion, or Leave? (t/s/p/l): ");
                c = cleanChar();
                while (c != 't' && c != 's' && c != 'l' && c != 'p') {
                    System.out.print("You must Teleport to town, Search the area, use a Potion, or Leave. (t/s/p/l): ");
                    c = cleanChar();
                }
            }
            if (c == 't') {
                System.out.println("You grasp your amulet and focus in prayer...");
                player.returnToTown();
                mapLoop = false;
            } else if (c == 'l') {
                player.moveCharacterPrompt();
            } else if (c == 'p') {
                potionUseMenu();
            } else {
                if (place.rollForDungeon(player.posZ, player.getLevel())) {
                    // checkDungeon returns true if player has not visited the dungeon
                    // and generates the boss monster ID based on the biome
                    if (checkDungeon(player.getPosZ())) {
                        d = 0.0 + (double) player.getPosZ() / 10;
                    }
                } else if (place.rollForMonster()) {
                    d = place.rollForMonsterByLevel(player.getPosZ(), player.getLevel());
                } else {
                    d = 0.0;
                }
                if (d > 0.0) {
                    if (!runCombat(d)) {
                        mapLoop = false;
                    }
                } else {
                    //System.out.println("\nNo monsters, lucky!");
                    int i = item.rollForMoneyByLevel(player.getLevel());
                    player.setMoney(i);
                    System.out.print("\nYou find " + i + " gold " + text.getRandomMoneyDesc() + ".\n");
                }
            }
        }
    }

    // COMBAT MENU METHODS:
    // This runs combat, returning true if the player is alive after combat has finished.
    private boolean runCombat(double monsterID) {
        loadMonsterStats(monsterID);
        System.out.print("\n-----COMBAT MENU-----\n\n");
        System.out.print(monster.getFlavourText());
        rollInitiative(player.getDexterityMod(), monster.getDexterityMod());
        boolean combatLoop = true;
        while (combatLoop) {
            if (combatMenu()) {
                combatLoop = combatRound();
                if (monster.getCurrentHP() <= 0) {
                    System.out.print("\n\nYou have defeated the " + monster.getName() + "!");
                    player.setDungeonClear(monsterID);
                    player.addExperience();
                    if (player.checkForLevelUp()) {
                        System.out.print("\nYou have defeated enough monsters to earn a level!\nReturn to town and rest to level up.\n");
                    }
                    double d = item.createRandomItem(player.getLevel());
                    if (d == 0.0) {
                        int i = item.rollForMoneyByLevel(player.getLevel());
                        player.setMoney(i);
                        System.out.print("\nYou find " + i + " gold on the " + monster.getName() + ".");
                    } else {
                        //System.out.println("-----DEBUG----- Checking itemID: " + d);
                        System.out.print("\nYou find a " + item.getItemName(d) + " on the " + monster.getName() + ".\n");
                        if (d <= 3.5 && d >= 1.0) {
                            player.checkForEquipUpgrade(d);
                        } else if (d >= 4.0) {
                            player.addPotion(d);
                        }
                    }
                    combatLoop = false;
                }
            } else {
                combatLoop = false;
            }
        }
        return player.getCurrentHP() > 0;
    }
    // This provides option to the player in combat.
    private boolean combatMenu() {
        monster.setDescriptor();
        System.out.print("\n\nThe " + monster.getName() + " looks " + monster.getDescriptor() + "!\n");
        System.out.print("\nYou have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\n");
        if (potionCheck()) {
            System.out.print("\nWill you Attack, use a Potion or Run? (a/p/r): ");
        } else {
            System.out.print("\nWill you Attack or Run? (a/r): ");
        }
        char c = cleanChar();
        while (c != 'a' && c != 'p' && c != 'r') {
            if (potionCheck()) {
                System.out.print("\nYou must Attack, use a Potion or Run. (a/p/r): ");
            } else {
                System.out.print("\nYou must Attack or Run. (a/r): ");
            }
            c = cleanChar();
        }
        while (c == 'p' && player.getTotalPotions() <= 0) {
            System.out.print("\nYou have no potions left.\nYou must Attack or Run. (a/r): ");
            c = cleanChar();
        }
        if (c == 'p' && player.getTotalPotions() >= 1) {
            potionUseMenu();
        }
        if (c == 'r') {
            System.out.print("\nYou try to run...\n");
            return !runAway();
        } else {
            return true;
        }
    }
    // This allows the player a chance to escape from combat.
    private boolean runAway() {
        int i = (int) (Math.random() * difficulty.getRunChance()) + 1;
        if (i == 1) {
            System.out.println("You escape from the " + monster.getName() + ".");
            return true;
        } else {
            System.out.println("You can't escape the " + monster.getName() + ".");
            return false;
        }
    }
    // This handles the combat math. Rolls for initiative, hit and damage.
    private boolean combatRound() {
        int i;
        System.out.print("\n");
        // if the player goes first
        if (testInitiative()) {
            // player rolls to strike first
            if (rollToHit(player.getFinalToHit(), monster.getFinalAC())) {
                i = rollForDamage(player.getWeaponMax(), player.getWeaponMin(), player.getFinalDamageBonus());
                System.out.print("You deal " + i + " damage to the " + monster.getName() + ".\n");
                monster.setCurrentHP(-i);
            } else {
                System.out.print("You miss the " + monster.getName() + ".\n");
            }
            // check if the monster survived
            if (monster.getCurrentHP() > 0) {
                // then the monster rolls to strike first
                if (rollToHit(monster.getFinalToHit(), player.getFinalAC())) {
                    i = rollForDamage(monster.getWeaponMax(), monster.getWeaponMin(), monster.getFinalDamageBonus());
                    System.out.print("The " + monster.getName() + " hits you for " + i + " damage!");
                    player.setCurrentHP(-i);
                } else {
                    System.out.print("The " + monster.getName() + " misses you.");
                }
            }
            // if the monster goes first
        } else {
            // the monster rolls to strike
            if (rollToHit(monster.getFinalToHit(), player.getFinalAC())) {
                i = rollForDamage(monster.getWeaponMax(), monster.getWeaponMin(), monster.getFinalDamageBonus());
                System.out.print("The " + monster.getName() + " hits you for " + i + " damage!\n");
                player.setCurrentHP(-i);
            } else {
                System.out.print("The " + monster.getName() + " misses you.\n");
            }
            // check if the player survived
            if (player.getCurrentHP() > 0) {
                // player rolls to strike
                if (rollToHit(player.getFinalToHit(), monster.getFinalAC())) {
                    i = rollForDamage(player.getWeaponMax(), player.getWeaponMin(), player.getFinalDamageBonus());
                    System.out.print("You deal " + i + " damage to the " + monster.getName() + ".");
                    monster.setCurrentHP(-i);
                } else {
                    System.out.print("You miss the " + monster.getName() + ".");
                }
            }
        }
        return player.getCurrentHP() > 0;
    }
    private void towerMenu() {
        System.out.print("\n-----TOWER MENU-----\n");
        char c;
        boolean towerLoop = true;
        while (towerLoop) {
            if (!player.isTowerOneClear() && !player.isTowerTwoClear()) {
                System.out.print(text.getTowerDesc(0)); // Tower entrance text, 0 = Tower level 1
                System.out.print("\nYou have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\nWill you Fight or Run? (f/r): ");
                c = cleanChar();
                while (c != 'f' && c != 'r') {
                    System.out.print("\nYou must choose to Fight or Run. (f/r): ");
                    c = cleanChar();
                }
                if (c == 'f') {
                    runCombat(1.0);
                    if (player.currentHP > 0) {
                        player.setTowerOneClear(true);
                    } else { towerLoop = false; }
                } else {
                    towerLoop = false;
                }
            } else if (player.isTowerOneClear() && !player.isTowerTwoClear()) {
                System.out.print(text.getTowerDesc(1)); // Tower second level text, 1 = Tower level 2
                System.out.print("\nYou have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\nWill you Fight or Run? (f/r): ");
                c = cleanChar();
                while (c != 'f' && c != 'r') {
                    System.out.print("\nYou must choose to Fight or Run. (f/r): ");
                    c = cleanChar();
                }
                if (c == 'f') {
                    runCombat(2.0);
                    if (player.currentHP > 0) {
                        player.setTowerTwoClear(true);
                    } else { towerLoop = false; }
                } else {
                    towerLoop = false;
                }
            } else {
                System.out.print(text.getTowerDesc(2)); // Tower third level text, 2 = Tower level 3
                System.out.print("\nYou have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\nWill you Fight or Run? (f/r): ");
                c = cleanChar();
                while (c != 'f' && c != 'r') {
                    System.out.print("\nYou must choose to Fight or Run. (f/r): ");
                    c = cleanChar();
                }
                if (c == 'f') {
                    runCombat(3.0);
                    if (player.currentHP > 0) {
                        System.out.println(text.getWinText());
                        System.out.print("\n-----GAME OVER-----\n");
                        towerLoop = false;
                    }
                } else {
                    towerLoop = false;
                }
            }
        }
        player.setTowerOneClear(false);
        player.setTowerTwoClear(false);
    }

    // COMBAT MATH METHODS:
    private void loadMonsterStats(double monsterID) {
        // This reduces initializes d with a value ~0.1... less than it should be
        int i = (int) monsterID;
        double d = monsterID - i;
        // This rounds d back up to it's proper value
        d = (double) Math.round(d * 10) / 10;
        // Loads Tower monsters
        if (d == 0.0) {
            if (i == 1) {
                monster.setMonsterOnePointZero();
            } else if (i == 2) {
                monster.setMonsterTwoPointZero();
            } else {
                monster.setMonsterThreePointZero();
            }
            // Loads Cold monsters
        } else if (d == 0.1) {
            if (i == 0) {
                monster.setMonsterZeroPointOne();
            } else if (i == 1) {
                monster.setMonsterOnePointOne();
            } else if (i == 2) {
                monster.setMonsterTwoPointOne();
            } else if (i == 3) {
                monster.setMonsterThreePointOne();
            } else if (i == 4) {
                monster.setMonsterFourPointOne();
            } else if (i == 5) {
                monster.setMonsterFivePointOne();
            } else if (i == 6) {
                monster.setMonsterSixPointOne();
            } else if (i == 7) {
                monster.setMonsterSevenPointOne();
            } else if (i == 8) {
                monster.setMonsterEightPointOne();
            } else if (i == 9) {
                monster.setMonsterNinePointOne();
            }
            // Loads Tropical monsters
        } else if (d == 0.2) {
            if (i == 0) {
                monster.setMonsterZeroPointTwo();
            } else if (i == 1) {
                monster.setMonsterOnePointTwo();
            } else if (i == 2) {
                monster.setMonsterTwoPointTwo();
            } else if (i == 3) {
                monster.setMonsterThreePointTwo();
            } else if (i == 4) {
                monster.setMonsterFourPointTwo();
            } else if (i == 5) {
                monster.setMonsterFivePointTwo();
            } else if (i == 6) {
                monster.setMonsterSixPointTwo();
            } else if (i == 7) {
                monster.setMonsterSevenPointTwo();
            } else if (i == 8) {
                monster.setMonsterEightPointTwo();
            } else if (i == 9) {
                monster.setMonsterNinePointTwo();
            }
            // Loads Plains monsters
        } else if (d == 0.3) {
            if (i == 0) {
                monster.setMonsterZeroPointThree();
            } else if (i == 1) {
                monster.setMonsterOnePointThree();
            } else if (i == 2) {
                monster.setMonsterTwoPointThree();
            } else if (i == 3) {
                monster.setMonsterThreePointThree();
            } else if (i == 4) {
                monster.setMonsterFourPointThree();
            } else if (i == 5) {
                monster.setMonsterFivePointThree();
            } else if (i == 6) {
                monster.setMonsterSixPointThree();
            } else if (i == 7) {
                monster.setMonsterSevenPointThree();
            } else if (i == 8) {
                monster.setMonsterEightPointThree();
            } else if (i == 9) {
                monster.setMonsterNinePointThree();
            }
            // Loads Shores monsters
        } else if (d == 0.4) {
            if (i == 0) {
                monster.setMonsterZeroPointFour();
            } else if (i == 1) {
                monster.setMonsterOnePointFour();
            } else if (i == 2) {
                monster.setMonsterTwoPointFour();
            } else if (i == 3) {
                monster.setMonsterThreePointFour();
            } else if (i == 4) {
                monster.setMonsterFourPointFour();
            } else if (i == 5) {
                monster.setMonsterFivePointFour();
            } else if (i == 6) {
                monster.setMonsterSixPointFour();
            } else if (i == 7) {
                monster.setMonsterSevenPointFour();
            } else if (i == 8) {
                monster.setMonsterEightPointFour();
            } else if (i == 9) {
                monster.setMonsterNinePointFour();
            }
            // Loads Mountain monsters
        } else if (d == 0.5) {
            if (i == 0) {
                monster.setMonsterZeroPointFive();
            } else if (i == 1) {
                monster.setMonsterOnePointFive();
            } else if (i == 2) {
                monster.setMonsterTwoPointFive();
            } else if (i == 3) {
                monster.setMonsterThreePointFive();
            } else if (i == 4) {
                monster.setMonsterFourPointFive();
            } else if (i == 5) {
                monster.setMonsterFivePointFive();
            } else if (i == 6) {
                monster.setMonsterSixPointFive();
            } else if (i == 7) {
                monster.setMonsterSevenPointFive();
            } else if (i == 8) {
                monster.setMonsterEightPointFive();
            } else if (i == 9) {
                monster.setMonsterNinePointFive();
            }
        }
    }
    private boolean testInitiative() {
        return player.getInitiative() > monster.getInitiative();
    }
    private void rollInitiative(int playerDexMod, int monsterDexMod) {
        int monsterInitRoll = 0;
        int playerInitRoll = 0;
        while (playerInitRoll == monsterInitRoll) {
            playerInitRoll = ((int) (Math.random() * 20) + 1) + playerDexMod;
            monsterInitRoll = ((int) (Math.random() * 20) + 1) + monsterDexMod;
        }
        monster.setInitiative(monsterInitRoll);
        player.setInitiative(playerInitRoll);
    }
    private boolean rollToHit(int attackerToHit, int defenderAC) {
        int finalToHit = (int) ((Math.random() * 20) + 1) + attackerToHit;
        return finalToHit >= defenderAC;
    }
    private int rollForDamage(int damageMax, int damageMin, int damageBonus) {
        int i = (int) (Math.random() * (damageMax - damageMin)) + damageMin;
        i += damageBonus;
        if (i < 0) {
            i = 0;
        }
        return i;
    }

    // COMBAT POTION MENU METHODS:
    // This returns true if the player has any potions left.
    private boolean potionCheck() { return player.getTotalPotions() >= 1; }
    // These display potion option to the player during combat and map.
    private void potionUseMenu() {
        int i;
        char c;
        boolean potionLoop = true;
        // start menu
        System.out.println("-----USE POTION MENU-----");
        while (potionLoop) {
            if (player.getTotalPotions() <= 0) {
                System.out.println("You have no potions.");
                break;
            } else {
                System.out.println("You have:");
                displayPotions();
                c = cleanChar();
            }
            while (c != 'h' && c != 'g' && c != 's' && c != 'v' && c != 'e') {
                System.out.print("You must choose:\n");
                displayPotions();
                c = cleanChar();
            }
            if (c == 'h') {
                player.setHealPotionsOne(-1);
                i = item.rollHealPotOne();
                player.setCurrentHP(i);
                System.out.println("\nYou regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            } else if (c == 'g') {
                player.setHealPotionsTwo(-1);
                i = item.rollHealPotTwo();
                player.setCurrentHP(i);
                System.out.println("\nYou regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            } else if (c == 's') {
                player.setHealPotionsThree(-1);
                i = item.rollHealPotThree();
                player.setCurrentHP(i);
                System.out.println("\nYou regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            } else if (c == 'v') {
                player.setHealPotionsFour(-1);
                i = item.rollHealPotFour();
                player.setCurrentHP(i);
                System.out.println("\nYou regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            } else {
                potionLoop = false;
            }
        }
    }
    private void displayPotions() {
        String options = "\nor Exit? (";
        if (player.getHealPotionsOne() > 0) {
            System.out.print("\nHealing Potions (2d4+2): " + player.getHealPotionsOne() + "\n");
            options += "h/";
        }
        if (player.getHealPotionsTwo() > 0) {
            System.out.print("\nGreater Healing Potions (4d4+4): " + player.getHealPotionsTwo() + "\n");
            options += "g/";
        }
        if (player.getHealPotionsThree() > 0) {
            System.out.print("\nSuperior Healing Potion (8d4+8): " + player.getHealPotionsThree() + "\n");
            options += "s/";
        }
        if (player.getHealPotionsFour() > 0) {
            System.out.print("\nVery Supreme Healing Potion (10d4+10)" + player.getHealPotionsFour() + "\n");
            options += "v/";
        }
        options += "e): ";
        System.out.print(options);
    }

    // READ/WRITE METHODS:
    // This writes character data to the save file.
    private void writeSaveData() {
        try {
            java.io.File f = new java.io.File("savedata.txt");
            if (f.createNewFile()) {
                System.out.println("No save exists. Saving...");
            } else {
                System.out.println("Old save exists. Overwriting...");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter fW = new FileWriter("savedata.txt");
            fW.write(player.getName());
            fW.write(System.lineSeparator());
            int[] intData = {player.getLevel(), player.getRaceInt(), player.getExperience(), player.getMoney(), player.getPosZ(), player.getPosX(), player.getPosY(), player.getCurrentHP(), player.getMaxHP(), player.getStrength(), player.getConstitution(), player.getDexterity(), player.getHealPotionsOne(), player.getHealPotionsTwo(), player.getHealPotionsThree(), player.getHealPotionsFour()};
            for (int intDatum : intData) {
                fW.write(String.valueOf(intDatum));
                fW.write(System.lineSeparator());
            }
            double[] doubleData = {player.getEquippedArmorID(), player.getEquippedShieldID(), player.getEquippedSwordID()};
            for (double doubleDatum : doubleData) {
                fW.write(String.valueOf(doubleDatum));
                fW.write(System.lineSeparator());
            }
            boolean[] boolData = {player.isPlainsDungeonClear(), player.isColdDungeonClear(), player.isShoresDungeonClear(), player.isMountainDungeonClear(), player.isTropicalDungeonClear(), player.isTowerOneClear(), player.isTowerTwoClear()};
            for (boolean boolDatum : boolData) {
                fW.write(String.valueOf(boolDatum));
                fW.write(System.lineSeparator());
            }
            fW.close();
            System.out.println("...save complete!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // This reads character data from the save file and sets it to the current player.
    private void readLoadData() {
        try {
            java.io.File f = new java.io.File("savedata.txt");
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                for (int i = 0; i < 27; i++) {
                    String data = s.nextLine();
                    if (i == 0 ){
                        player.setName(data);
                    } else if (i == 1) {
                        player.setLevel(Integer.parseInt(data));
                    } else if (i == 2) {
                        player.setRace(Integer.parseInt(data));
                    } else if (i == 3) {
                        player.setExperience(Integer.parseInt(data));
                    } else if (i == 4) {
                        player.setMoney(Integer.parseInt(data));
                    } else if (i == 5) {
                        player.setPosZ(Integer.parseInt(data));
                    } else if (i == 6) {
                        player.setPosX(Integer.parseInt(data));
                    } else if (i == 7) {
                        player.setPosY(Integer.parseInt(data));
                    } else if (i == 8) {
                        player.setCurrentHP(Integer.parseInt(data));
                    } else if (i == 9) {
                        player.setMaxHPLoad(Integer.parseInt(data));
                    } else if (i == 10) {
                        player.setStrength(Integer.parseInt(data));
                    } else if (i == 11) {
                        player.setConstitution(Integer.parseInt(data));
                    } else if (i == 12) {
                        player.setDexterity(Integer.parseInt(data));
                    } else if (i == 13) {
                        player.setHealPotionsOne(Integer.parseInt(data));
                    } else if (i == 14) {
                        player.setHealPotionsTwo(Integer.parseInt(data));
                    } else if (i == 15) {
                        player.setHealPotionsThree(Integer.parseInt(data));
                    } else if (i == 16) {
                        player.setHealPotionsFour(Integer.parseInt(data));
                    } else if (i == 17) {
                        player.setEquippedArmor(Double.parseDouble(data));
                    } else if (i == 18) {
                        player.setEquippedShield(Double.parseDouble(data));
                    } else if (i == 19) {
                        player.setEquippedSword(Double.parseDouble(data));
                    } else if (i == 20) {
                        player.setPlainsDungeonClear(Boolean.parseBoolean(data));
                    } else if (i == 21) {
                        player.setColdDungeonClear(Boolean.parseBoolean(data));
                    } else if (i == 22) {
                        player.setShoresDungeonClear(Boolean.parseBoolean(data));
                    } else if (i == 23) {
                        player.setMountainDungeonClear(Boolean.parseBoolean(data));
                    } else if (i == 24) {
                        player.setTropicalDungeonClear(Boolean.parseBoolean(data));
                    } else if (i == 25) {
                        player.setTowerOneClear(Boolean.parseBoolean(data));
                    } else {
                        player.setTowerTwoClear(Boolean.parseBoolean(data));
                    }
                }
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // OTHER METHODS:
    // This accepts a single char (but doesn't prompt) and cleans the input to lowercase.
    private char cleanChar() {
        Scanner s = new Scanner(System.in);
        char c = s.next().charAt(0);
        c = Character.toLowerCase(c);
        return c;
    }

    // This returns true if the player has not cleared the dungeon in the supplied int biome.
    private boolean checkDungeon(int z) {
        if (z == 1) {
            if (player.isColdDungeonClear()) {
                System.out.println("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                System.out.println("You discover the frozen dungeon!");
                return true;
            }
        } else if (z == 2) {
            if (player.isTropicalDungeonClear()) {
                System.out.println("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                System.out.println("You discover the jungle dungeon!");
                return true;
            }
        } else if (z == 3) {
            if (player.isPlainsDungeonClear()) {
                System.out.println("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                System.out.println("You discover the plains dungeon!");
                return true;
            }
        } else if (z == 4) {
            if (player.isShoresDungeonClear()) {
                System.out.println("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                System.out.println("You discover the shore dungeon!");
                return true;
            }
            // this is "else if" instead of "else" to support future biomes/dungeons
        } else if (z == 5) {
            if (player.isMountainDungeonClear()) {
                System.out.println("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                System.out.println("You discover the mountain dungeon!");
                return true;
            }
        } else {
            return false;
        }
    }

    // This returns true if the player has cleared all the dungeons.
    private boolean checkBoss() {
        return player.isPlainsDungeonClear() && player.isColdDungeonClear() && player.isShoresDungeonClear() && player.isTropicalDungeonClear() && player.isMountainDungeonClear();
    }

    // This returns true if the player is in town.
    private boolean checkTown() {
        return player.getPosZ() == 0;
    }
}