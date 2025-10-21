package fcai.sclibrary.ga.operators.selection;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static java.util.Collections.binarySearch;

@Data
public class RouletteWheelSelection<T extends Chromosome<N>, N extends Number> implements SelectionStrategy<T,N> {
    private Random random = new Random();
    private final Logger logger = Logger.getLogger(RouletteWheelSelection.class.getName());
    @Override
    public List<T> select(List<T> population, int numberToSelect, FitnessFunction<N> fitnessFunction) {
        // Calculating fitness
        List<Double> fitness = new ArrayList<>(population.parallelStream()
                .map(chromosome -> chromosome.getFitness())
                .toList());

        logger.info("Population size: " + population.size());
        logger.info("Fitness list size: " + fitness.size());

        // Cumulative fitness
        for (int i = 1; i < fitness.size(); i++) {
            fitness.set(i, fitness.get(i) + fitness.get(i - 1));
        }

        double l= 0, r= fitness.getLast();

        List<T> selected = new ArrayList<>();
        for (int i = 0; i < numberToSelect; i++) {
            double rand = l + (r - l) * random.nextDouble();
            int idx = binarySearch(fitness, rand);
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
