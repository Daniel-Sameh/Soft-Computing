package fcai.sclibrary.ga.chromosome;

import fcai.sclibrary.ga.chromosome.factory.Range;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IntegerChromosome implements Chromosome<Integer> {

    private List<Integer> genes;
    private FitnessFunction<Integer> fitnessFunction;
    private Double fitness;
    private Range<Integer> range;

    public IntegerChromosome(List<Integer> genes, FitnessFunction<Integer> fitnessFunction, Range<Integer> range) {
        this.range = range;
        this.genes = genes;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Chromosome<Integer> copy() {
        return new IntegerChromosome(List.copyOf(this.genes), fitnessFunction, fitness, range);
    }

    @Override
    public int length() {
        return genes.size();
    }

    @Override
    public List<Integer> getGenes() {
        return genes;
    }

    @Override
    public void setGenes(List<Integer> genes) {
        if (!this.genes.equals(genes)) {
            this.genes = genes;
            this.fitness = null;
        }
    }

    @Override
    public double getFitness() {
//        return fitnessFunction.evaluate(this, range);
        // Caching the fitness
        if  (fitness == null) {
            fitness = fitnessFunction.evaluate(this, range);
        }
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerChromosome)) return false;
        IntegerChromosome that = (IntegerChromosome) o;
        return genes.equals(that.genes);
    }

    @Override
    public int hashCode() {
        return genes.hashCode();
    }
}
