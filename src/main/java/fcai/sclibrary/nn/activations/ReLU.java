package fcai.sclibrary.nn.activations;

public class ReLU implements ActivationFunction {
    public ReLU() {}

    @Override
    public double forward(double x) {
        return x < 0 ? 0.0 : x;
    }

    @Override
    public double derivative(double x) {
        return x < 0 ? 0.0 : 1.0;
    }
}
