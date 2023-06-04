import json
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

feature_vectors = '10_epochs_vector64_1685387834.json'
input_img = 'images\\0050020.png'

with open(feature_vectors, 'r') as file:
    data = json.load(file)

vector1 = np.array(data[input_img]['feature_vector'])

similarities = {}

for k,v in data.items():
    # Convert the vector from the file to a NumPy array
    vector = np.array(v['feature_vector'])

    # Reshape the vectors to be 2D arrays
    vector1_2d = vector1.reshape(1, -1)
    vector2_2d = vector.reshape(1, -1)

    # Calculate cosine similarity
    similarity = cosine_similarity(vector1_2d, vector2_2d)
    similarities[k]=(similarity[0][0])

similarities = dict(sorted(similarities.items(), key=lambda x: x[1],reverse=True))

i = 5
for k,v in similarities.items():
    print(k,'|',v)
    i -= 1
    if i == 0:
        break