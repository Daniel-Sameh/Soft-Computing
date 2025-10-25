package fcai.sclibrary.usecases.artisticImageApproximation.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;
import fcai.sclibrary.ga.operators.mutation.Mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShapeMutation implements Mutation<Integer> {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final int width;
    private final int height;
    private static final int GENES_PER_SHAPE = 7;

    public ShapeMutation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Chromosome<Integer> mutate(Chromosome<Integer> chromosome, double pm, Range<Integer> range) {
        List<Integer> genes = new ArrayList<>(chromosome.getGenes());
        boolean mutated = false;

        int numShapes = genes.size() / GENES_PER_SHAPE;

        boolean[] shouldMutate = new boolean[numShapes];
        for (int i = 0; i < numShapes; i++) {
            shouldMutate[i] = random.nextDouble() < pm;
        }

        for (int i = 0; i < numShapes; i++) {
            if (shouldMutate[i]) {
                mutated = true;
                int base = i * GENES_PER_SHAPE;

                // Pick which attribute to mutate
                int attribute = random.nextInt(GENES_PER_SHAPE);

                switch (attribute) {
                    case 0: // X position - small adjustment
                        int x = genes.get(base);
                        genes.set(base, clamp(x + random.nextInt(-30, 31), 0, width));
                        break;

                    case 1: // Y position
                        int y = genes.get(base + 1);
                        genes.set(base + 1, clamp(y + random.nextInt(-30, 31), 0, height));
                        break;

                    case 2: // Radius
                        int radius = genes.get(base + 2);
                        genes.set(base + 2, clamp(radius + random.nextInt(-10, 11), 5, Math.min(width, height) / 4));
                        break;

                    case 3: // Red
                        int red = genes.get(base + 3);
                        genes.set(base + 3, clamp(red + (int)(random.nextGaussian() * 20), 0, 255));
                        break;

                    case 4: // Green
                        int green = genes.get(base + 4);
                        genes.set(base + 4, clamp(green + (int)(random.nextGaussian() * 20), 0, 255));
                        break;

                    case 5: // Blue
                        int blue = genes.get(base + 5);
                        genes.set(base + 5, clamp(blue + (int)(random.nextGaussian() * 20), 0, 255));
                        break;

                    case 6: // Alpha
                        int alpha = genes.get(base + 6);
                        genes.set(base + 6, clamp(alpha + random.nextInt(-15, 16), 20, 150));
                        break;
                }
            }
        }

        if (mutated) {
            Chromosome<Integer> result = chromosome.copy();
            result.setGenes(genes);
            return result;
        }
        return null;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
