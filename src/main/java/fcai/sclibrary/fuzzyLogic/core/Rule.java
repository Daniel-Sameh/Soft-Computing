package fcai.sclibrary.fuzzyLogic.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

    public Rule(List<Antecedent> antecedents, List<Consequent> consequences) {
        this.antecedents = antecedents;
        this.consequences = consequences;
    }


}

/*
IF <FuzzyVariable1> IS <FuzzySet1>
[AND/OR <FuzzyVariable2> IS <FuzzySet2> ...]
THEN <FuzzyVariable3> IS <FuzzySet3>
[AND <FuzzyVariable4> IS <FuzzySet4> ...]
 */