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

    public Tensor dot(Tensor a) {
        if (this.cols != a.rows) {
            throw new IllegalArgumentException(
                    "Dot product dimension mismatch: (" +
                            this.rows + "x" + this.cols + ") · (" +
                            a.rows + "x" + a.cols + ")"
            );
        }

        Tensor result = new Tensor(this.rows, a.cols);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < a.cols; j++) {
                double sum = 0.0;
                for (int k = 0; k < this.cols; k++) {
                    sum += this.data[i][k] * a.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    public Tensor add(Tensor a){
        if (a.rows == 1 && a.cols == this.cols) {
            Tensor result = new Tensor(this.rows, this.cols);
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                    result.data[i][j] = this.data[i][j] + a.data[0][j];
                }
            }
            return result;
        }

        if (this.rows != a.rows || this.cols != a.cols) {
            throw new IllegalArgumentException(
                    "Add dimension mismatch: (" +
                            this.rows + "x" + this.cols + ") + (" +
                            a.rows + "x" + a.cols + ")"
            );
        }

        Tensor result = new Tensor(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] + a.data[i][j];
            }
        }
        return result;
    }

    public Tensor subtract(Tensor a){
        if (this.rows != a.rows || this.cols != a.cols) {
            throw new IllegalArgumentException(
                    "Subtract dimension mismatch: (" +
                            this.rows + "x" + this.cols + ") - (" +
                            a.rows + "x" + a.cols + ")"
            );
        }

        Tensor result = new Tensor(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] - a.data[i][j];
            }
        }
        return result;
    }

    public Tensor multiply(Tensor a){
        if (this.rows != a.rows || this.cols != a.cols) {
            throw new IllegalArgumentException(
                    "Multiply dimension mismatch: (" +
                            this.rows + "x" + this.cols + ") ⊙ (" +
                            a.rows + "x" + a.cols + ")"
            );
        }

        Tensor result = new Tensor(this.rows, this.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] * a.data[i][j];
            }
        }
        return result;
    }

    public Tensor transpose(){
        Tensor result = new Tensor(this.cols, this.rows);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[j][i] = this.data[i][j];
            }
        }
        return result;
    }

    public double get(int i,int j) {
        return data[i][j];
    }
}
