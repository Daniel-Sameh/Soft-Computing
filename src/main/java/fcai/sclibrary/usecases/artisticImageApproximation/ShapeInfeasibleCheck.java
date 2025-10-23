package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.InfeasibleCheck;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.List;

public class ShapeInfeasibleCheck implements InfeasibleCheck<Integer> {
    private final int width;
    private final int height;
    private final int GENES_PER_SHAPE = 7; // x, y, size, r, g, b, alpha

    public ShapeInfeasibleCheck(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean isInfeasible(Chromosome<Integer> chromosome, Range<Integer> range) {
        List<Integer> genes = chromosome.getGenes();

        // Check if the size is valid
        if (genes.size()%GENES_PER_SHAPE!=0) {
            return true;
        }

        int numShapes = genes.size() / GENES_PER_SHAPE;

        for (int i = 0; i < numShapes; i++) {
            int base = i * GENES_PER_SHAPE;

            //Validate each shape's parameters
            int x = genes.get(base);
            int y = genes.get(base + 1);
            int radius = genes.get(base + 2);
            int r = genes.get(base + 3);
            int g = genes.get(base + 4);
            int b = genes.get(base + 5);
            int a = genes.get(base + 6);

            if (x < -50 || x >= width+50) return true;
            if (y < -50 || y >= height+50) return true;
            if (radius < 5 || radius > width) return true;
            if (r < 0 || r > 255) return true;
            if (g < 0 || g > 255) return true;
            if (b < 0 || b > 255) return true;
            if (a < 0 || a > 255) return true;
        }

        return false;
    }

}
