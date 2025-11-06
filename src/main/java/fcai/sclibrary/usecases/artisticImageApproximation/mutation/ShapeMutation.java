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
    private final boolean allowTypeMutation;
    private static final int GENES_PER_SHAPE = 9;

    public ShapeMutation(int width, int height) {
        this(width, height, false);
    }

    public ShapeMutation(int width, int height, boolean allowTypeMutation) {
        this.width = width;
        this.height = height;
        this.allowTypeMutation = allowTypeMutation;
    }

    @Override
    public Chromosome<Integer> mutate(Chromosome<Integer> chromosome, double pm, Range<Integer> range) {
        List<Integer> genes = new ArrayList<>(chromosome.getGenes());
        boolean mutated = false;

        int numShapes = genes.size() / GENES_PER_SHAPE;

        // Randomly decide whether to swap shapes (z-index mutation) or mutate attributes
        // Use a lower probability for shape swapping to balance with attribute mutation
        if (numShapes >= 2 && random.nextDouble() < pm * 0.3) {
            // Swap two random shapes to change z-index
            int shape1 = random.nextInt(numShapes);
            int shape2 = random.nextInt(numShapes);
            while (shape2 == shape1) {
                shape2 = random.nextInt(numShapes);
            }

            int base1 = shape1 * GENES_PER_SHAPE;
            int base2 = shape2 * GENES_PER_SHAPE;

            // Swap all 9 genes of the two shapes
            for (int i = 0; i < GENES_PER_SHAPE; i++) {
                int temp = genes.get(base1 + i);
                genes.set(base1 + i, genes.get(base2 + i));
                genes.set(base2 + i, temp);
            }
            mutated = true;
        }

        // Attribute mutation for individual shapes
        boolean[] shouldMutate = new boolean[numShapes];
        for (int i = 0; i < numShapes; i++) {
            shouldMutate[i] = random.nextDouble() < pm;
        }

        for (int i = 0; i < numShapes; i++) {
            int base = i * GENES_PER_SHAPE;

            // Check if shape type is invalid (> 3) and fix it
            if (genes.get(base) > 3) {
                genes.set(base, random.nextInt(4)); // Set to valid type 0-3
                mutated = true;
            }

            if (shouldMutate[i]) {
                mutated = true;

                // Pick which attribute to mutate (include type if allowed, otherwise skip it)
                int startIdx = allowTypeMutation ? 0 : 1;
                int attribute = random.nextInt(startIdx, GENES_PER_SHAPE);

                // If shape is circle or square, skip param2 mutation
                while (attribute == 4 && genes.get(base) < 2) {
                    attribute = random.nextInt(startIdx, GENES_PER_SHAPE);
                }

                switch (attribute) {
                    case 0: // Type (only when allowTypeMutation is true)
                        int currentType = genes.get(base);
                        int newType = random.nextInt(4); // 0-3
                        // Ensure it's actually changing
                        while (newType == currentType) {
                            newType = random.nextInt(4);
                        }
                        genes.set(base, newType);
                        break;

                    case 1: // X position
                        int x = genes.get(base + 1);
                        genes.set(base + 1, clamp(x + random.nextInt(-30, 31), 0, width));
                        break;

                    case 2: // Y position
                        int y = genes.get(base + 2);
                        genes.set(base + 2, clamp(y + random.nextInt(-30, 31), 0, height));
                        break;

                    case 3: // Param1 (radius/side/width/base)
                        int param1 = genes.get(base + 3);
                        genes.set(base + 3, clamp(param1 + random.nextInt(-10, 11), 5, Math.min(width, height) / 4));
                        break;

                    case 4: // Param2 (height for rectangle/triangle)
                        int param2 = genes.get(base + 4);
                        genes.set(base + 4, clamp(param2 + random.nextInt(-10, 11), 5, Math.min(width, height) / 4));
                        break;

                    case 5: // Red
                        int red = genes.get(base + 5);
                        genes.set(base + 5, clamp(red + (int)(random.nextGaussian() * 20), 0, 255));
                        break;

                    case 6: // Green
                        int green = genes.get(base + 6);
                        genes.set(base + 6, clamp(green + (int)(random.nextGaussian() * 20), 0, 255));
                        break;

                    case 7: // Blue
                        int blue = genes.get(base + 7);
                        genes.set(base + 7, clamp(blue + (int)(random.nextGaussian() * 20), 0, 255));
                        break;

                    case 8: // Alpha
                        int alpha = genes.get(base + 8);
                        genes.set(base + 8, clamp(alpha + random.nextInt(-15, 16), 20, 150));
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
