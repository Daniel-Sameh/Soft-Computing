package fcai.sclibrary.fuzzyLogic.core;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private enum Operator {
        AND,
        OR,
        NONE
    }
    private class Antecedent {
        FuzzyVariable var;
        FuzzySet outSet;
        Operator op;
    }
    private class Consequent {
        FuzzyVariable var;
        FuzzySet outSet;
    }
    private List<Antecedent> antecedents;
    private List<Consequent> consequences;

    public Rule(String ruleText) {
        antecedents = new ArrayList<Antecedent>();
        consequences = new ArrayList<Consequent>();


    }

}
