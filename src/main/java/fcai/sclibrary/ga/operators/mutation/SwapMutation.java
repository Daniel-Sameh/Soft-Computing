package fcai.sclibrary.ga.operators.mutation;


import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.List;
import java.util.Random;

public class SwapMutation<T extends Number> implements Mutation<T>{
    @Override
    public Chromosome<T> mutate(Chromosome<T> chromosome, double pm, Range<T> range) {
        List<T> genes = chromosome.getGenes();
        double min = 0.0;
        double max = 1.0;
        Random rand = new Random();
        double r = min + (rand.nextDouble() * (max - min));
        int minI = 0;
        int maxI = genes.size() - 1;

        int r1 = rand.nextInt(genes.size());
        int r2 = rand.nextInt(genes.size());
        boolean changed = false;

        if(r <= pm) {
            changed = true;
            T tmp = genes.get(r1);
            genes.set(r1, genes.get(r2));
            genes.set(r2, tmp);
        }
        chromosome.setGenes(genes);
        if(!changed)
            return null;
        else
            return chromosome;
    }
}
