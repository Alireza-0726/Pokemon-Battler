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
                case 3 -> System.out.println("Den här funktionen är inte färdig än");
                case 4 -> System.out.println("Den här funktionen är inte färdig än");
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
        pokedex.add(p);
        System.out.println(name + " lades till i pokedexen");
    }
}
