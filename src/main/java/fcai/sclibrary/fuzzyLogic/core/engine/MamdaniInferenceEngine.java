package fcai.sclibrary.fuzzyLogic.core.engine;

import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;
import fcai.sclibrary.fuzzyLogic.core.operators.StandardFuzzyOperators;
import lombok.Builder;

@Builder
public class MamdaniInferenceEngine implements InferenceEngine{
    @Builder.Default
    private FuzzyOperators fuzzyOperators = new StandardFuzzyOperators();


}
