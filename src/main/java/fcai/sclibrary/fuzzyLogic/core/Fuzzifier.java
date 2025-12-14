package fcai.sclibrary.fuzzyLogic.core;

import java.util.List;
import java.util.Map;

public class Fuzzifier {
    public Fuzzifier() {}

    public void Fuzzify(Map<FuzzyVariable, Double> inputs){
        for (Map.Entry<FuzzyVariable, Double> entry : inputs.entrySet()) {
            FuzzyVariable variable = entry.getKey();
            Double value = entry.getValue();
            List<FuzzySet> fuzzySets = variable.getFuzzySets();
            for (FuzzySet fuzzySet : fuzzySets) {
                double res = calcMembership(fuzzySet, value);
                fuzzySet.setValue(res);
            }
        }
    }

    private double calcMembership(FuzzySet fuzzySet, double x) {
        List<Double> points = fuzzySet.getIndices();
        int n = points.size();
        double a = points.get(0);
        double b = points.get(1);
        double c = points.get(2);
        double d = (n == 4 ? points.get(3) : points.get(2));

        if (x <= a || x >= d) {
            return 0.0;
        }

        if (x > a && x < b) {
            if (b == a) return 0;
            return (x - a) / (b - a);
        }

        if (n == 3) {
            if (x == b) return 1.0;
        } else {
            if (x >= b && x <= c) return 1.0;
        }

        if (x > c && x < d) {
            if (d == c) return 0;
            return ((c - x) / (d - c)) + 1.0;
        }

        return 0.0;
    }

}
