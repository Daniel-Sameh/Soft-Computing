package fcai.sclibrary.ga.operators.crossover;

import fcai.sclibrary.ga.chromosome.Chromosome;
import java.util.Collections;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OrderOneCrossover<T extends Number> implements Crossover<T> {
    private final Random random = new Random();

    public List<Chromosome<T>> crossover(Chromosome<T> chromosome1, Chromosome<T> chromosome2, double pc) {
        if(random.nextDouble()>=pc){
            return null;
        }
        else{
            int chromosomeSize = chromosome1.length();

            List<T> genes1 = chromosome1.getGenes();
            List<T> genes2 = chromosome2.getGenes();

            List<T> child1Genes = new ArrayList<>(Collections.nCopies(chromosomeSize, null));
            List<T> child2Genes = new ArrayList<>(Collections.nCopies(chromosomeSize, null));

            int point1 = random.nextInt(chromosomeSize - 1) + 1;
            int point2 = random.nextInt(chromosomeSize - 1) + 1;

            while (point1 == point2) {
                point2 = random.nextInt(chromosomeSize - 1) + 1;
            }

            if (point1 > point2) {
                int temp = point1;
                point1 = point2;
                point2 = temp;
            }

            for (int i = point1; i <= point2; i++) {
                child1Genes.set(i, genes1.get(i));
                child2Genes.set(i, genes2.get(i));
            }

            fillRemainingGenes(child1Genes, genes2, point2);
            fillRemainingGenes(child2Genes, genes1, point2);
            chromosome1.setGenes(child1Genes);
            chromosome2.setGenes(child2Genes);


            return Arrays.asList(chromosome1, chromosome2);
        }

    }

    private void fillRemainingGenes(List<T> childGenes, List<T> parentGenes, int startIndex) {
        int chromosomeSize = parentGenes.size();
        int currentIndex = (startIndex + 1) % chromosomeSize;

        for (T gene : parentGenes) {
            if (!childGenes.contains(gene)) {
                while (childGenes.get(currentIndex) != null) {
                    currentIndex = (currentIndex + 1) % chromosomeSize;
                }
                childGenes.set(currentIndex, gene);
            }
        }
    }

}
