package fcai.sclibrary.fuzzyLogic;

import fcai.sclibrary.fuzzyLogic.core.Fuzzifier;
import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import fcai.sclibrary.fuzzyLogic.core.Rule;
import fcai.sclibrary.fuzzyLogic.core.consequents.Consequent;
import fcai.sclibrary.fuzzyLogic.core.consequents.MamdanniConsequent;
import fcai.sclibrary.fuzzyLogic.core.consequents.SugenoConsequent;
import fcai.sclibrary.fuzzyLogic.core.defuzzification.Defuzzifier;
import fcai.sclibrary.fuzzyLogic.core.defuzzification.WeightedAvgDefuzzifier;
import fcai.sclibrary.fuzzyLogic.core.engine.InferenceEngine;
import fcai.sclibrary.fuzzyLogic.core.engine.MamdaniInferenceEngine;
import fcai.sclibrary.fuzzyLogic.core.engine.SugenoInferenceEngine;
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
//    private final MamdaniInferenceEngine mamdaniInferenceEngine = new MamdaniInferenceEngine();
//    private final SugenoInferenceEngine sugenoInferenceEngine = new SugenoInferenceEngine();
    @Builder.Default private InferenceEngine inferenceEngine = new MamdaniInferenceEngine();
    @Builder.Default private Defuzzifier defuzzifier = new WeightedAvgDefuzzifier();


    public void create(String rule){
        if (rules==null){
            rules=new ArrayList<>();
        }
        final Rule[] currentRule = {null};
        final Rule.Antecedent[] curAntecedent = {null};
        final Consequent[] curConsequent = {null};
        final Rule.Operator[] curOperator = {null};
        List<Rule.Antecedent> antecedents = new ArrayList<>();
        List<Consequent> consequents = new ArrayList<>();
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
                            currentRule[0] = new Rule();
                            currentRule[0].setAntecedents(new ArrayList<>(antecedents));
                        }
                        // create new consequent
                        String varName = strings[1];
                        String varSet = strings[3];
                        FuzzyVariable var = getVariable(varName);
                        FuzzySet set = getFuzzySet(varSet, var);

                        curConsequent[0] = new MamdanniConsequent(var, set);
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

    public void createSugeno(String rule){
        if (rules==null){
            rules=new ArrayList<>();
        }
        final Rule[] currentRule = {null};
        final Rule.Antecedent[] curAntecedent = {null};
        final SugenoConsequent[] curConsequent = {null};
        final Rule.Operator[] curOperator = {null};
        List<Rule.Antecedent> antecedents = new ArrayList<>();
        List<SugenoConsequent> consequents = new ArrayList<>();
        try (Stream<String> lines = rule.lines()) {
            lines.forEach(line -> {

                String[] strings= line.split(" ");
                if (strings.length==0 || strings[0].isEmpty() || strings[0].charAt(0) == '#'){
                    return;
                }

                if (strings.length<4 || (!strings[0].equalsIgnoreCase("THEN") && !Objects.equals(strings[2], "IS"))){
                    throw new IllegalArgumentException("Invalid rule format, line: "+line);
                }

                switch (strings[0].toUpperCase()) {
                    case "IF" -> curOperator[0] = Rule.Operator.NONE;
                    case "AND" -> curOperator[0] = Rule.Operator.AND;
                    case "OR" -> curOperator[0] = Rule.Operator.OR;
                    case "THEN" -> {
                        // add last antecedent to rule
                        if (!antecedents.isEmpty()) {
                            currentRule[0] = new Rule();
                            currentRule[0].setAntecedents(new ArrayList<>(antecedents));
                        }
                        // create new Sugeno consequent
                        String outVar = strings[1];
                        if (!strings[2].equals("="))
                            throw new IllegalArgumentException("Invalid rule format, expected '=' after output variable in Sugeno rule");

                        Map<String, Double> coefficients = new HashMap<>();
                        // X = a*Var1 + b*Var2 +...
                        // Coefficients are Var1->a, Var2->b, ...
                        Double constant = 0.0;
                        if (strings.length==4){ // THEN X = c
                            constant = Double.parseDouble(strings[3]);
                        }else{ // THEN X = a*Var1 + b*Var2 + ...
                            int sign = 1;
                            for (int i = 3; i < strings.length; i++) {
                                String tmp=strings[i];
                                if (tmp.equals("+")){
                                    sign = 1;
                                    continue;
                                }else if (tmp.equals("-")){
                                    sign = -1;
                                    continue;
                                }
                                String[] parts = tmp.split("\\*");
                                System.out.println(" Parts "+ i+": "+Arrays.toString(parts));
                                if (parts.length==1){
                                    coefficients.put(parts[0], sign*1.0);
                                }else{
                                    try {
                                        coefficients.put(parts[1], sign*Double.parseDouble(parts[0]));
                                    }catch (NumberFormatException e){
                                        throw new IllegalArgumentException("Invalid coefficient format in Sugeno rule");
                                    }
                                }
                            }
                        }

                        SugenoConsequent cons = new SugenoConsequent(outVar, coefficients, constant);
                        curConsequent[0] = cons;
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
        createAllRules(filePath, "Mamdani");
    }
    public void createAllRules(String filePath, String ruleType){
        StringBuilder curRuleString = new StringBuilder();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                String[] strings= line.split(" ");
                if (strings.length==0 || strings[0].isEmpty() || strings[0].charAt(0) == '#'){
                    return;
                }

                if (strings.length<4 || (!strings[0].equalsIgnoreCase("THEN") && !Objects.equals(strings[2], "IS"))){
                    throw new IllegalArgumentException("Invalid rule format, line: "+line);
                }

                curRuleString.append(line).append("\n");

                if (strings[0].equals("THEN")){
                    if (ruleType.equalsIgnoreCase("Mamdani"))
                        this.create(curRuleString.toString().trim());
                    else if (ruleType.equalsIgnoreCase("Sugeno"))
                        this.createSugeno(curRuleString.toString().trim());
                    else throw new IllegalArgumentException("Invalid inference/rule type (Mamdani/Sugeno)");

                    curRuleString.setLength(0);
                }

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        inferenceEngine.setRules(this.rules);
        inferenceEngine.setFuzzyOperators(this.operators);

        // Print all rules
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            System.out.println("Rule "+(i + 1)+":");
            for (Object ant : rule.getAntecedents()) {
                Rule.Antecedent antecedent = (Rule.Antecedent) ant;
                System.out.println(" Antecedent: "+antecedent.getVar().getName() + " IS " + antecedent.getOutSet().getName() + " OP: " + antecedent.getOp());
            }
            for (int j = 0; j < rule.getConsequences().toArray().length; j++) {
                Consequent cons = (Consequent) rule.getConsequences().toArray()[j];
                cons.print();
            }
            System.out.println(" Full Rule: \n\""+rule.getString()+"\"\n");
        }
    }
    public double evaluate(Map<FuzzyVariable, Double> inputs){
        return evaluate(inputs, "Mamdani");
    }
    public double evaluate(Map<FuzzyVariable, Double> inputs, String inferenceType){
        // Reset all variables
        for (FuzzyVariable var : variables) {
            for (FuzzySet set : var.getFuzzySets()) {
                set.setValue(0.0);
            }
        }

        // Fuzzify
        fuzzifier.Fuzzify(inputs);

        // Inference and Aggregation
        List<FuzzyVariable> inferenceOutput= new ArrayList<>();
        if (inferenceType.equalsIgnoreCase("Mamdani")){
            inferenceOutput= (List<FuzzyVariable>)inferenceEngine.evaluate(variables);

        }else if (inferenceType.equalsIgnoreCase("Sugeno")){
            // Making input map for sugeno engine
            Map<String, Double> crispInputs = new HashMap<>();
            for (Map.Entry<FuzzyVariable, Double> entry : inputs.entrySet()) {
                crispInputs.put(entry.getKey().getName(), entry.getValue());
            }
            SugenoInferenceEngine engine = (SugenoInferenceEngine) inferenceEngine;
            engine.setCrispInputValues(crispInputs);

            // Sugeno returns the final output directly
            return (double)engine.evaluate(variables);
        }

        // Defuzzify
        return defuzzifier.defuzzify(inferenceOutput.getLast());
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
