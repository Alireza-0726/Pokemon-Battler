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
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Namn får inte vara tomt");
        }
        if (type == null) {
            throw new IllegalArgumentException("Typ får inte vara null");
        }
        if (maxHp <= 0) {
            throw new IllegalArgumentException("Max-HP måste vara > 0");
        }
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed måste vara > 0");
        }
        this.name = name;
        this.type = type;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.speed = speed;
        this.attacks = new ArrayList<>();
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Skada kan inte vara negativ");
        }
        currentHp = Math.max(0, currentHp - amount);
    }

    public String toDisplayString() {
        return name + " (" + type + ") HP: " + currentHp + "/" + maxHp
                + " - Speed: " + speed
                + " - " + attacks.size() + " attacker";
    }
}
