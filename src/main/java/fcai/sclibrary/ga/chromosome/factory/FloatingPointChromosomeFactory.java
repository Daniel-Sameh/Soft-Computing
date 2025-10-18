package fcai.sclibrary.ga.chromosome.factory;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FloatingPointChromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FloatingPointChromosomeFactory implements ChromosomeFactory<Float> {

    Random random = new Random();

    @Override
    public Chromosome createRandomChromosome(int size, Range<Float>range) {
        Float upper = range.getUpper();
        Float lower = range.getLower();
        List<Float> genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            genes.add(random.nextFloat(lower + (upper - lower) * random.nextFloat()));
        }
        return new FloatingPointChromosome(genes);

    }
}
