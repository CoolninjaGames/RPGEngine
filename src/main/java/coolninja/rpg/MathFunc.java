package coolninja.rpg;

import coolninja.rpg.Cons.Enemy;
import coolninja.rpg.Cons.Item;
import coolninja.rpg.Required.Player;

/**
 * The built-in math functions
 * @version 1.0
 * @since 1.0
 */
public class MathFunc {
    
    /**
     * Gets a random number from 0-10
     * (0 = max of 10)
     * @param clamp
     * @since 1.0
     */
    public static int random(int clamp){
        if(clamp == 0){
            clamp = 10;
        }
        return Math.min(clamp, (int) Math.round(Math.random()*10));
    }
    
    /**
     * Calculates if the move should hit
     * @param acc
     * @since 1.0
     */
    public static boolean accHitCalc(double acc){
        
        if(acc > random(0)/10){
            return true;
        }else if (acc == 1){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Calculates extra damage using luck
     * @param luck
     * @since 1.0
     */
    public static double luckDamageCalc(int luck){
        
        double temp = luck * (MathFunc.random(0)/2.2);
        
        return temp;
    }
    
    /**
     * Calculates if an item should drop
     * (chance of 1 will always drop)
     * @param enemy
     * @param drop
     * @since 1.0
     */
    public static boolean shouldDrop(Enemy enemy, Item drop){

        if(drop.chance == 1){
            return true;
        }
        
        int temp = random(0)/10;  
        
        if(temp < drop.chance){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Sees if the player can run from a battle
     * @since 1.0
     * @param pLuck
     * @param eLuck
     */
    public static boolean canRun(int pLuck, int eLuck){
        int t = pLuck - eLuck;
        if(t <= 0) return false;
        if(random(0) < t){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Adds the luck of enemies togehter
     * @since 1.0
     * @param enemies
     */
    public static int addLuck(Enemy[] enemies){
        int t = 0;
        for(int i = 0; i < enemies.length; i++){
            t += enemies[i].luck;
        }
        return t;
    }
    
    /**
     * Adds the player and companions luck togehter
     * @since 1.0
     * @param plrAndComps
     */
    public static int addPlayerLuck(Player[] plrAndComps){
        int t = 0;
        for(int i = 0; i < plrAndComps.length; i++){
            t += plrAndComps[i].luck;
        }
        return t;
    }
}
