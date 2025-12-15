package fcai.sclibrary.nn.core;

import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class Tensor {
    private double[][] data;
    private int rows;
    private int cols;

    public Tensor dot(Tensor a){
        return null;
    }

    public Tensor add(Tensor a){
        return null;
    }

    public Tensor subtract(Tensor a){
        return null;
    }

    public Tensor multiply(Tensor a){
        return null;
    }

    public Tensor transpose(){
        return null;
    }
}
