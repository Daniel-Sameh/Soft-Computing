package fcai.sclibrary.ga.chromosome.factory;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;

public interface ChromosomeFactory<T extends Number> {
    Chromosome<T> createRandomChromosome(int size, Range<T> range, FitnessFunction<T> fitnessFunction);
}
