public class Monster {

    // MEMBER VARIABLES
    protected String name; // Monster/player name
    private String statusDescriptor; // Describes the monsters current health.
    private String flavourText; // Describes encountering monster.
    protected int hpBase; // Base hit points.
    protected int currentHP; // Current hit points.
    protected int maxHP; // Maximum hit points.
    protected int strength; // Key stat.
    protected int dexterity; // Key stat.
    protected int constitution; // Key stat.
    protected int strengthMod; // Bonus to rolls from strength.
    protected int dexterityMod; // Bonus to rolls from dexterity.
    protected int constitutionMod; // Bonus to rolls from constitution.
    protected int posZ; // Biome.
    protected int initiative; // Determines combat order.
    protected int finalAC; // Armor Class after bonuses.
    protected int finalDamageBonus; // Damage bonus after bonuses.
    protected int finalToHit; // To hit value after bonuses.
    protected int weaponMin; // Minimum damage from a weapon/attack.
    protected int weaponMax; // Maximum damage from a weapon/attack.

    // GETTERS:
    // These all return the associated stat.
    public String getName() { return name; }
    public String getDescriptor() {
        return statusDescriptor;
    }
    public String getFlavourText() { return flavourText; }
    public int getCurrentHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getConstitution() { return constitution; }
    public int getDexterityMod() { return dexterityMod; }
    public int getPosZ() { return posZ; }
    public int getInitiative() { return initiative; }
    public int getFinalAC() { return finalAC; }
    public int getFinalDamageBonus() { return finalDamageBonus; }
    public int getFinalToHit() { return finalToHit; }
    public int getWeaponMin() { return weaponMin; }
    public int getWeaponMax() { return weaponMax; }

    // This returns a formatted summary of stats.
    public void getStats() {
        System.out.print("\nStrength: " + strength + "\nDexterity: " + dexterity + "\nConstitution: " + constitution + "\nStrength Modifier: " + strengthMod + "\nDexterity Modifier: " + dexterityMod + "\nConstitution Modifier: " + constitutionMod + "\nMax HP: " + maxHP);
    }

    // SETTERS:
    // These set stats according to game math.
    public void setName(String name) { this.name = name; }
    public void setDescriptor() {
        float hpPercent = (currentHP * 100.0f) / maxHP;
        if (hpPercent == 100.0) {
            statusDescriptor = "very aggressive";
        } else if (hpPercent <= 99.0 && hpPercent > 75.0) {
            statusDescriptor = "slightly hurt";
        } else if (hpPercent <= 75.0 && hpPercent > 50.0) {
            statusDescriptor = "badly wounded";
        } else if (hpPercent <= 50.0 && hpPercent > 25.0) {
            statusDescriptor = "bloodied";
        } else if (hpPercent <= 25.0 && hpPercent > 10.0) {
            statusDescriptor = "severely injured";
        } else if (hpPercent <= 10.0) {
            statusDescriptor = "near death";
        }
    }
    public void setCurrentHP(int currentHP) {
        this.currentHP += currentHP;
        if (this.currentHP < 0) {
            this.currentHP = 0;
        } else if (this.currentHP > maxHP) {
            this.currentHP = maxHP;
        }
    }
    public void setPosZ(int z) { posZ = z; }
    public void setInitiative(int initiative) { this.initiative = initiative + dexterityMod; }

    // OTHER METHODS
    // This rolls key stats.
    public int rollStats() {
        return (int)(Math.random() * 15) + 3; // range is 3-18
    }
    // This determines bonuses from key stats.
    public int rollStatBonus(int statScore) {
        return Math.floorDiv(statScore - 10, 2);
    }

