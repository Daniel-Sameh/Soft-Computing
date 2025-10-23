package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
//import fcai.sclibrary.usecases.artisticImageApproximation.shape.Circle;
//import fcai.sclibrary.usecases.artisticImageApproximation.shape.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageRenderer {

    private static final int GENES_PER_SHAPE = 7;

    public static BufferedImage renderChromosome(Chromosome<Integer> chromosome, int width, int height) {
        if (chromosome == null) return null;

        List<Integer> genes = chromosome.getGenes();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Enable anti-aliasing for smoother shapes
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        // Draw each circle (7 genes per circle)
        int numShapes = genes.size() / GENES_PER_SHAPE;
        for (int i = 0; i < numShapes; i++) {
            int base = i * GENES_PER_SHAPE;

            int x = genes.get(base) % width;// Wrap to image bounds
            int y = genes.get(base + 1) % height;
            int radius = Math.max(5, genes.get(base + 2) % (Math.min(width, height) / 4));
            int red = genes.get(base + 3) & 0xFF;
            int green = genes.get(base + 4) & 0xFF;
            int blue = genes.get(base + 5) & 0xFF;
            int alpha = Math.max(20, Math.min(150, genes.get(base + 6) & 0xFF));

            g.setColor(new Color(red, green, blue, alpha));
            g.fillOval(x-radius, y-radius, radius*2, radius*2);
        }

        g.dispose();
        return image;
    }

}