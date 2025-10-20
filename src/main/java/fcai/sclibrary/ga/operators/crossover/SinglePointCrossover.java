package fcai.sclibrary.ga.operators.crossover;

import fcai.sclibrary.ga.chromosome.Chromosome;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SinglePointCrossover<T extends Number> implements Crossover<T>{
    private final Random random = new Random();
    public List<Chromosome<T>> crossover(Chromosome<T> chromosome1, Chromosome<T> chromosome2, double pc){
        if(random.nextDouble()>=pc){
            return null;
        }
        else{
            int chromosome_size=chromosome1.length();
            int crossoverpoint=generateCrossoverPoint(chromosome_size);
            List<T> child1genes=new ArrayList<T>();
            List <T> child2genes=new ArrayList<T>();
            List<T> genes1 = chromosome1.getGenes();
            List<T> genes2 = chromosome2.getGenes();
            child1genes.addAll(genes1.subList(0, crossoverpoint));
            child1genes.addAll(genes2.subList(crossoverpoint, chromosome_size));

            child2genes.addAll(genes2.subList(0, crossoverpoint));
            child2genes.addAll(genes1.subList(crossoverpoint, chromosome_size));


            chromosome1.setGenes(child1genes);
            chromosome2.setGenes(child2genes);
            return Arrays.asList(chromosome1, chromosome2);
        }
    }

    private int generateCrossoverPoint(int chromosomeSize) {
        if (chromosomeSize <= 1) {
            throw new IllegalArgumentException("Chromosome size must be at least 2 for crossover");
        }
        return random.nextInt(chromosomeSize - 1) + 1;
    }
}
