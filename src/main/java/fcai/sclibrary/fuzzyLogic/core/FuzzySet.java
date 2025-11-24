package fcai.sclibrary.fuzzyLogic.core;

import java.security.KeyPair;
import java.util.List;
import java.util.Map;

public class FuzzySet {
    private String type;
    private String name;
    private class Range{
        private Integer start, end;
    }
    private class Line{
        private Double slope, intercept;
    }
    private Map<Range, Line> linesEquations;
    private List<Integer> indices;
}
