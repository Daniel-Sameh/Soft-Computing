package fcai.sclibrary.ga.chromosome;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FloatingPointChromosome implements Chromosome<Double>{
    private List<Double>genes;
    private FitnessFunction<Double> fitnessFunction;
    private Double fitness;

    public FloatingPointChromosome(List<Double> genes, FitnessFunction<Double> fitnessFunction){
        this.genes = genes;
        this.fitnessFunction = fitnessFunction;
    }

    @Override
    public Chromosome<Double> copy() {
        return new FloatingPointChromosome(List.copyOf(this.genes), fitnessFunction, fitness);
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public List<Double> getGenes() {
        return genes;
    }

    @Override
    public void setGenes(List<Double> genes) {
        this.genes = genes;
    }

    @Override
    public double getFitness() {
        if (fitnessFunction == null) {
            fitness = fitnessFunction.evaluate(this);
        }
        return fitness;
    }
}
