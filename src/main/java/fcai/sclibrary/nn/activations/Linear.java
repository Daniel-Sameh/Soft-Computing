package fcai.sclibrary.nn.activations;

public class Linear implements ActivationFunction {
    public Linear() {}

    @Override
    public double forward(double x) {
        return x;
    }

    @Override
    public double derivative(double x) {
        return 1;
    }
}
