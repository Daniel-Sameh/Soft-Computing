package fcai.sclibrary.ga.core;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.FitnessFunction;
import fcai.sclibrary.ga.chromosome.IntegerChromosome;
import fcai.sclibrary.ga.chromosome.factory.ChromosomeFactory;
import fcai.sclibrary.ga.chromosome.factory.Range;
import fcai.sclibrary.ga.operators.crossover.Crossover;
import fcai.sclibrary.ga.operators.crossover.SinglePointCrossover;
import fcai.sclibrary.ga.operators.mutation.FlipMutation;
import fcai.sclibrary.ga.operators.mutation.Mutation;
import fcai.sclibrary.ga.operators.replacement.Replacement;
import fcai.sclibrary.ga.operators.replacement.SteadyStateReplacement;
import fcai.sclibrary.ga.operators.selection.RouletteWheelSelection;
import fcai.sclibrary.ga.operators.selection.SelectionStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GAConfig<T extends Chromosome<?>, N extends Number> {
    private final int chromosomeSize;
    private final Range<N> range;
    private FitnessFunction<N> fitnessFunction;
    private ChromosomeFactory<N> chromosomeFactory;
    @Builder.Default private final int populationSize = 100;
    @Builder.Default private final int generations = 1000;
    @Builder.Default private final double mutationRate = 0.001;
    @Builder.Default private final double crossoverRate = 0.8;
    @Builder.Default private final double upperLimit = 1.0;
    @Builder.Default private final double lowerLimit = 0.0;

    @Builder.Default private final Optimization optimization = Optimization.MAXIMIZE;

    @Builder.Default private final SelectionStrategy<?, ?> selectionStrategy =
            new RouletteWheelSelection<IntegerChromosome, Integer>();
    @Builder.Default private final Crossover<?> crossover = new SinglePointCrossover<Integer>();
    @Builder.Default private final Mutation<?> mutation = new FlipMutation();
    @Builder.Default private final Replacement<?> replacement = new SteadyStateReplacement<>();
}
