package fcai.sclibrary.fuzzyLogic.core.defuzzification;
import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import java.util.ArrayList;
import java.util.List;

public class MeanMaxDefuzzifier implements Defuzzifier {
    @Override
    public double defuzzify(FuzzyVariable outputVariable) {
        double MaxNum = -1;
        List<FuzzySet> maxSets = new ArrayList<>();
        for (FuzzySet set : outputVariable.getFuzzySets()) {
            if (set.getValue() != null) {
                if (set.getValue() > MaxNum) {
                    MaxNum = set.getValue();
                    maxSets.clear();
                    maxSets.add(set);
                } else if (set.getValue() == MaxNum) {
                    maxSets.add(set);
                }
            }
        }
        FuzzySet selectedSet = maxSets.get(maxSets.size() / 2);
        //////////////////////////////////////////////////////////////////
        //awl 7aga hn3ml add kol el arkam el leha el max membership ll set wa b3dha na5od el middle bt3hom lw kaza w7da
        //////////////////////////////////////////////////////////////////
        List<Double> indices = selectedSet.getIndices();
        if (indices == null || indices.size() < 3) return 0.0;
        double y=selectedSet.getValue();
        double x1,x2;
        if (indices.size()==3) {
            double a=indices.get(0);
            double b=indices.get(1);
            double c=indices.get(2);
            x1 =a+(b-a)*y;
            x2 =c-(c-b)*y;
        } else {
            double a=indices.get(0);
            double b=indices.get(1);
            double c=indices.get(2);
            double d=indices.get(3);
            x1=a+(b-a)*y;
            x2=d-(d-c)*y;
        }
        if(x1==0){
            return x2;
        }
        else if(x2==0){
            return x1;
        }
        else{
            return (x1 + x2)/2.0;
        }
    }
}