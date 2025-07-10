# Genetic Algorithm for Stock Trading

AI ALGO - Genetic Programming:  evaluation using accuracy and F1-score.

## Requirements

1. Java 21 (Recompile if you have a different version)
2. Makefile (Optional)

## Process

1. Randomly with programs (trees) built from your function and terminal sets.

2. evaluate the fitness of each program against your data.

3. check if termination not met (e.g., max generations or desired fitness),

4. Select parents (via tournament or other selection methods).

5. generate new programs from parents using crossover and mutation.

6. repeat from step 2 with the new population.


## Our functions and terminals are

### Terminals (input data)

Open - Todays price
High - Todays price
Low - Todays price
Close - Todays price
Adj Close - Yesterdays close
Const - Scale Thresholds


### Functions (+,-)

| **Operator**    | **Arity** | **Description**                                                      |
| --------------- | --------- | -------------------------------------------------------------------- |
| `+`             | 2         | Addition                                                             |
| `-`             | 2         | Subtraction                                                          |
| `*`             | 2         | Multiplication                                                       |
| `protected_div` | 2         | Division with zero protection (returns 1 on div-by-zero)             |
| `abs`           | 1         | Absolute value                                                       |
| `max`           | 2         | Maximum of two inputs                                                |
| `min`           | 2         | Minimum of two inputs                                                |


## Fitness function

Accuracy is imbalanced  
Chosen approach is fitness = 1.0 - f1_score;

Precision = TP / (TP + FP)
Recall = TP / (TP + FN)
F1 Score = 2 * (Precision * Recall) / (Precision + Recall)

0 = Dont buy
1 = Buy

| Term                    | What it Means                                        | In Your Case                        |
| ----------------------- | ---------------------------------------------------- | ----------------------------------- |
| **True Positive (TP)**  | Model predicted **1** and the actual value was **1** | Correctly predicted a **Buy**       |
| **True Negative (TN)**  | Model predicted **0** and the actual value was **0** | Correctly predicted **Don’t Buy**   |
| **False Positive (FP)** | Model predicted **1** but actual value was **0**     | Incorrectly predicted a **Buy**     |
| **False Negative (FN)** | Model predicted **0** but actual value was **1**     | Incorrectly predicted **Don’t Buy** |


## Initial Population
Terminals are Fixed by csv
Functions are explored by us

| Parameter             | Value      |
| --------------------- | ---------- |
| Population Size       | 20        |
| Generations           | 10         |
| Max Tree Depth        | 10         |
| Min Tree Depth        | 4          |
| Tournament Size       | 5          |
| Mutation Probability  | 0.2        |
| Crossover Probability | 0.8        |
| Selection Strategy    | Tournament |
| Fitness Function      | F1 Score   |

Syntax Generation:
 Ramped half-and-half

Combines both:
    For each tree in the initial population
        A depth between min_depth and max_depth


## Mutation
| Method    | % Used | Approx. Count (ie if 100)  |
| --------- | ------ | -------------------------- |
| Crossover | 70%    | 70 individuals             |
| Mutation  | 25%    | 25 individuals             |
| Elitism   | 5%     | 5 individuals             |


Program execution runtime is the full process of training and test
- start to finish

program runtimes include
- evaluateFitness()
- createNextGeneration()

## Downloaded Libs from here
wget https://repo1.maven.org/maven2/com/?

# Java Makefile for Genetic Algorithm Project

(IF YOU WANT TO RECOMPILE TO YOUR OWN JAVA JDK VERSION, rerun the entire compilation process)

### 1. Compile Source Code

```makefile
make compile
```
All Java source files are compiled to `.class` files in the `bin/` directory.

---

### 2. Test Run with Dependencies (Optional)

```makefile
make test-run
```

IF you see an error run one of the following commands:

> On Windows CMD, use `;` instead of `:` in the classpath:

```sh
java -cp "bin;lib\*;lib\commons-lang3-3.12.0.jar" Main
java -cp "bin:lib\*:lib\commons-lang3-3.12.0.jar" Main
```

---

### 3. Package into a JAR

```makefile
make package  
```
Packages all compiled `.class` files into a JAR file named `genetic_algo.jar`.

---

### 4. Run JAR File

```makefile
make run-jar
```
Runs the JAR file.

