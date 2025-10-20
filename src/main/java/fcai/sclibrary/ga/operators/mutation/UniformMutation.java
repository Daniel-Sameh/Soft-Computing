package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.List;
import java.util.Random;

public class UniformMutation implements Mutation<Double>{
    @Override
    public Chromosome<Double> mutate(Chromosome<Double> chromosome, double pm, Range<Double> range){
        List<Double> genes = chromosome.getGenes();
        double lb = range.getLower(), ub = range.getUpper();
        double min = 0.0;
        double max = 1.0;
        Random rand = new Random();
        boolean changed = false;
        for(int i = 0; i < genes.size(); i++){
            double r = min + (rand.nextDouble() * (max - min));
            if(r <= pm){
                changed = true;
                double lxi = genes.get(i) - lb;
                double uxi = ub - genes.get(i);
                double r1 = min + (rand.nextDouble() * (max - min));
                double delta;
                if(r1 <= 0.5){
                    delta = lxi;
                }
                else {
                    delta = uxi;
                }
                double r2 = min + (rand.nextDouble() * (delta - min));
                if((delta == lxi))
                    genes.set(i, genes.get(i) - r2);
                else if(delta == uxi)
                    genes.set(i, genes.get(i) + r2);
            }
        }
        chromosome.setGenes(genes);
        if(!changed)
            return null;
        else
            return chromosome;
    }
}
