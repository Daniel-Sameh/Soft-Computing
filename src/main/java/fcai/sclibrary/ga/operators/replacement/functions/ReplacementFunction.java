package fcai.sclibrary.ga.operators.replacement.functions;

import fcai.sclibrary.ga.chromosome.Chromosome;

import java.util.List;

public interface ReplacementFunction<T extends Number> {
    List<Chromosome<T>> select(List<Chromosome<T>> ls, int size);
}
