package fcai.sclibrary.fuzzyLogic.core.consequents;

import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MamdanniConsequent extends Consequent {
    private FuzzyVariable var;
    private FuzzySet outSet;

    public MamdanniConsequent(FuzzyVariable var, FuzzySet outSet) {
        this.var = var;
        this.outSet = outSet;
        this.setVariableName(var.getName());
    }

    @Override
    public void print(){
        System.out.println(" MamdanniConsequent: Variable = " + var.getName() + ", Set = " + outSet.getName());
    }
}
