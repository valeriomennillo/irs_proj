import tensorflow as tf
from tensorflow.keras.models import load_model

# Carica il modello HDF5
model = load_model('10_epochs_vector64_1685547139.h5')

# Rimuovi l'ultimo layer dal modello
model_without_last_layer = tf.keras.Model(inputs=model.input, outputs=model.layers[-2].output)

# Salva il modello modificato in formato SavedModel
tf.saved_model.save(model_without_last_layer, 'saved_model')
