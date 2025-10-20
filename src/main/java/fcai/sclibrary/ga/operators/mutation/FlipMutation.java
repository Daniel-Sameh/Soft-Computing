package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlipMutation implements Mutation<Integer>{
    @Override
    public Chromosome<Integer> mutate(Chromosome<Integer> chromosome, double pm, Range<Integer> range){
        List<Integer> genes = chromosome.getGenes();
        double min = 0.0;
        double max = 1.0;
        Random rand = new Random();
        boolean changed = false;
        for(int i = 0; i < genes.size(); i++){
            double r = min + (rand.nextDouble() * (max - min));
            if(r <= pm){
                changed = true;
                if(genes.get(i) == 1){
                    genes.set(i, 0);
                }
                else{
                    genes.set(i, 1);
                }
            }
        }
        chromosome.setGenes(genes);
        if(!changed)
            return null;
        return chromosome;

    }
}
