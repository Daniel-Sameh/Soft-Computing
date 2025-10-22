package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageRenderer {
    private static final Utils utils = new Utils();

    public static BufferedImage renderChromosome(Chromosome<Integer> chromosome, int width, int height) {
        if (chromosome == null) return null;

        List<Integer> genes = chromosome.getGenes();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (index < genes.size()) {
                    int pixel = utils.geneToPixel(genes.get(index));
                    image.setRGB(x, y, pixel);
                    index++;
                }
            }
        }

        return image;
    }
}
