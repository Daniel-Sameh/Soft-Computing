package fcai.sclibrary.nn.layers;

import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.optimizers.Optimizer;
import lombok.Getter;

@Getter
public class InputLayer implements Layer {

    private Tensor input;
    private int inputSize;

    public InputLayer(int inputSize) {
        this.inputSize = inputSize;
    }

    @Override
    public Tensor forward(Tensor input) {
        this.input = input;
        return input;
    }

    @Override
    public Tensor backward(Tensor gradOutput) {
        return gradOutput;
    }

    @Override
    public void updateParameters(Optimizer optimizer) {
        // No parameters to update
        return;
    }

    @Override
    public Tensor getInputData() {
        return input;
    }

    @Override
    public Tensor getOutputData() {
        return null;
    }

    @Override
    public void build(int inputSize) {
        this.inputSize = inputSize;
    }
}