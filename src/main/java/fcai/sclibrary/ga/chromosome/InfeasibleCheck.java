package fcai.sclibrary.ga.chromosome;

import fcai.sclibrary.ga.chromosome.factory.Range;

public interface InfeasibleCheck<T extends Number> {
    boolean isInfeasible(Chromosome<T> chromosome, Range<T> range);
}
