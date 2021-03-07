package com.coolninja.rpgengine.Cons;

import java.io.Serializable;
import java.net.URI;

/**
 *
 * @author Ben
 */
public class Move implements Serializable {

    public String name;
    public int damage, mDamage, manaCost;
    public String type;
    public Graphic graphic;
    public URI sound;

    public Move(String name) {
        this.name = name;
    }

    public Move setDamage(int damage, int mDamage) {
        this.damage = damage;
        this.mDamage = mDamage;
        return this;
    }

    public Move setGraphic(Graphic g) {
        this.graphic = g;
        return this;
    }

    public int[] getDamage() {
        return new int[]{this.damage, this.mDamage};
    }

    public Move setManaCost(int manaCost) {
        this.manaCost = manaCost;
        return this;
    }

    public int getManaCost() {
        return this.manaCost;
    }

    public Graphic getGraphic() {
        return this.graphic;
    }

    public Move setSound(URI sound) {
        this.sound = sound;
        return this;
    }

    public URI getSound() {
        return this.sound;
    }

    public Move setType(String type) {
        this.type = type;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public void Use() {

    }

}