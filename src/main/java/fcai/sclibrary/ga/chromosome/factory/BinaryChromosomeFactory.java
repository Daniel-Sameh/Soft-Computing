package fcai.sclibrary.ga.chromosome.factory;

import fcai.sclibrary.ga.chromosome.BinaryChromosome;
import fcai.sclibrary.ga.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BinaryChromosomeFactory implements ChromosomeFactory<Integer> {
    Random random = new Random();
    @Override
    public Chromosome createRandomChromosome(int size, Range<Integer> range) {
        Integer lower = range.getLower();
        Integer upper = range.getUpper();
        List<Integer> genes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            genes.add(random.nextInt(upper - lower + 1) + lower);
        }
        return new BinaryChromosome(genes);
    }

}
