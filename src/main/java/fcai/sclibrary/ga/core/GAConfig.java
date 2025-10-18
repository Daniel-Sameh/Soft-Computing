package fcai.sclibrary.ga.core;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.chromosome.factory.Range;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GAConfig<T extends Chromosome, N extends Number> {
    ///TODO: ADD ALL Rates, probabilities, etc...
    private final int chromosomeSize;
    private final Range<N> range;
    @Builder.Default private final double upperLimit = 1.0;
    @Builder.Default private final double lowerLimit = 0.0;
    @Builder.Default private final int populationSize = 100;
    @Builder.Default private final int generations = 1000;
    @Builder.Default private final double mutationRate = 0.001;
    @Builder.Default private final double crossoverRate = 0.8;


    ///TODO: Add the operators interfaces & fitness function class
    ///TODO: & add the use case listener
}
