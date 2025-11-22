package fcai.sclibrary.ga.operators.selection;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Data
public class RankSelection<T extends Chromosome<N>, N extends Number> implements SelectionStrategy<T,N>{
    private Random random = new Random();
    private final Logger logger = Logger.getLogger(RankSelection.class.getName());

    @Override
    public List<T> select(List<T> population, int numberToSelect, FitnessFunction<N> fitnessFunction) {
        // Sort population based on fitness in ascending order
        List<T> sortedPopulation = new ArrayList<>(population);
        sortedPopulation.sort((a, b) -> Double.compare(a.getFitness(), b.getFitness()));

        int size = sortedPopulation.size();
        int n = (size*(size+1))/2;

        // Ranks (i/n)
        List<Double> ranks = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            ranks.add((double) i / n);
        }

        // Cumulative ranks
        for (int i = 1; i < size; i++) {
            ranks.set(i, ranks.get(i) + ranks.get(i - 1));
        }

        double l= 0, r= ranks.getLast();

        List<T> selected = new ArrayList<>();
        for (int i = 0; i < numberToSelect; i++) {
            double rand = l + (r-l) * random.nextDouble();
            int idx = binarySearch(ranks, rand);
            if (idx != -1) {
                selected.add(population.get(idx));
            }
        }

        return selected;
    }

    private int binarySearch(List<Double> fitness, double value) {
        int left = 0;
        int right = fitness.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (fitness.get(mid) < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left < fitness.size() ? left : -1;
    }
}
