package fcai.sclibrary.ga.operators.selection;

import fcai.sclibrary.ga.chromosome.BinaryChromosome;
import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.factory.BinaryChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentSelectionTest {
    private SelectionStrategy<BinaryChromosome, Integer> selection;
    private FitnessFunction<Integer> fitnessFunction;
    private Random random;
    private List<BinaryChromosome> population;

    @BeforeEach
    void setUp() {
        selection = new TournamentSelection(2);
        population = new ArrayList<>();
        ChromosomeFactory factory = new BinaryChromosomeFactory();
        fitnessFunction = Mockito.mock(FitnessFunction.class);

        for (int i = 0; i < 6; i++)
            population.add((BinaryChromosome) factory.createRandomChromosome(65536, new Range<>(0,1), fitnessFunction));

        // Mock fitness function
        Mockito.when(fitnessFunction.evaluate(population.get(0))).thenReturn(1.0);
        Mockito.when(fitnessFunction.evaluate(population.get(1))).thenReturn(2.0);
        Mockito.when(fitnessFunction.evaluate(population.get(2))).thenReturn(3.0);
        Mockito.when(fitnessFunction.evaluate(population.get(3))).thenReturn(4.0);
        Mockito.when(fitnessFunction.evaluate(population.get(4))).thenReturn(10.0);
        Mockito.when(fitnessFunction.evaluate(population.get(5))).thenReturn(5.0);
    }

    @Test
    void testSelectionReturnsCorrectNumber() {
        List<BinaryChromosome> selected = selection.select(population, 3, fitnessFunction);
        System.out.println("Selected size: " + selected.size());
        assert selected.size() == 3 : "Should select the requested number of individuals";
    }

    @Test
    void testTournamentSelectionPicksFitterIndividuals() {
        int trials = 10000;
        int count = 0;

        for (int i = 0; i < trials; i++) {
            List<BinaryChromosome> selected = selection.select(population, 2, fitnessFunction);
            if (selected.get(0) == population.get(4) || selected.get(1) == population.get(4))
                count++;
        }

        double probability = (double) count / trials;
        System.out.println("Probability of selecting highest fitness in tournament: " + probability*100);
        assert probability > 0.4 : "Highest fitness individual should have noticeably higher selection probability in tournaments";
    }


    @Test
    void testLowerFitnessHasLowerProbability(){
        int trials= 10000;
        int count = 0;

        for (int i = 0; i < trials; i++) {
            List<BinaryChromosome> selected = selection.select(population, 2, fitnessFunction);
            if (selected.get(0) == population.get(0) || selected.get(1) == population.get(0))
                count++;
            if (i%1000==0){
                for (Chromosome chr : selected) {
                    System.out.println("Chromosome fitness: " + chr.getFitness());
                }
                System.out.println("---------------");
            }
        }

        double probability = (double) count / trials;
        System.out.println("Probability of selecting lowest fitness in tournament: " + probability*100);

        assert probability < 0.3 : "Lowest fitness individual should have noticeably lower selection probability in tournaments";
    }

    @Test
    void testGettingAllPopulation(){
        List<BinaryChromosome> selected = selection.select(population, population.size(), fitnessFunction);
        System.out.println("Selected size: " + selected.size());
        for (BinaryChromosome chr : selected) {
            System.out.println("Chromosome fitness: " + chr.getFitness());
        }
        assert selected.size() == population.size() : "Should select all individuals when requested number equals population size";
    }

    @Test
    void testGetAllPopulationHighestFitness() {
        List<BinaryChromosome> selected = selection.select(population, population.size(), fitnessFunction);
        long countHighestFitness = selected.stream()
                .filter(chr -> chr.getFitness() == 10.0)
                .count();
        System.out.println("Number of times highest fitness (10.0) appears: " + countHighestFitness);
        assert countHighestFitness > 0 : "Highest fitness individual should appear at least once";
        assert countHighestFitness < population.size() : "Highest fitness individual should not be selected for all positions";
      }


}
