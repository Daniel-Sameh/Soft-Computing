package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
//import fcai.sclibrary.usecases.artisticImageApproximation.shape.Circle;
//import fcai.sclibrary.usecases.artisticImageApproximation.shape.Shape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageRenderer {

    private static final int GENES_PER_SHAPE = 9;

    public static BufferedImage renderChromosome(Chromosome<Integer> chromosome, int width, int height) {
        if (chromosome == null) return null;

        List<Integer> genes = chromosome.getGenes();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        int numShapes = genes.size() / GENES_PER_SHAPE;
        for (int i = 0; i < numShapes; i++) {
            int base = i * GENES_PER_SHAPE;

            int type = genes.get(base);
            int x = genes.get(base + 1) % width;
            int y = genes.get(base + 2) % height;
            int param1 = Math.max(5, genes.get(base + 3) % (width / 4));  // size/width/base
            int param2 = Math.max(5, genes.get(base + 4) % (height / 4)); // height
            int red = genes.get(base + 5) & 0xFF;
            int green = genes.get(base + 6) & 0xFF;
            int blue = genes.get(base + 7) & 0xFF;
            int alpha = Math.max(20, Math.min(150, genes.get(base + 8) & 0xFF));

            g.setColor(new Color(red, green, blue, alpha));

            switch (type) {
                case 0: // Circle
                    int radius = param1;
                    g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                    break;
                case 1: // Square
                    int side = param1;
                    g.fillRect(x - side/2, y - side/2, side, side);
                    break;
                case 2: // Rectangle
                    int w = param1;
                    int h = param2;
                    g.fillRect(x - w/2, y - h/2, w, h);
                    break;
                case 3: // Triangle with varied orientation
                    int tBase = param1;
                    int tHeight = param2;

                    // Use combination of genes to determine rotation (0, 60, 120, 180, 240, 300 degrees)
                    // This gives 6 different orientations without adding genes
                    int rotationSeed = (red + green + blue + alpha) % 6;
                    double angle = Math.toRadians(rotationSeed * 60);

                    // Base triangle points (pointing up)
                    double[] baseX = {0, -tBase/2.0, tBase/2.0};
                    double[] baseY = {-tHeight/2.0, tHeight/2.0, tHeight/2.0};

                    // Rotate and translate
                    int[] xPoints = new int[3];
                    int[] yPoints = new int[3];
                    for (int j = 0; j < 3; j++) {
                        xPoints[j] = x + (int)(baseX[j] * Math.cos(angle) - baseY[j] * Math.sin(angle));
                        yPoints[j] = y + (int)(baseX[j] * Math.sin(angle) + baseY[j] * Math.cos(angle));
                    }
                    g.fillPolygon(xPoints, yPoints, 3);
                    break;
            }
        }

        g.dispose();
        return image;
    }

}