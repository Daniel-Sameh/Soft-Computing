package fcai.sclibrary.fuzzyLogic.core.engine;

import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;

import java.util.List;
import java.util.Map;

public interface InferenceEngine {
    public List<FuzzyVariable> evaluate(List<FuzzyVariable> levelsOfMembership);
}
