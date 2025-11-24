package fcai.sclibrary.fuzzyLogic;

import fcai.sclibrary.fuzzyLogic.core.Fuzzifier;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.defuzzification.Defuzzifier;
import fcai.sclibrary.fuzzyLogic.core.defuzzification.WeightedAvgDefuzzifier;
import fcai.sclibrary.fuzzyLogic.core.engine.InferenceEngine;
import fcai.sclibrary.fuzzyLogic.core.engine.MamdaniInferenceEngine;
import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;
import fcai.sclibrary.fuzzyLogic.core.operators.StandardFuzzyOperators;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public class FuzzyLogicEvaluator {
    private List<Rule> rules;
    private List<FuzzyVariable> variables;
    @Builder.Default private FuzzyOperators operators = new StandardFuzzyOperators();

    @Builder.Default private Fuzzifier fuzzifier = new Fuzzifier();
    @Builder.Default private InferenceEngine inferenceEngine = new MamdaniInferenceEngine();
    @Builder.Default private Defuzzifier defuzzifier = new WeightedAvgDefuzzifier();


    public void create(Rule rule){}
    public void edit(String ruleId, Rule newRule){}
    public void enable(String ruleId){}
    public void disable(String ruleId){}

    public void createAllRules(String filePath){
        /// TO BE IMPLEMENTED (Parsing from file)
    }

    public double evaluate(Map<FuzzyVariable, Double> inputs){
        /// TO BE IMPLEMENTED
        /// Fuzzify
        /// Inference
        /// Aggregate
        /// Defuzzify
        return 0.0;
    }
}
