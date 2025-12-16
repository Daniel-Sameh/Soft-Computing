package fcai.sclibrary.nn.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Tensor {
    private double[][] data;
    private int rows;
    private int cols;

    public Tensor(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Tensor(double[][] data) {
        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;
    }

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

    public double get(int i,int j) {
        return data[i][j];
    }
}
