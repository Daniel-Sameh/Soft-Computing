package fcai.sclibrary.fuzzyLogic.core.engine;

import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;
import fcai.sclibrary.fuzzyLogic.core.operators.StandardFuzzyOperators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MamdaniInferenceEngine implements InferenceEngine{
    @Builder.Default
    private FuzzyOperators fuzzyOperators = new StandardFuzzyOperators();

    private List<Rule> rules;

    @Override
    public List<Double> evaluate(List<FuzzyVariable> levelsOfMembership) {
        return List.of();
    }
}
