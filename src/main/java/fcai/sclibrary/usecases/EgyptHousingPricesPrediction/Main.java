package fcai.sclibrary.usecases.EgyptHousingPricesPrediction;

import fcai.sclibrary.nn.activations.Linear;
import fcai.sclibrary.nn.activations.ReLU;
import fcai.sclibrary.nn.activations.Sigmoid;
import fcai.sclibrary.nn.activations.Tanh;
import fcai.sclibrary.nn.config.NetworkConfig;
import fcai.sclibrary.nn.config.TrainingConfig;
import fcai.sclibrary.nn.core.NeuralNetwork;
import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.initialization.Xavier;
import fcai.sclibrary.nn.layers.DenseLayer;
import fcai.sclibrary.nn.layers.InputLayer;
import fcai.sclibrary.nn.layers.Layer;
import fcai.sclibrary.nn.loss.MeanSquaredError;
import fcai.sclibrary.nn.optimizers.GradientDescent;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.ColumnType;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.DataLoader;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.RawDataset;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing.PreprocessingState;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing.Preprocessor;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing.ProcessedDataset;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing.Split;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class Main {
    private static final DecimalFormat EGP_FORMAT = new DecimalFormat("#,##0.00");

    // Helper methods for normalization and formatting
    private static double denormalizePrice(Preprocessor preprocessor, double normalizedValue) {
        return ((Number) preprocessor.deNormalization(normalizedValue, 1)).doubleValue();
    }

    private static String formatEGP(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            return "EGP (invalid)";
        }
        return "EGP " + EGP_FORMAT.format(Math.max(0.0, amount));
    }

    private static double mseDenormalized(NeuralNetwork nn, Tensor x, Tensor y, Preprocessor preprocessor) {
        Tensor yPred = nn.predict(x);
        int n = y.getRows() * y.getCols();
        double sum = 0.0;

        for (int i = 0; i < y.getRows(); i++) {
            for (int j = 0; j < y.getCols(); j++) {
                double pred = denormalizePrice(preprocessor, yPred.getData()[i][j]);
                double actual = denormalizePrice(preprocessor, y.getData()[i][j]);
                double diff = pred - actual;
                sum += diff * diff;
            }
        }
        return sum / n;
    }

    private static double maeDenormalized(NeuralNetwork nn, Tensor x, Tensor y, Preprocessor preprocessor) {
        Tensor yPred = nn.predict(x);
        int n = y.getRows() * y.getCols();
        double sum = 0.0;

        for (int i = 0; i < y.getRows(); i++) {
            for (int j = 0; j < y.getCols(); j++) {
                double pred = denormalizePrice(preprocessor, yPred.getData()[i][j]);
                double actual = denormalizePrice(preprocessor, y.getData()[i][j]);
                sum += Math.abs(pred - actual);
            }
        }
        return sum / n;
    }

    // Input helpers
    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    private static String readCategory(Scanner sc, String prompt, Map<String, Integer> availableCategories) {
        // Show only first 10 categories for readability
        String categoriesList = availableCategories.size() > 10
            ? String.join(", ", availableCategories.keySet().stream().limit(10).toList()) + "... (+" + (availableCategories.size() - 10) + " more)"
            : String.join(", ", availableCategories.keySet());

        while (true) {
            System.out.print(prompt + " [" + categoriesList + "]: ");
            String input = sc.nextLine().trim().toLowerCase();

            if (availableCategories.containsKey(input)) {
                return input;
            }
            System.out.println("Invalid option '" + input + "'. Please choose from available options (case-insensitive).");
        }
    }

    private static double normalizeNumeric(PreprocessingState state, int colIndex, double value) {
        double min = state.getMinValues().get(colIndex);
        double max = state.getMaxValues().get(colIndex);
        double denom = max - min;
        return (denom == 0.0) ? 0.0 : (value - min) / denom;
    }

    private static Tensor buildFeatureVector(
            List<ColumnType> schema,
            int labelIndex,
            PreprocessingState state,
            Object[] rawRow
    ) {
        int featureCount = schema.size() - 1;
        double[][] features = new double[1][featureCount];
        int featureIndex = 0;

        for (int col = 0; col < schema.size(); col++) {
            if (col == labelIndex) continue;

            if (schema.get(col) == ColumnType.NUMERIC) {
                double value = ((Number) rawRow[col]).doubleValue();
                features[0][featureIndex++] = normalizeNumeric(state, col, value);
            } else {
                String category = ((String) rawRow[col]).toLowerCase();
                Map<String, Integer> encoding = state.getCategoryMaps().get(col);
                Integer encodedValue = encoding.get(category);
                if (encodedValue != null) {
                    // Normalize categorical encoding to [0, 1] range (same as training)
                    int maxCategoryIndex = encoding.size() - 1;
                    double normalizedCategory = (maxCategoryIndex == 0) ? 0.0 : (double) encodedValue / maxCategoryIndex;
                    features[0][featureIndex++] = normalizedCategory;
                } else {
                    features[0][featureIndex++] = 0.0;
                }
            }
        }

        return new Tensor(features);
    }

    private static void runPredictionMenu(
            NeuralNetwork nn,
            Preprocessor preprocessor,
            List<ColumnType> schema,
            List<String> headers,
            int labelIndex
    ) {
        Scanner sc = new Scanner(System.in);
        PreprocessingState state = preprocessor.getState();

        while (true) {
            System.out.println("\n========================================");
            System.out.println("  Egypt Housing Price Predictor");
            System.out.println("========================================");
            System.out.println("1) Predict house price");
            System.out.println("2) Exit");
            System.out.println("========================================");

            int choice = readInt(sc, "Choose an option: ");

            if (choice == 2) {
                System.out.println("Goodbye!");
                break;
            }

            if (choice != 1) {
                System.out.println("Invalid option. Please try again.");
                continue;
            }

            System.out.println("\n--- Enter House Details ---");
            Object[] rawRow = new Object[schema.size()];

            for (int col = 0; col < schema.size(); col++) {
                if (col == labelIndex) {
                    rawRow[col] = 0.0; // placeholder for price
                    continue;
                }

                String columnName = headers.get(col);

                if (schema.get(col) == ColumnType.NUMERIC) {
                    rawRow[col] = readDouble(sc, columnName + ": ");
                } else {
                    Map<String, Integer> categories = state.getCategoryMaps().get(col);
                    rawRow[col] = readCategory(sc, columnName, categories);
                }
            }

            Tensor input = buildFeatureVector(schema, labelIndex, state, rawRow);
            Tensor prediction = nn.predict(input);

            double normalizedPrice = prediction.getData()[0][0];
            double priceEGP = denormalizePrice(preprocessor, normalizedPrice);

            System.out.println("\n========================================");
            System.out.println("  Normalized prediction: " + String.format("%.6f", normalizedPrice));
            System.out.println("  Predicted Price: " + formatEGP(priceEGP));
            System.out.println("========================================\n");
        }
    }
    public static void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader();
        List<ColumnType> dataSchema = List.of(
                ColumnType.CATEGORICAL,   // Type
                ColumnType.NUMERIC,       // Price (label)
                ColumnType.NUMERIC,       // Bedrooms
                ColumnType.NUMERIC,       // Bathrooms
                ColumnType.NUMERIC,       // Area
                ColumnType.CATEGORICAL,   // Furnished
                ColumnType.CATEGORICAL,   // Delivery_Term
                ColumnType.CATEGORICAL    // City
        );

        RawDataset dataset = dataLoader.load(
                "src/main/java/fcai/sclibrary/usecases/EgyptHousingPricesPrediction/data/processed.csv",
                dataSchema
        );

        int labelIndex = 1; // Price column
        Preprocessor preprocessor = new Preprocessor(dataSchema, labelIndex);
        preprocessor.fit(dataset);

        // Debug: price range (convert from log values to actual EGP)
        double minLogPrice = preprocessor.getState().getMinValues().get(labelIndex);
        double maxLogPrice = preprocessor.getState().getMaxValues().get(labelIndex);
        double minActualPrice = Math.exp(minLogPrice) - 1;
        double maxActualPrice = Math.exp(maxLogPrice) - 1;
        System.out.println("Price range in dataset:");
        System.out.println("  Min: " + formatEGP(minActualPrice));
        System.out.println("  Max: " + formatEGP(maxActualPrice));
        System.out.println("  Log range: " + String.format("%.2f - %.2f", minLogPrice, maxLogPrice));

        ProcessedDataset processedDataset = preprocessor.transform(dataset);

        double[][] features = processedDataset.getFeatures();
        double[][] labels = processedDataset.getLabels();

        Split split = new Split();
        split.trainTestSplit(features, labels, 0.2, 42L);

        Tensor XTrain = split.getXTrain();
        Tensor yTrain = split.getYTrain();
        Tensor XTest = split.getXTest();
        Tensor yTest = split.getYTest();

        int inputSize = features[0].length;

        List<Layer> layers = new ArrayList<>();
        layers.add(new InputLayer(inputSize));
        layers.add(new DenseLayer(64, new ReLU()));
        layers.add(new DenseLayer(32, new ReLU()));
        layers.add(new DenseLayer(16, new ReLU()));
        layers.add(new DenseLayer(1, new Sigmoid())); // Sigmoid to constrain output to [0,1]

        TrainingConfig trainingConfig = TrainingConfig.builder()
                .epochs(1000)
                .learningRate(0.3)
                .useBias(true)
                .build();

        NetworkConfig networkConfig = NetworkConfig.builder()
                .loss(new MeanSquaredError())
                .optimizer(new GradientDescent(trainingConfig.getLearningRate()))
                .layers(layers)
                .weightInitializer(new Xavier())
                .build();

        NeuralNetwork nn = NeuralNetwork.builder()
                .networkConfig(networkConfig)
                .trainingConfig(trainingConfig)
                .build();

        System.out.println("Compiling neural network...");
        nn.compile();

        System.out.println("Training neural network...");
        nn.train(XTrain, yTrain);

        // Evaluate on test set
        System.out.println("\n========================================");
        System.out.println("  Model Evaluation");
        System.out.println("========================================");

        double testMseNormalized = nn.evaluateMSE(XTest, yTest);
        double testMseEGP = mseDenormalized(nn, XTest, yTest, preprocessor);
        double testMAE = maeDenormalized(nn, XTest, yTest, preprocessor);
        double rmseEGP = Math.sqrt(testMseEGP);

        System.out.println("Test MSE (normalized): " + String.format("%.6f", testMseNormalized));
        System.out.println("Test RMSE (EGP): " + formatEGP(rmseEGP));
        System.out.println("Test MAE (EGP): " + formatEGP(testMAE));
        System.out.println("========================================\n");

        // Launch interactive prediction menu
        runPredictionMenu(nn, preprocessor, dataSchema, dataset.getHeaders(), labelIndex);
    }
}