    // CONSTRUCTORS:
    // FROZEN MONSTER DATA
    public void setMonsterZeroPointOne(){
        name = "Adult White Dragon";
        finalAC = 18;
        maxHP = 200;
        currentHP = maxHP;
        strength = 22;
        dexterity = 10;
        constitution = 22;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 18;
        finalToHit = 11;
        weaponMin = 9;
        weaponMax = 84;
        flavourText = "Ruler of the tundra and guardian of the <item>, the huge dragon sheds a thick mist of bellowing vapor.";
        setDescriptor();
    }
    public void setMonsterOnePointOne(){
        name = "Skeleton";
        finalAC = 13;
        maxHP = 13;
        currentHP = maxHP;
        strength = 10;
        dexterity = 14;
        constitution = 15;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 2;
        finalToHit = 4;
        weaponMin = 1;
        weaponMax = 6;
        flavourText = "An animated human skeleton approaches with sword in hand.";
        setDescriptor();
    }
    public void setMonsterTwoPointOne(){
        name = "Ice Mephit";
        finalAC = 11;
        maxHP = 21;
        currentHP = maxHP;
        strength = 7;
        dexterity = 13;
        constitution = 10;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 1;
        finalToHit = 3;
        weaponMin = 1;
        weaponMax = 4;
        flavourText = "A small fly imp, apparently made from ice, bursts from a snowbank.";
        setDescriptor();
    }
    public void setMonsterThreePointOne(){
        name = "Animated Armor";
        finalAC = 18;
        maxHP = 33;
        currentHP = maxHP;
        strength = 14;
        dexterity = 11;
        constitution = 13;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 2;
        finalToHit = 4;
        weaponMin = 1;
        weaponMax = 6;
        flavourText = "An empty suit of armor, animated by some unseen force, rises from the snow.";
        setDescriptor();
    }
    public void setMonsterFourPointOne(){
        name = "Polar Bear";
        finalAC = 12;
        maxHP = 42;
        currentHP = maxHP;
        strength = 20;
        dexterity = 10;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 5;
        finalToHit = 7;
        weaponMin = 2;
        weaponMax = 12;
        flavourText = "The large white bear springs at you from a nearby hiding place.";
        setDescriptor();
    }
    public void setMonsterFivePointOne(){
        name = "Winter Wolf";
        finalAC = 13;
        maxHP = 75;
        currentHP = maxHP;
        strength = 18;
        dexterity = 13;
        constitution = 14;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 6;
        weaponMin = 2;
        weaponMax = 12;
        flavourText = "You might have noticed the lone wolf stalking you if not for it's pure white fur.";
        setDescriptor();
    }
    public void setMonsterSixPointOne(){
        name = "Ghost";
        finalAC = 11;
        maxHP = 45;
        currentHP = maxHP;
        strength = 7;
        dexterity = 13;
        constitution = 10;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 3;
        finalToHit = 5;
        weaponMin = 4;
        weaponMax = 24;
        flavourText = "An ethereal human form floating a foot off the ground. ";
        setDescriptor();
    }
    public void setMonsterSevenPointOne(){
        name = "Flesh Golem";
        finalAC = 9;
        maxHP = 93;
        currentHP = maxHP;
        strength = 19;
        dexterity = 9;
        constitution = 18;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 8;
        finalToHit = 7;
        weaponMin = 4;
        weaponMax = 32;
        flavourText = "A mindless mass of stitched together body parts shambles towards you.";
        setDescriptor();
    }
    public void setMonsterEightPointOne(){
        name = "Mammoth";
        finalAC = 13;
        maxHP = 126;
        currentHP = maxHP;
        strength = 24;
        dexterity = 9;
        constitution = 21;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 7;
        finalToHit = 10;
        weaponMin = 4;
        weaponMax = 32;
        flavourText = "The huge furry elephant thunders across the tundra.";
        setDescriptor();
    }
    public void setMonsterNinePointOne(){
        name = "Frost Giant";
        finalAC = 15;
        maxHP = 138;
        currentHP = maxHP;
        strength = 23;
        dexterity = 9;
        constitution = 21;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 12;
        finalToHit = 9;
        weaponMin = 6;
        weaponMax = 72;
        flavourText = "A huge, blue-skinned giant stomps out of a nearby cave carrying a greataxe.";
        setDescriptor();
    }

