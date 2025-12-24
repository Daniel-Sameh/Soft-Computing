package fcai.sclibrary.nn.loss;

import fcai.sclibrary.nn.core.Tensor;

public class MeanSquaredError implements LossFunction {
    @Override
    public double computeError(Tensor predicted, Tensor target){
        int rows= predicted.getRows();
        int col= predicted.getCols();
        double error=0;
        for (int i=0; i<rows; i++){
            for (int j=0; j<col; j++){
                double diff=predicted.get(i,j)-target.get(i,j);
                error+=diff*diff;
            }
        }
        // MSE=(1/2N)*sum(error²), N= number of samples (rows)
        return error / (2.0 * rows);
    }

}
