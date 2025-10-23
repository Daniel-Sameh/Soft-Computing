package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.clamp;

public class ShapeFactory implements ChromosomeFactory<Integer> {
    private final Random random = new Random();
    private final int width;
    private final int height;
    private final int numShapes;
    private final List<Integer> targetPixels;

    public ShapeFactory(int width, int height, int numShapes, List<Integer> targetPixels) {
        this.width = width;
        this.height = height;
        this.numShapes = numShapes;
        this.targetPixels = targetPixels;
    }

    @Override
    public Chromosome<Integer> createRandomChromosome(int size, Range<Integer> range, Range<Integer> range2,
                                                      FitnessFunction<Integer> fitnessFunction) {
        List<Integer> genes = new ArrayList<>();

        for (int i = 0; i < numShapes; i++) {
            // Sample a random pixel from target to get color hints
            int targetIdx = random.nextInt(targetPixels.size());
            int targetPixel = targetPixels.get(targetIdx);

            int targetX = targetIdx % width;
            int targetY = targetIdx / width;

            // Generate shape near sampled location
            // The gene is [x, y, radius, r, g, b, a] sequence
            if (random.nextBoolean()) {
                genes.add(targetX + random.nextInt(-50, 51));  // x with variation
                genes.add(targetY + random.nextInt(-50, 51));  // y with variation
            }else{
                genes.add(random.nextInt(0, width));  // completely random x
                genes.add(random.nextInt(0, height)); // completely random y
            }
            genes.add(random.nextInt(10, width / 6)); // radius

            int red = (targetPixel >> 16) & 0xFF;
            int green = (targetPixel >> 8) & 0xFF;
            int blue = targetPixel & 0xFF;

            genes.add(clamp(red + (int) (random.nextGaussian()*30),0 , 255)); // red from target
            genes.add(clamp(green+ (int)(random.nextGaussian()*30), 0, 255)); // green from target
            genes.add(clamp(blue+ (int)(random.nextGaussian()*30), 0, 255)); // blue from target
            genes.add(random.nextInt(40, 100)); // semi-transparent alpha
        }

        return new IntegerChromosome(genes, fitnessFunction, range2);
    }
}
