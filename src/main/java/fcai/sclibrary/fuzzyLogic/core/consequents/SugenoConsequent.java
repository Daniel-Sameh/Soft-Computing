package fcai.sclibrary.fuzzyLogic.core.consequents;

import java.util.Map;

public class SugenoConsequent extends Consequent {
    private Map<String, Double> coefficients;
    private Double constant;

    public SugenoConsequent(String variableName, Map<String, Double> coefficients, Double constant) {
        this.setVariableName(variableName);
        this.coefficients = coefficients;
        this.constant = constant;
    }

    public double calculateZ(Map<String, Double> crispInputs) {
        double z = constant;
        for (Map.Entry<String, Double> entry : coefficients.entrySet()) {
            String inputName = entry.getKey();
            Double coeff = entry.getValue();

            // Get the actual crisp input value (e.g., Temp = 25)
            Double inputValue = crispInputs.getOrDefault(inputName, 0.0);
            z += (coeff * inputValue);
        }
        return z;
    }
}
