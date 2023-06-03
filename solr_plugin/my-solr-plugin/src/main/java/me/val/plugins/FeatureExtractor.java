package me.val.plugins;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;

import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.datavec.image.loader.Java2DNativeImageLoader;

import org.deeplearning4j.nn.modelimport.keras.SequentialModelImporter;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class FeatureExtractor {
    private static final int IMAGE_WIDTH = 224;
    private static final int IMAGE_HEIGHT = 224;

    public static double[] extractFeatures(BufferedImage image) throws IOException {
        // Load the Keras model
        String modelPath = "C:\\Users\\gianlu\\Desktop\\solr-9.2.1\\server\\tmp\\model.h5";
        try {
            MultiLayerNetwork network = SequentialModelImporter.importSequentialModel(modelPath);

            ComputationGraph model = KerasModelImport.importKerasModelAndWeights(modelPath);

            // Load and preprocess the image
            Java2DNativeImageLoader loader = new Java2DNativeImageLoader(IMAGE_WIDTH, IMAGE_HEIGHT, 3);
            INDArray ndarray = loader.asMatrix(image);
            ndarray = ndarray.div(255.0); // Normalize pixel values to [0, 1]

            // Reshape the image array to match the model's input shape
            INDArray reshapedImage = ndarray.reshape(1, 3, IMAGE_WIDTH, IMAGE_HEIGHT);

            // Extract the features from the image
            INDArray features = model.outputSingle(reshapedImage);

            // Convert the features to a Java double array
            double[] featuresArray = features.data().asDouble();

            return featuresArray;
        } catch (UnsupportedKerasConfigurationException | InvalidKerasConfigurationException e) {
            // Handle the exception
            e.printStackTrace();
        }
        return null;
    }

}
