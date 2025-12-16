package fcai.sclibrary.nn.layers;

import fcai.sclibrary.nn.activations.ActivationFunction;
import fcai.sclibrary.nn.activations.Sigmoid;
import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.initialization.WeightInitializer;
import fcai.sclibrary.nn.optimizers.Optimizer;

public class DenseLayer implements Layer {

    private Tensor input;
    private Tensor output;

    private int inputSize;
    private int outputSize;

    private Tensor weights;
    private Tensor biases;

    private Tensor weightGradients;
    private Tensor biasGradients;
    private Tensor z;

    private ActivationFunction activationFunction;

    public DenseLayer(int neurons, ActivationFunction activationFunction) {
        this.outputSize = neurons;
        this.activationFunction = activationFunction;
    }
    public DenseLayer(int neurons){
        this.outputSize = neurons;
    }
    public DenseLayer(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }
    public DenseLayer(int inputSize, int outputSize, ActivationFunction activationFunction) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activationFunction = activationFunction;
    }

    public Tensor forward(Tensor input) {
        this.input = input;

        Tensor result = input.dot(weights).add(biases);
        double[][] data = result.getData();
        this.z = result;
        for (int i = 0; i < result.getRows(); i++) {
            for (int j = 0; j < result.getCols(); j++) {
                data[i][j] = activationFunction.forward(data[i][j]);
            }
        }
        result.setData(data);
        this.output = result;
        return result;
    }

    public Tensor backward(Tensor gradOutput) {
        int rows = gradOutput.getRows();
        int cols = gradOutput.getCols();

        // ============================
        // 1) dL/dZ = dL/dA ⊙ f'(Z)
        // ============================
        double[][] gradZData = new double[rows][cols];
        double[][] gradOutData = gradOutput.getData();
        double[][] zData = z.getData();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gradZData[i][j] =
                        gradOutData[i][j] *
                                activationFunction.derivative(zData[i][j]);
            }
        }

        Tensor gradZ = new Tensor(gradZData, rows, cols);

        // ============================
        // 2) dL/dW = Xᵀ · dL/dZ
        // ============================
        weightGradients = input.transpose().dot(gradZ);

        // ============================
        // 3) dL/db = sum(dL/dZ)
        // ============================
        int batchSize = gradZ.getRows();
        int outSize = gradZ.getCols();

        double[][] biasGradData = new double[1][outSize];
        double[][] gradZDataBias = gradZ.getData();

        for (int i = 0; i < batchSize; i++) {
            for (int j = 0; j < outSize; j++) {
                biasGradData[0][j] += gradZDataBias[i][j];
            }
        }

        biasGradients = new Tensor(biasGradData, 1, outSize);// (1 × outputSize)

        // ============================
        // 4) dL/dX = dL/dZ · Wᵀ
        // ============================
        return gradZ.dot(weights.transpose());
    }

    @Override
    public void updateParameters(Optimizer optimizer) {
        if (weightGradients == null || biasGradients == null) {
            return; // backward not called yet
        }

        weights = optimizer.update(weights, weightGradients);
        biases  = optimizer.update(biases, biasGradients);
    }

    @Override
    public Tensor getInputData() {
        return input;
    }

    @Override
    public Tensor getOutputData() {
        return output;
    }

    @Override
    public void build(int inputSize) {
        this.inputSize = inputSize;
    }
}
