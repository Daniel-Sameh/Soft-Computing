package fcai.sclibrary.ga.operators.replacement.functions;

import fcai.sclibrary.ga.chromosome.Chromosome;

import java.util.Comparator;
import java.util.List;

public class CutoffFunction<T extends Object> implements ReplacementFunction<T> {
    @Override
    public List<Chromosome<T>> select(List<Chromosome<T>> ls, int size) {
        ls.sort(Comparator.comparingDouble((Chromosome<T> c) -> c.getFitness()).reversed());
        return ls.subList(0, size);
    }
}
