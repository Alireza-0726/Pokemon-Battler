package se.systementor;

import java.util.Scanner;

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

}
