package fcai.sclibrary.ga.operators.replacement;

import fcai.sclibrary.ga.chromosome.Chromosome;

import java.util.List;

public interface Replacement<T extends Number> {
    List<Chromosome<T>> replace(List<Chromosome<T>> parent, List<Chromosome<T>> new_ch, int k);
}