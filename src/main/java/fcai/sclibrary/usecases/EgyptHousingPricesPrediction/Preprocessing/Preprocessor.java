package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing;

import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.ColumnType;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.DataRow;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.RawDataset;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        for (int col = 0; col < schema.size(); col++) {

            if (col == labelIndex) continue;

            if (schema.get(col) == ColumnType.NUMERIC) {

                double min = Double.MAX_VALUE;
                double max = Double.MIN_VALUE;
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
                    String category = (String) row.get(col);
                    if (!encoding.containsKey(category)) {
                        encoding.put(category, idx++);
                        state.getFeatureNames().add(
                                dataset.getHeaders().get(col) + "_" + category
                        );
                    }
                }

                state.getCategoryMaps().put(col, encoding);
            }
        }
    }

    public ProcessedDataset transform(RawDataset dataset) {

        int rows = dataset.getRows().size();
        int featuresCount = state.getFeatureNames().size();

        double[][] X = new double[rows][featuresCount];
        double[][] y = new double[rows][1];

        for (int i = 0; i < rows; i++) {

            DataRow row = dataset.getRows().get(i);
            int featureIndex = 0;

            for (int col = 0; col < schema.size(); col++) {

                if (col == labelIndex) {
                    y[i][0] = (double) row.get(col);
                    continue;
                }

                if (schema.get(col) == ColumnType.NUMERIC) {

                    double value = (double) row.get(col);
                    double min = state.getMinValues().get(col);
                    double max = state.getMaxValues().get(col);

                    double normalized = (value - min) / (max - min);
                    X[i][featureIndex++] = normalized;

                } else { // CATEGORICAL

                    Map<String, Integer> encoding =
                            state.getCategoryMaps().get(col);

                    int categoryIndex =
                            encoding.get((String) row.get(col));

                    for (int j = 0; j < encoding.size(); j++) {
                        X[i][featureIndex++] =
                                (j == categoryIndex) ? 1.0 : 0.0;
                    }
                }
            }
        }

        return new ProcessedDataset(X, y, state);
    }


}
