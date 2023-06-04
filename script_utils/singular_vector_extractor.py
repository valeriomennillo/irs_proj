import glob
from keras.applications.vgg16 import preprocess_input
from keras.models import load_model, Model
import numpy as np

from tensorflow.keras.preprocessing.image import load_img, img_to_array

import json

# Load the saved model
saved_model_path = '10_epochs_vector64_1685547139.h5'
model = load_model(saved_model_path)


# Extract the features from the last layer
feature_extractor = Model(inputs=model.input, outputs=model.get_layer('dense').output)

def normalize_input(image):
    img_array = np.array(image)

    # Normalizza l'immagine rispetto ai valori specifici di VGG16
    img_array = img_array.astype(np.float32)
    img_array -= np.array([123.68, 116.779, 103.939])  # Sottrae la media
    img_array /= 1.0  # Divide per il fattore di scala (1.0 nel caso di VGG16)

    return img_array

def calculate_feature_vector(image_path):
    # Load and preprocess the image
    img = load_img(image_path, target_size=(224, 224))
    x = img_to_array(img)
    x = normalize_input(x)#preprocess_input(x)
    x = x.reshape((1,) + x.shape)

    # Extract the features from the image
    features = feature_extractor.predict(x)
    return features.tolist()[0]

sol = []

def get_id_from_name(path):
    return int(path.split("\\")[1].split(".")[0][:3])-1


import sys 
image_path = sys.argv[1]
print(calculate_feature_vector(image_path))

    