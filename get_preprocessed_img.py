import glob
from keras.applications.vgg16 import preprocess_input
from keras.models import load_model, Model

from tensorflow.keras.preprocessing.image import load_img, img_to_array

import json

# Load the saved model
saved_model_path = '10_epochs_vector64_1685547139.h5'
model = load_model(saved_model_path)


# Extract the features from the last layer
feature_extractor = Model(inputs=model.input, outputs=model.get_layer('dense').output)

def calculate_feature_vector(image_path):
    from keras.applications.vgg16 import preprocess_input
    # Load and preprocess the image
    from tensorflow.keras.preprocessing.image import load_img, img_to_array
    img = load_img(image_path, target_size=(224, 224))
    x = img_to_array(img)
    print(type(x))
    print(x.shape)
    

    x = preprocess_input(x)
    x = x.reshape((1,) + x.shape)
    print(type(x))
    import numpy 
    print(x)
    
    exit()
    # Extract the features from the image
    features = feature_extractor.predict(x)
    return features.tolist()[0]

sol = []

'''
Line 1: Scientific (Latin) name of the butterfly
Line 2: Common (English) name of the butterfly
Line 3: Textual description of the butterfly from eNature.com
'''
def get_id_from_name(path):
    return int(path.split("\\")[1].split(".")[0][:3])-1

scientific_names=[
    "Danaus plexippus",
    "Heliconius charitonius",
    "Heliconius erato",
    "Junonia coenia",
    "Lycaena phlaeas",
    "Nymphalis antiopa",
    "Papilio cresphontes",
    "Pieris rapae",
    "Vanessa atalanta",
    "Vanessa cardui"
]

common_names = [
    "Monarch",
    "Zebra Longwing",
    "Crimson-patched Longwing",
    "Common Buckeye",
    "American Copper",
    "Mourning Cloak",
    "Giant Swallowtail",
    "Cabbage White",
    "Red Admiral",
    "Painted Lady"
]

descriptions = [
    '3 1/2-4" (89-102 mm). Very large, with FW long and drawn out. Above, bright, burnt-orange with black veins and black margins sprinkled with white dots; FW tip broadly black interrupted by larger white and orange spots. Below, paler, duskier orange. 1 black spot appears between HW cell and margin on male above and below. Female darker with black veins smudged.',
    '3-3 3/8" (76-78 mm). Wings long and narrow. Jet-black above, banded with lemon-yellow (sometimes pale yellow). Beneath similar; bases of wings have crimson spots.',
    '3-3 3/8" (76-86 mm). Wings long, narrow, and rounded. Black above, crossed on FW by broad crimson patch, and on HW by narrow yellow line. Below, similar but red is pinkish and HW has less yellow.',
    '2-2 1/2" (51-63 mm). Wings scalloped and rounded except at drawn-out FW tip. Highly variable. Above, tawny-brown to dark brown; 2 orange bars in FW cell, orange submarginal band on HW, white band diagonally crossing FW. 2 bright eyespots on each wing above: on FW, 1 very small near tip and 1 large eyespot in white FW bar; on HW, 1 large eyespot near upper margin and 1 small eyespot below it. Eyespots black, yellow-rimmed, with iridescent blue and lilac irises. Beneath, FW resembles above in lighter shades; HW eyespots tiny or absent, rose-brown to tan, with vague crescent-shaped markings.',
    '7/8-1 1/8" (22-28 mm). Above, FW bright copper or brass-colored with dark spots and margin; HW dark brown with copper margin. Undersides mostly grayish with black dots; FW has some orange, HW has prominent submarginal orange band.',
    '2 7/8-3 3/8" (73-86 mm). Large. Wing margins ragged. Dark with pale margins. Above, rich brownish-maroon, iridescent at close range, with ragged, cream-yellow band, bordered inwardly by brilliant blue spots all along both wings. Below, striated, ash-black with row of blue-green to blue-gray chevrons just inside dirty yellow border.',
    '3 3/8-5 1/2" (86-140 mm). Very large. Long, dark, spoon-shaped tails have yellow center. Dark brownish-black above with 2 broad bands of yellow spots converging at tip of FW. Orange spot at corner of HW flanked by blue spot above; both recur below, but blue continuing in chevrons across underwing, which also has orange patch. Otherwise, yellow below with black veins and borders. Abdomen yellow with broad black midline tapering at tip; notch on top of abdomen near rear. Thorax has yellow lengthwise spots or stripes.',
    '1 1/4-1 7/8" (32-48 mm). Milk-white above with charcoal FW tips, black submarginal sex spots on FW (1 on male, 2 on female) and on HW costa. Below, FW tip and HW pale to bright mustard-yellow, speckled with grayish spots and black FW spots.',
    '1 3/4-2 1/4" (44-57 mm). FW tip extended, clipped. Above, black with orange-red to vermilion bars across FW and on HW border. Below, mottled black, brown, and blue with pink bar on FW. White spots at FW tip above and below, bright blue patch on lower HW angle above and below.',
    '2-2 1/4" (51-57 mm). FW tip extended slightly, rounded. Above, salmon-orange with black blotches, black-patterned margins, and broadly black FW tips with clear white spots; outer HW crossed by small black-rimmed blue spots. Below, FW dominantly rose-pink with olive, black, and white pattern; HW has small blue spots on olive background with white webwork. FW above and below has white bar running from costa across black patch near tip.'
]


for img_path in glob.glob("images/*.png"):
    id = get_id_from_name(img_path)
    sol.append({"image_id":img_path.split("\\")[1].split(".")[0],"image_path":img_path,"feature_vector":calculate_feature_vector(img_path),"scientific_name":scientific_names[id],"common_name":common_names[id],"description":descriptions[id]})
    print(img_path)

    