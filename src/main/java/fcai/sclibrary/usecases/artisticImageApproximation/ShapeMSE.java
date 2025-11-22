package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

public class ShapeMSE implements FitnessFunction<Integer> {
    private final List<Integer> target;
    private final int width;
    private final int height;
    private static final int GENES_PER_SHAPE = 9;

    public ShapeMSE(List<Integer> target, int width, int height) {
        this.target = target;
        this.width = width;
        this.height = height;
    }

    @Override
    public double evaluate(Chromosome<Integer> chromosome, Range<Integer> range) {
        // Render the chromosome's shapes
        BufferedImage rendered = ImageRenderer.renderChromosome(chromosome, width, height);

        // Calculate MSE between rendered image and target
        double totalError = IntStream.range(0, width * height)
            .parallel()
            .mapToDouble(i -> {
                int x = i % width;
                int y = i / width;
                int renderedPixel = rendered.getRGB(x, y);
                int targetPixel = target.get(i);

                int r1 = (renderedPixel >> 16) & 0xFF;
                int g1 = (renderedPixel >> 8) & 0xFF;
                int b1 = renderedPixel & 0xFF;

                int r2 = (targetPixel >> 16) & 0xFF;
                int g2 = (targetPixel >> 8) & 0xFF;
                int b2 = targetPixel & 0xFF;

                //making penalty for large differences
                int dr = r1 - r2;
                if (dr>120) dr *=2;
                int dg = g1 - g2;
                if (dg>120) dg *=2;
                int db = b1 - b2;
                if (db>120) db *=2;

                return (double) dr * dr + (double) dg * dg + (double) db * db;
            })
            .sum();

        double mse = totalError / (width * height * 3);
        return -mse; // Negative for minimization
    }
}
