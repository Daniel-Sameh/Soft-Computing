package fcai.sclibrary.ga.chromosome;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IntegerChromosome implements Chromosome {

    private List<Integer> genes;

    @Override
    public Chromosome copy() {
        return new IntegerChromosome(List.copyOf(this.genes));
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public List<Integer> getGenes() {
        return genes;
    }
}
