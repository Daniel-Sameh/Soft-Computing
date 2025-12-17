package fcai.sclibrary.usecases.EgyptHousingPricesPrediction;

import fcai.sclibrary.nn.activations.Linear;
import fcai.sclibrary.nn.activations.ReLU;
import fcai.sclibrary.nn.config.NetworkConfig;
import fcai.sclibrary.nn.config.TrainingConfig;
import fcai.sclibrary.nn.core.NeuralNetwork;
import fcai.sclibrary.nn.core.Tensor;
import fcai.sclibrary.nn.initialization.RandomUniform;
import fcai.sclibrary.nn.initialization.Xavier;
import fcai.sclibrary.nn.layers.DenseLayer;
import fcai.sclibrary.nn.layers.InputLayer;
import fcai.sclibrary.nn.layers.Layer;
import fcai.sclibrary.nn.loss.MeanSquaredError;
import fcai.sclibrary.nn.optimizers.GradientDescent;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.ColumnType;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.DataLoader;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.RawDataset;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing.Preprocessor;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing.ProcessedDataset;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing.Split;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {
    public static void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader();
        List<ColumnType> dataSchema = List.of(
                ColumnType.CATEGORICAL,   // Type
                ColumnType.NUMERIC,   // Price
                ColumnType.NUMERIC, // Bedrooms
                ColumnType.NUMERIC, // Bathrooms
                ColumnType.NUMERIC, // Area
                ColumnType.CATEGORICAL,  // Furnished
                ColumnType.CATEGORICAL, // Delivery_Term
                ColumnType.CATEGORICAL  // City
        );
        RawDataset dataset = dataLoader.load("src/main/java/fcai/sclibrary/usecases/EgyptHousingPricesPrediction/data/processed.csv", dataSchema);
        Preprocessor preprocessor = new Preprocessor(dataSchema, 8);
        preprocessor.fit(dataset);
        ProcessedDataset processedDataset = preprocessor.transform(dataset);

        double[][] features = processedDataset.getFeatures();
        double[][] labels   = processedDataset.getLabels();
        Split split = new Split();
        split.trainTestSplit(features, labels, 0.2, 42L);

        Tensor XTrain = split.getXTrain();
        Tensor yTrain = split.getYTrain();
        Tensor XTest  = split.getXTest();
        Tensor yTest  = split.getYTest();

        int inputSize = processedDataset.getFeatures()[0].length;
        int outputSize = 1;

        List<Layer> layers = new ArrayList<>();

        layers.add(new InputLayer(inputSize)); // inputSize AFTER one-hot encoding + numeric scaling

// Hidden layers (ReLU for nonlinearity)
//        layers.add(new DenseLayer(64, new ReLU()));
//        layers.add(new DenseLayer(32, new ReLU()));
        layers.add(new DenseLayer(16, new ReLU()));

// Output layer: Linear for regression
        layers.add(new DenseLayer(1, new Linear()));

        TrainingConfig trainingConfig = TrainingConfig.builder()
                .epochs(100)
                .learningRate(0.001)
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

        nn.compile();
        nn.train(XTrain, yTrain);

//        int size = XTest.getCols();
//        for (int i = 0; i < XTest.getRows(); i++) {
//            double[][] one = new double[1][size];
//            System.arraycopy(XTest.getData()[i], 0, one[0], 0, size);
//
//            Tensor xOne = new Tensor(one);
//            Tensor yOnePred = nn.predict(xOne);
//
//            double pred = yOnePred.getData()[0][0];
//            double actual = yTest.getData()[i][0];
//
//            System.out.println("Row " + i + " -> Predicted: " + pred + ", Actual: " + actual);
//        }
        nn.predict(XTest);
        double testMse = nn.evaluateMSE(XTest, yTest);
        System.out.println("Test MSE: " + testMse);
    }
}
