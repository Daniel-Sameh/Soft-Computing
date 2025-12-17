package fcai.sclibrary.nn.core;

import fcai.sclibrary.nn.config.NetworkConfig;
import fcai.sclibrary.nn.config.TrainingConfig;
import fcai.sclibrary.nn.exceptions.InvalidConfigurationException;
import fcai.sclibrary.nn.layers.DenseLayer;
import fcai.sclibrary.nn.layers.InputLayer;
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

    public void compile(){
        List<Layer> layers = networkConfig.getLayers();
        if (layers.size()<2){
            throw new InvalidConfigurationException("Neural network must have at least input and output layers.");
        }
        if (!(layers.get(0) instanceof InputLayer)){
            throw new InvalidConfigurationException("First layer must be an InputLayer.");
        }
        for (int i = 1; i < layers.size(); i++) {
            if (!(layers.get(i) instanceof DenseLayer)){
                throw new InvalidConfigurationException("All hidden and output layers must be DenseLayer.");
            }
            Layer currentLayer = layers.get(i);
            Layer previousLayer = layers.get(i - 1);

            // Get the output size of the previous layer as the input of the current layer
            int inSize = previousLayer.getOutputSize();

            // Build and initialize the current layer
            currentLayer.build(inSize);
            currentLayer = networkConfig.getWeightInitializer().initialize(currentLayer, trainingConfig.isUseBias());
        }
    }
    public void fit(Tensor X, Tensor y) {

    }


}
