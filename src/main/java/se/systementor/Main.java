package se.systementor;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Random;


public class Main {
    static int wins = 0;
    static int losses = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Pokemon> pokedex = new ArrayList<>();
        boolean running = true;

        System.out.println("Hej och välkommen till pokedex!");
        load(pokedex);
        if (pokedex.isEmpty()) {
            seedData(pokedex);
            System.out.println("Första körningen, standardpokedex laddat.");
        }

        while (running) {
            System.out.println("\n---- HUVUDMENY ----");
            System.out.println("1. Visa hela pokedexen");
            System.out.println("2. Skapa ny pokemon");
            System.out.println("3. Ändra info om pokemon");
            System.out.println("4. Radera pokemon");
            System.out.println("5. Spara till textfil");
            System.out.println("6. Läs in från textfil");
            System.out.println("7. Återställ standardpokedex");
            System.out.println("8. Starta strid");
            System.out.println("9. Avsluta programmet");

            int choice = InputHelper.readIntInRange(scanner, "Ditt menyval: ",1, 9);

            switch (choice) {
                case 1 -> showAll(pokedex);
                case 2 -> add(scanner, pokedex);
                case 3 -> edit(scanner, pokedex);
                case 4 -> remove(scanner, pokedex);
                case 5 -> save(pokedex);
                case 6 -> load(pokedex);
                case 7 -> {
                    seedData(pokedex);
                    System.out.println("Standardpokedexen återställd!");
                }
                case 8 -> startBattle(scanner, pokedex);
                case 9 -> {
                    save(pokedex);
                    System.out.println("Hej då, välkommen åter!");
                    running = false;
                }
            }

        }
        scanner.close();
    }

    public static void showAll  (ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Pokedexen har inga pokemons än.");
            return;
        }
        for (Pokemon p : pokedex) {
            System.out.println(p.toDisplayString());
        }
    }

    public static void add(Scanner scanner, ArrayList<Pokemon> pokedex) {
        System.out.println("--- Skapa ny pokemon ---");
        String name = InputHelper.readNonEmptyString(scanner, "Ange namn på pokemonen: ");
        Type type = InputHelper.readType(scanner, "Ange typ (FIRE, WATER, GRASS, ELECTRIC, NORMAL): ");
        int maxHp = InputHelper.readIntInRange(scanner, "Ange maximalt HP: ", 1, 1000);
        int speed = InputHelper.readIntInRange(scanner, "Ange speed (1-200): ", 1, 200);

        Pokemon p = new Pokemon(name, type, maxHp, speed);

        int numAttacks = InputHelper.readIntInRange(scanner, "Hur många attacker (1-4)? ", 1, 4);
        for (int i = 0; i < numAttacks; i++) {
            System.out.println("Attack " + (i + 1) + " av " + numAttacks + ":");
            Attack a = InputHelper.readAttack(scanner);
            p.attacks.add(a);
        }

        pokedex.add(p);
        System.out.println(name + " lades till i pokedexen");
    }

    public static void remove(Scanner scanner, ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Inget att radera, pokedexen är tom.");
            return;
        }
        System.out.println("--- Radera pokemon ---");
        int index = InputHelper.selectPokemonIndex(scanner, pokedex);
        String name = pokedex.get(index).name;
        pokedex.remove(index);
        System.out.println(name + " togs bort från pokedexen.");
    }

    public static void startBattle(Scanner scanner, ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Du har inga pokemons att strida med.");
            return;
        }
        if (pokedex.size() < 2) {
            System.out.println("Du behöver minst 2 pokemons för att strida.");
            return;
        }

        System.out.println("--- Välj din Pokemon ---");
        int playerIndex = InputHelper.selectPokemonIndex(scanner, pokedex);
        Pokemon player = pokedex.get(playerIndex);

        if (player.currentHp <= 0) {
            System.out.println(player.name + " har 0 HP och kan inte strida!");
            return;
        }
        if (player.attacks.isEmpty()) {
            System.out.println(player.name + " har inga attacker och kan inte strida!");
            return;
        }
        Random random = new Random();
        Pokemon enemy;
        do {
            enemy =  pokedex.get(random.nextInt(pokedex.size()));
        } while (enemy == player);

        if (enemy.currentHp <= 0 || enemy.attacks.isEmpty()) {
            System.out.println(enemy.name + " kan inte strida just nu. Försök igen.");
            return;
        }

        Battle battle = new Battle(player, enemy, scanner);
        boolean won = battle.start();

        if (won) {
            wins++;
        } else {
            losses++;
        }

        player.currentHp = player.maxHp;
        enemy.currentHp = enemy.maxHp;
    }

    public static void edit(Scanner scanner, ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Inget att redigera, pokedexen är tom.");
            return;
        }
        System.out.println("--- Redigera pokemon ---");
        int index = InputHelper.selectPokemonIndex(scanner, pokedex);
        Pokemon p = pokedex.get(index);

        System.out.println("Vald pokemon: " + p.toDisplayString());
        String newName = InputHelper.readNonEmptyString(scanner, "Nytt namn: ");
        p.name = newName;
        System.out.println("Namnet uppdaterat!");

        Type newType = InputHelper.readType(scanner, "Ny typ (FIRE, WATER, GRASS, ELECTRIC, NORMAL): ");
        p.type = newType;
        System.out.println("Typen uppdaterad!");

        int newHp = InputHelper.readIntInRange(scanner, "Nytt max HP: ", 1, 1000);
        p.maxHp = newHp;
        if (p.currentHp > p.maxHp) {
            p.currentHp = p.maxHp;
        }
        System.out.println("HP uppdaterat!");

        int newSpeed = InputHelper.readIntInRange(scanner, "Ny speed (1-200): ", 1, 200);
        p.speed = newSpeed;
        System.out.println("Speed uppdaterat!");

        System.out.println("Nuvarande attacker: " + p.attacks.size());
        if (p.attacks.size() < 4) {
            String answer = InputHelper.readNonEmptyString(scanner, "Vill du lägga till en attack? (ja/nej):  ");
            if (answer.equalsIgnoreCase("ja")) {
                Attack nyAttack = InputHelper.readAttack(scanner);
                p.attacks.add(nyAttack);
                System.out.println("Attacken tillagd!");
            }
        } else {
            System.out.println("Pokemonen har redan 4 attacker (max).");
        }

        if (p.attacks.size() > 1) {
            String answer2 = InputHelper.readNonEmptyString(scanner, "Vill du ta bort en attack? (ja/nej):  ");
            if (answer2.equalsIgnoreCase("ja")) {
                System.out.println("Vilken attack vill du ta bort?");
                for (int i = 0; i < p.attacks.size(); i++) {
                    System.out.println((i + 1) + ". " + p.attacks.get(i).name);
                }
                int attackIndex = InputHelper.readIntInRange(scanner, "Välj nummer: ", 1, p.attacks.size()) - 1;
                String removedName = p.attacks.get(attackIndex).name;
                p.attacks.remove(attackIndex);
                System.out.println(removedName + " togs bort.");
            }
        } else  {
            System.out.println("Pokemonen måste ha minst 1 attack.");
        }
    }

    public static void seedData(ArrayList<Pokemon> pokedex) {
        pokedex.clear();

        Pokemon p1 = new Pokemon("Charizard", Type.FIRE, 78, 100);
        p1.attacks.add(new Attack("Flamethrower", 90, 100, Type.FIRE));
        p1.attacks.add(new Attack("Wing Attack", 60, 100, Type.NORMAL));
        pokedex.add(p1);

        Pokemon p2 = new Pokemon("Blastoise", Type.WATER, 79, 78);
        p2.attacks.add(new Attack("Hydro Pump", 110, 80, Type.WATER));
        p2.attacks.add(new Attack("Bite", 60, 100, Type.NORMAL));
        pokedex.add(p2);

        Pokemon p3 = new Pokemon("Venusaur", Type.GRASS, 80, 80);
        p3.attacks.add(new Attack("Solar Beam", 120, 85, Type.GRASS));
        p3.attacks.add(new Attack("Vine Whip", 45, 100, Type.GRASS));
        pokedex.add(p3);

        Pokemon p4 = new Pokemon("Pikachu", Type.ELECTRIC, 35, 90);
        p4.attacks.add(new Attack("Thunderbolt", 90, 100, Type.ELECTRIC));
        p4.attacks.add(new Attack("Quick Attack", 40, 100, Type.NORMAL));
        pokedex.add(p4);

        Pokemon p5 = new Pokemon("Snorlax", Type.NORMAL, 160, 30);
        p5.attacks.add(new Attack("Body Slam", 85, 100, Type.NORMAL));
        p5.attacks.add(new Attack("Rest", 1, 100, Type.NORMAL));
        pokedex.add(p5);

        Pokemon p6 = new Pokemon("Gengar", Type.NORMAL, 60, 110);
        p6.attacks.add(new Attack("Shadow Ball", 80, 100, Type.NORMAL));
        p6.attacks.add(new Attack("Lick", 30, 100, Type.NORMAL));
        pokedex.add(p6);
    }

    public static void save(ArrayList<Pokemon> pokedex) {
        Path path = Path.of("pokedex.csv");
        StringBuilder sb = new StringBuilder();

        for (Pokemon p : pokedex) {
            sb.append(p.name).append(";");
            sb.append(p.type).append(";");
            sb.append(p.maxHp).append(";");
            sb.append(p.currentHp).append(";");
            sb.append(p.speed).append(";");

            for (int i  = 0; i < p.attacks.size(); i++) {
                Attack a = p.attacks.get(i);
                if (i > 0) sb.append("|");
                sb.append(a.name).append(",")
                        .append(a.baseDamage).append(",")
                        .append(a.accuracy).append(",")
                        .append(a.type);

            }
            sb.append("\n");
        }

        try {
            Files.writeString(path, sb.toString());
            System.out.println("Sparat till: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Kunde inte spara: " + e.getMessage());
        }
    }

    public static void load(ArrayList<Pokemon> pokedex) {
        Path path = Path.of("pokedex.csv");

        if (!Files.exists(path)) {
            System.out.println("Ingen sparad fil hittades.");
            return;
        }

        try {
            ArrayList<String> lines = new ArrayList<>(Files.readAllLines(path));
            pokedex.clear();

            for (String line : lines) {
                if (line.isBlank()) continue;

                String[] fields = line.split(";");
                if (fields.length !=6) continue;

                try {
                    String name = fields[0];
                    Type type = Type.valueOf(fields[1]);
                    int maxHp = Integer.parseInt(fields[2]);
                    int currentHp = Integer.parseInt(fields[3]);
                    int speed = Integer.parseInt(fields[4]);

                    Pokemon p = new Pokemon(name, type, maxHp, speed);
                    p.currentHp = currentHp;

                    if (!fields[5].isEmpty()) {
                        String[] attacks = fields[5].split("\\|");
                        for (String atk : attacks) {
                            String[] atkFields = atk.split(",");
                            if (atkFields.length !=4) continue;

                            String atkName = atkFields[0];
                            int dmg = Integer.parseInt(atkFields[1]);
                            int acc =  Integer.parseInt(atkFields[2]);
                            Type atkType = Type.valueOf(atkFields[3]);

                            p.attacks.add(new Attack(atkName, dmg, acc, atkType));
                        }
                    }

                    pokedex.add(p);
                } catch (IllegalArgumentException e) {
                    System.out.println("Hoppar över trasig rad: " + line);
                }
            }

            System.out.println("Laddat " + pokedex.size() + " pokemons från fil.");
        } catch (IOException e) {
            System.out.println("Kunde inte läsa filen: " + e.getMessage());
        }
    }

    public static void saveStats() {
        Path path = Path.of("battle_stats.csv");
        String data = wins + ";" + losses;
        try {
            Files.writeString(path, data);
        } catch (IOException e) {
            System.out.println("Kunde inte spara statistik: " + e.getMessage());
        }
    }

    public static void loadStats() {
        Path path = Path.of("battle_stats.csv");
        if (!Files.exists(path)) {
            return;
        }
        try {
            String data = Files.readString(path).trim();
            String[] fields = data.split(";");
            if (fields.length == 2) {
                wins = Integer.parseInt(fields[0]);
                losses = Integer.parseInt(fields[1]);
            } return;
        } catch (IOException e) {
            System.out.println("Kunde inte spara statistik: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Trasig statistikfil, startar från 0.");
            wins = 0;
            losses = 0;
        }
    }
}
