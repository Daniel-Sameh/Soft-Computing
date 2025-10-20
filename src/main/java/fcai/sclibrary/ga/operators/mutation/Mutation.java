package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

public interface Mutation<T extends Number> {
    public Chromosome<T> mutate(Chromosome<T> chromosome, double pm, Range<T> range);
}
