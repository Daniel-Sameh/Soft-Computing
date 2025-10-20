package fcai.sclibrary;

import fcai.sclibrary.ga.chromosome.BinaryChromosome;
import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;
import fcai.sclibrary.ga.chromosome.factory.BinaryChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.IntegerChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import fcai.sclibrary.ga.operators.crossover.*;
import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BinaryChromosomeFactory factory = new BinaryChromosomeFactory();

        Chromosome<Integer> chromosome = factory.createRandomChromosome(10, new Range<Integer>(0, 1), null);
        Mutation<Integer> m = new FlipMutation();
        m.mutate(chromosome, 0.5, new Range<Integer>(0, 1));

    }
}