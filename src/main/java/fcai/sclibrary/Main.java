package fcai.sclibrary;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.BinaryChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import fcai.sclibrary.ga.operators.mutation.FlipMutation;
import fcai.sclibrary.ga.operators.mutation.Mutation;

public class Main {
    public static void main(String[] args) {
        BinaryChromosomeFactory factory = new BinaryChromosomeFactory();

        Chromosome<Integer> chromosome = factory.createRandomChromosome(10, new Range<Integer>(0, 1), null);
        Mutation<Integer> m = new FlipMutation();
        m.mutate(chromosome, 0.5, new Range<Integer>(0, 1));

    }
}