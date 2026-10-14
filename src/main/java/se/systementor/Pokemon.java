package se.systementor;

import java.util.ArrayList;

public class Pokemon {
    public String name;
    public Type type;
    public int maxHp;
    public int currentHp;
    public int speed;
    public ArrayList<Attack> attacks;

    public Pokemon(String name, Type type, int  maxHp, int speed) {
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.speed = speed;
        this.attacks = new ArrayList<>();
    }

    public String toDisplayString() {
        return name + " (" + type + ") HP: " + currentHp + "/" + maxHp
                + " - Speed:" + speed
                + " - " + attacks.size() + " attacker";
    }
}
