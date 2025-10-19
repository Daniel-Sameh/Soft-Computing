package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

public interface Mutation {
    public void mutate(Chromosome chromosome, int pm, Range range);
}
