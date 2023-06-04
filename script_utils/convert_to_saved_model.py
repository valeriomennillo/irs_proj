import tensorflow as tf
model = tf.keras.models.load_model("10_epochs_vector64_1685547139.h5")
tf.saved_model.save(model, "./saved_model")
