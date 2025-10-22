package fcai.sclibrary.ga.chromosome;

import java.util.List;

public interface Chromosome<T extends Number> {
    Chromosome<T> copy();
    int length();
    List<T> getGenes();
    void setGenes(List<T> genes);
    double getFitness();
    FitnessFunction<T> getFitnessFunction();
}
