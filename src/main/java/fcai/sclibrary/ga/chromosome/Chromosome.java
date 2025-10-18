package fcai.sclibrary.ga.chromosome;

import java.util.List;

public interface Chromosome<T extends Number> {
    Chromosome copy();
    int length();
    List<T> getGenes();
}
