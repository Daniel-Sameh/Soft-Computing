package fcai.sclibrary.nn.initialization;

import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.layers.DenseLayer;
import fcai.sclibrary.nn.layers.Layer;
import lombok.AllArgsConstructor;

import java.util.Random;

public class RandomUniform implements WeightInitializer{

    private double minVal, maxVal;
    private final Random rand;

    public RandomUniform(double minVal, double maxVal, long seed) {
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.rand =  new Random(seed);
    }
    public RandomUniform(double minVal, double maxVal) {
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.rand =  new Random(System.currentTimeMillis());
    }


    @Override
    public Layer initialize(Layer layer, boolean bias) {
        if (!(layer instanceof DenseLayer))
            return layer;

        DenseLayer denseLayer = (DenseLayer)layer;
        int in= denseLayer.getInputSize();
        int out= denseLayer.getOutputSize();

        double[][] w = new double[out][in];

        for (int i = 0; i < out; i++) {
            for (int j = 0; j < in; j++) {
                w[i][j]= rand.nextDouble() * (maxVal - minVal) + minVal;
            }
        }
        denseLayer.setWeights(new Tensor(w));

        if (bias){
            Tensor biases = new Tensor(1, out);
            double[][] b = biases.getData();
            for (int j = 0; j < out; j++) {
                b[0][j]= 0.0;
            }
            denseLayer.setBiases(biases);
        }

        return denseLayer;
    }
}
