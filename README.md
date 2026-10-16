# Pokemon Battler
En konsolapplikation i Java där du kan hantera en Pokedex och slåss mot en datorstyrd motståndare. Byggt som en fortsättning på Pokedex uppgiften (inlämning 1).

## Beskrivning
Programmet har en huvudmeny med 10 val:

1. Visa hela Pokedexen
2. Skapa ny Pokemon
3. Ändra info om Pokemon
4. Radera Pokemon
5. Spara till textfil
6. Läs in från textfil
7. Återställ standardpokedex
8. Starta strid
9. Visa stridresultat
10. Avsluta programmet

Pokedexen sparas till `pokedex.csv` med menyval 5 och när programmet avslutas. Stridresultat (vinster/förluster) sparas till `battle_stats.csv` efter varje strid. Båda filerna sparas automatiskt och ligger i `.gitignore`.

## Hur man kör
Kräver **Java 21**. Öppna projektet i IntelliJ och kör `Main.java`.


## Klasser

- **Pokemon** – modell. Håller namn, typ, maxHp, currentHp, speed och en lista av attacker. Validering i konstruktorn (namn får inte vara tomt, HP/speed måste vara > 0). Har `takeDamage()` för att minska HP säkert.
- **Attack** – modell. Håller namn, basskada, träffsäkerhet och typ. Validering i konstruktorn (namn inte tomt, basskada ≥ 1, träffsäkerhet 1-100).
- **Type** – enum med FIRE, WATER, GRASS, ELECTRIC, NORMAL.
- **Battle** – kör en enskild strid mellan två pokemons. Turordning, attack-val, träffsäkerhet, skada och HP-uppdatering. Vet inget om meny eller filhantering.
- **PokemonService** – CRUD-operationer (visa, skapa, ändra, radera, seed-data).
- **Storage** – all filhantering (spara/ladda pokédex och statistik).
- **InputHelper** – statiska hjälpmetoder för säker inläsning (readIntInRange, readNonEmptyString, readType, readAttack).
- **Main** – meny-loopen. Anropar de andra klasserna, förbereder strider (`startBattle`) och visar statistik (`showStats`). Själva stridslogiken ligger i `Battle`.


## OOP-val

Projektet är uppdelat i klasser med tydligt ansvar (Separation of Concerns):

- **Modeller** (`Pokemon`, `Attack`, `Type`) – rena databärare med validering i konstruktorn.
- **Battle** – all stridslogik för en enskild strid. Main anropar `battle.start()`.
- **PokemonService** – CRUD-logiken. Flyttat ut ur Main för att Main ska bli en tunn meny.
- **Storage** – all fil-I/O. Gör att Main slipper bry sig om CSV-format eller IOException.
- **Main** – meny, samordning, samt `startBattle` (väljer pokemon, motståndare, räknar vinster/förluster) och `showStats`.

Detta är grundläggande SOC. Ingen full paketstruktur (model/repository/service/ui) används eftersom det är VG-nivå, men varje klass har ett tydligt ansvar.


## Skadeberäkning

Formel:

    damage = baseDamage * slumpfaktor

Där slumpfaktorn är mellan **0.85 och 1.0** (slumpas varje träff).

Träffsäkerhet: ett slag mellan 1 och 100. Om slaget är högre än attackens `accuracy` så missar attacken.

Turordning: pokemonen med **högst speed** börjar. Vid lika speed börjar spelaren.

CPU väljer attack slumpvis bland sina egna attacker.

HP återställs till max efter varje strid. Om input avbryts mitt i en strid (Ctrl+D) sparas data och programmet avslutas snyggt utan krasch.


## Kända begränsningar

- Ingen typ-effektivitetstabell (FIRE vs GRASS ger samma skada som FIRE vs WATER).
- Persistens sker i CSV-format, inte JSON.
- Stridsstatistik sparas bara som totala vinster/förluster, inte per pokemon eller per attack.
- Ingen kritisk träff-mekanik.
- Motståndaren väljs slumpvis från din egen Pokédex, så du behöver minst 2 pokemons för att kunna strida.
- Namn får inte innehålla `;`, `,` eller `|` eftersom de används som separatorer i CSV-filen.