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
import static org.junit.jupiter.api.Assertions.*;

public class RouletteWheelSelectionTest {
    private SelectionStrategy selection;
    private FitnessFunction<Integer> fitnessFunction;
    private List<BinaryChromosome> population;

    @BeforeEach
    void setUp() {
        selection = new RouletteWheelSelection<BinaryChromosome, Integer>();
        population = new ArrayList<>();
        ChromosomeFactory<Integer> factory = new BinaryChromosomeFactory();
        fitnessFunction = Mockito.mock(FitnessFunction.class);

        for (int i = 0; i < 5; i++)
            population.add((BinaryChromosome) factory.createRandomChromosome(65536, new Range<>(0,1), fitnessFunction));

        // Mock fitness function
        Mockito.when(fitnessFunction.evaluate(population.get(0))).thenReturn(1.0);
        Mockito.when(fitnessFunction.evaluate(population.get(1))).thenReturn(2.0);
        Mockito.when(fitnessFunction.evaluate(population.get(2))).thenReturn(3.0);
        Mockito.when(fitnessFunction.evaluate(population.get(3))).thenReturn(4.0);
        Mockito.when(fitnessFunction.evaluate(population.get(4))).thenReturn(10.0);
    }

    @Test
    void testSelectionReturnsCorrectNumber() {
        List<BinaryChromosome> selected = selection.select(population, 3, fitnessFunction);
        assertEquals(3, selected.size(), "Should select the requested number of individuals");
    }

    @Test
    void testHigherFitnessHasHigherProbability() {
        int trials = 10000;
        int count = 0;

        for (int i = 0; i < trials; i++) {
            List<BinaryChromosome> selected = selection.select(population, 1, fitnessFunction);
            if (selected.get(0) == population.get(4))
                count++;
        }

        double probability = (double) count / trials;
        System.out.println("Probability of selecting highest fitness: " + probability*100);
        assertTrue(probability > 0.3, "Highest fitness individual should have noticeably higher selection probability");
    }

    @Test
    void testLowerFitnessHasLowerProbability(){
        int trials= 10000;
        int count = 0;

        for (int i = 0; i < trials; i++) {
            List<BinaryChromosome> selected = selection.select(population, 1, fitnessFunction);
            if (selected.get(0) == population.get(0))
                count++;
        }

        double probability = (double) count / trials;
        System.out.println("Probability of selecting lowest fitness: " + probability*100);
        assertTrue(probability < 0.1, "Lowest fitness individual should have noticeably lower selection probability");
    }

}
