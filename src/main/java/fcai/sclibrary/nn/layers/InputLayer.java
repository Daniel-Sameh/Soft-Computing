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
}
