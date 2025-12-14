package fcai.sclibrary.fuzzyLogic.core.engine;

import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.consequents.SugenoConsequent;
import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;
import fcai.sclibrary.fuzzyLogic.core.operators.StandardFuzzyOperators;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SugenoInferenceEngine implements  InferenceEngine {
    @Builder.Default
    private FuzzyOperators fuzzyOperators = new StandardFuzzyOperators();
    private Map<String, Double> crispInputValues;
    private List<Rule> rules;

    @Override
    public Double evaluate(List<FuzzyVariable> levelsOfMembership) {
        double numerator = 0.0;
        double denominator = 0.0;

        for (Rule<SugenoConsequent> rule : rules) {
            double lastFuzzySetValue = 0;
            for (Rule.Antecedent antecedent : rule.getAntecedents()) {
                if (antecedent.getOp().name().equals("NONE")) {
                    int idxVar = levelsOfMembership.indexOf(antecedent.getVar());
                    if (idxVar != -1) {
                        List<FuzzySet> fuzzySets = levelsOfMembership.get(idxVar).getFuzzySets();
                        int idxSet = fuzzySets.indexOf(antecedent.getOutSet());
                        if (idxSet != -1) {
                            lastFuzzySetValue = fuzzySets.get(idxSet).getValue();
                        }
                    }
                } else if (antecedent.getOp().name().equals("AND")) {
                    int idxVar = levelsOfMembership.indexOf(antecedent.getVar());
                    if (idxVar != -1) {
                        List<FuzzySet> fuzzySets = levelsOfMembership.get(idxVar).getFuzzySets();
                        int idxSet = fuzzySets.indexOf(antecedent.getOutSet());
                        if (idxSet != -1) {
                            lastFuzzySetValue = fuzzyOperators.AND(lastFuzzySetValue, fuzzySets.get(idxSet).getValue());
                        }
                    }
                } else if (antecedent.getOp().name().equals("OR")) {
                    int idxVar = levelsOfMembership.indexOf(antecedent.getVar());
                    if (idxVar != -1) {
                        List<FuzzySet> fuzzySets = levelsOfMembership.get(idxVar).getFuzzySets();
                        int idxSet = fuzzySets.indexOf(antecedent.getOutSet());
                        if (idxSet != -1) {
                            lastFuzzySetValue = fuzzyOperators.OR(lastFuzzySetValue, fuzzySets.get(idxSet).getValue());
                        }
                    }
                }
            }

            for (SugenoConsequent consequent : rule.getConsequences()) {
                if (consequent != null) {
                    // Calculate z using the helper method we wrote
                    double z = ((SugenoConsequent) consequent).calculateZ(crispInputValues);

                    // Update Weighted Average Sums
                    numerator += (lastFuzzySetValue * z);
                    denominator += lastFuzzySetValue;
                }
            }
        }

        if (denominator == 0) return 0.0;

        return numerator / denominator;
    }
}
