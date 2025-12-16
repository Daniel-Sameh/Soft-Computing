package fcai.sclibrary.nn.loss;

import fcai.sclibrary.nn.core.Tensor;

public class CrossEntropy implements LossFunction {
    @Override
    public double computeError(Tensor predicted, Tensor target){
        int rows= predicted.getRows();
        int col= predicted.getCols();
        double error=0;
        for (int i=0; i<rows; i++){
            for (int j=0; j<col; j++){
                error+=-target.get(i,j)*Math.log(Math.max(predicted.get(i,j),1e-9));
            }
        }
        return error;
    }


}
