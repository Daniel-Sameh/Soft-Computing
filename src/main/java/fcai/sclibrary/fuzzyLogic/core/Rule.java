package fcai.sclibrary.fuzzyLogic.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Rule {
    private String string;

    public enum Operator {
        AND,
        OR,
        NONE
    }

    @AllArgsConstructor
    @Getter
    public class Antecedent {
        FuzzyVariable var;
        FuzzySet outSet;
        Operator op;
    }

    @AllArgsConstructor
    @Getter
    public class Consequent {
        FuzzyVariable var;
        FuzzySet outSet;
    }

    private List<Antecedent> antecedents;
    private List<Consequent> consequences;

    private boolean enabled=true;

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