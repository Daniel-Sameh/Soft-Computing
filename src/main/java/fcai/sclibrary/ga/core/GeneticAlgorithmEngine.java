package fcai.sclibrary.ga.core;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.InfeasibleCheck;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import fcai.sclibrary.ga.operators.crossover.Crossover;
import fcai.sclibrary.ga.operators.crossover.PopulationCrossover;
import fcai.sclibrary.ga.operators.mutation.Mutation;
import fcai.sclibrary.ga.operators.replacement.Replacement;
import fcai.sclibrary.ga.operators.selection.SelectionStrategy;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class GeneticAlgorithmEngine<T extends Number> {
    private ChromosomeFactory<T> chromosomeFactory;
    private List<Chromosome<T>> population = new ArrayList<>();
    private GAConfig<Chromosome<T>, T> config;
    private final List<GAProgressListener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean stopped = false;

    public GeneticAlgorithmEngine(GAConfig<Chromosome<T>, T> config){
        this.config = config;
        chromosomeFactory = config.getChromosomeFactory();

        // Creating initial random population
        for (int i = 0; i < config.getPopulationSize(); i++) {
            population.add(chromosomeFactory.createRandomChromosome(
                    config.getChromosomeSize(),
                    config.getRange(),
                    new Range<>(0, config.getChromosomeSize()),
                    config.getFitnessFunction()));
        }
    }


    public void addListener(GAProgressListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GAProgressListener listener) {
        listeners.remove(listener);
    }

    public void stop(){
        stopped = true;
    }

    @SuppressWarnings("unchecked")
    public Chromosome<T> run(){
        SelectionStrategy<Chromosome<T>, T> selection = config.getSelectionStrategy();
        PopulationCrossover<T> populationCrossover = new PopulationCrossover<>();
        Crossover<T> crossover = config.getCrossover();
        Mutation<T> mutation = (Mutation<T>) config.getMutation();
        Replacement<T> replacement = config.getReplacement();
        FitnessFunction<T> fitnessFunction = config.getFitnessFunction();
        InfeasibleCheck<T> infeasibleCheck = config.getInfeasibleCheck();
        Optimization optimization = config.getOptimization();

        int numGenerations = config.getGenerations();
        Chromosome<T> bestChromosome = null;


        for (int generation = 0; generation<numGenerations; generation++) {
            if(stopped) break;

            // Selection
            List<Chromosome<T>> selectedPopulation = selection.select(
                    population, population.size(), fitnessFunction);

            // Crossover
            List<Chromosome<T>> offspringPopulation =
                (List<Chromosome<T>>) (List<?>) populationCrossover.crossoverPopulation(
                    selectedPopulation, crossover, config.getCrossoverRate());

            // Mutation
            if (offspringPopulation != null) {
                final List<Chromosome<T>> source = offspringPopulation;
                int size = source.size();
                offspringPopulation = IntStream.range(0, size)
                    .parallel()
                    .mapToObj(i -> {
                        Chromosome<T> chromosome = source.get(i);
                        Chromosome<T> mutatedChromosome = mutation.mutate(
                                chromosome,
                                config.getMutationRate(),
                                config.getRange());

                        // Check infeasibility
                        if (mutatedChromosome != null &&
                            infeasibleCheck != null &&
                            infeasibleCheck.isInfeasible(mutatedChromosome, config.getRange())) {
                            // reject infeasible mutation
                            return chromosome;
                        }

                        return mutatedChromosome != null ? mutatedChromosome : chromosome;
                    })
                    .collect(Collectors.toList());
            }

            // Pass parent count
            int parentCount = Math.max(1, config.getPopulationSize() / 5);
            population = replacement.replace(
                    population,
                    offspringPopulation != null ? offspringPopulation : new ArrayList<>(),
                    parentCount);

            // Get best chromosome
            Chromosome<T> currentBest = getBestChromosome(population, optimization);
            if (currentBest != null &&
                (bestChromosome == null || isBetter(currentBest, bestChromosome, optimization))) {
                bestChromosome = currentBest;
            }

            // Notify listeners
            final int gen = generation;
            final Chromosome<T> bestInGen = bestChromosome;
            listeners.forEach(listener -> listener.onGenerationComplete(gen, bestInGen));
        }


        final Chromosome<T> finalBestChromosome = bestChromosome;
        listeners.forEach(listener -> listener.onComplete(finalBestChromosome));

        return finalBestChromosome;
    }


    private Chromosome<T> getBestChromosome(List<Chromosome<T>> population, Optimization optimization){
        if (population == null || population.isEmpty()){
            return null;
        }

        if(optimization == Optimization.MAXIMIZE){
            return population.stream()
                .max((c1, c2) -> Double.compare(c1.getFitness(), c2.getFitness()))
                .orElse(null);
        }
        else{
            return population.stream()
                .min((c1, c2) -> Double.compare(c1.getFitness(), c2.getFitness()))
                .orElse(null);
        }
    }

    private boolean isBetter(Chromosome<?> c1, Chromosome<?> c2, Optimization optimization){
        if (c1==null){
            return false;
        }

        if (c2==null){
            return true;
        }

        if(optimization == Optimization.MAXIMIZE){
            return c1.getFitness() > c2.getFitness();
        }
        else{
            return c1.getFitness() < c2.getFitness();
        }
    }
}
