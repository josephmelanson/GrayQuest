import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    // OBJECTS:
    private Player player = new Player(); // Used to access Player class for stat and combat calculations.
    private Monster monster = new Monster(); // Used to access monster class for stat and combat calculations.
    private final Item item = new Item(); // Used to access itemID manipulation methods in item.
    private final Place place = new Place(); // Used to access map boundaries.
    private final Text text = new Text(); // Used to access stored blocks of text.
    private final Difficulty difficulty = new Difficulty(); // Used to access difficulty options in stat and combat calculations.

    private JTextArea combatLog = new JTextArea(20,60);
    private JScrollPane sp = new JScrollPane(combatLog);

    // METHODS:
    // GAME METHODS:
    public void runGame() {
        // This is the main method which runs/loops the game.
        boolean preGame = true;
        updateCombatLog(text.getIntroText());
        // START game loop
        while (preGame) {
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
            updateCombatLog("\n-----DEATH-----\n\nYou died. Game over.");
            String[] buttons = {"Reload", "Quit"};
            int input = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
            if (input == -1 || input == 1) {
                preGame = false;
                System.exit(0);
            }
        }
    }

    private void preGame() {
        player.newCharacter(); // this resets character data in the event of a soft reload
        File f = new File("saveData.txt");
        if (f.exists()) {
            updateCombatLog("Save data found. Will you load the game or start a new one?");
            String[] buttons = {"Load", "New"};
            int input = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
            if (input == -1) {
                System.exit(0);
            } else if (input == 0) {
                updateCombatLog("\n-----LOAD DATA-----\n\n");
                readLoadData();
                player.setSecondaryStats();
                updateCombatLog(player.getCharacterSummary());
            } else {
                characterCreation();
            }
        } else characterCreation();
    }


    private void characterCreation() {
        updateCombatLog("\n-----CHARACTER CREATION-----\n\nEnter character name:\n");
        String s = JOptionPane.showInputDialog(null, sp, text.getTitleStamp(), JOptionPane.PLAIN_MESSAGE);
        player.setName(s);
        updateCombatLog(s);
        updateCombatLog(text.getRacePrompt());
        String[] rButtons = {"Human", "Orc", "Dwarf", "Elf"};
        int input = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, rButtons, rButtons[2]);
        if (input == -1) {
            System.exit(0);
        } else if (input == 0) {
            player.setRace(1);
        } else if (input == 1) {
            player.setRace(2);
        } else if (input == 2) {
            player.setRace(3);
        } else if (input == 3) {
            player.setRace(4);
        }
        if (player.getRaceInt() == 1 || player.getRaceInt() == 3) {
            updateCombatLog("You are a " + player.getRaceString(player.getRaceInt()) + ".\n");
        } else {
            updateCombatLog("You are an " + player.getRaceString(player.getRaceInt()) + ".\n");
        }
        boolean statLoop = true;
        while (statLoop) {
            player.setMainStats();
            updateCombatLog(player.getStats() + "\n\nWill you keep these stats or reroll?");
            String[] sButtons = {"Keep", "Reroll"};
            input = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, sButtons, sButtons[0]);
            if (input == -1) {
                System.exit(0);
            } else if (input == 0) {
                statLoop = false;
            }
        }
    }

    // TOWN MENU METHODS:

    private void townMenu() {
        // Main town menu.
        // 0 = rest, 1 = save, 2 = market, 3 = leave/map, 4 = boss
        int c;
        updateCombatLog("\n" + getTownTextWithHint(player.getLevel()));
        if (checkBoss()) {
            String[] buttons = {"Rest", "Market", "Leave", "Produce"};
            updateCombatLog("You have gathered all the glass produce. Where will you go?");
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
        } else {
            String[] buttons = {"Rest", "Market", "Leave"};
            updateCombatLog("Where will you go?");
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
        }
        if (c == -1) {
            System.exit(0);
        } else if (c == 0) {
            // checks for level and if so levels
            if (player.checkForLevelUp()) {
                player.levelUp();
                updateCombatLog("You gained enough experience to leveled!\nYou are now level " + player.getLevel() + ".");
            }
            // restores player hp
            player.setCurrentHP(player.getMaxHP());
            updateCombatLog("You rest and recover all your hp...");
            // write save data
            writeSaveData();
        } else if (c == 1) {
            shopMenu();
        } else if (c == 2) {
            moveCharacterPrompt();
        } else if (c == 3) {
            // Begin final dungeon / boss encounter
            towerMenu();
        }
    }
    // These are the shop submenus.
    private void shopMenu() {
        boolean shopLoop = true;
        while (shopLoop) {
            String[] buttons = {"Armor", "Shields", "Weapons", "Potions", "Leave"};
            updateCombatLog("\n-----MARKET MENU-----\n\nWelcome to the market.\nYou have " + player.getMoney() + " gold.\nWhere will you shop?");
            int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                armorMenu();
            } else if (c == 1) {
                shieldMenu();
            } else if (c == 2) {
                weaponMenu();
            }  else if (c == 3) {
                potionMenu();
            } else if (c == 4) shopLoop = false;
        }
    }

    private void armorMenu() {
        boolean armorLoop = true;
        String[] armorButtons = {"Common (+1)", "Uncommon (+2)", "Rare (+3)", "Very Rare (+4)", "Leave"};
        updateCombatLog("\n-----ARMOR SHOP-----\n\nWelcome to the armor shop.\n\nYou have " + player.getMoney() + " gold.\n\nYou are wearing " + item.getItemName(player.getEquippedArmorID()) + ".\n\nChoose your plate:\n\nCommon (+1): " + difficulty.getCommonPrice() + " gold\nUncommon (+2): " + difficulty.getUncommonPrice() + " gold\nRare (+3): " + difficulty.getRarePrice() + " gold\nVery Rare (+4): " + difficulty.getVRarePrice() + " gold\n\n\"What will you buy?");
        while (armorLoop) {
            // Common = 0, Uncommon = 1, Rare = 2, Very Rare = 3, Leave = 4
            int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, armorButtons, armorButtons[4]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                gearCheckSell(1.1);
            } else if (c == 1) {
                gearCheckSell(1.2);
            } else if (c == 2) {
                gearCheckSell(1.3);
            } else if (c == 3) {
                gearCheckSell(1.4);
            } else armorLoop = false;
        }
    }

    private void weaponMenu() {
        boolean weaponLoop = true;
        String[] weaponButtons = {"Common (+1)", "Uncommon (+2)", "Rare (+3)", "Very Rare (+4)", "Leave"};
        updateCombatLog("\n-----WEAPON SHOP-----\n\nWelcome to the weapon shop.\n\nYou have " + player.getMoney() + " gold.\n\nYou are wielding a " + item.getItemName(player.getEquippedSwordID()) + ".\n\nChoose your sword:\n\nCommon (+1): " + difficulty.getCommonPrice() + " gold\nUncommon (+2): " + difficulty.getUncommonPrice() + " gold\nRare (+3): " + difficulty.getRarePrice() + " gold\nVery Rare (+4): " + difficulty.getVRarePrice() + " gold\n\nWhat will you buy?");
        while (weaponLoop) {
            // Common = 0, Uncommon = 1, Rare = 2, Very Rare = 3, Leave = 4
            int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, weaponButtons, weaponButtons[4]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                gearCheckSell(2.1);
            } else if (c == 1) {
                gearCheckSell(2.2);
            } else if (c == 2) {
                gearCheckSell(2.3);
            } else if (c == 3) {
                gearCheckSell(2.4);
            } else weaponLoop = false;
        }
    }

    private void shieldMenu() {
        boolean shieldLoop = true;
        String[] shieldButtons = {"Common (+1)", "Uncommon (+2)", "Rare (+3)", "Very Rare (+4)", "Leave"};
        updateCombatLog("\n-----SHIELD SHOP-----\n\nYou have " + player.getMoney() + " gold.\n\nYou are wielding a " + item.getItemName(player.getEquippedSwordID()) + ".\n\nChoose your sword:\n\nCommon (+1): " + difficulty.getCommonPrice() + " gold\nUncommon (+2): " + difficulty.getUncommonPrice() + " gold\nRare (+3): " + difficulty.getRarePrice() + " gold\nVery Rare (+4): " + difficulty.getVRarePrice() + " gold\n\nWhat will you buy?");
        while (shieldLoop) {
            // Common = 0, Uncommon = 1, Rare = 2, Very Rare = 3, Leave = 4
            int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, shieldButtons, shieldButtons[4]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                gearCheckSell(3.1);
            } else if (c == 1) {
                gearCheckSell(3.2);
            } else if (c == 2) {
                gearCheckSell(3.3);
            } else if (c == 3) {
                gearCheckSell(3.4);
            } else shieldLoop = false;
        }
    }

    public void gearCheckSell(double itemID) {
        if (player.checkMoney(item.getItemPrice(itemID))) {
            if (player.getEquippedArmorBonus() >= item.getItemBonusNumber(itemID)) {
                updateCombatLog("You're wearing the same or better. No sale.");
            } else {
                player.setMoney(item.sellItem(player.getEquippedArmorID()));
                player.setMoney(-item.getItemPrice(itemID));
                updateCombatLog("You sell your " + item.getItemName(player.getEquippedArmorID()) + " for " + item.sellItem(player.getEquippedArmorID()) + " gold.\nYou buy the " + item.getItemName(itemID) + " for " + item.getItemPrice(itemID) + " gold, and equip it.");
                player.setEquippedArmor(itemID);
            }
        } else {
            updateCombatLog("You can't afford that.");
        }
    }

    private void potionMenu() {
        boolean potionLoop = true;
        String[] potionButtons = {"Healing", "Greater", "Superior", "Pristine", "Leave"};
        updateCombatLog("\n-----POTION SHOP-----\n\nYou have " + player.getMoney() + " gold and the following potions:\n\nHealing Potions: " + player.getHealPotionsOne() + "\nGreater Healing Potions: " + player.getHealPotionsTwo() + "\nSuperior Healing Potions: " + player.getHealPotionsThree() + "\nPristine Healing Potions: " + player.getHealPotionsFour() + "\n\nChoose your potion:\n\nHealing Potion (2d4+2): " + difficulty.getCommonPrice() + " gold\nGreater Healing (4d4+4): " + difficulty.getUncommonPrice() + " gold\nSuperior Healing Potion (8d4+8): " + difficulty.getRarePrice() + " gold\nPristine  Healing (10d4+20): " + difficulty.getVRarePrice() + " gold");
        while (potionLoop) {
            // Healing = 0, Greater = 1, Superior = 2, Pristine = 3, Leave = 4
            int c = JOptionPane.showOptionDialog(null, "What will you buy?", text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, potionButtons, potionButtons[4]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                if (player.checkMoney(difficulty.getCommonPrice())) {
                    player.setHealPotionsOne(1);
                    player.setMoney(-difficulty.getCommonPrice());
                    updateCombatLog("You buy a Healing Potion.");
                } else {
                    updateCombatLog("You can't afford that.");
                }
            } else if (c == 1) {
                if (player.checkMoney(difficulty.getUncommonPrice())) {
                    player.setHealPotionsTwo(1);
                    player.setMoney(-difficulty.getUncommonPrice());
                    updateCombatLog("You buy an Uncommon Healing Potion.");
                } else {
                    updateCombatLog("You can't afford that.");
                }
            } else if (c == 2) {
                if (player.checkMoney(difficulty.getRarePrice())) {
                    player.setHealPotionsThree(1);
                    player.setMoney(-difficulty.getRarePrice());
                    updateCombatLog("You buy a Superior Healing Potion.");
                } else {
                    updateCombatLog("You can't afford that.");
                }
            } else if (c == 3) {
                if (player.checkMoney(difficulty.getVRarePrice())) {
                    player.setHealPotionsFour(1);
                    player.setMoney(-difficulty.getVRarePrice());
                    updateCombatLog("You buy a Pristine Healing Potion.");
                } else {
                    updateCombatLog("You can't afford that.");
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
        } else if (player.isPlainsDungeonClear() && player.isColdDungeonClear() && player.isMountainDungeonClear() && player.isShoresDungeonClear() && player.isTropicalDungeonClear()) {
            return text.getRandomTownText(); // + text.getBossTownText();???
        } else return text.getRandomTownText();
    }

    // MAP MENU:
    private void mapMenu() {
        double d = 0.0;
        boolean mapLoop = true;
        while (mapLoop) {
            int c;
            if (player.getTotalPotions() > 0) {
                String[] mapButtons = {"Search", "Travel", "Teleport", "Potion"};
                updateCombatLog("Will you search the area, travel farther, teleport home, or use a potion?");
                c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp() + " - Wilderness", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, mapButtons, mapButtons[1]);
            } else {
                String[] mapButtons = {"Search", "Travel", "Teleport"};
                updateCombatLog("Will you search the area, travel farther, or teleport home?");
                c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp() + " - Wilderness", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, mapButtons, mapButtons[1]);
            }
            if (c == -1) {
                System.exit(0);
            }
            // c == 0 move to else for readability
            else if (c == 1) {
                moveCharacterPrompt();
            } else if (c == 2) {
                updateCombatLog("You grasp your amulet and focus in prayer...");
                returnToTown();
                mapLoop = false;
            } else if (c == 3) {
                potionUseMenu();
            } else {
                if (place.rollForDungeon(player.posZ, player.getLevel())) {
                    // checkDungeon returns true if player has not visited the dungeon
                    // and generates the boss monster ID based on the biomes
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
                    int i = item.rollForMoneyByLevel(player.getLevel());
                    player.setMoney(i);
                    updateCombatLog("No monsters, lucky! You find " + i + " gold " + text.getRandomMoneyDesc() + ".");
                }
            }
        }
    }

    // COMBAT MENU METHODS:
    // This runs combat, returning true if the player is alive after combat has finished.
    private boolean runCombat(double monsterID) {
        loadMonsterStats(monsterID);
        updateCombatLog(monster.getFlavourText());
        rollInitiative(player.getDexterityMod(), monster.getDexterityMod());
        boolean combatLoop = true;
        while (combatLoop) {
            if (combatMenu()) {
                combatLoop = combatRound();
                if (monster.getCurrentHP() <= 0) {
                    updateCombatLog("You defeated the " + monster.getName() + "!");
                    player.setDungeonClear(monsterID);
                    player.addExperience();
                    if (player.checkForLevelUp()) {
                        updateCombatLog("You have defeated enough monsters to earn a level! Return to town and rest to level up.");
                    }
                    double d = item.createRandomItem(player.getLevel());
                    if (d == 0.0) {
                        int i = item.rollForMoneyByLevel(player.getLevel());
                        player.setMoney(i);
                        updateCombatLog("You find " + i + " gold on the " + monster.getName() + ".");
                    } else {
                        //System.out.println("-----DEBUG----- Checking itemID: " + d);
                        updateCombatLog("You find a " + item.getItemName(d) + " on the " + monster.getName() + ".");
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
        String[] combatButtons;
        updateCombatLog("The " + monster.getName() + " looks " + monster.getDescriptor() + "!\n\nYou have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\nWhat will you do?");
        if (potionCheck()) {
            combatButtons = new String[]{"Attack", "Run", "Potion"};
        } else {
            combatButtons = new String[]{"Attack", "Run"};
        }
        int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp() + " - Combat Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, combatButtons, combatButtons[0]);
        if (c == -1) {
            System.exit(0);
            return false;
        }
        if (c == 1) {
            int i = (int) (Math.random() * difficulty.getRunChance()) + 1;
            if (i == 1) {
                updateCombatLog("You try to run...\nYou escape from the " + monster.getName() + "!");
                return false;
            } else {
                updateCombatLog("You try to run...");
                return true;
            }
        }
        if (c == 2 && player.getTotalPotions() >= 1) {
            potionUseMenu();
            return true;
        }
        else return false;
    }

    // This handles the combat math. Rolls for initiative, hit and damage.
    private boolean combatRound() {
        int i;
        // if the player goes first
        if (testInitiative()) {
            // player rolls to strike first
            if (rollToHit(player.getFinalToHit(), monster.getFinalAC())) {
                i = rollForDamage(player.getWeaponMax(), player.getWeaponMin(), player.getFinalDamageBonus());
                updateCombatLog("You deal " + i + " damage to the " + monster.getName() + ".");
                monster.setCurrentHP(-i);
            } else {
                updateCombatLog("You miss the " + monster.getName() + ".");
            }
            // check if the monster survived
            if (monster.getCurrentHP() > 0) {
                // then the monster rolls to strike first
                if (rollToHit(monster.getFinalToHit(), player.getFinalAC())) {
                    i = rollForDamage(monster.getWeaponMax(), monster.getWeaponMin(), monster.getFinalDamageBonus());
                    updateCombatLog("The " + monster.getName() + " hits you for " + i + " damage!");
                    player.setCurrentHP(-i);
                } else {
                    updateCombatLog("The " + monster.getName() + " misses you.");
                }
            }
            // if the monster goes first
        } else {
            // the monster rolls to strike
            if (rollToHit(monster.getFinalToHit(), player.getFinalAC())) {
                i = rollForDamage(monster.getWeaponMax(), monster.getWeaponMin(), monster.getFinalDamageBonus());
                updateCombatLog("The " + monster.getName() + " hits you for " + i + " damage!");
                player.setCurrentHP(-i);
            } else {
                updateCombatLog("The " + monster.getName() + " misses you.");
            }
            // check if the player survived
            if (player.getCurrentHP() > 0) {
                // player rolls to strike
                if (rollToHit(player.getFinalToHit(), monster.getFinalAC())) {
                    i = rollForDamage(player.getWeaponMax(), player.getWeaponMin(), player.getFinalDamageBonus());
                    updateCombatLog("You deal " + i + " damage to the " + monster.getName() + ".");
                    monster.setCurrentHP(-i);
                } else {
                    updateCombatLog("You miss the " + monster.getName() + ".");
                }
            }
        }
        return player.getCurrentHP() > 0;
    }
    private void towerMenu() {
        boolean towerLoop = true;
        String[] towerButtons = {"Fight", "Run"};
        while (towerLoop) {
            if (!player.isTowerOneClear() && !player.isTowerTwoClear()) {
                updateCombatLog(text.getTowerDesc(0)); // Tower entrance text, 0 = Tower level 1
                updateCombatLog("You have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\nWhat will you do?");
                int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, towerButtons, towerButtons[1]);
                if (c == -1) {
                    System.exit(0);
                } else if (c == 0) {
                    runCombat(1.0);
                    if (player.currentHP > 0) {
                        player.setTowerOneClear(true);
                    } else { towerLoop = false; }
                } else {
                    towerLoop = false;
                }
            } else if (player.isTowerOneClear() && !player.isTowerTwoClear()) {
                updateCombatLog(text.getTowerDesc(1));
                updateCombatLog("You have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\nWhat will you do?");
                int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, towerButtons, towerButtons[1]);
                if (c == -1) {
                    System.exit(0);
                } else if (c == 0) {
                    runCombat(2.0);
                    if (player.currentHP > 0) {
                        player.setTowerTwoClear(true);
                    } else { towerLoop = false; }
                } else {
                    towerLoop = false;
                }
            } else {
                updateCombatLog(text.getTowerDesc(2));
                updateCombatLog("You have " + player.getCurrentHP() + " HP and " + player.getTotalPotions() + " potions.\nWhat will you do?");
                int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, towerButtons, towerButtons[1]);
                if (c == -1) {
                    System.exit(0);
                } else if (c == 0) {
                    runCombat(3.0);
                    if (player.currentHP > 0) {
                        System.out.print("\n-----WIN-----\n");
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
        // This rounds d back up to its proper value
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
            // Load Shores monsters
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
        boolean potionLoop = true;
        // start menu
        String[] potionButtons;
        while (potionLoop) {
            if (player.getTotalPotions() <= 0) {
                updateCombatLog("You have no potions.");
                break;
            } else {
                updateCombatLog("You have:");
                potionButtons = displayPotionsForMenu().toArray(new String[0]);
            }
            updateCombatLog("What will you drink?");
            int c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, potionButtons, potionButtons[potionButtons.length-1]);
            if (c == -1) {
                System.exit(0);
            } else if (c == potionButtons.length-1) {
                potionLoop = false;
            } else if (c == 0) {
                player.setHealPotionsOne(-1);
                i = item.rollHealPotOne();
                player.setCurrentHP(i);
                updateCombatLog("You regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            } else if (c == 1) {
                player.setHealPotionsTwo(-1);
                i = item.rollHealPotTwo();
                player.setCurrentHP(i);
                updateCombatLog("You regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            } else if (c == 2) {
                player.setHealPotionsThree(-1);
                i = item.rollHealPotThree();
                player.setCurrentHP(i);
                updateCombatLog("You regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            } else if (c == 3) {
                player.setHealPotionsFour(-1);
                i = item.rollHealPotFour();
                player.setCurrentHP(i);
                updateCombatLog("You regain " + i + " HP.\nYou're now at " + player.getCurrentHP() + " HP.");
            }
        }
    }

    private ArrayList<String> displayPotionsForMenu() {
        ArrayList<String> buttons = new ArrayList<>();
        if (player.getHealPotionsOne() > 0) {
            updateCombatLog("Healing Potions (2d4+2): " + player.getHealPotionsOne());
            buttons.add("Healing");
        }
        if (player.getHealPotionsTwo() > 0) {
            updateCombatLog("Greater Healing Potions (4d4+4): " + player.getHealPotionsTwo());
            buttons.add("Greater");
        }
        if (player.getHealPotionsThree() > 0) {
            updateCombatLog("Superior Healing Potion (8d4+8): " + player.getHealPotionsThree());
            buttons.add("Superior");
        }
        if (player.getHealPotionsFour() > 0) {
            updateCombatLog("Pristine Healing Potion (10d4+10)" + player.getHealPotionsFour());
            buttons.add("Pristine");
        }
        buttons.add("Exit");
        return buttons;
    }

    // READ/WRITE METHODS:
    // This writes character data to the save file.
    private void writeSaveData() {
        try {
            java.io.File f = new java.io.File("saveData.txt");
            if (f.createNewFile()) {
                updateCombatLog("No save exists. Saving...");
            } else {
                updateCombatLog("Old save exists. Overwriting...");
            }
        } catch (IOException e) {
            updateCombatLog("An error occurred." + e);
            e.printStackTrace();
        }
        try {
            FileWriter fW = new FileWriter("saveData.txt");
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
            updateCombatLog("...save complete!");
        } catch (IOException e) {
            updateCombatLog("An error occurred." + e);
            e.printStackTrace();
        }
    }

    // This reads character data from the save file and sets it to the current player.
    private void readLoadData() {
        try {
            java.io.File f = new java.io.File("saveData.txt");
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
            updateCombatLog("An error occurred." + e);
            e.printStackTrace();
        }
    }

    // OTHER METHODS:
    // Returns true if the player has not cleared the dungeon in the supplied int biome.
    private boolean checkDungeon(int z) {
        if (z == 1) {
            if (player.isColdDungeonClear()) {
                updateCombatLog("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                updateCombatLog("You discover the frozen dungeon!");
                return true;
            }
        } else if (z == 2) {
            if (player.isTropicalDungeonClear()) {
                updateCombatLog("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                updateCombatLog("You discover the jungle dungeon!");
                return true;
            }
        } else if (z == 3) {
            if (player.isPlainsDungeonClear()) {
                updateCombatLog("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                updateCombatLog("You discover the plains dungeon!");
                return true;
            }
        } else if (z == 4) {
            if (player.isShoresDungeonClear()) {
                updateCombatLog("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                updateCombatLog("You discover the shore dungeon!");
                return true;
            }
            // this is "else if" instead of "else" to support future biomes/dungeons
        } else if (z == 5) {
            if (player.isMountainDungeonClear()) {
                updateCombatLog("You discover an entrance to a dungeon, but you have already cleared it.");
                return false;
            } else {
                updateCombatLog("You discover the mountain dungeon!");
                return true;
            }
        } else {
            return false;
        }
    }

    // Returns true if the player has cleared all the dungeons.
    private boolean checkBoss() {
        return player.isPlainsDungeonClear() && player.isColdDungeonClear() && player.isShoresDungeonClear() && player.isTropicalDungeonClear() && player.isMountainDungeonClear();
    }

    // Returns true if the player is in town.
    private boolean checkTown() { return player.getPosZ() == 0; }



    // MOVEMENT METHODS
    // This offers the player movement options depending on current location.
    public void moveCharacterPrompt() {
        char d = 'x'; //disposable value
        int c;
        if (player.getPosY() < place.getNorthLimit() && player.getPosY() > place.getSouthLimit() && player.getPosX() < place.getEastLimit() && player.getPosX() > place.getWestLimit()) {
            // all directions n-e-s-w
            updateCombatLog("What direction will you go?");
            String[] moveButtons = {"North", "East","South", "West"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 'n';
            } else if (c == 1) {
                d = 'e';
            } else if (c == 2) {
                d = 's';
            } else if (c == 3) {
                d = 'w';
            }
        } else if (player.getPosY() == place.getNorthLimit() && player.getPosY() > place.getSouthLimit() && player.getPosX() == place.getEastLimit() && player.getPosX() > place.getWestLimit()) {
            // south and west only
            updateCombatLog("Towering glaciers block your path to the north, and giant mountains block your path to the east.\nWhat direction will you go?");
            String[] moveButtons = {"South", "West"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 's';
            } else if (c == 1) {
                d = 'w';
            }
        } else if (player.getPosY() == place.getNorthLimit() && player.getPosY() > place.getSouthLimit() && player.getPosX() < place.getEastLimit() && player.getPosX() == place.getWestLimit()) {
            // south and east only
            updateCombatLog("Towering glaciers block your path to the north, and a vast ocean blocks your path to the west.\nWhat direction will you go?");
            String[] moveButtons = {"South", "East"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 's';
            } else if (c == 1) {
                d = 'e';
            }
        } else if (player.getPosY() < place.getNorthLimit() && player.getPosY() == place.getSouthLimit() && player.getPosX() == place.getEastLimit() && player.getPosX() > place.getWestLimit()) {
            // north and west only
            updateCombatLog("The jungle ends in a steep cliff which blocks your path to the south, and giant mountains block your path to the east.\nWhat direction will you go?");
            String[] moveButtons = {"North", "West"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 'n';
            } else if (c == 1) {
                d = 'w';
            }
        } else if (player.getPosY() < place.getNorthLimit() && player.getPosY() == place.getSouthLimit() && player.getPosX() < place.getEastLimit() && player.getPosX() == place.getWestLimit()) {
            // north and east only
            updateCombatLog("The jungle ends in a steep cliff which blocks your path to the south, and a vast ocean blocks your path to the west.\nWhat direction will you go?");
            String[] moveButtons = {"North", "East"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 'n';
            } else if (c == 1) {
                d = 'e';
            }
        } else if (player.getPosY() < place.getNorthLimit() && player.getPosY() > place.getSouthLimit() && player.getPosX() == place.getEastLimit() && player.getPosX() > place.getWestLimit()) {
            // north, south, and west only
            updateCombatLog("Giant mountains block your path to the east.\nWhat direction will you go?");
            String[] moveButtons = {"North", "South", "West"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 'n';
            } else if (c == 1) {
                d = 's';
            } else if (c == 2) {
                d = 'w';
            }
        } else if (player.getPosY() < place.getNorthLimit() && player.getPosY() > place.getSouthLimit() && player.getPosX() < place.getEastLimit() && player.getPosX() == place.getWestLimit()) {
            // north, south, and east only nse
            updateCombatLog("A vast ocean blocks your path to the west.\nWhat direction will you go?");
            String[] moveButtons = {"North", "East", "South"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 'n';
            } else if (c == 1) {
                d = 'e';
            } else if (c == 2) {
                d = 's';
            }
        } else if (player.getPosY() < place.getNorthLimit() && player.getPosY() == place.getSouthLimit() && player.getPosX() < place.getWestLimit() && player.getPosX() > place.getWestLimit()) {
            // north, east, or west only
            updateCombatLog("The jungle ends in a steep cliff which blocks your path to the south.\nWhat direction will you go?");
            String[] moveButtons = {"North", "East", "West"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 'n';
            } else if (c == 1) {
                d = 'e';
            } else if (c == 2) {
                d = 'w';
            }
        } else if (player.getPosY() == place.getNorthLimit() && player.getPosY() > place.getSouthLimit() && player.getPosX() < place.getEastLimit() && player.getPosX() > place.getWestLimit()) {
            // south, east, or west only
            updateCombatLog("Towering glaciers block your path to the north.\nWhich direction would you like to go?\nWhat direction will you go?");
            String[] moveButtons = {"East", "South", "West"};
            c = JOptionPane.showOptionDialog(null, sp, text.getTitleStamp(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, moveButtons, moveButtons[0]);
            if (c == -1) {
                System.exit(0);
            } else if (c == 0) {
                d = 'e';
            } else if (c == 1) {
                d = 's';
            } else if (c == 2) {
                d = 'w';
            }
        }
        c = 4;
        if (d == 'n') {
            c = 0;
        } else if (d == 'e') {
            c = 1;
        } else if (d == 's') {
            c = 2;
        } else if (d == 'w') {
            c = 3;
        }
        moveCharacter(c);
    }

    // This take in character movement selection, checks for out of bounds movement, the applies movement if able.
    public void moveCharacter(int i) {
        if (i == 0) {
            if (player.getPosY() + 1 > place.getNorthLimit()) {
                updateCombatLog("Towering glaciers block your path, you can go no farther north");
            } else {
                player.setPosY(player.getPosY() + 1);
            }
        } else if (i == 1) {
            if (player.getPosY() - 1 < place.getSouthLimit()) {
                updateCombatLog("The jungle ends in a steep cliff, you can go no farther south.");
            } else {
                player.setPosY(player.getPosY() - 1);
            }
        } else if (i == 2) {
            if (player.getPosX() + 1 > place.getEastLimit()) {
                updateCombatLog("The shore ends at a vast ocean, you can go no farther west.");
            } else {
                player.setPosX(player.getPosX() + 1);
            }
        } else if (i == 3) {
            if (player.getPosX() - 1 < place.getWestLimit()) {
                updateCombatLog("The giant mountains are impassable, you can go no farther east.");
            } else {
                player.setPosX(player.getPosX() - 1);
            }
        } else if (i == 4) {
            updateCombatLog("\n-----ERROR IN moveCharacterPrompt()-----\n"); // DEBUG CODE
        }
        setBiome();
    }

    // Sets posX, posY, and posZ to town.
    public void returnToTown() {
        player.setPosX(0);
        player.setPosY(0);
        setBiome();
    }

    // Called at the end of movement.
    public void setBiome() {
        if (player.getPosY() == 0 && player.getPosX() == 0) {
            player.setPosZ(0);
        } else if (player.getPosY() > 5) {
            player.setPosZ(1);
        } else if (player.getPosY() < -5) {
            player.setPosZ(2);
        } else if (player.getPosX() == -3) {
            player.setPosZ(4);
        } else if (player.getPosX() == 3) {
            player.setPosZ(5);
        } else {
            player.setPosZ(3);
        }
    }

    private void updateCombatLog(String newLine) {
        combatLog.append(newLine + "\n");
        combatLog.getCaret().setDot(Integer.MAX_VALUE);
    }

}
