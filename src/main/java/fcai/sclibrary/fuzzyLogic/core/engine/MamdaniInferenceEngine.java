package fcai.sclibrary.fuzzyLogic.core.engine;

import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.consequents.Consequent;
import fcai.sclibrary.fuzzyLogic.core.consequents.MamdanniConsequent;
import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;
import fcai.sclibrary.fuzzyLogic.core.operators.StandardFuzzyOperators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MamdaniInferenceEngine{
    @Builder.Default
    private FuzzyOperators fuzzyOperators = new StandardFuzzyOperators();

    private List<Rule> rules;

    public List<FuzzyVariable> evaluate(List<FuzzyVariable> levelsOfMembership) {
        List<FuzzyVariable> result = new ArrayList<>();

        for (Rule<MamdanniConsequent> rule : rules) {
            double lastFuzzySetValue = 0;
            for(Rule.Antecedent antecedent : rule.getAntecedents()) {
                if(antecedent.getOp().name().equals("NONE")){
                    int idxVar = levelsOfMembership.indexOf(antecedent.getVar());
                    if(idxVar != -1){
                        List<FuzzySet> fuzzySets = levelsOfMembership.get(idxVar).getFuzzySets();
                        int idxSet = fuzzySets.indexOf(antecedent.getOutSet());
                        if(idxSet != -1){
                            lastFuzzySetValue = fuzzySets.get(idxSet).getValue();
                        }
                    }
                }
                else if(antecedent.getOp().name().equals("AND")) {
                    int idxVar = levelsOfMembership.indexOf(antecedent.getVar());
                    if (idxVar != -1) {
                        List<FuzzySet> fuzzySets = levelsOfMembership.get(idxVar).getFuzzySets();
                        int idxSet = fuzzySets.indexOf(antecedent.getOutSet());
                        if (idxSet != -1) {
                            lastFuzzySetValue = fuzzyOperators.AND(lastFuzzySetValue, fuzzySets.get(idxSet).getValue());
                        }
                    }
                }
                else if(antecedent.getOp().name().equals("OR")) {
                    int idxVar = levelsOfMembership.indexOf(antecedent.getVar());
                    if(idxVar != -1){
                        List<FuzzySet> fuzzySets = levelsOfMembership.get(idxVar).getFuzzySets();
                        int idxSet = fuzzySets.indexOf(antecedent.getOutSet());
                        if(idxSet != -1){
                            lastFuzzySetValue = fuzzyOperators.OR(lastFuzzySetValue, fuzzySets.get(idxSet).getValue());
                        }
                    }
                }
            }

            for(MamdanniConsequent consequent : rule.getConsequences()){
                FuzzyVariable fuzzyVariable = consequent.getVar();
                List<FuzzySet> fuzzySets = consequent.getVar().getFuzzySets();
                FuzzySet fuzzySet = consequent.getOutSet();
                fuzzySet.setValue(lastFuzzySetValue);
                int idx = fuzzySets.indexOf(fuzzySet);
                if(idx != -1){
                    fuzzySets.set(idx, fuzzySet);
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
