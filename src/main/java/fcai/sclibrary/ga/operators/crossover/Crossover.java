package fcai.sclibrary.ga.operators.crossover;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.List;

public interface Crossover <T extends Object>{

    public List<Chromosome<T>> crossover(Chromosome<T> chromosome1,Chromosome<T> chromosome2,double pc);
}
