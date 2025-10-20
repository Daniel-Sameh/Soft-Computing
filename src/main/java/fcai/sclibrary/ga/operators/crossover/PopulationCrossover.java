package fcai.sclibrary.ga.operators.crossover;

import fcai.sclibrary.ga.chromosome.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PopulationCrossover<T extends Number> {
    private final Random random = new Random();

    public List<Chromosome> crossoverPopulation(List<Chromosome<T>> Selected,Crossover crossover_operator, double pc){
        if(Selected.size() == 0||Selected.size() == 1){
            return null;
        }
        List<Chromosome> workingPopulation = new ArrayList<>(Selected);
        Collections.shuffle(workingPopulation, random);

        List<Chromosome> crossoveredChildren = new ArrayList<>();
        for (int i=0;i<workingPopulation.size()-1;i+=2){
            Chromosome parent1 = workingPopulation.get(i);
            Chromosome parent2 = workingPopulation.get(i+1);
            List<Chromosome> children=crossover_operator.crossover(parent1,parent2,pc);
            if(children!=null){
                crossoveredChildren.addAll(children);
            }

        }
        if(crossoveredChildren.isEmpty()){
            return null;
        }
        return crossoveredChildren;
    }

}
