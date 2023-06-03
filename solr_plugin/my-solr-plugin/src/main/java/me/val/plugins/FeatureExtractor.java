package me.val.plugins;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;

public class FeatureExtractor {
    private static final int IMAGE_WIDTH = 224;
    private static final int IMAGE_HEIGHT = 224;

    public static double[] extractFeatures(BufferedImage image) throws IOException {
        // Load the Keras model
        String modelPath = "C:\\Users\\gianlu\\Desktop\\solr-9.2.1\\server\\tmp\\model.h5";
        try {
            Model model = KerasModelImport.importKerasModelAndWeights(modelPath);

        } catch (UnsupportedKerasConfigurationException | InvalidKerasConfigurationException e) {
            // Handle the exception
            e.printStackTrace();
        }
        return null;
    }

}
