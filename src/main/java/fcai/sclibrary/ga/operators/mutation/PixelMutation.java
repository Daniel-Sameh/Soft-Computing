package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PixelMutation implements Mutation<Integer> {
    private final Random random = new Random();

    @Override
    public Chromosome<Integer> mutate(Chromosome<Integer> chromosome, double pm, Range<Integer> range) {
        List<Integer> genes = new ArrayList<>(chromosome.getGenes());
        boolean mutated = false;

        for (int i = 0; i < genes.size(); i++) {
            if (random.nextDouble() < pm) {
                int currentPixel = genes.get(i);

                // Extract RGB
                int r = (currentPixel >> 16) & 0xFF;
                int g = (currentPixel >> 8) & 0xFF;
                int b = currentPixel & 0xFF;

                // Add Gaussian noise (±20 per channel)
                r = clamp(r + (int)(random.nextGaussian() * 20));
                g = clamp(g + (int)(random.nextGaussian() * 20));
                b = clamp(b + (int)(random.nextGaussian() * 20));

                // Reconstruct pixel
                genes.set(i, (r << 16) | (g << 8) | b);
                mutated = true;
            }
        }

        if (mutated) {
            return new IntegerChromosome(genes, chromosome.getFitnessFunction());
        }
        return null;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
