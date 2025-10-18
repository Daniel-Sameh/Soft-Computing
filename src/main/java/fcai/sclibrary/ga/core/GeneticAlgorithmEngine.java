package fcai.sclibrary.ga.core;

import fcai.sclibrary.ga.chromosome.BinaryChromosome;
import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GeneticAlgorithmEngine {
    ///TODO: the crossover interface
    ///TODO: the replacement interface
    private ChromosomeFactory chromosomeFactory;
    private List<Chromosome> population = new ArrayList<>();
    private GAConfig config;

    public GeneticAlgorithmEngine(GAConfig config){
        this.config = config;
        for (int i = 0; i < config.getPopulationSize(); i++) {
            population.add(chromosomeFactory.createRandomChromosome(config.getChromosomeSize(), config.getRange()));
        }
    }

    ///TODO: Implement this function
    public Chromosome run(){
        return new BinaryChromosome(List.of());
    }
}
