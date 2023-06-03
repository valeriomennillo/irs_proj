
model_path = 'C:\\Users\\gianlu\\Desktop\\irs_proj\\solr_plugin\\my-solr-plugin\\src\\main\\resources\\saved_model\\saved_model.pb'
import tensorflow as tf

# Carica il modello
graph_def = tf.compat.v1.GraphDef()
with tf.io.gfile.GFile(model_path, 'rb') as f:
    graph_def.ParseFromString(f.read())

# Estrai i nomi dei tensori dal grafo
tensor_names = []
for node in graph_def.node:
    tensor_names.append(node.name)

# Stampa i nomi dei tensori
for name in tensor_names:
    print(name)