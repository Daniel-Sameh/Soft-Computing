package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InversionMutation<T extends Number> implements Mutation<T> {
    @Override
    public Chromosome<T> mutate(Chromosome<T> chromosome, double pm, Range<T> range){
        Random rand = new Random();
        if (rand.nextDouble() > pm) {
            return null; // No mutation occurs
        }

        // 1 2 3 4 5 6 7 8

        List<T> genes = chromosome.getGenes();
        double min = 0.0;
        double max = 1.0;
        double r = min + (rand.nextDouble() * (max - min));
        int minI = 0;
        int maxI = genes.size() - 1;

        int r1 = rand.nextInt(genes.size());
        int r2 = rand.nextInt(genes.size());
        boolean changed = false;
        if(r <= pm) {
            changed = true;
            for(int i = Math.min(r1, r2); i <= Math.max(r1, r2) / 2; i++) {
                Collections.swap(genes, i, r2 - i);
            }
        }
        chromosome.setGenes(genes);
        if(!changed)
            return null;
        else
            return chromosome;
    }
}
