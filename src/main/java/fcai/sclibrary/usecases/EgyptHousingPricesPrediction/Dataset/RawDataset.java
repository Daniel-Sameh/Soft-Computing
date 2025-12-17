package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset;

import lombok.Getter;

import java.util.List;

@Getter
public class RawDataset {

    private List<String> headers;
    private List<ColumnType> schema;
    private List<DataRow> rows;

    public RawDataset(
            List<String> headers,
            List<ColumnType> schema,
            List<DataRow> rows
    ) {
        this.headers = headers;
        this.schema = schema;
        this.rows = rows;
    }
}