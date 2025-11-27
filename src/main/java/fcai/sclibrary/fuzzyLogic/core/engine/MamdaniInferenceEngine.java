package fcai.sclibrary.fuzzyLogic.core.engine;

import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.consequents.Consequent;
import fcai.sclibrary.fuzzyLogic.core.consequents.MamdanniConsequent;
import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;
import fcai.sclibrary.fuzzyLogic.core.operators.StandardFuzzyOperators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MamdaniInferenceEngine implements InferenceEngine{
    @Builder.Default
    private FuzzyOperators fuzzyOperators = new StandardFuzzyOperators();

    private List<Rule> rules;

    @Override
    public List<FuzzyVariable> evaluate(List<FuzzyVariable> levelsOfMembership) {
        List<FuzzyVariable> result = new ArrayList<>();

        for (Rule<MamdanniConsequent> rule : rules) {
            if (!rule.isEnabled()) continue;

            double lastFuzzySetValue = 0.0;
            for(Rule.Antecedent antecedent : rule.getAntecedents()) {
                int idxVar = levelsOfMembership.indexOf(antecedent.getVar());
                if (idxVar == -1) continue;

                List<FuzzySet> fuzzySets = levelsOfMembership.get(idxVar).getFuzzySets();
                int idxSet = fuzzySets.indexOf(antecedent.getOutSet());
                if (idxSet == -1) continue;

                double val = fuzzySets.get(idxSet).getValue();

                switch (antecedent.getOp()) {
                    case NONE -> lastFuzzySetValue = val;
                    case AND -> lastFuzzySetValue = fuzzyOperators.AND(lastFuzzySetValue, val);
                    case OR -> lastFuzzySetValue = fuzzyOperators.OR(lastFuzzySetValue, val);
                }

            }

            for(MamdanniConsequent consequent : rule.getConsequences()){
                FuzzyVariable fuzzyVariable = consequent.getVar();
                List<FuzzySet> fuzzySets = fuzzyVariable.getFuzzySets();
                FuzzySet out = consequent.getOutSet();

                int idx = fuzzySets.indexOf(out);
                if(idx != -1){
                    // aggregate
                    double existing = fuzzySets.get(idx).getValue();
                    double aggregated = fuzzyOperators.OR(existing, lastFuzzySetValue);
                    fuzzySets.get(idx).setValue(aggregated);
                }
                fuzzyVariable.setFuzzySets(fuzzySets);

                int found = result.indexOf(fuzzyVariable);
                if(found == -1){
                    result.add(fuzzyVariable);
                }
            }
        }

        return result;
    }
}
