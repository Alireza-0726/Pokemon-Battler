package se.systementor;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;


public class Main {
    static int wins = 0;
    static int losses = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Pokemon> pokedex = new ArrayList<>();
        boolean running = true;

        System.out.println("Hej och välkommen till pokedex!");
        Storage.loadPokedex(pokedex);
        int [] stats = Storage.loadStats();
        wins = stats[0];
        losses = stats[1];
        if (pokedex.isEmpty()) {
            PokemonService.seedData(pokedex);
            System.out.println("Första körningen, standardpokedex laddat.");
        }

        try {
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
                System.out.println("9. Visa stridresultatet");
                System.out.println("10. Avsluta programmet");

                int choice = InputHelper.readIntInRange(scanner, "Ditt menyval: ", 1, 10);

                switch (choice) {
                    case 1 -> PokemonService.showAll(pokedex);
                    case 2 -> PokemonService.add(scanner, pokedex);
                    case 3 -> PokemonService.edit(scanner, pokedex);
                    case 4 -> PokemonService.remove(scanner, pokedex);
                    case 5 -> Storage.savePokedex(pokedex);
                    case 6 -> Storage.loadPokedex(pokedex);
                    case 7 -> {
                        PokemonService.seedData(pokedex);
                        System.out.println("Standardpokedexen återställd!");
                    }
                    case 8 -> startBattle(scanner, pokedex);
                    case 9 -> showStats();
                    case 10 -> running = false;
                }
            }

        } catch (java.util.NoSuchElementException e) {
            System.out.println();
            System.out.println("Ingen mer input. Avslutar och sparar.");
        }

        Storage.savePokedex(pokedex);
        Storage.saveStats(wins, losses);
        System.out.println("Hej då, välkommen åter!");
        scanner.close();
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
            enemy = pokedex.get(random.nextInt(pokedex.size()));
        } while (enemy == player);

        if (enemy.currentHp <= 0 || enemy.attacks.isEmpty()) {
            System.out.println(enemy.name + " kan inte strida just nu. Försök igen.");
            return;
        }

        Battle battle = new Battle(player, enemy, scanner);

        try {
            boolean won = battle.start();
            if (won) {
                wins++;
            } else {
                losses++;
            }
            Storage.saveStats(wins, losses);
        } finally {
            player.currentHp = player.maxHp;
            enemy.currentHp = enemy.maxHp;
        }
    }


    public static void showStats() {
        System.out.println("--- Stridsresultat ---");
        System.out.println("Vinster: " + wins);
        System.out.println("Förluster: " + losses);

        int total = wins + losses;
        if (total > 0) {
            double ratio = (double) wins / total * 100;
            System.out.printf("Vinstprocent: %.1f%%%n", ratio);
        } else {
            System.out.println("Inga strider spelade ännu.");
        }
    }
}
