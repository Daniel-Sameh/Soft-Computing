package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreprocessingState {

    // Column index → category → encoded index
    private Map<Integer, Map<String, Integer>> categoryMaps = new HashMap<>();

    // Column index → min, max
    private Map<Integer, Double> minValues = new HashMap<>();
    private Map<Integer, Double> maxValues = new HashMap<>();

    // Final feature ordering
    private List<String> featureNames = new ArrayList<>();

    public Map<Integer, Map<String, Integer>> getCategoryMaps() {
        return categoryMaps;
    }

    public Map<Integer, Double> getMinValues() {
        return minValues;
    }

    public Map<Integer, Double> getMaxValues() {
        return maxValues;
    }

    public List<String> getFeatureNames() {
        return featureNames;
    }
}
