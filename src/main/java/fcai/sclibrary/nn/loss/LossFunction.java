package fcai.sclibrary.nn.loss;

import fcai.sclibrary.nn.core.Tensor;

public interface LossFunction {
    double computeError(Tensor predicted, Tensor target);
}

