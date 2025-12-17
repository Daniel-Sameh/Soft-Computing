package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset;

import java.util.List;

public class DataRow {
    private List<Object> values;

    public DataRow(List<Object> values) {
        this.values = values;
    }

    public Object get(int index) {
        return values.get(index);
    }
}