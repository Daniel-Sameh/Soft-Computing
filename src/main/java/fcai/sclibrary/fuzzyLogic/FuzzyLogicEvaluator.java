package fcai.sclibrary.fuzzyLogic;

import fcai.sclibrary.fuzzyLogic.core.Fuzzifier;
import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.defuzzification.Defuzzifier;
import fcai.sclibrary.fuzzyLogic.core.defuzzification.WeightedAvgDefuzzifier;
import fcai.sclibrary.fuzzyLogic.core.engine.InferenceEngine;
import fcai.sclibrary.fuzzyLogic.core.engine.MamdaniInferenceEngine;
import fcai.sclibrary.fuzzyLogic.core.operators.FuzzyOperators;
import fcai.sclibrary.fuzzyLogic.core.operators.StandardFuzzyOperators;
import lombok.Builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


@Builder
public class FuzzyLogicEvaluator {
    private List<Rule> rules;
    private List<FuzzyVariable> variables;
    private Map<String, FuzzyVariable> variableMap = new HashMap<>();
    @Builder.Default private FuzzyOperators operators = new StandardFuzzyOperators();

    @Builder.Default private Fuzzifier fuzzifier = new Fuzzifier();
    @Builder.Default private InferenceEngine inferenceEngine = new MamdaniInferenceEngine();
    @Builder.Default private Defuzzifier defuzzifier = new WeightedAvgDefuzzifier();


    public void create(String rule){
        if (rules==null){
            rules=new ArrayList<>();
        }
        final Rule[] currentRule = {null};
        final Rule.Antecedent[] curAntecedent = {null};
        final Rule.Consequent[] curConsequent = {null};
        final Rule.Operator[] curOperator = {null};
        List<Rule.Antecedent> antecedents = new ArrayList<>();
        List<Rule.Consequent> consequents = new ArrayList<>();
        try (Stream<String> lines = rule.lines()) {
            lines.forEach(line -> {

                String[] strings= line.split(" ");
                if (strings.length==0 || strings[0].isEmpty() || strings[0].charAt(0) == '#'){
                    return;
                }

                if (strings.length<4 || !strings[2].equalsIgnoreCase("IS")){
                    throw new IllegalArgumentException("Invalid rule format");
                }

                switch (strings[0].toUpperCase()) {
                    case "IF" -> curOperator[0] = Rule.Operator.NONE;
                    case "AND" -> curOperator[0] = Rule.Operator.AND;
                    case "OR" -> curOperator[0] = Rule.Operator.OR;
                    case "THEN" -> {
                        // add last antecedent to rule
                        if (!antecedents.isEmpty()) {
//                            antecedents.add(curAntecedent[0]);
                            currentRule[0] = new Rule();
                            currentRule[0].setAntecedents(new ArrayList<>(antecedents));
                        }
                        // create new consequent
                        String varName = strings[1];
                        String varSet = strings[3];
                        FuzzyVariable var = getVariable(varName);
                        FuzzySet set = getFuzzySet(varSet, var);

                        curConsequent[0] = new Rule().new Consequent(var, set);
                        // add consequent to rule and add rule to rules list
                        consequents.add(curConsequent[0]);

                        assert currentRule[0] != null;
                        currentRule[0].setConsequences(new ArrayList<>(consequents));

                        currentRule[0].setString(rule);

                        rules.add(currentRule[0]);

                        // reset for next rule
                        curAntecedent[0] = null;
                        antecedents.clear();
                        consequents.clear();

                        return;
                    }
                    // Other cases are invalid
                    default -> throw new IllegalArgumentException("Invalid rule format");
                }

                String varName = strings[1];
                String varSet = strings[3];
                FuzzyVariable var= getVariable(varName);
                FuzzySet set=getFuzzySet(varSet, var);

                curAntecedent[0] = new Rule().new Antecedent(var, set, curOperator[0]);
                antecedents.add(curAntecedent[0]);
            });

        }
    }
    public void edit(Integer ruleId, String newRule){
        create(newRule);
        Rule rule = rules.getLast();
        rules.set(ruleId, rule);
        rules.removeLast();
    }
    public void enable(Integer ruleId){
        rules.get(ruleId).setEnabled(true);
    }
    public void disable(Integer ruleId){
        rules.get(ruleId).setEnabled(false);
    }

    public void setVariables(List<FuzzyVariable> variables) {
        this.variables = variables;
        this.variableMap = new HashMap<>();
        for (FuzzyVariable var : variables) {
            this.variableMap.put(var.getName(), var);
        }
    }

    public void createAllRules(String filePath){

        StringBuilder curRuleString = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                String[] strings= line.split(" ");
                if (strings.length==0 || strings[0].isEmpty() || strings[0].charAt(0) == '#'){
                    return;
                }

                if (strings.length<4 || !Objects.equals(strings[2], "IS")){
                    throw new IllegalArgumentException("Invalid rule format");
                }

                curRuleString.append(line).append("\n");

                if (strings[0].equals("THEN")){
                    this.create(curRuleString.toString().trim());
                    curRuleString.setLength(0);
                }

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            System.out.println("Rule "+(i + 1)+":");
            for (Rule.Antecedent antecedent : rule.getAntecedents()) {
                System.out.println(" Antecedent: "+antecedent.getVar().getName() + " IS " + antecedent.getOutSet().getName() + " OP: " + antecedent.getOp());
            }
            for (Rule.Consequent consequent : rule.getConsequences()) {
                System.out.println(" Consequent: " + consequent.getVar().getName()+" IS " +consequent.getOutSet().getName());
            }
            System.out.println(" Full Rule: \n\""+rule.getString()+"\"\n");
        }
    }

    public double evaluate(Map<FuzzyVariable, Double> inputs){
        /// TO BE IMPLEMENTED
        /// Fuzzify
        fuzzifier.Fuzzify(inputs);
        /// Inference
        inferenceEngine.evaluate(variables);
        /// Aggregate
        /// Defuzzify
        return 0.0;
    }

    private FuzzyVariable getVariable(String varName){
        if (!variableMap.containsKey(varName)) {
            throw new IllegalArgumentException("Variable " + varName + " not found in map. Available: " + variableMap.keySet());
        }
        return variableMap.get(varName);
    }

    private FuzzySet getFuzzySet(String varSet, FuzzyVariable var){
        return var.getFuzzySets().stream()
                .filter(fs -> fs.getName().equalsIgnoreCase(varSet))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Set " + varSet + " not found in variable " + var.getName()));
    }
}
