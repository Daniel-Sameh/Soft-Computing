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

        List<String> headers = Arrays.asList(
            "Type", "Price", "Bedrooms", "Bathrooms",
            "Area", "Furnished", "Delivery_Term", "City"
        );
        List<DataRow> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String firstLine = br.readLine();
            if (firstLine == null) {
                return new RawDataset(headers, schema, rows);
            }

            // Check if first line is a header by trying to parse it
            boolean hasHeader = isHeaderRow(firstLine, schema);

            // If not a header, process it as data
            if (!hasHeader) {
                rows.add(parseRow(firstLine, schema));
            }

            // Process remaining lines
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(parseRow(line, schema));
            }
        }
        return new RawDataset(headers, schema, rows);
    }

    private boolean isHeaderRow(String line, List<ColumnType> schema) {
        String[] tokens = line.split(",");
        if (tokens.length != schema.size()) {
            return false;
        }

        // Check if numeric columns contain non-numeric values (indicating headers)
        for (int i = 0; i < tokens.length; i++) {
            if (schema.get(i) == ColumnType.NUMERIC) {
                try {
                    Double.parseDouble(tokens[i].trim());
                } catch (NumberFormatException e) {
                    return true; // Cannot parse as number, must be header
                }
            }
        }
        return false; // All numeric columns parsed successfully, not a header
    }

    private DataRow parseRow(String line, List<ColumnType> schema) {
        String[] tokens = line.split(",");
        List<Object> values = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            if (schema.get(i) == ColumnType.NUMERIC) {
                values.add(Double.parseDouble(tokens[i].trim()));
            } else {
                values.add(tokens[i].trim()); // categorical stays String
            }
        }

        return new DataRow(values);
    }
}
