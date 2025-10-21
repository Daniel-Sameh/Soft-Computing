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
        if (population== null || population.isEmpty()){
            throw new IllegalArgumentException("Population size must not be empty");
        }

        if (numberToSelect<1){
            throw new IllegalArgumentException("Number to select must be at least 1");
        }

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
            int idx = random.nextInt(size);
            T bestChromosome = getBestChromosome(matingPool, idx, size);
            selectedChromosomes.add(bestChromosome);
        }

        return selectedChromosomes;
    }

    private T getBestChromosome(List<T> matingPool, int i, int size) {
        int k = Math.min(this.k, size);

        List<Integer> tournamentIndices = new ArrayList<>();
        tournamentIndices.add(random.nextInt(size));

        // Randomly selecting k unique chromosomes
        while (tournamentIndices.size() < k){
            int idx = random.nextInt(size);
            if (!tournamentIndices.contains(idx)){
                tournamentIndices.add(idx);
            }
        }

        T bestChromosome = matingPool.get(tournamentIndices.getFirst());
        double bestFitness = bestChromosome.getFitness();
//        System.out.println("Tournament candidates:");

        // Getting the best chromosome from k candidates
        for (int j = 0; j < k; j++) {
            T currentChromosome = matingPool.get(tournamentIndices.get(j));
//            System.out.println("Chromosome fitness: " + currentChromosome.getFitness());
            double currentFitness = currentChromosome.getFitness();
            if (currentFitness > bestFitness) {
                bestChromosome = currentChromosome;
                bestFitness = currentFitness;
            }
        }
//        System.out.println("Selected chromosome fitness: " + bestFitness);
        return bestChromosome;
    }

}
