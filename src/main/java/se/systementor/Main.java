package se.systementor;

import java.util.ArrayList;
import java.util.Scanner;

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
                case 5 -> System.out.println("Den här funktionen är inte färdig än");
                case 6 -> System.out.println("Den här funktionen är inte färdig än");
                case 7 -> System.out.println("Den här funktionen är inte färdig än");
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
    }
}
