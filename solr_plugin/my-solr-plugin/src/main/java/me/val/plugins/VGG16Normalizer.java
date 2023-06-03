package me.val.plugins;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class VGG16Normalizer {
    
    private static final float[] MEAN_VALUES = {123.68f, 116.779f, 103.939f};

    public static BufferedImage normalizeImage(BufferedImage image) {
        normalize(image);

        return image;
    }

    private static void normalize(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                r -= MEAN_VALUES[0];
                g -= MEAN_VALUES[1];
                b -= MEAN_VALUES[2];

                int normalizedRgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, normalizedRgb);
            }
        }
    }
}
