# Soft-Computing

A modular, object-oriented **Soft Computing** library (Java + Maven) implementing:

- **Genetic Algorithms (GA)**: chromosomes, operators (selection/crossover/mutation/replacement), and a reusable optimization engine.
- **Fuzzy Logic**: Mamdani + Sugeno inference engines, fuzzification, defuzzification, and rule evaluation.
- **Neural Networks (NN)**: a feed-forward network with tensors, layers, activations, losses, and optimizers.

The repository also contains **use cases** that demonstrate how to apply each module to real problems.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Build & Run](#build--run)
  - [Build](#build)
  - [Run a Use Case](#run-a-use-case)
  - [Run Tests](#run-tests)
- [Module Overview](#module-overview)
  - [Genetic Algorithm (GA)](#genetic-algorithm-ga)
  - [Fuzzy Logic](#fuzzy-logic)
  - [Neural Networks (NN)](#neural-networks-nn)
- [Use Cases](#use-cases)
- [Project Structure (Source Tree)](#project-structure-source-tree)
- [Contributors](#contributors)

---

## Features

### Genetic Algorithm (GA)
- Multiple chromosome types:
  - Binary
  - Integer
  - Floating point
- Pluggable operators:
  - Selection (Tournament / Rank / Roulette)
  - Crossover (Single-point / N-point / Order-one / Population crossover)
  - Mutation (Flip / Swap / Inversion / Uniform / Non-uniform / Pixel)
  - Replacement (Generational / Steady-state / Elitist)
- Configurable GA engine and optimization workflow.

### Fuzzy Logic
- Core fuzzy building blocks:
  - Fuzzy variables and sets
  - Rule evaluation
  - Fuzzification
- Inference engines:
  - Mamdani
  - Sugeno
- Defuzzification:
  - Mean of maxima
  - Weighted average

### Neural Networks (NN)
- Core abstractions:
  - `Tensor` operations
  - `Layer` hierarchy (`InputLayer`, `DenseLayer`)
  - `NeuralNetwork` builder
- Activation functions:
  - Linear, ReLU, Sigmoid, Tanh
- Loss functions:
  - Mean Squared Error, Cross Entropy
- Optimizers:
  - Gradient Descent

---

## Tech Stack

- **Java** (project has been used with modern JDKs)
- **Maven** (`pom.xml`)
- **JUnit** tests under `src/test/java`

---

## Build & Run

### Build

```bash
mvn clean package
```

### Run a Use Case

This project contains multiple executable `Main` classes. In IntelliJ IDEA, you can run them directly.

Alternatively, you can run a specific main class with Maven (example shown – adjust the main class):

```bash
mvn -q -DskipTests exec:java -Dexec.mainClass="fcai.sclibrary.Main"
```

> If Maven exec isn’t configured in `pom.xml`, run via IntelliJ run configurations.

### Run Tests

```bash
mvn test
```

---

## Module Overview

### Genetic Algorithm (GA)
Main packages:
- `fcai.sclibrary.ga.core` — GA configuration, engine, and progress hooks.
- `fcai.sclibrary.ga.chromosome` — chromosome definitions and fitness hooks.
- `fcai.sclibrary.ga.operators` — selection/crossover/mutation/replacement operators.

Typical flow:
1. Choose a chromosome representation.
2. Provide a fitness function.
3. Configure operators (selection/crossover/mutation/replacement).
4. Run `GeneticAlgorithmEngine` for optimization.

### Fuzzy Logic
Main packages:
- `fcai.sclibrary.fuzzyLogic.core` — fuzzy variables, sets, fuzzifier, rules.
- `fcai.sclibrary.fuzzyLogic.core.engine` — inference engines (Mamdani/Sugeno).
- `fcai.sclibrary.fuzzyLogic.core.defuzzification` — defuzzifiers.

### Neural Networks (NN)
Main packages:
- `fcai.sclibrary.nn.core` — `Tensor`, `NeuralNetwork`.
- `fcai.sclibrary.nn.layers` — `InputLayer`, `DenseLayer`.
- `fcai.sclibrary.nn.activations` — common activation functions.
- `fcai.sclibrary.nn.loss` — loss functions.
- `fcai.sclibrary.nn.optimizers` — optimizers.

---

## Use Cases

Located in: `src/main/java/fcai/sclibrary/usecases/`

- `artisticImageApproximation/`
  - GA-based approximation of an image using shapes.
- `EgyptHousingPricesPrediction/`
  - Neural network prediction pipeline (includes dataset loading + preprocessing).
- `MADRSdepressionTest/`
  - Fuzzy logic system based on MADRS depression assessment rules.

---

## Project Structure (Source Tree)

```
Soft-Computing/
├─ pom.xml
├─ README.md
├─ .gitignore
├─ src/
│  ├─ main/
│  │  └─ java/
│  │     └─ fcai/
│  │        └─ sclibrary/
│  │           ├─ Main.java
│  │           ├─ fuzzyLogic/
│  │           │  ├─ FuzzyLogicEvaluator.java
│  │           │  └─ core/
│  │           │     ├─ Fuzzifier.java
│  │           │     ├─ FuzzySet.java
│  │           │     ├─ FuzzyVariable.java
│  │           │     ├─ Rule.java
│  │           │     ├─ consequents/
│  │           │     │  ├─ Consequent.java
│  │           │     │  ├─ MamdanniConsequent.java
│  │           │     │  └─ SugenoConsequent.java
│  │           │     ├─ defuzzification/
│  │           │     │  ├─ Defuzzifier.java
│  │           │     │  ├─ MeanMaxDefuzzifier.java
│  │           │     │  └─ WeightedAvgDefuzzifier.java
│  │           │     ├─ engine/
│  │           │     │  ├─ InferenceEngine.java
│  │           │     │  ├─ MamdaniInferenceEngine.java
│  │           │     │  └─ SugenoInferenceEngine.java
│  │           │     └─ operators/
│  │           │        ├─ FuzzyOperators.java
│  │           │        ├─ ProductFuzzyOperators.java
│  │           │        └─ StandardFuzzyOperators.java
│  │           ├─ ga/
│  │           │  ├─ chromosome/
│  │           │  │  ├─ BinaryChromosome.java
│  │           │  │  ├─ Chromosome.java
│  │           │  │  ├─ FitnessFunction.java
│  │           │  │  ├─ FloatingPointChromosome.java
│  │           │  │  ├─ InfeasibleCheck.java
│  │           │  │  ├─ IntegerChromosome.java
│  │           │  │  └─ factory/
│  │           │  │     ├─ BinaryChromosomeFactory.java
│  │           │  │     ├─ ChromosomeFactory.java
│  │           │  │     ├─ FloatingPointChromosomeFactory.java
│  │           │  │     ├─ IntegerChromosomeFactory.java
│  │           │  │     └─ Range.java
│  │           │  ├─ core/
│  │           │  │  ├─ GAConfig.java
│  │           │  │  ├─ GAProgressListener.java
│  │           │  │  ├─ GeneticAlgorithmEngine.java
│  │           │  │  └─ Optimization.java
│  │           │  ├─ factory/
│  │           │  │  ├─ BinaryOperatorFactory.java
│  │           │  │  ├─ DefaultOperatorFactory.java
│  │           │  │  ├─ FloatingPointOperatorFactory.java
│  │           │  │  ├─ IntegerOperatorFactory.java
│  │           │  │  └─ OperatorFactory.java
│  │           │  └─ operators/
│  │           │     ├─ crossover/
│  │           │     │  ├─ Crossover.java
│  │           │     │  ├─ NPointCrossover.java
│  │           │     │  ├─ OrderOneCrossover.java
│  │           │     │  ├─ PopulationCrossover.java
│  │           │     │  └─ SinglePointCrossover.java
│  │           │     ├─ mutation/
│  │           │     │  ├─ FlipMutation.java
│  │           │     │  ├─ InversionMutation.java
│  │           │     │  ├─ Mutation.java
│  │           │     │  ├─ NonUniformMutation.java
│  │           │     │  ├─ PixelMutation.java
│  │           │     │  ├─ SwapMutation.java
│  │           │     │  └─ UniformMutation.java
│  │           │     ├─ replacement/
│  │           │     │  ├─ ElitistReplacement.java
│  │           │     │  ├─ GenerationalReplacement.java
│  │           │     │  ├─ Replacement.java
│  │           │     │  ├─ SteadyStateReplacement.java
│  │           │     │  └─ functions/
│  │           │     │     ├─ CutoffFunction.java
│  │           │     │     ├─ MixingFunction.java
│  │           │     │     └─ ReplacementFunction.java
│  │           │     └─ selection/
│  │           │        ├─ RankSelection.java
│  │           │        ├─ RouletteWheelSelection.java
│  │           │        ├─ SelectionStrategy.java
│  │           │        └─ TournamentSelection.java
│  │           ├─ nn/
│  │           │  ├─ activations/
│  │           │  │  ├─ ActivationFunction.java
│  │           │  │  ├─ Linear.java
│  │           │  │  ├─ ReLU.java
│  │           │  │  ├─ Sigmoid.java
│  │           │  │  └─ Tanh.java
│  │           │  ├─ config/
│  │           │  │  ├─ NetworkConfig.java
│  │           │  │  └─ TrainingConfig.java
│  │           │  ├─ core/
│  │           │  │  ├─ NeuralNetwork.java
│  │           │  │  └─ Tensor.java
│  │           │  ├─ exceptions/
│  │           │  │  ├─ DimensionMismatchException.java
│  │           │  │  ├─ InvalidConfigurationException.java
│  │           │  │  └─ TrainingException.java
│  │           │  ├─ initialization/
│  │           │  │  ├─ RandomUniform.java
│  │           │  │  ├─ WeightInitializer.java
│  │           │  │  └─ Xavier.java
│  │           │  ├─ layers/
│  │           │  │  ├─ DenseLayer.java
│  │           │  │  ├─ InputLayer.java
│  │           │  │  └─ Layer.java
│  │           │  ├─ loss/
│  │           │  │  ├─ CrossEntropy.java
│  │           │  │  ├─ LossFunction.java
│  │           │  │  └─ MeanSquaredError.java
│  │           │  └─ optimizers/
│  │           │     ├─ GradientDescent.java
│  │           │     └─ Optimizer.java
│  │           └─ usecases/
│  │              ├─ artisticImageApproximation/
│  │              │  ├─ ArtisticApproximationUI.java
│  │              │  ├─ ImageRenderer.java
│  │              │  ├─ ShapeInfeasibleCheck.java
│  │              │  ├─ ShapeMSE.java
│  │              │  ├─ Utils.java
│  │              │  ├─ factory/
│  │              │  │  └─ ShapeFactory.java
│  │              │  └─ mutation/
│  │              │     └─ ShapeMutation.java
│  │              ├─ EgyptHousingPricesPrediction/
│  │              │  ├─ Main.java
│  │              │  ├─ Dataset/
│  │              │  │  ├─ ColumnType.java
│  │              │  │  ├─ DataLoader.java
│  │              │  │  ├─ DataRow.java
│  │              │  │  └─ RawDataset.java
│  │              │  ├─ Preprocessing/
│  │              │  │  ├─ PreprocessedDataset.java
│  │              │  │  ├─ Preprocessor.java
│  │              │  │  ├─ PreprocessingState.java
│  │              │  │  └─ Split.java
│  │              │  └─ data/
│  │              │     └─ processed.csv
│  │              └─ MADRSdepressionTest/
│  │                 ├─ DepressionTest.java
│  │                 ├─ rules.txt
│  │                 └─ ruleSugeno.txt
│
└─ target/  (generated by Maven build)
```

---

## Contributors

- Daniel Sameh
- Abanoub Essam
- Youssef Ehab
- Marcelino Maximos
- Daniel Raafat