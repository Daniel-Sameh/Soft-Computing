package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset;

import lombok.Getter;

import java.util.List;

@Getter
public class DataRow {
    private List<Object> values;

    public DataRow(List<Object> values) {
        this.values = values;
    }

    public Object get(int index) {
        return values.get(index);
    }
}