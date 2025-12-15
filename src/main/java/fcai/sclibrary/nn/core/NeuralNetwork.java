package fcai.sclibrary.nn.core;

import fcai.sclibrary.nn.config.NetworkConfig;
import fcai.sclibrary.nn.config.TrainingConfig;
import fcai.sclibrary.nn.layers.Layer;
import fcai.sclibrary.nn.loss.LossFunction;
import fcai.sclibrary.nn.optimizers.Optimizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class NeuralNetwork {
    @Builder.Default
    private NetworkConfig networkConfig = NetworkConfig.builder().build();
    @Builder.Default
    private TrainingConfig trainingConfig = TrainingConfig.builder().build();

    public void fit(Tensor X, Tensor y) {

    }


}
