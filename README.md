God simulator: a game where you decide how your ant colony evolves.
<br>
To run: './mvnw clean javafx:run' or './mvnw.cmd clean javafx:run'


Example call stack in a world with 3 colonies, with 100 Ants each:
```
World#calculateFrame
├── AntPopulation#calculateFrame (population 1)
│   ├── Ant#act (ant 1)
│   ├── Ant#act (ant 2)
│   └── ... (up to ant 100)
├── AntPopulation#calculateFrame (population 2)
│   └── ... (100 ants)
└── AntPopulation#calculateFrame (population 3)
    └── ... (100 ants)
```