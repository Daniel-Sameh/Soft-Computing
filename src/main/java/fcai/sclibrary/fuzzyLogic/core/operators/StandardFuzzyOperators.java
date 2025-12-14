package fcai.sclibrary.fuzzyLogic.core.operators;

public class StandardFuzzyOperators implements  FuzzyOperators {

    @Override
    public double AND(double a, double b) {
        return Math.min(a, b);
    }

    @Override
    public double OR(double a, double b) {
        return Math.max(a, b);
    }
}
