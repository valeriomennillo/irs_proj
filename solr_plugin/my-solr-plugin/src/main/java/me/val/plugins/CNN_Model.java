package me.val.plugins;

import java.awt.Graphics2D;
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

import javax.imageio.ImageIO;
import org.tensorflow.DataType;

import java.nio.FloatBuffer;
import java.io.File;

import me.val.plugins.VGG16Normalizer;


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
        System.out.println("calculating FEATURE VECTOR");

        String inputTensorName = "serving_default_vgg16_input";
        String outputTensorName = "StatefulPartitionedCall";

        BufferedImage resizedImage = resizeImage(image, 224, 224);

        //BufferedImage normalizedImage = VGG16Normalizer.normalizeImage(resizedImage);

        float[][][][] inputArray = convertImageToArray(resizedImage);

        // Create the input tensor
        Tensor<Float> inputTensor = Tensors.create(inputArray);

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
        System.out.println("CALCULATED FEATURE VECTOR");
        return features;
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
