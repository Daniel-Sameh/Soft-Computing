package fcai.sclibrary.nn.initialization;

import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.layers.DenseLayer;
import fcai.sclibrary.nn.layers.Layer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
public class Xavier implements WeightInitializer{

    private boolean applyHe=false;
    private final Random rand;

    public Xavier(boolean applyHe, long seed) {
        this.applyHe = applyHe;
        this.rand = new Random(seed);
    }
    public  Xavier(boolean applyHe) {
        this.applyHe = applyHe;
        this.rand = new Random(System.currentTimeMillis());
    }
    public Xavier(){
        this.rand = new Random(System.currentTimeMillis());
    }

    @Override
    public Layer initialize(Layer layer, boolean bias) {
        if (!(layer instanceof DenseLayer))
            return layer;

        DenseLayer dense = (DenseLayer) layer;
        int in = dense.getInputSize();
        int out = dense.getOutputSize();

        double limit;
        if (applyHe) {
            // He uniform
            limit = Math.sqrt(6.0 / in);
        } else {
            // Xavier uniform
            limit = Math.sqrt(6.0 / (in + out));
        }

        double[][] w = new double[out][in];
        // Range is [-limit, limit]
        for (int i = 0; i < out; i++) {
            for (int j = 0; j < in; j++) {
                w[i][j] = (rand.nextDouble() * 2 * limit)-limit;
            }
        }
        dense.setWeights(new Tensor(w));

        if (bias){
            double[][] b = new double[1][out];
            for (int j = 0; j < out; j++) {
                b[0][j]= 0.0;
            }
            dense.setBiases(new Tensor(b));
        }

        return dense;
    }
}
