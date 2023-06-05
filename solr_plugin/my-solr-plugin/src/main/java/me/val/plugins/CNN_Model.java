package me.val.plugins;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;
import org.tensorflow.TensorFlow;

import java.nio.FloatBuffer;

public class CNN_Model {

    private static final float MEAN_R = 123.68f;
    private static final float MEAN_G = 116.779f;
    private static final float MEAN_B = 103.939f;
    private static final float SCALE_FACTOR = 1.0f;

    private static Session session;
    private static SavedModelBundle savedModel;

    public CNN_Model(String modelPath) {
        System.out.println("[APACHE SOLR PLUGIN] TENSORFLOW VERSION: " + TensorFlow.version());
        try {
            savedModel = SavedModelBundle.load(modelPath, "serve");
            session = savedModel.session();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public float[] calculateFeatureVector(BufferedImage image) {
        System.out.println("[APACHE SOLR PLUGIN] calculating FEATURE VECTOR");

        String inputTensorName = "serving_default_vgg16_input";
        String outputTensorName = "StatefulPartitionedCall";

        BufferedImage resizedImage = resizeImage(image, 224, 224);

        float[][][][] inputArray = convertImageToArray(resizedImage);

        float[][][][] inputArray2 = normalizeImage(inputArray);

        // Create the input tensor
        Tensor<Float> inputTensor = Tensors.create(inputArray2);

        // Run inference
        Tensor<Float> outputTensor = session.runner()
                .feed(inputTensorName, inputTensor)
                .fetch(outputTensorName)
                .run()
                .get(0)
                .expect(Float.class);

        long[] shape = outputTensor.shape();
        int batchSize = (int) shape[0];
        int featureSize = (int) shape[1];

        // Print the dimensions
        System.out.println("Output tensor shape: (" + batchSize + ", " + featureSize + ")");

        FloatBuffer floatBuffer = FloatBuffer.allocate(outputTensor.numElements());
        outputTensor.writeTo(floatBuffer);

        // Copy the float values from the buffer to a float array
        float[] features = new float[outputTensor.numElements()];
        floatBuffer.flip();
        floatBuffer.get(features);

        // Implement the logic to calculate the feature vector using the loaded model
        System.out.println("[APACHE SOLR PLUGIN] CALCULATED FEATURE VECTOR");
        return features;
    }

    public static float[][][][] normalizeImage(float[][][][] image) {
        int batchSize = image.length;
        int height = image[0].length;
        int width = image[0][0].length;
        int channels = image[0][0][0].length;

        float[][][][] normalizedImage = new float[batchSize][height][width][channels];

        // Itera su tutti i pixel dell'immagine e applica la normalizzazione
        for (int b = 0; b < batchSize; b++) {
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {
                    float red = image[b][h][w][2];
                    float green = image[b][h][w][1];
                    float blue = image[b][h][w][0];

                    // Inverti i canali R e B
                    float invertedRed = blue;
                    float invertedBlue = red;

                    // Applica la normalizzazione
                    float normalizedRed = (invertedRed - MEAN_R) / SCALE_FACTOR;
                    float normalizedGreen = (green - MEAN_G) / SCALE_FACTOR;
                    float normalizedBlue = (invertedBlue - MEAN_B) / SCALE_FACTOR;

                    // Combina i canali normalizzati e invertiti in un singolo pixel
                    normalizedImage[b][h][w][0] = normalizedBlue;
                    normalizedImage[b][h][w][1] = normalizedGreen;
                    normalizedImage[b][h][w][2] = normalizedRed;
                }
            }
        }

        return normalizedImage;
    }


    public static BufferedImage resizeImage(BufferedImage image, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(image, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();
        return resizedImage;
    }

    public static float[][][][] convertImageToArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float[][][][] imageArray = new float[1][height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image.getRGB(j, i);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                imageArray[0][i][j][0] = r;
                imageArray[0][i][j][1] = g;
                imageArray[0][i][j][2] = b;
            }
        }

        return imageArray;
    }
}
