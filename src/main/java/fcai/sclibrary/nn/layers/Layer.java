package fcai.sclibrary.nn.layers;

import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.optimizers.Optimizer;

public interface Layer {
    Tensor forward(Tensor input);
    Tensor backward(Tensor gradOutput);
    void updateParameters(Optimizer optimizer);
    Tensor getInputData();
    Tensor getOutputData();
    void build(int inputSize);
}
