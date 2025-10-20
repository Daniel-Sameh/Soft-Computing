package fcai.sclibrary.ga.chromosome;

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
    ///TODO: Add Use Case Fitness Class
    ///TODO: Add the mutation class

    public BinaryChromosome(List<Integer> genes, FitnessFunction<Integer> fitnessFunction){
        this.genes = genes;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Chromosome<Integer> copy() {
        return new BinaryChromosome(List.copyOf(this.genes), fitnessFunction, fitness);
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
        if (fitness == null) {
            fitness = fitnessFunction.evaluate(this);
        }
        return fitness;
    }
}
