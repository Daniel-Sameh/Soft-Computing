package fcai.sclibrary.usecases.EgyptHousingPricesPrediction;

import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.ColumnType;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.DataLoader;
import fcai.sclibrary.usecases.EgyptHousingPricesPrediction.Dataset.RawDataset;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader();
        List<ColumnType> dataSchema = List.of(
                ColumnType.CATEGORICAL,   // Type
                ColumnType.NUMERIC,   // Price
                ColumnType.NUMERIC, // Bedrooms
                ColumnType.NUMERIC, // Bathrooms
                ColumnType.NUMERIC, // Area
                ColumnType.CATEGORICAL,  // Furnished
                ColumnType.CATEGORICAL, // Delivery_Term
                ColumnType.CATEGORICAL  // City
        );
        RawDataset dataset = dataLoader.load("D:\\FCAI materials\\Level 4 (Senior) Term 1\\Soft Computing\\Project\\Soft-Computing\\src\\main\\java\\fcai\\sclibrary\\usecases\\EgyptHousingPricesPrediction\\data\\processed.csv", dataSchema);

    }
}
