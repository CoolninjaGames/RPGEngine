package coolninja.rpg.Required;

import coolninja.rpg.Cons.*;
import coolninja.rpg.Console.Colors;
import coolninja.rpg.Console.Console;
import coolninja.rpg.InputHandler;
import coolninja.rpg.MathFunc;
import coolninja.rpg.Vars;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The required class to a define player. This class should not be instantiated
 * directly, you should use a sub class.
 *
 * @since 1.0
 * @version 1.0
 * @author Ben Ballard
 */
public class Player implements Serializable {

    static final long serialVersionUID = 1;

    public String name;
    public int level, health, maxHealth, mana, maxMana, attack, defense, luck, mAttack, mDefense, specialAttack, exp, expToNextLevel;
    public double heathgrowthRate, manaGrowhRate, attackGrowthRate, defenseGrowthRate, mAttackGrowthRate, mDefenseGrowthRate = 1.0;
    public double luckGrowthRate = 0.1;

    /**
     * Player's moves
     */
    public ArrayList<Move> moves;
    /**
     * 0 = feet 1 = legs 2 = arms 3 = chest 4 = head 5 = weapon 6 = mod
     */
    public Equipment[] equipment = new Equipment[7];

    /**
     * Player inventory
     */
    public ArrayList<Item> inv;

    public Weakness currentWeakness;
    public StatusEffect statusEffect;

    /**
     * Used for internal handling of level ups
     */
    protected int[] stat;
    protected String[] names = new String[]{"Max Health", "Max Mana", "Attack", "Defense", "Luck", "Magic Attack", "Magic Defense", "Special Attack"};
    public int skillPoints = 0;
    protected double[] growthRates = new double[7];

    /**
     * If name == "You", then "Your turn" will be used in battle
     *
     * @param name
     * @param health
     * @param mana
     * @param maxMana
     * @param defense
     * @param attack
     * @param luck
     * @param mAttack
     * @param specialAttack
     * @param mDefense
     * @since 1.0
     */
    public Player(String name, int health, int mana, int maxMana, int attack, int defense,
            int luck, int mAttack, int mDefense, int specialAttack) {
        this.inv = new ArrayList<>();
        this.moves = new ArrayList<>();
        this.name = name;
        this.level = 1;
        this.health = health;
        this.maxHealth = health;
        this.mana = mana;
        this.maxMana = maxMana;
        this.attack = attack;
        this.defense = defense;
        this.luck = luck;
        this.mAttack = mAttack;
        this.mDefense = mDefense;
        this.specialAttack = specialAttack;
        this.expToNextLevel = 10;
        this.exp = 0;
    }

    /**
     * Increases the exp
     *
     * @param exp
     * @since 1.0
     */
    public void increaseEXP(int exp) {
        this.exp += exp;
    }

    /**
     * Used to level up the player when the required exp is obtained (Can be
     * overwritten)
     *
     * @since 1.0
     */
    public void levelUp() {
        if (level >= 99) {
            level = 99;
            if (this.name.equalsIgnoreCase("you")) {
                System.out.println("You are max level!");
            } else {
                System.out.println(this.name + " is max level!");
            }
        }
        if (exp >= expToNextLevel) {

            growthRates[0] = this.heathgrowthRate;
            growthRates[1] = this.manaGrowhRate;
            growthRates[2] = this.attackGrowthRate;
            growthRates[3] = this.defenseGrowthRate;
            growthRates[4] = this.mAttackGrowthRate;
            growthRates[5] = this.mDefenseGrowthRate;
            growthRates[6] = this.luckGrowthRate;

            int levelNeeded = 1;

            while (exp > expToNextLevel) {
                exp -= expToNextLevel;
                expToNextLevel += (MathFunc.random(0) + 3) * level;
                levelNeeded++;
            }

            if (this.name.equalsIgnoreCase("you")) {
                System.out.println("You have leveled up!");
            } else {
                System.out.println(this.name + " has leveled up!");
            }

            Console.waitFull(2);

            Console.clear();

            stat = null;

            stat = new int[]{maxHealth, maxMana, attack, defense, luck, mAttack, mDefense, specialAttack};

            LevelUpHelper(levelNeeded);

            levelUp();

            heal();
        }
    }

