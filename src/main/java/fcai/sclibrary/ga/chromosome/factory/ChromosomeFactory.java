package fcai.sclibrary.ga.chromosome.factory;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;

public interface ChromosomeFactory<T extends Object> {
    Chromosome<T> createRandomChromosome(int size, Range<T> range, Range<Integer> chromosomeRange, FitnessFunction<T> fitnessFunction);
}
