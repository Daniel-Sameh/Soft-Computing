package fcai.sclibrary.ga.chromosome;

import fcai.sclibrary.ga.chromosome.factory.Range;

public interface FitnessFunction<T extends Object>{
    double evaluate(Chromosome<T> chromosome, Range<Integer> range);
}