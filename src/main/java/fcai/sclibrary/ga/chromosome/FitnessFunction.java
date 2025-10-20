package fcai.sclibrary.ga.chromosome;

public interface FitnessFunction<T extends Number>{
    double evaluate(Chromosome<T> chromosome);
}