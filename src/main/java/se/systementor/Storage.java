package se.systementor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Storage {

    public static void savePokedex(ArrayList<Pokemon> pokedex) {
        Path path = Path.of("pokedex.csv");
        StringBuilder sb = new StringBuilder();

        for (Pokemon p : pokedex) {
            sb.append(p.name).append(";");
            sb.append(p.type).append(";");
            sb.append(p.maxHp).append(";");
            sb.append(p.currentHp).append(";");
            sb.append(p.speed).append(";");

            for (int i = 0; i < p.attacks.size(); i++) {
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

    public static void loadPokedex(ArrayList<Pokemon> pokedex) {
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

                String[] fields = line.split(";", -1);
                if (fields.length != 6) continue;

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
                            if (atkFields.length != 4) continue;

                            String atkName = atkFields[0];
                            int dmg = Integer.parseInt(atkFields[1]);
                            int acc = Integer.parseInt(atkFields[2]);
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

    public static void saveStats(int wins, int losses) {
        Path path = Path.of("battle_stats.csv");
        String data = wins + ";" + losses;
        try {
            Files.writeString(path, data);
        } catch (IOException e) {
            System.out.println("Kunde inte spara statistik: " + e.getMessage());
        }
    }

    // Returnerar int[2] = {wins, losses}
    public static int[] loadStats() {
        Path path = Path.of("battle_stats.csv");
        if (!Files.exists(path)) {
            return new int[]{0, 0};
        }
        try {
            String data = Files.readString(path).trim();
            String[] fields = data.split(";");
            if (fields.length == 2) {
                int wins = Integer.parseInt(fields[0]);
                int losses = Integer.parseInt(fields[1]);
                return new int[]{wins, losses};
            }
        } catch (IOException e) {
            System.out.println("Kunde inte läsa statistik: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Trasig statistikfil, startar från 0.");
        }
        return new int[]{0, 0};
    }
}