package fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLoader {
    public RawDataset load(
            String path,
            List<ColumnType> schema
    ) throws IOException {

        List<String> headers;
        List<DataRow> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            // Read header
            headers = Arrays.asList(br.readLine().split(","));

            String line;
            while ((line = br.readLine()) != null) {

                String[] tokens = line.split(",");
                List<Object> values = new ArrayList<>();

                for (int i = 0; i < tokens.length; i++) {
                    if (schema.get(i) == ColumnType.NUMERIC) {
                        values.add(Double.parseDouble(tokens[i]));
                    } else {
                        values.add(tokens[i]); // categorical stays String
                    }
                }

                rows.add(new DataRow(values));
            }
        }
        return new RawDataset(headers, schema, rows);
    }
}
