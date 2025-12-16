package fcai.sclibrary.nn.activations;

public class Sigmoid implements ActivationFunction {
    public Sigmoid() {}

    @Override
    public double forward(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public double derivative(double x) {
        return forward(x) * (1 - forward(x));
    }
}
