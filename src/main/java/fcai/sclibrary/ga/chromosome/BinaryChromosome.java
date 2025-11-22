package fcai.sclibrary.ga.chromosome;

import fcai.sclibrary.ga.chromosome.factory.Range;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BinaryChromosome implements Chromosome<Integer>{
    private List<Integer> genes;
    private FitnessFunction<Integer> fitnessFunction;
    private Double fitness;
    private Range<Integer> range;

    public BinaryChromosome(List<Integer> genes, FitnessFunction<Integer> fitnessFunction, Range<Integer> range) {
        this.genes = genes;
        this.fitnessFunction = fitnessFunction;
        this.range = range;
    }

    @Override
    public Chromosome<Integer> copy() {
        return new BinaryChromosome(List.copyOf(this.genes), fitnessFunction, fitness, range);
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
        if (fitness == null) {
            fitness = fitnessFunction.evaluate(this, range);
        }
        return fitness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryChromosome)) return false;
        BinaryChromosome that = (BinaryChromosome) o;
        return genes.equals(that.genes);
    }

    @Override
    public int hashCode() {
        return genes.hashCode();
    }
}
