package fcai.sclibrary.ga.chromosome;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FloatingPointChromosome implements Chromosome{
    private List<Float>genes;

    @Override
    public Chromosome copy() {
        return new FloatingPointChromosome(List.copyOf(this.genes));
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public List<Float> getGenes() {
        return genes;
    }
}
