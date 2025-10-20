package fcai.sclibrary.ga.operators.selection;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;
@Data
@Setter
public class TournamentSelection<T extends Chromosome<N>, N extends Number> implements SelectionStrategy<T, N> {

    private int k;
    private Random random = new Random();
    private final Logger logger = Logger.getLogger(TournamentSelection.class.getName());

    public TournamentSelection(int k) {
        this.k = k;
    }

    @Override
    public List<T> select(List<T> population, int numberToSelect, FitnessFunction<N> fitnessFunction) {
//        if (population.size()%numberToSelect!=0){
//            throw new RuntimeException("Population size must be multiple of number to select for tournament selection");
//        }

        List<T> matingPool = new ArrayList<>(population);
        List<T> selectedChromosomes = new ArrayList<>();
        int size=matingPool.size();

        // Shuffle the chromosomes in the mating pool
        for (int i = matingPool.size()-1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Swap
            T temp = matingPool.get(i);
            matingPool.set(i, matingPool.get(j));
            matingPool.set(j, temp);
        }

        for (int i = 0; i < numberToSelect; i++) {
            T bestChromosome = getBestChromosome(matingPool, i, size);
            selectedChromosomes.add(bestChromosome);
        }

        return selectedChromosomes;
    }

    private T getBestChromosome(List<T> matingPool, int i, int size) {
        T bestChromosome = matingPool.get(i);
        double bestFitness = bestChromosome.getFitness();

        // Getting the best chromosome from k candidates
        for (int j = i; j < k+ i; j++) {
            T currentChromosome = matingPool.get(j% size);
            double currentFitness = currentChromosome.getFitness();
            if (currentFitness > bestFitness) {
                bestChromosome = currentChromosome;
                bestFitness = currentFitness;
            }
        }
        return bestChromosome;
    }

}
