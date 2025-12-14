package fcai.sclibrary.fuzzyLogic.core.engine;

import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;

import java.util.List;

public interface InferenceEngine {
    public Object evaluate(List<FuzzyVariable> levelsOfMembership);
    public void setRules(List<Rule> rules);
    public void setFuzzyOperators(FuzzyOperators fuzzyOperators);
}
