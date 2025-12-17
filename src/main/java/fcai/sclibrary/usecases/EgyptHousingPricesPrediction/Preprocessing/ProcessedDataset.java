package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing;

public class ProcessedDataset {

    private double[][] features;
    private double[][] labels;
    private PreprocessingState state;

    public ProcessedDataset(double[][] features,
                            double[][] labels,
                            PreprocessingState state) {
        this.features = features;
        this.labels = labels;
        this.state = state;
    }

    public double[][] getFeatures() { return features; }
    public double[][] getLabels() { return labels; }
    public PreprocessingState getState() { return state; }
}
