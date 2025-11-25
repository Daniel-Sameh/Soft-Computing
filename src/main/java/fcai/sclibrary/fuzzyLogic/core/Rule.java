package fcai.sclibrary.fuzzyLogic.core;

import fcai.sclibrary.fuzzyLogic.core.consequents.Consequent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Rule<T extends Consequent> {
    public enum Operator {
        AND,
        OR,
        NONE
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class Antecedent {
        FuzzyVariable var;
        FuzzySet outSet;
        Operator op;
    }


    private List<Antecedent> antecedents;
    private List<T> consequences;


    private boolean enabled=true;
    public Rule(List<Antecedent> antecedents, List<T> consequences) {
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