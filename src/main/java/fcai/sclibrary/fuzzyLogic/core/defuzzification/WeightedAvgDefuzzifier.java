package fcai.sclibrary.fuzzyLogic.core.defuzzification;


import fcai.sclibrary.fuzzyLogic.core.FuzzySet;
import fcai.sclibrary.fuzzyLogic.core.FuzzyVariable;
import java.util.ArrayList;
import java.util.List;

public class WeightedAvgDefuzzifier implements Defuzzifier{
    @Override
    public double defuzzify(FuzzyVariable outputVariable){
        double num=0.0;
        double dem=0.0;
        for(FuzzySet fs:outputVariable.getFuzzySets()){
            double membership = fs.getValue();
            if (membership == 0.0) continue;
            double sum=0.0;
            for(double x:fs.getIndices()){
                sum+=x;
            }
            double centroid=sum/(fs.getIndices().size());
            num+=(centroid*membership);
            dem+=membership;
        }
        if(dem==0.0) return 0;
        return num/dem;
    }
}
