package fcai.sclibrary.ga.operators.mutation;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;

import java.util.List;
import java.util.Random;

public class NonUniformMutation implements Mutation<Double> {
    @Override
    public Chromosome<Double> mutate(Chromosome<Double> chromosome, double pm, Range<Double> range){
        List<Double> genes = chromosome.getGenes();
        double lb = range.getLower(), ub = range.getUpper();
        double min = 0.0;
        double max = 1.0;
        Random rand = new Random();
        boolean changed = false;
        for(int i = 0; i < genes.size(); i++){
            double rm = min + (rand.nextDouble() * (max - min));
            if(rm <= pm){
                changed = true;
                double lxi = genes.get(i) - lb;
                double uxi = ub - genes.get(i);
                double r1 = min + (rand.nextDouble() * (max - min));
                double y;
                if(r1 <= 0.5){
                    y = lxi;
                }
                else {
                    y = uxi;
                }
                double r = min + (rand.nextDouble() * (max - min));
                double b = 0.5;
                double t = 1, T = 1;
                double delta =y * (1 - Math.pow(r, Math.pow(((1 - t) / T), b)));
                if(y == lxi){
                    genes.set(i, genes.get(i) - delta);
                }
                else if(y == uxi){
                    genes.set(i, genes.get(i) + delta);
                }
            }
        }
        chromosome.setGenes(genes);
        if(!changed)
            return null;
        else
            return chromosome;
    }
}