    // TROPICAL MONSTER DATA
    public void setMonsterZeroPointTwo(){
        name = "Red Dragon Wyrmling";
        finalAC = 17;
        maxHP = 75;
        currentHP = maxHP;
        strength = 19;
        dexterity = 10;
        constitution = 17;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 6;
        weaponMin = 2;
        weaponMax = 16;
        flavourText = "Ruler of the jungle and guardian of the <item>, the heat radiating from the huge dragon is nearly unbearable.";
        setDescriptor();
    }
    public void setMonsterOnePointTwo(){
        name = "Panther";
        finalAC = 12;
        maxHP = 13;
        currentHP = maxHP;
        strength = 14;
        dexterity = 15;
        constitution = 10;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 2;
        finalToHit = 4;
        weaponMin = 1;
        weaponMax = 6;
        flavourText = "A large, well muscled black cat emerges from the foliage.";
        setDescriptor();
    }
    public void setMonsterTwoPointTwo(){
        name = "Crocodile";
        finalAC = 12;
        maxHP = 19;
        currentHP = maxHP;
        strength = 15;
        dexterity = 10;
        constitution = 13;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 2;
        finalToHit = 4;
        weaponMin = 1;
        weaponMax = 10;
        flavourText = "A large reptile with a maw full of teeth skulks from a nearby swamp.";
        setDescriptor();
    }
    public void setMonsterThreePointTwo(){
        name = "Dryad";
        finalAC = 16;
        maxHP = 22;
        currentHP = maxHP;
        strength = 10;
        dexterity = 12;
        constitution = 11;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 6;
        weaponMin = 1;
        weaponMax = 8;
        flavourText = "You're not sure if it's a small tree, large bush, or strange person, but it's carrying a club.";
        setDescriptor();
    }
    public void setMonsterFourPointTwo(){
        name = "Awakened Tree";
        finalAC = 13;
        maxHP = 59;
        currentHP = maxHP;
        strength = 19;
        dexterity = 6;
        constitution = 15;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 6;
        weaponMin = 3;
        weaponMax = 18;
        flavourText = "A huge tree reaches it's branches towards you.";
        setDescriptor();
    }
    public void setMonsterFivePointTwo(){
        name = "Manticore";
        finalAC = 14;
        maxHP = 68;
        currentHP = maxHP;
        strength = 17;
        dexterity = 16;
        constitution = 17;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 6;
        finalToHit = 5;
        weaponMin = 2;
        weaponMin = 12;
        flavourText = "A large monstrosity with the head of a man, the body of a lion, and the tail of a scorpion.";
        setDescriptor();
    }
    public void setMonsterSixPointTwo(){
        name = "Weretiger";
        finalAC = 12;
        maxHP = 120;
        currentHP = maxHP;
        strength = 17;
        dexterity = 15;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 6;
        finalToHit = 5;
        weaponMin = 2;
        weaponMax = 16;
        flavourText = "A humanoid tiger emerges through the vines.";
        setDescriptor();
    }
    public void setMonsterSevenPointTwo(){
        name = "Troll";
        finalAC = 15;
        maxHP = 84;
        currentHP = maxHP;
        strength = 18;
        dexterity = 13;
        constitution = 20;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 8;
        finalToHit = 7;
        weaponMin = 4;
        weaponMax = 24;
        flavourText = "You stumble upon a giant chewing on human sized bones.";
        setDescriptor();
    }
    public void setMonsterEightPointTwo(){
        name = "Drider";
        finalAC = 19;
        maxHP = 123;
        currentHP = maxHP;
        strength = 16;
        dexterity = 16;
        constitution = 18;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 9;
        finalToHit = 6;
        weaponMin = 3;
        weaponMax = 30;
        flavourText = "A large monstrosity with the body of a massive spider and the torso of a human.";
        setDescriptor();
    }
    public void setMonsterNinePointTwo(){
        name = "Giant Ape";
        finalAC = 12;
        maxHP = 157;
        currentHP = maxHP;
        strength = 23;
        dexterity = 14;
        constitution = 18;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 12;
        finalToHit = 9;
        weaponMin = 6;
        weaponMax = 60;
        flavourText = "A huge primate swings down from a tree.";
        setDescriptor();
    }

