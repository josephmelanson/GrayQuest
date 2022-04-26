import java.util.ArrayList;
import java.util.Arrays;

public class Text {

    // MEMBER VARIABLES:
    // This ArrayList holds gossip spoken to player on returning to down. This is also where hint gossip is displayed.
    private final ArrayList<String> townText = new ArrayList<>(Arrays.asList("Welcome back.", "Back again?", "Haven't I seen you here before?", "You're looking stronger."));
    // This ArrayList contains description of loot containers on the world map.
    private final ArrayList<String> moneyDesc = new ArrayList<>(Arrays.asList("in a rotten boot", "in a hallow tree-stump", "in a small coin-purse", "in a large coin-purse", "in an old back-pack", "in a small treasure chest", "in a large treasure chest", "in a broken musical instrument", "in a smelly hat", "in a broken wagon", "in a hole in the ground", "in a small cave", "behind a large rock", "under some bushes", "under a broken shield", "in a series of increasingly smaller containers", "in a dirty rug", "in an old barrel", "in the ashes of a campfire", "hidden in a tree", "on a dead Goblin", "on a dead traveler", "in a foot-print the size of a large house", "a large circular burned out patch", "sitting under a broken trap", "on a stained altar", "among some ruins", "in a pile of muck"));
    // This ArrayList contains the descriptions for each level of the final dungeon/boss fights.
    private final ArrayList<String> towerDesc = new ArrayList<>(Arrays.asList("\n-----TOWER LEVEL ONE-----\n", "\n-----TOWER LEVEL TWO-----\n", "\n-----TOWER LEVEL THREE-----\n"));

    // GETTERS:
    // This displays game name and version number.
    public String getTitleStamp() { return "GrayQuest v" + "1.0"; }

    public String getIntroText() { return "Welcome to " + getTitleStamp() + "!"; }

    // This displays the race prompt for character creation.
    public String getRacePrompt() { return "\n-----RACE OPTIONS-----\n\nHuman:\tSTR +1\tDEX +1\tCON +1\nOrc:\tSTR +2\t----------\tCON +1\nDwarf:\t----------\tDEX +1\tCON +2\nElf:\tSTR +1\tDEX +2\t----------\n\nChoose your race:\n\n"; } // This is the race options menu.

    public String getWinText() { return "\n-----YOU WIN-----\n"; }

    // This uses a random number to determine map loot container description.
    public String getRandomMoneyDesc() { return moneyDesc.get((int)(Math.random() * (moneyDesc.size()))); }

    // This uses a random numbers to determine town gossip before dungeon hints.
    public String getRandomTownText() { return townText.get((int)(Math.random() * (townText.size()))); }

    // This returns the intro text for each level (0=1, 1=2, 2=3) of the tower.
    public String getTowerDesc(int level) { return towerDesc.get(level); }

    // These are the gossip hints randomly displayed to the character upon returning to town if they meet the level requirement for a dungeon.
    public String getColdHint() { return "A frost bitten vagabond was ranting about a dungeon in the north. I'll take his word for it."; }
    public String getShoreHint() { return "A passing merchant mentioned seeing the entrance to a dungeon in the beaches to the west. Better him than me."; }
    public String getJungleHint() { return "I heard that some adventurer found the entrance to a dungeon in the jungle to the south. Shame about what happened to them."; }
    public String getMountainHint() { return "Someone told me they thought they saw an entrance to a dungeon in the mountains to the east. I wonder how far down it goes."; }
    public String getPlainsHint() { return "Travelers are reporting that the entrance to the dungeon here in the plains has opened. I think I'll stick close to town for the next little while."; }

}