package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Preprocessing;
import fcai.sclibrary.nn.core.Tensor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
@Getter
public class Split {
    private Tensor XTrain, yTrain, XTest, yTest;


    public void trainTestSplit(double[][] X, double[][] y, double testRatio, long seed) {
        int n = X.length;
        int testSize = (int) Math.round(n * testRatio);
        int trainSize = n - testSize;

        int[] idx = new int[n];
        for (int i = 0; i < n; i++) idx[i] = i;

        // shuffle indices
        Random rnd = new Random(seed);
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = idx[i];
            idx[i] = idx[j];
            idx[j] = tmp;
        }

        double[][] XTrain = new double[trainSize][];
        double[][] yTrain = new double[trainSize][];
        double[][] XTest = new double[testSize][];
        double[][] yTest = new double[testSize][];

        for (int i = 0; i < trainSize; i++) {
            XTrain[i] = X[idx[i]];
            yTrain[i] = y[idx[i]];
        }
        for (int i = 0; i < testSize; i++) {
            XTest[i] = X[idx[trainSize + i]];
            yTest[i] = y[idx[trainSize + i]];
        }
        this.XTrain = new Tensor(XTrain);
        this.yTrain = new Tensor(yTrain);
        this.XTest  = new Tensor(XTest);
        this.yTest  = new Tensor(yTest);
    }
}