    // PLAINS MONSTER DATA
    public void setMonsterZeroPointThree(){
        name = "Ancient Green Dragon";
        finalAC = 21;
        maxHP = 385;
        currentHP = maxHP;
        strength = 27;
        dexterity = 12;
        constitution = 25;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 24;
        finalToHit = 15;
        weaponMin = 15;
        weaponMax = 96;
        flavourText = "Ruler of the plains and guardian of the <item>, the atmosphere surrounding the huge dragon makes you nauseous.";
        setDescriptor();
    }
    public void setMonsterOnePointThree(){
        // THIS IS ACTUALLY THE GOBLIN
        name = "Goblin";
        finalAC = 15;
        maxHP = 7;
        currentHP = maxHP;
        strength = 8;
        dexterity = 14;
        constitution = 10;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 2;
        finalToHit = 4;
        weaponMin = 1;
        weaponMax = 6;
        flavourText = "A small, green-skinned humanoid is picking over the corpse of a freshly slain traveler.";
        setDescriptor();
    }
    public void setMonsterTwoPointThree(){
        name = "Orc";
        finalAC = 13;
        maxHP = 15;
        currentHP = maxHP;
        strength = 16;
        dexterity = 12;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 3;
        finalToHit = 5;
        weaponMin = 1;
        weaponMax = 12;
        flavourText = "A large, green-skinned humanoid lumbers towards with you a greataxe.";
        setDescriptor();
    }
    public void setMonsterThreePointThree(){
        name = "Brown Bear";
        finalAC = 11;
        maxHP = 34;
        currentHP = maxHP;
        strength = 19;
        dexterity = 10;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 5;
        weaponMin = 2;
        weaponMax = 12;
        flavourText = "You accidentally awaken a large bear.";
        setDescriptor();
    }
    public void setMonsterFourPointThree(){
        name = "Mimic";
        finalAC = 12;
        maxHP = 58;
        currentHP = maxHP;
        strength = 18;
        dexterity = 14;
        constitution = 14;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 3;
        finalToHit = 5;
        weaponMin = 1;
        weaponMax = 8;
        flavourText = "You thought it was strange for a chest to be in the middle of a field, until it tried to bite you.";
        setDescriptor();
    }
    public void setMonsterFivePointThree(){
        name = "Knight";
        finalAC = 18;
        maxHP = 52;
        currentHP = maxHP;
        strength = 16;
        dexterity = 11;
        constitution = 14;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 6;
        finalToHit = 5;
        weaponMin = 4;
        weaponMax = 24;
        flavourText = "An evil warrior challenges you.";
        setDescriptor();
    }
    public void setMonsterSixPointThree(){
        name = "Elephant";
        finalAC = 12;
        maxHP = 76;
        currentHP = maxHP;
        strength = 22;
        dexterity = 9;
        constitution = 17;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 6;
        finalToHit = 8;
        weaponMin = 3;
        weaponMax = 24;
        flavourText = "You happen upon the huge creature at a small, muddy pond.";
        setDescriptor();
    }
    public void setMonsterSevenPointThree(){
        name = "Triceratops";
        finalAC = 13;
        maxHP = 95;
        currentHP = maxHP;
        strength = 22;
        dexterity = 9;
        constitution = 17;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 6;
        finalToHit = 9;
        weaponMin = 4;
        weaponMax = 32;
        flavourText = "A huge lizard with three horns and a plated shield on it's head.";
        setDescriptor();
    }
    public void setMonsterEightPointThree(){
        name = "Wyvern";
        finalAC = 13;
        maxHP = 110;
        currentHP = maxHP;
        strength = 19;
        dexterity = 10;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 8;
        finalToHit = 7;
        weaponMin = 4;
        weaponMax = 24;
        flavourText = "This large dragon stands on its hind legs, and instead of forelegs has only clawed wings.";
        setDescriptor();
    }
    public void setMonsterNinePointThree(){
        name = "Tyrannosaurus Rex";
        finalAC = 13;
        maxHP = 136;
        currentHP = maxHP;
        strength = 25;
        dexterity = 10;
        constitution = 19;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 14;
        finalToHit = 10;
        weaponMin = 8;
        weaponMax = 96;
        flavourText = "A huge lizard that seems to be mostly teeth and appetite comes sprinting towards you.";
        setDescriptor();
    }

