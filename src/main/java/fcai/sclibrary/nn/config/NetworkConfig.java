package fcai.sclibrary.nn.config;

import fcai.sclibrary.nn.initialization.RandomUniform;
import fcai.sclibrary.nn.initialization.WeightInitializer;
import fcai.sclibrary.nn.layers.Layer;
import fcai.sclibrary.nn.loss.LossFunction;
import fcai.sclibrary.nn.loss.MeanSquaredError;
import fcai.sclibrary.nn.optimizers.GradientDescent;
import fcai.sclibrary.nn.optimizers.Optimizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class NetworkConfig {
    private List<Layer> layers;

    @Builder.Default
    private WeightInitializer weightInitializer= new RandomUniform();

    @Builder.Default
    private LossFunction loss = new MeanSquaredError();

    @Builder.Default
    private Optimizer optimizer = new GradientDescent();

}
