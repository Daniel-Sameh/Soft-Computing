package fcai.sclibrary.fuzzyLogic.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FuzzySet {
    private String type;
    private String name;
    private List<Integer> indices;

    private Double value;

}