    // SHORE MONSTER DATA
    public void setMonsterZeroPointFour(){
        // boss
        name = "Blue Dragon Wyrmling";
        finalAC = 17;
        maxHP = 52;
        currentHP = maxHP;
        strength = 17;
        dexterity = 10;
        constitution = 15;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 3;
        finalToHit = 5;
        weaponMin = 2;
        weaponMax = 16;
        flavourText = "Ruler of the shores and guardian of the <item>, the huge dragon gives off small bolts of electricity.";
        setDescriptor();
    }
    public void setMonsterOnePointFour(){
        name = "Giant Frog";
        finalAC = 11;
        maxHP = 18;
        currentHP = maxHP;
        strength = 12;
        dexterity = 13;
        constitution = 11;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 1;
        finalToHit = 3;
        weaponMin = 1;
        weaponMax = 6;
        flavourText = "A slimy frog the size of a large dog hops toward you.";
        setDescriptor();
    }
    public void setMonsterTwoPointFour(){
        name = "Sahuagin";
        finalAC = 12;
        maxHP = 22;
        currentHP = maxHP;
        strength = 13;
        dexterity = 11;
        constitution = 12;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 1;
        finalToHit = 3;
        weaponMin = 1;
        weaponMax = 8;
        flavourText = "A green-skinned, gilled humanoid with fins approaches you with a spear.";
        setDescriptor();
    }
    public void setMonsterThreePointFour(){
        name = "Giant Octopus";
        finalAC = 11;
        maxHP = 52;
        currentHP = maxHP;
        strength = 17;
        dexterity = 13;
        constitution = 13;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 3;
        finalToHit = 5;
        weaponMin = 2;
        weaponMax = 12;
        flavourText = "The large octopus, writhing in the surf, reaches it's tentacles towards you.";
        setDescriptor();
    }
    public void setMonsterFourPointFour(){
        name = "Gelatinous Cube";
        finalAC = 6;
        maxHP = 84;
        currentHP = maxHP;
        strength = 14;
        dexterity = 3;
        constitution = 20;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 0;
        finalToHit = 4;
        weaponMin = 3;
        weaponMax = 18;
        flavourText = "What you mistook a small, shimmering pool turns out to be a quivering mass that oozes from it's hollow in the ground.";
        setDescriptor();
    }
    public void setMonsterFivePointFour(){
        name = "Wight";
        finalAC = 14;
        maxHP = 45;
        currentHP = maxHP;
        strength = 15;
        dexterity = 14;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 4;
        weaponMin = 2;
        weaponMax = 20;
        flavourText = "An undead humanoid carrying a large sword rises from a grounded raft.";
        setDescriptor();
    }
    public void setMonsterSixPointFour(){
        name = "Chuul";
        finalAC = 16;
        maxHP = 93;
        currentHP = maxHP;
        strength = 19;
        dexterity = 10;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 8;
        finalToHit = 6;
        weaponMin = 4;
        weaponMax = 24;
        flavourText = "A slimy lobster the size of a horse. Terrifying, but possibly delicious.";
        setDescriptor();
    }
    public void setMonsterSevenPointFour(){
        name = "Water Elemental";
        finalAC = 14;
        maxHP = 114;
        currentHP = maxHP;
        strength = 18;
        dexterity = 14;
        constitution = 18;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 8;
        finalToHit = 7;
        weaponMin = 4;
        weaponMax = 32;
        flavourText = "A cyclone of water spins onto the shore and moves towards you with intent.";
        setDescriptor();
    }
    public void setMonsterEightPointFour(){
        name = "Chimera";
        finalAC = 14;
        maxHP = 114;
        currentHP = maxHP;
        strength = 19;
        dexterity = 11;
        constitution = 19;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 12;
        finalToHit = 7;
        weaponMin = 6;
        weaponMax = 36;
        flavourText = "A large monstrosity with the front body and head of a lion, the rear body and head of a goat, and the wings, tail, and head of a dragon.";
        setDescriptor();
    }
    public void setMonsterNinePointFour(){
        name = "Hydra";
        finalAC = 15;
        maxHP = 172;
        currentHP = maxHP;
        strength = 20;
        dexterity = 12;
        constitution = 20;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 25;
        finalToHit = 8;
        weaponMin = 5;
        weaponMax = 50;
        flavourText = "A huge lizard with five-headed serpentine heads thunders out of the surf towards you.";
        setDescriptor();
    }

