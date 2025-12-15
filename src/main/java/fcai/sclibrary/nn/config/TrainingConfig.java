package fcai.sclibrary.nn.config;

import fcai.sclibrary.nn.initialization.RandomUniform;
import fcai.sclibrary.nn.initialization.WeightInitializer;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TrainingConfig {
    @Builder.Default
    private int epochs=10;

    @Builder.Default
    private int batchSize=32;

    @Builder.Default
    private double learningRate=0.01;

}