    private int[] mutliLevelUp(int needed) {
        int[] tempStat = new int[8];
        for (int i = 0; i < needed; i++) {
            for (int x = 0; x < stat.length; x++) {
                tempStat[x] += MathFunc.statInc(this.level, x < growthRates.length - 1 ? growthRates[x] : 1);
            }
        }
        return tempStat;
    }

    private void LevelUpHelper(int levelNeeded) {
        int[] tempStat = new int[8];
        level += levelNeeded;
        Console.clear();

        System.out.println("Level " + level + "!");
        Console.waitHalf(5);

        for (int i = 0; i < stat.length - 1; i++) {
            if (levelNeeded > 1) {
                tempStat = mutliLevelUp(levelNeeded);
                levelNeeded = 0;
            } else if (levelNeeded == 1) {
                tempStat[i] += MathFunc.statInc(this.level, i >= growthRates.length ? 1 : growthRates[i]);
            }
            System.out.println(names[i] + " increased by " + tempStat[i]);
            if (Vars.shouldScroll) {
                for (int x = 0; x < 5; x++) {
                    System.out.println("\n");
                    Console.waitReal(50);
                }
            }
            Console.waitFull(1);
        }

        maxHealth += tempStat[0];
        maxMana += tempStat[1];
        attack += tempStat[2];
        defense += tempStat[3];
        luck += tempStat[4];
        mAttack += tempStat[5];
        mDefense += tempStat[6];
        specialAttack += tempStat[7];
        skillPoints = MathFunc.random(3) + 2 * level;

        while (skillPoints != 0) {
            pickStat(skillPoints);
        }
        printStats();

        Console.waitHalf(1);

        InputHandler.pressEnter();

        Console.clear();
    }

    private void pickStat(int skillPoints) {
        stat = new int[]{maxHealth, maxMana, attack, defense, luck, mAttack, mDefense, specialAttack};

        for (int i = 0; i < stat.length; i++) {
            System.out.println("(" + i + ")" + names[i] + ": " + stat[i]);
            Console.waitReal(300);
        }

        System.out.println("You have " + skillPoints + " skill points.\n Which one? (Use number)");

        int s;

        try {
            s = Integer.parseInt(InputHandler.getInput());
        } catch (NumberFormatException e) {
            Console.printError("That's not a number", 2500);
            Console.clear();
            return;
        }

        usePoint(s);

        Console.clear();

    }

    private void usePoint(int index) {
        System.out.println("How Much?");

        int s;
        try {
            s = Integer.parseInt(InputHandler.getInput());
        } catch (NumberFormatException e) {
            Console.printError("That's Not A Number!", 500);
            return;
        }

        if (s > skillPoints) {
            Console.printError("That's Too Much!", 500);
            return;
        }

        stat[index] += s;
        skillPoints -= s;

        maxHealth = stat[0];
        maxMana = stat[1];
        attack = stat[2];
        defense = stat[3];
        luck = stat[4];
        mAttack = stat[5];
        mDefense = stat[6];
        specialAttack = stat[7];

    }

    /**
     * Sets the player's stats using the given equipment (Should be overwritten)
     *
     * @since 1.0
     * @param equipment
     */
    public void setStats(Equipment equipment) {

    }

    /**
     * Adds a move to the player
     *
     * @param move
     * @return
     * @since 1.0
     */
    public Player addMove(Move move) {
        moves.add(move);
        return this;
    }

    /**
     * Adds a array move to the player
     *
     * @param ms
     * @return
     * @since 1.0
     */
    public Player addMoves(Move[] ms) {
        moves.addAll(Arrays.asList(ms));
        return this;
    }

    /**
     * Prints the players moves
     *
     * @since 1.0
     */
    public void printMoves() {
        for (int i = 0; i < moves.size(); i++) {
            Move m = moves.get(i);
            if (m.manaCost != 0) {
                System.out.println("  -" + Colors.BLACK + Colors.WHITE_BACKGROUND + m.name + " | Mana: " + m.manaCost + Colors.reset());
            } else {
                System.out.print(Colors.BLACK);
                System.out.println("  -" + Colors.BLACK + Colors.WHITE_BACKGROUND + m.name + Colors.reset());
            }
        }
    }

