package fcai.sclibrary.fuzzyLogic.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FuzzyVariable {
    private List<FuzzySet> fuzzySets;
    private String name;
}
