# Pokedex

En konsolapplikation i java för att hantera en samling pokemon. Byggd som inlämningsuppgift 1 i kursen programmering java grund.

## Funktioner

- Visa alla pokemon i pokedexen
- Skapa nya pokemon med 1-4 attacker
- Redigera befintliga pokemon (namn, typ, HP, lägg till/ta bort attacker)
- Radera pokemon
- Spara och ladda från textfil (pokedex.csv)
- Återställ pokedexen till 6 fördefinierade pokemon
- Automatisk seeding vid första körningen
- Automatisk sparning vid avslut

## Hur man kör

### Med IntelliJ
1. Öppna projektet i IntelliJ
2. Vänta tills maven har laddat klart
3. Höger klicka på `src/main/java/se/systementor/Main.java`
4. Välj "Run 'Main.main()'"

## Projektstruktur
- `src/main/java/se/systementor/` - all källkod
- `Main.java` - meny och programflöde
- `Pokemon.java` - pokemon klass
- `Attack.java` - attack klass
- `Type.java` - enum med typer (FIRE, WATER, GRASS, ELECTRIC, NORMAL)
- `InputHelper.java` - hjälpmetoder för säker inläsning
- `pokedex.csv` - sparad data (skapas automatiskt vid första sparning)

## Exempel på användning
```
---- HUVUDMENY ----
1. Visa hela pokedexen
2. Skapa ny pokemon
3. Ändra info om pokemon
4. Radera pokemon
5. Spara till textfil
6. läs in från textfil
7. Återställ standardpokedex
8. Avsluta programmet

Ditt menyval: 2
--- Skapa ny pokemon ---
Ange namn på pokemonen: Charizard
Ange typ (FIRE, WATER, GRASS, ELECTRIC, NORMAL): fire
Ange maximalt HP: 78
Hur många attacker (1-4)? 2
...
```

## Teknisk info
- Java 21
- Maven
- Filformat: CSV med `;` mellan pokemon fält och `|` mellan attacker