package fcai.sclibrary.nn.activations;

public interface ActivationFunction {
    double forward(double x);
    double derivative(double x);
}