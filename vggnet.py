import tensorflow as tf
from tensorflow.keras.applications import VGG16
from tensorflow.keras.preprocessing.image import ImageDataGenerator

import time

size_dense = 64
# Parametri per il training
batch_size = 32
epochs = 10
target_size = (224, 224)

# Carica il modello VGG16 pre-addestrato con pesi di ImageNet
base_model = VGG16(weights='imagenet', include_top=False, input_shape=(224, 224, 3))

# Congela i pesi del modello base
base_model.trainable = False

print("test")

# Costruisci il modello completo
model = tf.keras.models.Sequential([
    base_model,
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dense(size_dense, activation=tf.keras.layers.LeakyReLU(alpha=0.2)),
    tf.keras.layers.Dense(10, activation='softmax')  # 10 classi
])


# Compila il modello
model.compile(optimizer='adam',
              loss='categorical_crossentropy',
              metrics=['accuracy'])

# Crea i generatori di immagini per il training, la validazione e il test set
datagen = ImageDataGenerator(rescale=1./255, preprocessing_function=tf.keras.applications.vgg16.preprocess_input) # rescale=1./255, 

# Carica il dataset Oxford-IIIT Pet
train_dir = './butterflies/train/'
val_dir = './butterflies/validation/'
test_dir = './butterflies/test/'

train_generator = datagen.flow_from_directory(
    train_dir,
    target_size=target_size,
    batch_size=batch_size,
    class_mode='categorical',
)

validation_generator = datagen.flow_from_directory(
    val_dir,
    target_size=target_size,
    batch_size=batch_size,
    class_mode='categorical',
)

test_generator = datagen.flow_from_directory(
    test_dir,
    target_size=target_size,
    batch_size=batch_size,
    class_mode='categorical',
    shuffle=False,
)

# Addestra il modello
model.fit(
    train_generator,
    steps_per_epoch=train_generator.samples // batch_size,
    epochs=epochs,
    validation_data=validation_generator,
    validation_steps=validation_generator.samples // batch_size
)

model.save(str(epochs)+'_epochs_vector'+str(size_dense)+'_'+str(int(time.time()))+'.h5')
# Valuta il modello sul test set
test_loss, test_acc = model.evaluate(test_generator)
print('Test accuracy:', test_acc)
