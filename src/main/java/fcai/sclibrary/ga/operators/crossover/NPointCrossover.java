package fcai.sclibrary.ga.operators.crossover;


import fcai.sclibrary.ga.chromosome.Chromosome;
import java.lang.reflect.Field;
import java.util.*;

public class NPointCrossover<T extends Number> implements Crossover<T> {
    private final Random random = new Random();
    protected int numberOfPoints;
    public NPointCrossover(int numberOfPoints) {
        if (numberOfPoints < 1) {
            throw new IllegalArgumentException("Number of crossover points must be at least 1");
        }
        this.numberOfPoints = numberOfPoints;
    }
    public List<Chromosome<T>> crossover(Chromosome<T> chromosome1, Chromosome<T> chromosome2, double pc) {
        if(random.nextDouble()>=pc){
            return null;
        }
        else{
            int chromosomeSize = chromosome1.length();
            List<Integer> crossoverPoints = generateCrossoverPoints(chromosomeSize);

            List<T> genes1 = chromosome1.getGenes();
            List<T> genes2 = chromosome2.getGenes();

            List<T> child1Genes = new ArrayList<>();
            List<T> child2Genes = new ArrayList<>();

            boolean takeFromParent1 = true;
            int previousPoint = 0;
            for (int point : crossoverPoints) {
                List<T> source1 = takeFromParent1 ? genes1 : genes2;
                List<T> source2 = takeFromParent1 ? genes2 : genes1;

                child1Genes.addAll(source1.subList(previousPoint, point));
                child2Genes.addAll(source2.subList(previousPoint, point));

                takeFromParent1 = !takeFromParent1;
                previousPoint = point;
            }

            List<T> finalSource1 = takeFromParent1 ? genes1 : genes2;
            List<T> finalSource2 = takeFromParent1 ? genes2 : genes1;
            child1Genes.addAll(finalSource1.subList(previousPoint, chromosomeSize));
            child2Genes.addAll(finalSource2.subList(previousPoint, chromosomeSize));

            chromosome1.setGenes(child1Genes);
            chromosome2.setGenes(child2Genes);


            return Arrays.asList(chromosome1, chromosome2);
        }

    }
    private List<Integer> generateCrossoverPoints(int chromosomeSize) {
        if (numberOfPoints >= chromosomeSize - 1) {
            throw new IllegalArgumentException(
                    "Number of crossover points (" + numberOfPoints +
                            ") must be less than chromosome size-1 (" + (chromosomeSize - 1) + ")"
            );
        }

        List<Integer> points = new ArrayList<>();
        while (points.size() < numberOfPoints) {
            int point = random.nextInt(chromosomeSize - 1) + 1;
            if (!points.contains(point)) {
                points.add(point);
            }
        }
        Collections.sort(points);
        return points;
    }
}
