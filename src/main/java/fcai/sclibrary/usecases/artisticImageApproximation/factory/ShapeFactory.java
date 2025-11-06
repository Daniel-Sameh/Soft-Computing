package fcai.sclibrary.usecases.artisticImageApproximation.factory;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.clamp;

public class ShapeFactory implements ChromosomeFactory<Integer> {
    protected final Random random = new Random();
    protected final int width;
    protected final int height;
    protected final int numShapes;
    protected final List<Integer> targetPixels;
    @Setter
    protected int type=0; // 0: circle, 1: square, 2: rectangle, 3: triangle

    public ShapeFactory(int width, int height, int numShapes, List<Integer> targetPixels) {
        this.width = width;
        this.height = height;
        this.numShapes = numShapes;
        this.targetPixels = targetPixels;
    }

    public ShapeFactory(int width, int height, int numShapes, List<Integer> targetPixels, int type) {
        this.width = width;
        this.height = height;
        this.numShapes = numShapes;
        this.targetPixels = targetPixels;
        this.type = type;
    }

    @Override
    public Chromosome<Integer> createRandomChromosome(int size, Range<Integer> range, Range<Integer> range2,
                                                      FitnessFunction<Integer> fitnessFunction) {
        List<Integer> genes = new ArrayList<>();

        for (int i = 0; i < numShapes; i++) {
            // If type is 4 or higher (mixed shapes), randomize the type for each shape
            int shapeType = (type >= 4) ? random.nextInt(4) : type;
            genes.add(shapeType); // type (0=circle, 1=square, 2=rectangle, 3=triangle)
            addGenes(genes, i);
        }

        return new IntegerChromosome(genes, fitnessFunction, range2);
    }

    protected void addGenes(List<Integer> genes, int i){
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

        // Size parameters
        genes.add(random.nextInt(10, width / 6));// radius/side/width/base
        genes.add(random.nextInt(10, width / 6)); // only for rectangle and triangle

        int red = (targetPixel >> 16) & 0xFF;
        int green = (targetPixel >> 8) & 0xFF;
        int blue = targetPixel & 0xFF;

        genes.add(clamp(red + (int) (random.nextGaussian()*30),0 , 255)); // red from target
        genes.add(clamp(green+ (int)(random.nextGaussian()*30), 0, 255)); // green from target
        genes.add(clamp(blue+ (int)(random.nextGaussian()*30), 0, 255)); // blue from target
        genes.add(random.nextInt(40, 100)); // semi-transparent alpha
    }
}
