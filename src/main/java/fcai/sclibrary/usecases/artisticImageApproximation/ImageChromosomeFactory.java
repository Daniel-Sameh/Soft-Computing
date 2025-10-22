package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class ImageChromosomeFactory implements ChromosomeFactory<Integer> {
    private final Random random = new Random();
    private final List<Integer> targetGenes;
    private final double initRandomness;

    public ImageChromosomeFactory(List<Integer> targetGenes, double initRandomness) {
        this.targetGenes = targetGenes;
        this.initRandomness = Math.max(0.0, Math.min(1.0, initRandomness));
    }


    @Override
    public Chromosome<Integer> createRandomChromosome(int size, Range<Integer> range, FitnessFunction<Integer> fitnessFunction) {
        Integer l = range.getLower();
        Integer u = range.getUpper();
        List<Integer> genes = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            int gene;
            if (i< targetGenes.size() && random.nextDouble() > initRandomness) {
                int targetPixel = targetGenes.get(i).intValue();
                int variation = (int) (random.nextGaussian()*50); // 10% variation
                gene = Math.max(l, Math.min(u, targetPixel + variation));
            } else {
                gene = l + random.nextInt(u - l + 1);
            }
            genes.add(gene);
        }

        return new IntegerChromosome(genes, fitnessFunction);
    }
}
