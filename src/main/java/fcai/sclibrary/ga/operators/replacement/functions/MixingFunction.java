package fcai.sclibrary.ga.operators.replacement.functions;

import fcai.sclibrary.ga.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MixingFunction<T extends Number> implements ReplacementFunction<T> {
    @Override
    public List<Chromosome<T>> select(List<Chromosome<T>> ls, int size) {
        ls.sort(Comparator.comparingDouble((Chromosome<T> c) -> c.getFitness()).reversed());
        List<Chromosome<T>> res = new ArrayList<>();
        int size1 =  Math.ceilDiv(size, 2);
        int size2 =  Math.floorDiv(size, 2);
        res.addAll(ls.subList(0, size1));
        res.addAll(ls.subList(ls.size() -  size2, ls.size()));
        return res;
    }
}
