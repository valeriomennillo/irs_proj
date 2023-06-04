import glob
from keras.applications.vgg16 import preprocess_input
from keras.models import load_model, Model

from tensorflow.keras.preprocessing.image import load_img, img_to_array

import json

# Load the saved model
saved_model_path = '10_epochs_vector64_1685547139.h5'
model = load_model(saved_model_path)


# Extract the features from the last layer
feature_extractor = Model(inputs=model.input, outputs=model.get_layer('dense_1').output)

def calculate_label(image_path):
    from keras.applications.vgg16 import preprocess_input
    # Load and preprocess the image
    img = load_img(image_path, target_size=(224, 224))
    x = img_to_array(img)
    x = preprocess_input(x)
    x = x.reshape((1,) + x.shape)

    # Extract the features from the image
    features = feature_extractor.predict(x)
    return features.tolist()[0]

sol = []

def get_id_from_name(path):
    return int(path.split("\\")[1].split(".")[0][:3])-1

for img_path in glob.glob("images/*.png"):
    id = get_id_from_name(img_path)
    print(img_path)
    print(calculate_label(img_path))
