package fcai.sclibrary.usecases.artisticImageApproximation;

import lombok.Data;

@Data
public class Utils {
    // Convert pixel to gene (normalize RGB to [0.0, 1.0])
    public static int pixelToGene(int c) {
        // Normalize the RGB value (0-16777215) to [0.0, 1.0]
        return c & 0xFFFFFF;
    }

    // Convert gene to pixel (denormalize [0.0, 1.0] to RGB)
    public static int geneToPixel(int gene) {
        return 0xFF000000 | (gene & 0xFFFFFF); // Add alpha channel
    }

    // Extract color components from pixel
    public static int[] extractRGB(int c) {
        int r = (c >> 16) & 0xFF;
        int g = (c >> 8) & 0xFF;
        int b = c & 0xFF;
        return new int[]{r, g, b};
    }
}
