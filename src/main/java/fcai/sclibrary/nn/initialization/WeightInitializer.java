package fcai.sclibrary.nn.initialization;

import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.layers.Layer;

public interface WeightInitializer {
    Layer initialize(Layer layer, boolean bias);
}
