Generation: 15 Highest Fitness: 0.9306532663316583
Desired fitness reached. Terminating.

===== GENETIC PROGRAMMING RESULTS =====

BEST PROGRAM:
F1 Score: 0.9306532663316583
Tree Structure:
+ [function]
├── max [function]
│   ├── Close [terminal]
│   └── * [function]
│       ├── / [function]
│       │   ├── 1.5 [constant]
│       │   └── / [function]
│       │       ├── 1.0 [constant]
│       │       └── Open [terminal]
│       └── - [function]
│           ├── * [function]
│           │   ├── / [function]
│           │   │   ├── High [terminal]
│           │   │   └── - [function]
│           │   │       ├── High [terminal]
│           │   │       └── High [terminal]
│           │   └── / [function]
│           │       ├── / [function]
│           │       │   ├── 1.0 [constant]
│           │       │   └── Open [terminal]
│           │       └── min [function]
│           │           ├── Close [terminal]
│           │           └── Close [terminal]
│           └── 1.5 [constant]
└── * [function]
    ├── / [function]
    │   ├── / [function]
    │   │   ├── Close [terminal]
    │   │   └── Close [terminal]
    │   └── - [function]
    │       ├── Close [terminal]
    │       └── Open [terminal]
    └── / [function]
        ├── / [function]
        │   ├── 1.0 [constant]
        │   └── Open [terminal]
        └── min [function]
            ├── Close [terminal]
            └── Close [terminal]

RUNTIME STATISTICS:
Total Generations: 20
Generation Runtimes (ms):
  Generation 0: 285 ms
  Generation 1: 211 ms
  Generation 2: 44 ms
  Generation 3: 37 ms
  Generation 4: 39 ms
  Generation 5: 33 ms
  Generation 6: 24 ms
  Generation 7: 17 ms
  Generation 8: 21 ms
  Generation 9: 25 ms
  Generation 10: 31 ms
  Generation 11: 32 ms
  Generation 12: 24 ms
  Generation 13: 24 ms
  Generation 14: 24 ms
Total Runtime: 871 ms
Average Generation Runtime: 58 ms

PARAMETERS:
Population Size: 40
Generations: 20
Tree Depth Range: 4-10
Tournament Size: 5
Mutation Probability: 0.2
Crossover Probability: 0.8
Elitism Percentage: 0.05
Random Seed: 1748517586491

======================================

===== TESTING BEST SOLUTION ON TEST DATA =====
Test Data Results:
F1 Score: 0.9430379746835443
Precision: 0.988391376451078
Recall: 0.9016641452344932

Confusion Matrix:
TP: 596 (Predicted Buy, Actual Buy)
FP: 7 (Predicted Buy, Actual Don't Buy)
FN: 65 (Predicted Don't Buy, Actual Buy)
TN: 593 (Predicted Don't Buy, Actual Don't Buy)
Accuracy: 0.9429024583663759
===========================================
Function: +(max(Close, *(/(1.5, /(1.0, Open)), -(*(/(High, -(High, High)), /(/(1.0, Open), min(Close, Close))), 1.5))), *(/(/(Close, Close), -(Close, Open)), /(/(1.0, Open), min(Close, Close))))
Program Execution time: 989 ms