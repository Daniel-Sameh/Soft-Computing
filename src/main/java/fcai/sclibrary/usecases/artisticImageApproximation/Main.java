package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.BinaryChromosome;
import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.core.GAConfig;
import fcai.sclibrary.ga.core.GeneticAlgorithmEngine;

public class Main {
    public static void main(String[] args) {
        ArtisticApproximationUI ui = new ArtisticApproximationUI();

        GAConfig<BinaryChromosome, Integer> config = GAConfig.<BinaryChromosome, Integer>builder()
//                .populationSize(200)
                .generations(100)
                .mutationRate(0.05)
                .crossoverRate(0.8)
                .chromosomeSize(50)
                .build();
        GeneticAlgorithmEngine engine = new GeneticAlgorithmEngine(config);
//        engine.run();
    }

}
