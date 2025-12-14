package fcai.sclibrary.fuzzyLogic.core.operators;

public class ProductFuzzyOperators implements  FuzzyOperators {
    @Override
    public double AND(double a, double b) {
        return a * b;
    }

    @Override
    public double OR(double a, double b) {
        return a + b - (a * b);
    }
}
