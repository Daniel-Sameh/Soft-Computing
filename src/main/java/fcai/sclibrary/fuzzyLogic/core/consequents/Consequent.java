package fcai.sclibrary.fuzzyLogic.core.consequents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public abstract class Consequent {
    @Getter
    @Setter
    private String variableName;

    public abstract void print();
}
