package se.systementor;

public class Attack {
    public String name;
    public int baseDamage;
    public int accuracy;
    public Type type;

    public Attack(String name, int baseDamage, int accuracy, Type type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attackens namn får inte vara tomt");
        }
        if (baseDamage < 1) {
            throw new IllegalArgumentException("Basskada måste vara minst 1");
        }
        if (accuracy < 1 || accuracy > 100) {
            throw new IllegalArgumentException("Träffsäkerhet måste vara mellan 1 och 100");
        }
        if (type == null) {
            throw new IllegalArgumentException("Typ får inte vara null");
        }
        this.name = name;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
        this.type = type;
    }
}