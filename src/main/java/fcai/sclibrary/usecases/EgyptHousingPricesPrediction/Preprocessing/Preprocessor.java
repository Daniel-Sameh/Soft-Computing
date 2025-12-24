// File: `src/main/java/fcai/sclibrary/usecases/EgyptHousingPricesPrediction/Preprocessing/Preprocessor.java`
package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing;

import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.ColumnType;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.DataRow;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.RawDataset;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Preprocessor {
    private PreprocessingState state;
    private List<ColumnType> schema;
    private int labelIndex;

    public Preprocessor(List<ColumnType> schema, int labelIndex) {
        this.schema = schema;
        this.labelIndex = labelIndex;
        this.state = new PreprocessingState();
    }

    public void fit(RawDataset dataset) {
        state.getFeatureNames().clear();

        for (int col = 0; col < schema.size(); col++) {
            // Label should NOT be part of featureNames / X
            if (col == labelIndex) {
                // For price (label), compute min/max of LOG-transformed values
                if (schema.get(col) == ColumnType.NUMERIC) {
                    double min = Double.MAX_VALUE;
                    double max = -Double.MAX_VALUE;
                    for (DataRow row : dataset.getRows()) {
                        double value = (double) row.get(col);
                        double logValue = Math.log(value + 1); // log transform
                        min = Math.min(min, logValue);
                        max = Math.max(max, logValue);
                    }
                    state.getMinValues().put(col, min);
                    state.getMaxValues().put(col, max);
                }
                continue;
            }

            if (schema.get(col) == ColumnType.NUMERIC) {
                double min = Double.MAX_VALUE;
                double max = -Double.MAX_VALUE;

                for (DataRow row : dataset.getRows()) {
                    double value = (double) row.get(col);
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                }

                state.getMinValues().put(col, min);
                state.getMaxValues().put(col, max);
                state.getFeatureNames().add(dataset.getHeaders().get(col));
            } else { // CATEGORICAL
                Map<String, Integer> encoding = new LinkedHashMap<>();
                int idx = 0;

                for (DataRow row : dataset.getRows()) {
                    String category = ((String) row.get(col)).trim().toLowerCase();
                    if (!encoding.containsKey(category)) {
                        encoding.put(category, idx++);
                    }
                }

                state.getCategoryMaps().put(col, encoding);
                state.getFeatureNames().add(dataset.getHeaders().get(col));
            }
        }
    }

    public ProcessedDataset transform(RawDataset dataset) {
        int rows = dataset.getRows().size();
        int featuresCount = schema.size() - 1; // exclude label
        double[][] X = new double[rows][featuresCount];
        double[][] y = new double[rows][1];

        for (int i = 0; i < rows; i++) {
            DataRow row = dataset.getRows().get(i);
            int featureIndex = 0;

            for (int col = 0; col < schema.size(); col++) {
                if (col == labelIndex) {
                    // Apply log transform then normalize for price label
                    double value = (double) row.get(col);
                    double logValue = Math.log(value + 1);
                    double min = state.getMinValues().get(col);
                    double max = state.getMaxValues().get(col);
                    double denom = (max - min);
                    double normalized = (denom == 0.0) ? 0.0 : (logValue - min) / denom;
                    y[i][0] = normalized;
                    continue;
                }

                if (schema.get(col) == ColumnType.NUMERIC) {
                    double value = (double) row.get(col);
                    double min = state.getMinValues().get(col);
                    double max = state.getMaxValues().get(col);
                    double denom = (max - min);
                    double normalized = (denom == 0.0) ? 0.0 : (value - min) / denom;
                    X[i][featureIndex++] = normalized;
                } else {
                    Map<String, Integer> encoding = state.getCategoryMaps().get(col);
                    String category = ((String) row.get(col)).trim().toLowerCase();
                    int categoryIndex = encoding.get(category);
                    // Normalize categorical encoding to [0, 1] range
                    int maxCategoryIndex = encoding.size() - 1;
                    double normalizedCategory = (maxCategoryIndex == 0) ? 0.0 : (double) categoryIndex / maxCategoryIndex;
                    X[i][featureIndex++] = normalizedCategory;
                }
            }
        }

        return new ProcessedDataset(X, y, state);
    }

    public Object deNormalization(double normalizedValue, int colIndex) {
        if (schema.get(colIndex) == ColumnType.NUMERIC) {
            double min = state.getMinValues().get(colIndex);
            double max = state.getMaxValues().get(colIndex);
            double logValue = normalizedValue * (max - min) + min;

            // If this is the label column (price), reverse the log transform
            if (colIndex == labelIndex) {
                return Math.exp(logValue) - 1;
            }
            return logValue;
        } else {
            Map<String, Integer> encoding = state.getCategoryMaps().get(colIndex);
            for (Map.Entry<String, Integer> entry : encoding.entrySet()) {
                if (entry.getValue() == (int) normalizedValue) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }
}
