package fcai.sclibrary.nn.optimizers;

import fcai.sclibrary.nn.core.Tensor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class GradientDescent implements Optimizer{
    private double learningRate;
    public GradientDescent(double learningRate){
        this.learningRate = learningRate;
    }
    @Override
    public Tensor update(Tensor params, Tensor grads) {

        int rows = params.getRows();
        int cols = params.getCols();

        double[][] newData = new double[rows][cols];
        double[][] pData = params.getData();
        double[][] gData = grads.getData();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                newData[i][j] =
                        pData[i][j] - learningRate * gData[i][j];
            }
        }

        return new Tensor(newData, rows, cols);
    }
}