    /**
     * Returns the player's moves
     *
     * @return
     * @since 1.0
     */
    public ArrayList<Move> getMoves() {
        return this.moves;
    }

    /**
     * Prints the players inventory (Can be overwritten)
     *
     * @since 1.0
     */
    public void printInv() {
        for (int i = 0; i < inv.size(); i++) {
            System.out.println(inv.get(i).name);
        }
    }

    /**
     * Returns the player's inventory
     *
     * @return
     * @since 1.0
     */
    public ArrayList<Item> getInv() {
        return this.inv;
    }

    /**
     * Heals the player to full health and mana
     *
     * @since 1.0
     */
    public void heal() {
        this.health = maxHealth;
        this.mana = maxMana;
    }

    /**
     * Prints the players stats
     *
     * @since 1.0
     */
    public void printStats() {
        stat = new int[]{attack, defense, luck, mAttack, mDefense, specialAttack};
        names = new String[]{"Attack", "Defense", "Luck", "Magic Attack", "Magic Defense", "Special Attack"};
        for (int i = 0; i < stat.length; i++) {
            Console.waitReal(300);
            System.out.println(names[i] + ": " + stat[i]);
        }
        System.out.println("Health: " + health + "/" + maxHealth
                + "\nMana: " + mana + "/" + maxMana
                + "\nExp: " + exp + "/" + expToNextLevel);
    }

    /**
     * Sets the player's current weakness
     *
     * @param weakness
     * @return
     * @since 1.0
     */
    public Player setWeakness(Weakness weakness) {
        this.currentWeakness = weakness;
        return this;
    }

    /**
     * Deals given damage to player/companion
     *
     * @since 1.0
     * @param damage
     */
    public void dealDamage(int damage) {
        this.health -= damage;
    }

    /**
     * Adds/Removes Armor
     *
     * @return
     * @since 1.0
     * @param removing
     * @param armor
     */
    public Player addArmor(boolean removing, Equipment armor) {
        if (!removing) {
            this.maxHealth += armor.maxHealth;
            this.maxMana += armor.maxMana;
            this.attack += armor.attack;
            this.defense += armor.defense;
            this.luck += armor.luck;
            this.mAttack += armor.mAttack;
            this.mDefense += armor.mDefense;
            if (armor.weakness != null) {
                this.currentWeakness = armor.weakness;
            }
            equipment[armor.slot.index] = armor;
        } else {
            this.maxHealth -= armor.maxHealth;
            this.maxMana -= armor.maxMana;
            this.attack -= armor.attack;
            this.defense -= armor.defense;
            this.luck -= armor.luck;
            this.mAttack -= armor.mAttack;
            this.mDefense -= armor.mDefense;
            this.currentWeakness = null;
            equipment[armor.slot.index] = null;
        }
        return this;
    }

    /**
     * Adds an item to the players inventory
     *
     * @param item
     * @since 1.0
     */
    public void addItemToInv(Item item) {
        inv.add(item);
    }

    /**
     * Sets the character's status effect
     *
     * @since 1.0
     * @param statusEffect
     */
    public void setStatusEffect(StatusEffect statusEffect) {
        this.statusEffect = statusEffect;
    }

    /**
     * Sets the character's growth rate for stats <br>
     * Used in this formula (growth-rate x level)/10 + random(2) <br>
     * Negative values will slowly decrease the stats, until it begins to apply
     * negative ones.
     *
     * @since 1.0
     * @param rate
     */
    public void setHeathGrowthRate(double rate) {
        this.heathgrowthRate = rate;
    }

    public void setManaGrowthRate(double rate) {
        this.manaGrowhRate = rate;
    }

    public void setAttackGrowthRate(double rate) {
        this.attackGrowthRate = rate;
    }

    public void setDefenseGrowthRate(double rate) {
        this.defenseGrowthRate = rate;
    }

    public void setMAttackGrowthRate(double rate) {
        this.mAttackGrowthRate = rate;
    }

    public void setMDefenseGrowthRate(double rate) {
        this.mDefenseGrowthRate = rate;
    }

    public void setLuckGrowthRate(double rate) {
        this.luckGrowthRate = rate;
    }
}
