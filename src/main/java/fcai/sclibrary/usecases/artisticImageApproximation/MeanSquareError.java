package fcai.sclibrary.usecases.artisticImageApproximation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;
import fcai.sclibrary.ga.core.Optimization;
import lombok.Data;

import java.util.List;
import java.util.stream.IntStream;

@Data
public class MeanSquareError implements FitnessFunction<Integer> {

    private final List<Integer> target;
    private static final Utils utils = new Utils();
    private static final Optimization optimization = Optimization.MINIMIZE;
    public MeanSquareError(List<Integer> target) {
        this.target = target;
    }
    @Override
    public double evaluate(Chromosome<Integer> chromosome) {
//        double meanSquareError = 0;
        List<Integer> genes = chromosome.getGenes();
//        for (int i = 0; i < target.size(); i++) {
//            double geneAsPixel = utils.geneToPixel(genes.get(i) /  (double) Integer.MAX_VALUE);
//            double targetPixel = utils.geneToPixel(target.get(i));
//            int[] geneRGB = utils.extractRGB(geneAsPixel);
//            int[] targetRGB = utils.extractRGB(targetPixel);
//            meanSquareError += Math.pow(geneRGB[0] - targetRGB[0], 2);
//            meanSquareError += Math.pow(geneRGB[1] - targetRGB[1], 2);
//            meanSquareError += Math.pow(geneRGB[2] - targetRGB[2], 2);
//        }
         double meanSquareError = IntStream.range(0, target.size())
                       .parallel()
                       .mapToDouble(i -> {
                           int geneAsPixel = genes.get(i).intValue();
                           int targetPixel = target.get(i).intValue();

                           int[] geneRGB = utils.extractRGB(geneAsPixel);
                           int[] targetRGB = utils.extractRGB(targetPixel);

                           double error = 0;
                           error += Math.pow(geneRGB[0] - targetRGB[0], 2);
                           error += Math.pow(geneRGB[1] - targetRGB[1], 2);
                           error += Math.pow(geneRGB[2] - targetRGB[2], 2);
                           return error;
                       })
                       .sum();

         double normalizedMSE = meanSquareError / (target.size() * 3);
         return -normalizedMSE;
    }
}
