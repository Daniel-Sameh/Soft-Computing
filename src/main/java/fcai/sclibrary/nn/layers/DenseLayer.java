package fcai.sclibrary.nn.layers;

import fcai.sclibrary.nn.activations.ActivationFunction;
import fcai.sclibrary.nn.activations.Sigmoid;
import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.optimizers.Optimizer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DenseLayer implements Layer {

    private Tensor input;
    private Tensor output;

    private int inputSize;
    private int outputSize;

    private Tensor weights;
    private Tensor biases;

    private ActivationFunction activationFunction= new Sigmoid();

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

    @Override
    public Tensor forward(Tensor input) {
        return null;
    }

    @Override
    public Tensor backward(Tensor gradOutput) {
        return null;
    }

    @Override
    public void updateParameters(Optimizer optimizer) {

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
