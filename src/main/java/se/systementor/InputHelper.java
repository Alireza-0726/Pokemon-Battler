package se.systementor;

import java.util.Scanner;
import java.util.ArrayList;

public class InputHelper {

    public static int readIntInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.printf("Ange ett tal mellan %d och %d.%n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Det där var inget heltal. Försök igen.");
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Får inte vara tomt. Försök igen");
                continue;
            }
            return input;
        }
    }

    public static Type readType(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Type.valueOf(input);
            }  catch (IllegalArgumentException e) {
                System.out.println("Ogiltig typ. Välj mellan: FIRE, WATER, GRASS, ELECTRIC, NORMAL");
            }
        }
    }

    public static Attack readAttack(Scanner scanner) {
        System.out.println(" -- Ny attack -- ");
        String name = readNonEmptyString(scanner, " Attackens namn: ");
        int baseDamage = readIntInRange(scanner, " Basskada (1-200): ", 1, 200);
        int accuracy = readIntInRange(scanner, " Träffsäkerhet (1-100): ", 1, 100);
        Type type = readType(scanner, "Typ: ");
        return new Attack(name, baseDamage, accuracy, type);
    }

    public static int selectPokemonIndex(Scanner scanner, ArrayList<Pokemon> pokedex) {
        for (int i = 0; i < pokedex.size(); i++) {
            System.out.println((i + 1) + ". " + pokedex.get(i).name);
        }
        return readIntInRange(scanner, "Välj nummer: ", 1, pokedex.size()) - 1;
    }

}
