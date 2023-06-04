import os
import shutil
import random

# Path to the directory containing the images
image_dir = 'images'

# Path to the directory where the training, validation, and test sets will be created
output_dir = 'data'

# Create the output directories
train_dir = os.path.join(output_dir, 'train')
os.makedirs(train_dir, exist_ok=True)

val_dir = os.path.join(output_dir, 'validation')
os.makedirs(val_dir, exist_ok=True)

test_dir = os.path.join(output_dir, 'test')
os.makedirs(test_dir, exist_ok=True)

# Get a list of all image files in the directory
image_files = [f for f in os.listdir(image_dir) if os.path.isfile(os.path.join(image_dir, f))]

# Shuffle the image files randomly
random.shuffle(image_files)

# Calculate the number of files for each set
total_files = len(image_files)
train_size = int(0.7 * total_files)
val_size = int(0.2 * total_files)
test_size = total_files - train_size - val_size

# Iterate over each image file
for i, filename in enumerate(image_files):
    # Extract the first three digits from the file name
    label = filename[:3]
    
    # Determine the destination directory based on the file count
    if i < train_size:
        dest_dir = train_dir
    elif i < train_size + val_size:
        dest_dir = val_dir
    else:
        dest_dir = test_dir
    
    # Create the label directory if it doesn't exist
    label_dir = os.path.join(dest_dir, label)
    os.makedirs(label_dir, exist_ok=True)
    
    # Move the image file to the label directory
    shutil.copy(os.path.join(image_dir, filename), label_dir)

print("Images have been divided into training, validation, and test sets.")