    // MOUNTAIN MONSTER DATA
    public void setMonsterZeroPointFive(){
        name = "Young Black Dragon";
        finalAC = 18;
        maxHP = 127;
        currentHP = maxHP;
        strength = 19;
        dexterity = 14;
        constitution = 17;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 12;
        finalToHit = 7;
        weaponMin = 7;
        weaponMax = 84;
        flavourText = "Ruler of the mountains and guardian of the <item>, the pungent stench of the huge dragon brings tears to your eyes.";
        setDescriptor();
    }
    public void setMonsterOnePointFive(){
        name = "Pseudodragon";
        finalAC = 13;
        maxHP = 7;
        currentHP = maxHP;
        strength = 6;
        dexterity = 15;
        constitution = 13;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 2;
        finalToHit = 4;
        weaponMin = 1;
        weaponMax = 4;
        flavourText = "A tiny dragon, about the size of a house cat, descends from the sky.";
        setDescriptor();
    }
    public void setMonsterTwoPointFive(){
        name = "Worg";
        finalAC = 13;
        maxHP = 26;
        currentHP = maxHP;
        strength = 16;
        dexterity = 13;
        constitution = 13;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 3;
        finalToHit = 4;
        weaponMin = 2;
        weaponMax = 12;
        flavourText = "A monstrously large wolf leaps in front of you from behind a rock.";
        setDescriptor();
    }
    public void setMonsterThreePointFive(){
        name = "Hippogriff";
        finalAC = 11;
        maxHP = 19;
        currentHP = maxHP;
        strength = 17;
        dexterity = 13;
        constitution = 13;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 3;
        finalToHit = 5;
        weaponMin = 2;
        weaponMax = 12;
        flavourText = "The large creature is half eagle, half horse, and all bad news.";
        setDescriptor();
    }
    public void setMonsterFourPointFive(){
        name = "Ogre";
        finalAC = 11;
        maxHP = 59;
        currentHP = maxHP;
        strength = 19;
        dexterity = 8;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 6;
        weaponMin = 2;
        weaponMax = 16;
        flavourText = "The giant has a dull look, and a large club.";
        setDescriptor();
    }
    public void setMonsterFivePointFive(){
        name = "Minotaur";
        finalAC = 14;
        maxHP = 76;
        currentHP = maxHP;
        strength = 18;
        dexterity = 11;
        constitution = 16;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 4;
        finalToHit = 6;
        weaponMin = 2;
        weaponMax = 24;
        flavourText = "Your path is blocked by a towering creature with the body of a man and the head of a bull.";
        setDescriptor();
    }
    public void setMonsterSixPointFive(){
        name = "Ettin";
        finalAC = 12;
        maxHP = 85;
        currentHP = maxHP;
        strength = 21;
        dexterity = 8;
        constitution = 17;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 10;
        finalToHit = 7;
        weaponMin = 4;
        weaponMax = 32;
        flavourText = "A large, ugly giant with two heads and a poor disposition.";
        setDescriptor();
    }
    public void setMonsterSevenPointFive(){
        name = "Earth Elemental";
        finalAC = 17;
        maxHP = 126;
        currentHP = maxHP;
        strength = 20;
        dexterity = 8;
        constitution = 20;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 10;
        finalToHit = 8;
        weaponMin = 4;
        weaponMax = 32;
        flavourText = "A large pile of stones rises from the ground as a whole and moves towards you with intent.";
        setDescriptor();
    }
    public void setMonsterEightPointFive(){
        name = "Vrock";
        finalAC = 15;
        maxHP = 104;
        currentHP = maxHP;
        strength = 17;
        dexterity = 15;
        constitution = 18;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 6;
        finalToHit = 6;
        weaponMin = 4;
        weaponMax = 24;
        flavourText = "An emaciated demonic vulture swoops towards you.";
        setDescriptor();
    }
    public void setMonsterNinePointFive(){
        name = "Stone Giant";
        finalAC = 17;
        maxHP = 126;
        currentHP = maxHP;
        strength = 23;
        dexterity = 15;
        constitution = 20;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 12;
        finalToHit = 9;
        weaponMin = 6;
        weaponMax = 48;
        flavourText = "The huge giant surprises you by leaping out from behind the rocky terrain wielding a greatclub.";
        setDescriptor();
    }

    // TOWER MONSTER DATA:
    public void setMonsterOnePointZero(){
        name = "Balor";
        finalAC = 19;
        maxHP = 262;
        currentHP = maxHP;
        strength = 26;
        dexterity = 15;
        constitution = 22;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 16;
        finalToHit = 14;
        weaponMin = 6;
        weaponMax = 48;
        flavourText = "Huge, horned, hoofed, winged, furry, red, bull-faced, and carrying a fire whip. He doesn't seem keen on you passing.";
        setDescriptor();
    }
    public void setMonsterTwoPointZero(){
        name = "Pit Fiend";
        finalAC = 19;
        maxHP = 300;
        currentHP = maxHP;
        strength = 26;
        dexterity = 14;
        constitution = 24;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 32;
        finalToHit = 14;
        weaponMin = 16;
        weaponMax = 96;
        flavourText = "Earl Gray has sold his soul and become a large demonic fiend. He now has scaly red skin, clawed feet and hands, a long tail and massive wings. He wields a large mace and peers at you with burning eyes.";
        setDescriptor();
    }
    public void setMonsterThreePointZero(){
        name = "Solar";
        finalAC = 21;
        maxHP = 243;
        currentHP = maxHP;
        strength = 26;
        dexterity = 22;
        constitution = 26;
        strengthMod = rollStatBonus(strength);
        dexterityMod = rollStatBonus(dexterity);
        constitutionMod = rollStatBonus(constitution);
        finalDamageBonus = 16;
        finalToHit = 15;
        weaponMin = 20;
        weaponMax = 144;
        flavourText = "The gods are fed up with your non-sense.";
        setDescriptor();
    }
}