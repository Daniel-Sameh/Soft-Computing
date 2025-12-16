package fcai.sclibrary.nn.layers;

import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.optimizers.Optimizer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InputLayer implements Layer{
    private Tensor inputData;
    private Tensor outputData;

    private int inputSize;
    private int outputSize;

    public InputLayer(int inputSize){
        this.inputSize = inputSize;
        this.outputSize = inputSize;
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
        return inputData;
    }

    @Override
    public Tensor getOutputData() {
        return outputData;
    }

    @Override
    public void build(int inputSize) {
        this.inputSize = inputSize;
    }
}
