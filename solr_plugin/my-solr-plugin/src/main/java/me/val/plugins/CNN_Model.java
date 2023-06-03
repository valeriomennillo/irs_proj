package me.val.plugins;

import java.awt.image.BufferedImage;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;
import org.tensorflow.TensorFlow;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.io.OutputStream;
import java.net.URL;
import org.tensorflow.Graph;
import org.tensorflow.Operation;

public class CNN_Model {
    final String value = "\n\nHello from " + TensorFlow.version();

    private static Session session;
    private static SavedModelBundle savedModel;

    public CNN_Model() {
        System.out.println(value);
        String modelPath = "C:\\Users\\gianlu\\Desktop\\solr-9.2.1\\server\\tmp\\saved_model";

        try {
            savedModel = SavedModelBundle.load(modelPath, "serve");
            session = savedModel.session();
            System.out.println(session.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<Operation> iterator = savedModel.graph().operations();
        while (iterator.hasNext()) {
            Operation operation = iterator.next();
            System.out.println(operation.name());
        }
        

        try (SavedModelBundle savedModel = SavedModelBundle.load(modelPath, "serve")) {
            // Get the MetaGraphDef from the saved model
            Graph graph = savedModel.graph();

            // Print the loaded model
            System.out.println(graph.toGraphDef());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public float[] calculateFeatureVector(BufferedImage image) {
        System.out.println("CALCULATED FEATURE VECTOR");

        float[] preprocessedImage = preprocessImage(image);

        String inputTensorName = "serving_default_vgg16_input";
        String outputTensorName = "dense_1/bias/v/Read/ReadVariableOp";

        // Reshape the preprocessed image to match the expected input shape (1, height,
        // width, channels)
        int height = 224;
        int width = 224;
        int channels = 3;
        float[][][][] inputArray = new float[1][height][width][channels];
        inputArray[0] = reshape(preprocessedImage, height, width, channels);

        // Create the input tensor
        Tensor<Float> inputTensor = Tensors.create(inputArray);

        // Run inference
        Tensor<Float> outputTensor = session.runner()
                .feed(inputTensorName, inputTensor)
                .fetch(outputTensorName)
                .run()
                .get(0)
                .expect(Float.class);

        // Extract the features from the output tensor
        float[] features = outputTensor.copyTo(new float[outputTensor.numElements()]);

        // Implement the logic to calculate the feature vector using the loaded model
        return features;
    }

    private float[][][] reshape(float[] array, int height, int width, int channels) {
        float[][][] reshapedArray = new float[height][width][channels];
        int idx = 0;
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                for (int c = 0; c < channels; c++) {
                    reshapedArray[h][w][c] = array[idx++];
                }
            }
        }
        return reshapedArray;
    }

    private static float[] preprocessImage(BufferedImage image) {
        // Resize the image to the desired input shape (224x224)
        BufferedImage resizedImage = resizeImage(image, 224, 224);

        // Normalize pixel values and convert to RGB float array
        int[] pixels = resizedImage.getRGB(0, 0, 224, 224, null, 0, 224);
        float[] floatArray = new float[224 * 224 * 3];
        int idx = 0;
        for (int pixel : pixels) {
            floatArray[idx++] = ((pixel >> 16) & 0xFF) / 255.0f; // Red channel
            floatArray[idx++] = ((pixel >> 8) & 0xFF) / 255.0f; // Green channel
            floatArray[idx++] = (pixel & 0xFF) / 255.0f; // Blue channel
        }

        // Apply mean subtraction
        float[] mean = { 0.485f, 0.456f, 0.406f };
        for (int i = 0; i < floatArray.length; i++) {
            floatArray[i] -= mean[i % 3];
        }

        // Apply standard deviation division
        float[] std = { 0.229f, 0.224f, 0.225f };
        for (int i = 0; i < floatArray.length; i++) {
            floatArray[i] /= std[i % 3];
        }

        return floatArray;
    }

    private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        resizedImage.getGraphics().drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0,
                null);
        return resizedImage;
    }

}
