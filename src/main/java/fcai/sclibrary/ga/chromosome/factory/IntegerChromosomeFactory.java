package fcai.sclibrary.ga.chromosome.factory;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntegerChromosomeFactory implements ChromosomeFactory<Integer> {

    Random random = new Random();


    @Override
    public Chromosome<Integer> createRandomChromosome(int size, Range<Integer> range, Range<Integer> chromosomeRange, FitnessFunction<Integer> fitnessFunction) {
        Integer lower = range.getLower();
        Integer upper = range.getUpper();
        List<Integer> genes = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            genes.add(random.nextInt(upper - lower + 1) + lower);
        }
        return new IntegerChromosome(genes, fitnessFunction, chromosomeRange);
    }
}
