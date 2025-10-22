package fcai.sclibrary.ga.core;

import fcai.sclibrary.ga.chromosome.BinaryChromosome;
import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.operators.crossover.Crossover;
import fcai.sclibrary.ga.operators.crossover.PopulationCrossover;
import fcai.sclibrary.ga.operators.mutation.Mutation;
import fcai.sclibrary.ga.operators.replacement.Replacement;
import fcai.sclibrary.ga.operators.selection.SelectionStrategy;
import fcai.sclibrary.usecases.artisticImageApproximation.MeanSquareError;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@AllArgsConstructor
public class GeneticAlgorithmEngine {
    ///TODO: the crossover interface
    ///TODO: the replacement interface
    private ChromosomeFactory chromosomeFactory;
    private List<Chromosome<?>> population = new ArrayList<>();
    private GAConfig config;
    private final List<GAProgressListener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean stopped = false;

    public GeneticAlgorithmEngine(GAConfig config){
        this.config = config;
        chromosomeFactory = config.getChromosomeFactory();
        for (int i = 0; i < config.getPopulationSize(); i++) {
            population.add(chromosomeFactory.createRandomChromosome(
                    config.getChromosomeSize(),
                    config.getRange(),
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
    public Chromosome<?> run(){
        SelectionStrategy selection = config.getSelectionStrategy();
        PopulationCrossover populationCrossover = new PopulationCrossover();
        Crossover crossover = config.getCrossover();
        Mutation mutation = config.getMutation();
        Replacement replacement = config.getReplacement();
        FitnessFunction fitnessFunction = config.getFitnessFunction();
        Optimization optimization = config.getOptimization();

        int numGenerations = config.getGenerations();
        Chromosome<?> bestChromosome = null;

        for (int generation = 0; generation < numGenerations; generation++) {
            // Selection
            List<Chromosome<?>> selectedPopulation = selection.select(
                    (List)population, population.size(), fitnessFunction);

            // Crossover
            List<Chromosome<?>> offspringPopulation= populationCrossover.crossoverPopulation(
                    selectedPopulation, crossover, config.getCrossoverRate());

            // Mutation
//            List<Chromosome<?>> newChromosomes = new ArrayList<>();
            for (int i = 0; i < offspringPopulation.size(); i++) {
                Chromosome<?> chromosome = offspringPopulation.get(i);
                Chromosome<?> mutatedChromosome = mutation.mutate(
                        chromosome,
                        config.getMutationRate(),
                        config.getRange());
                if(mutatedChromosome != null){
                    offspringPopulation.set(i, mutatedChromosome);
                }
            }

            population = replacement.replace(
                    (List) population,
                    (List) offspringPopulation,
                    10);


            Chromosome<?> currentBest = getBestChromosome(population, optimization);

            if (bestChromosome == null ||
                    (currentBest!=null && currentBest.getFitness()>bestChromosome.getFitness())) {
                bestChromosome = currentBest;
            }

            // Notify listeners about progress
            final int gen = generation;
            final Chromosome<?> bestInGen = currentBest;
            listeners.forEach(listener -> listener.onGenerationComplete(gen, population, bestInGen));
        }

        final Chromosome<?> finalBestChromosome = bestChromosome;
        listeners.forEach(listener -> listener.onComplete(finalBestChromosome));

        return finalBestChromosome;
    }

    private Chromosome<?> getBestChromosome(List<Chromosome<?>> population, Optimization optimization){
        return population.stream()
                .max((c1, c2) -> Double.compare(c1.getFitness(), c2.getFitness()))
                .orElse(null);
//        if(optimization == Optimization.MAXIMIZE){
//        }
//        else{
//            return population.stream()
//                    .min((c1, c2) -> Double.compare(c1.getFitness(), c2.getFitness()))
//                    .orElse(null);
//        }
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
