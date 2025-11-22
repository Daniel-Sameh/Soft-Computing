package fcai.sclibrary.ga.operators.selection;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;

import java.util.List;

public interface SelectionStrategy<T extends Chromosome<N>, N extends Number> {
    List<T> select(List<T> population, int numberToSelect, FitnessFunction<N> fitnessFunction);
}
