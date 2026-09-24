package se.systementor;

public class Attack {
    public String name;
    public int baseDamage;
    public int accuracy;
    public Type type;

    public Attack(String name, int baseDamage, int accuracy, Type type) {
        this.name = name;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
        this.type = type;
    }
}