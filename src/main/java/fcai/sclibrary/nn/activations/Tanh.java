package fcai.sclibrary.nn.activations;

public class Tanh implements ActivationFunction {
    public Tanh() {}

    @Override
    public double forward(double x) {
        return (2 / (1 + Math.exp(-2 * x))) - 1;
    }

    @Override
    public double derivative(double x) {
        return 1 - Math.pow(forward(x), 2);
    }
}
