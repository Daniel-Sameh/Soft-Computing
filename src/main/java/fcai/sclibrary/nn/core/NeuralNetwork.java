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
    public void train(Tensor X, Tensor y) {
        LossFunction lossFunction = networkConfig.getLoss();
        Optimizer optimizer = networkConfig.getOptimizer();

        List<Layer> layers =  networkConfig.getLayers();

        for (int epoch = 0; epoch < trainingConfig.getEpochs(); epoch++) {
            Tensor output = X;
            for (Layer layer : layers) {
                output = layer.forward(output);
            }
            double lossValue = lossFunction.computeError(output, y);
            System.out.println("Epoch: " + epoch);
            System.out.println("Loss: " + lossValue);
            int rows = output.getRows();
            int cols = output.getCols();

            double[][] grad = new double[rows][cols];
            double[][] p = output.getData();
            double[][] t = y.getData();

            int n = rows * cols;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grad[i][j] = 2.0 * (p[i][j] - t[i][j]) / n;
                }
            }
            Tensor gradient = new Tensor(grad);

            for (int i = layers.size() - 1; i > 0; i--) {
                gradient = layers.get(i).backward(gradient);
                layers.get(i).updateParameters(optimizer);
            }
        }
    }

    public Tensor predict(Tensor X) {
        List<Layer> layers = networkConfig.getLayers();
        Tensor output = X;
        for (Layer layer : layers) {
            output = layer.forward(output);
        }
        return output;
    }

    public double evaluateMSE(Tensor X, Tensor yTrue) {
        Tensor yPred = predict(X);
        return networkConfig.getLoss().computeError(yPred, yTrue);
    }

}
