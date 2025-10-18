package fcai.sclibrary.ga.chromosome;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class BinaryChromosome implements Chromosome{
    private List<Integer> genes = new ArrayList<>();
    ///TODO: Add Use Case Fitness Class
    ///TODO: Add the mutation class

    @Override
    public Chromosome copy() {
        return new BinaryChromosome(List.copyOf(this.genes));
    }

    @Override
    public int length() {
        return genes.size();
    }

    @Override
    public List<Integer> getGenes() {
        return genes;
    }
}
