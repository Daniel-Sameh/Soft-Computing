package fcai.sclibrary.nn.optimizers;

import fcai.sclibrary.nn.core.Tensor;

public interface Optimizer {
    Tensor update(Tensor parameter, Tensor gradient);
}
