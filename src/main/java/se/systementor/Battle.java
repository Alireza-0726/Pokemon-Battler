package se.systementor;

import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Pokemon playerPokemon;
    private Pokemon enemyPokemon;
    private Scanner scanner;
    private Random random;

    public Battle(Pokemon playerPokemon, Pokemon enemyPokemon, Scanner scanner) {
        this.playerPokemon = playerPokemon;
        this.enemyPokemon = enemyPokemon;
        this.scanner = scanner;
        this.random = new Random();
    }

    public boolean start() {
        System.out.println();
        System.out.println("=== STRID STARTAR ===");
        System.out.println("Du: " + playerPokemon.toDisplayString());
        System.out.println("Motståndare: " + enemyPokemon.toDisplayString());

        boolean playerTurn = playerPokemon.speed >= enemyPokemon.speed;
        if (playerTurn) {
            System.out.println(playerPokemon.name + " är snabbast och börjar!");
        } else {
            System.out.println(enemyPokemon.name + " är snabbast och börjar!");
        }

        while (playerPokemon.currentHp > 0 && enemyPokemon.currentHp > 0) {
            if (playerTurn) {
                playerAttack();
            } else {
                enemyAttack();
            }
            playerTurn = !playerTurn;
        }

        System.out.println();
        System.out.println("=== STRIDEN ÄR SLUT ===");
        if (playerPokemon.currentHp <= 0) {
            System.out.println("Du förlorade! " + playerPokemon.name + " blev besegrad!");
            return false;
        }  else {
            System.out.println("Du vann! " + playerPokemon.name + " besegrade " + enemyPokemon.name + "!");
            return true;
        }
    }

    private void playerAttack () {
        System.out.println();
        System.out.println("--- Din tur ---");
        showHp();

        for (int i = 0; i < playerPokemon.attacks.size(); i++) {
            System.out.println((i + 1) + ". " + playerPokemon.attacks.get(i).name);
        }
        int choice = InputHelper.readIntInRange(scanner, "Välj attack: ", 1, playerPokemon.attacks.size()) - 1;
        Attack attack =  playerPokemon.attacks.get(choice);
        executeAttack(playerPokemon, enemyPokemon, attack);
    }

    private void enemyAttack() {
        System.out.println();
        System.out.println("--- Motståndarens tur ---");
        showHp();

        Attack attack = enemyPokemon.attacks.get(random.nextInt(enemyPokemon.attacks.size()));
        executeAttack(enemyPokemon, playerPokemon, attack);
    }

    private void executeAttack(Pokemon attacker, Pokemon defender, Attack attack) {
        System.out.println(attacker.name + " använder " + attack.name + "!");

        int roll = random.nextInt(100) + 1;
        if (roll > attack.accuracy) {
            System.out.println("...men missade!");
            return;
        }

        double randomFactor = 0.85 + (random.nextDouble() * 0.15);
        int damage = (int) (attack.baseDamage * randomFactor);
        if (damage < 1) damage = 1;

        defender.takeDamage(damage);
        System.out.println("Träffade! " + damage + " damage");
        System.out.println(defender.name + " HP: " +  defender.currentHp + "/" + defender.maxHp);
    }

    private void showHp() {
        System.out.println(playerPokemon.name + ": " + playerPokemon.currentHp +  "/" + playerPokemon.maxHp + " HP" );
        System.out.println(enemyPokemon.name + ": " + enemyPokemon.currentHp + "/" + enemyPokemon.maxHp + " HP" );
    }
}
