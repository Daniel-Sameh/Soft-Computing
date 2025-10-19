package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class InversionMutation implements Mutation {
    @Override
    public void mutate(Chromosome chromosome, int pm, Range range){
        List<Integer> genes = chromosome.getGenes();
        double min = 0.0;
        double max = 1.0;
        Random rand = new Random();
        double r = min + (rand.nextDouble() * (max - min));
        int minI = 0;
        int maxI = genes.size() - 1;

        int r1 = minI + (rand.nextInt() * (maxI - minI));
        int r2 = minI + (rand.nextInt() * (maxI - minI));

        if(r <= pm) {
            for(int i = r1; i <= r2 / 2; i++) {
                Collections.swap(genes, i, r2 - i);
            }
        }
    }
}
