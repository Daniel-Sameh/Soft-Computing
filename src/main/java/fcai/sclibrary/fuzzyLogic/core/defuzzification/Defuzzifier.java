package fcai.sclibrary.fuzzyLogic.core.defuzzification;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;

public interface Defuzzifier {
    double defuzzify(FuzzyVariable outputVariable);
}