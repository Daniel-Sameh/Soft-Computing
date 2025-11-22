package fcai.sclibrary.ga.chromosome.factory;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.FloatingPointChromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FloatingPointChromosomeFactory implements ChromosomeFactory<Double> {

    Random random = new Random();

    @Override
    public Chromosome<Double> createRandomChromosome(int size, Range<Double>range, Range<Integer> chromosomeRange, FitnessFunction<Double> fitnessFunction) {
        Double upper = range.getUpper();
        Double lower = range.getLower();
        List<Double> genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            genes.add(random.nextDouble(lower + (upper - lower) * random.nextFloat()));
        }
        return new FloatingPointChromosome(genes, fitnessFunction, chromosomeRange);

    }
}
