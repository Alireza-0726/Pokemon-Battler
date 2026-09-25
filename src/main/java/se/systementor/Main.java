package se.systementor;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Pokemon> pokedex = new ArrayList<>();
        boolean running = true;

        System.out.println("Hej och välkommen till pokedex!");

        while (running) {
            System.out.println("\n---- HUVUDMENY ----");
            System.out.println("1. Visa hela pokedexen");
            System.out.println("2. Skapa ny pokemon");
            System.out.println("3. Andra info om pokemon");
            System.out.println("4. Radera pokemon");
            System.out.println("5. Spara till textfil");
            System.out.println("6. Läs in från textfil");
            System.out.println("7. Återställ standardpokedex");
            System.out.println("8. Avsluta programmet");

            int choice = InputHelper.readIntInRange(scanner, "Ditt menyval: ",1, 8);

            switch (choice) {
                case 1 -> visaAlla (pokedex);
                case 2 -> laggTill(scanner, pokedex);
                case 3 -> redigera(scanner, pokedex);
                case 4 -> taBort(scanner, pokedex);
                case 5 -> save(pokedex);
                case 6 -> load(pokedex);
                case 7 -> {
                    seedData(pokedex);
                    System.out.println("Standardpokedexen återställd!");
                }
                case 8 -> {
                    System.out.println("Hej då, välkommen åter!");
                    running = false;
                }
            }

        }
        scanner.close();
    }

    public static void visaAlla  (ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Pokedexen har inga pokemons än.");
            return;
        }
        for (Pokemon p : pokedex) {
            System.out.println(p.toDisplayString());
        }
    }

    public static void laggTill(Scanner scanner, ArrayList<Pokemon> pokedex) {
        System.out.println("--- Skapa ny pokemon ---");
        String name = InputHelper.readNonEmptyString(scanner, "Ange namn på pokemonen: ");
        Type type = InputHelper.readType(scanner, "Ange typ (FIRE, WATER, GRASS, ELECTRIC, NORMAL): ");
        int maxHp = InputHelper.readIntInRange(scanner, "Ange maximalt HP: ", 1, 1000);

        Pokemon p = new Pokemon(name, type, maxHp);

        int antalAttacker = InputHelper.readIntInRange(scanner, "Hur många attacker (1-4)? ", 1, 4);
        for (int i = 0; i < antalAttacker; i++) {
            System.out.println("Attack " + (i + 1) + " av " + antalAttacker + ":");
            Attack a = InputHelper.readAttack(scanner);
            p.attacks.add(a);
        }

        pokedex.add(p);
        System.out.println(name + " lades till i pokedexen");
    }

    public static void taBort(Scanner scanner, ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Inget att radera, pokedexen är tom.");
            return;
        }
        System.out.println("--- Radera pokemon ---");
        int index = InputHelper.valjPokemonIndex(scanner, pokedex);
        String namn = pokedex.get(index).name;
        pokedex.remove(index);
        System.out.println(namn + " togs bort från pokedexen.");
    }

    public static void redigera(Scanner scanner, ArrayList<Pokemon> pokedex) {
        if (pokedex.isEmpty()) {
            System.out.println("Inget att redigera, pokedexen är tom.");
            return;
        }
        System.out.println("--- Redigera pokemon ---");
        int index = InputHelper.valjPokemonIndex(scanner, pokedex);
        Pokemon p = pokedex.get(index);

        System.out.println("Vald pokemon: " + p.toDisplayString());
        String nyttNamn = InputHelper.readNonEmptyString(scanner, "Nytt namn: ");
        p.name = nyttNamn;
        System.out.println("Namnet uppdaterat!");

        Type nyType = InputHelper.readType(scanner, "Ny typ (FIRE, WATER, GRASS, ELECTRIC, NORMAL): ");
        p.type = nyType;
        System.out.println("Typen uppdaterad!");

        int nyttHp = InputHelper.readIntInRange(scanner, "Nytt max HP: ", 1, 1000);
        p.maxHp = nyttHp;
        if (p.currentHp > p.maxHp) {
            p.currentHp = p.maxHp;
        }
        System.out.println("HP uppdaterat!");

        System.out.println("Nuvarande attacker: " + p.attacks.size());
        if (p.attacks.size() < 4) {
            String svar = InputHelper.readNonEmptyString(scanner, "Vill du lägga till en attack? (ja/nej):  ");
            if (svar.equalsIgnoreCase("ja")) {
                Attack nyAttack = InputHelper.readAttack(scanner);
                p.attacks.add(nyAttack);
                System.out.println("Attacken tillagd!");
            }
        } else {
            System.out.println("Pokemonen har redan 4 attacker (max).");
        }

        if (p.attacks.size() > 1) {
            String svar2 = InputHelper.readNonEmptyString(scanner, "Vill du ta bort en attack? (ja/nej):  ");
            if (svar2.equalsIgnoreCase("ja")) {
                System.out.println("Vilken attack vill du ta bort?");
                for (int i = 0; i < p.attacks.size(); i++) {
                    System.out.println((i + 1) + ". " + p.attacks.get(i).name);
                }
                int attackIndex = InputHelper.readIntInRange(scanner, "Välj nummer: ", 1, p.attacks.size()) - 1;
                String borttagen = p.attacks.get(attackIndex).name;
                p.attacks.remove(attackIndex);
                System.out.println(borttagen + " togs bort.");
            }
        } else  {
            System.out.println("Pokemonen måste ha minst 1 attack.");
        }
    }

    public static void seedData(ArrayList<Pokemon> pokedex) {
        pokedex.clear();

        Pokemon p1 = new Pokemon("Charizard", Type.FIRE, 78);
        p1.attacks.add(new Attack("Flamethrower", 90, 100, Type.FIRE));
        p1.attacks.add(new Attack("Wing Attack", 60, 100, Type.NORMAL));
        pokedex.add(p1);

        Pokemon p2 = new Pokemon("Blastoise", Type.WATER, 79);
        p2.attacks.add(new Attack("Hydro Pump", 110, 80, Type.WATER));
        p2.attacks.add(new Attack("Bite", 60, 100, Type.NORMAL));
        pokedex.add(p2);

        Pokemon p3 = new Pokemon("Venusaur", Type.GRASS, 80);
        p3.attacks.add(new Attack("Solar Beam", 120, 85, Type.GRASS));
        p3.attacks.add(new Attack("Vine Whip", 45, 100, Type.GRASS));
        pokedex.add(p3);

        Pokemon p4 = new Pokemon("Pikachu", Type.ELECTRIC, 35);
        p4.attacks.add(new Attack("Thunderbolt", 90, 100, Type.ELECTRIC));
        p4.attacks.add(new Attack("Quick Attack", 40, 100, Type.NORMAL));
        pokedex.add(p4);

        Pokemon p5 = new Pokemon("Snorlax", Type.NORMAL, 160);
        p5.attacks.add(new Attack("Body Slam", 85, 100, Type.NORMAL));
        p5.attacks.add(new Attack("Rest", 1, 100, Type.NORMAL));
        pokedex.add(p5);

        Pokemon p6 = new Pokemon("Gengar", Type.NORMAL, 60);
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
            ArrayList<String> rader = new ArrayList<>(Files.readAllLines(path));
            pokedex.clear();

            for (String rad : rader) {
                if (rad.isBlank()) continue;

                String[] falt = rad.split(";");
                if (falt.length !=5) continue;

                try {
                    String namn = falt[0];
                    Type type = Type.valueOf(falt[1]);
                    int maxHp = Integer.parseInt(falt[2]);
                    int currentHp = Integer.parseInt(falt[3]);

                    Pokemon p = new Pokemon(namn, type, maxHp);
                    p.currentHp = currentHp;

                    if (!falt[4].isEmpty()) {
                        String[] attacker = falt[4].split("\\|");
                        for (String atk : attacker) {
                            String[] atkFalt = atk.split(",");
                            if (atkFalt.length !=4) continue;

                            String atkNamn = atkFalt[0];
                            int dmg = Integer.parseInt(atkFalt[1]);
                            int acc =  Integer.parseInt(atkFalt[2]);
                            Type atkType = Type.valueOf(atkFalt[3]);

                            p.attacks.add(new Attack(atkNamn, dmg, acc, atkType));
                        }
                    }

                    pokedex.add(p);
                } catch (IllegalArgumentException e) {
                    System.out.println("Hoppar över trasig rad: " + rad);
                }
            }

            System.out.println("Laddat " + pokedex.size() + " pokemons från fil.");
        } catch (IOException e) {
            System.out.println("Kunde inte läsa filen: " + e.getMessage());
        }
    }
}
