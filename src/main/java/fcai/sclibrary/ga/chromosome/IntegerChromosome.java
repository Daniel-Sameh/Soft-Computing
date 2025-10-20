package fcai.sclibrary.ga.chromosome;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IntegerChromosome implements Chromosome<Integer> {

    private List<Integer> genes;
    private FitnessFunction<Integer> fitnessFunction;
    private Double fitness;

    public IntegerChromosome(List<Integer> genes, FitnessFunction<Integer> fitnessFunction) {
        this.genes = genes;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Chromosome<Integer> copy() {
        return new IntegerChromosome(List.copyOf(this.genes), fitnessFunction, fitness);
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
        this.genes = genes;
    }

    @Override
    public double getFitness() {
        if  (fitness == null) {
            fitness = fitnessFunction.evaluate(this);
        }
        return fitness;
    }
}
