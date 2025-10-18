package fcai.sclibrary.ga.chromosome.factory;

import fcai.sclibrary.ga.chromosome.Chromosome;

public interface ChromosomeFactory<T extends Number> {
    Chromosome createRandomChromosome(int size, Range<T> range);
}
