# IRS APPUNTI

# 1. Scrivere CustomRequestHandler (o altre classi)

```java
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.security.PermissionNameProvider;


public class CustomRequestHandler extends RequestHandlerBase {

    @Override
    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
        rsp.add("message", "Custom request handler is working!");
    }

    @Override
    public String getDescription() {
        return "Custom Request Handler";
    }
    @Override
    public Name getPermissionName(AuthorizationContext context) {
        // Return the permission name based on the authorization context
        return null;
    }
}
```
# 2. Compilare in java
```cmd
javac -classpath solr-core-9.2.1.jar;solr-solrj-9.2.1.jar;solr-solrj-streaming-9.2.1.jar;solr-solrj-zookeeper-9.2.1.jar CustomRequestHandler.java
```

```cmd
jar cvf custom-handler.jar CustomRequestHandler.class
```

O alternativamente in Maven

```cmd
mvn clean install
```

Maven è utile per gestire le dipendenze del progetto. Alcune hanno scope "compile" e quindi verrano inserite nel jar. Altre sono "provided" cioè quelle che ci da solr e quindi non c'è bisogno di integrarle nel plugin.

# 3. Configurare Solr

```cmd
bin\solr.cmd stop -p 8983
bin\solr.cmd start -f
```

1. Andare in Core Admin, creare core copiando nella cartella i file da _default andando a specificare configurazioni particolari di `managed-schema.xml` e `solrconfig.xml`. Nel nostro caso un `RequestHandler`

Da adesso in poi, ci basta cambiare il file `solrconfig.xml` e riavviare solr per effettuare dei cambiamenti.

# 3. Caricare plugin su solr 
Il jar dovrà essere caricati nella cartella `.\solr\lib`.

Nella parte adibita alle lib, si specifica la libreria da caricare (plugin)
```xml
<lib dir="${solr.install.dir:../../../..}/lib" regex="CustomHandler-1\.jar" />
```
Inoltre, si specifica che si utilizzerà la classe come `RequestHandler`, nel file del configset `solrconfig.xml`:

```xml
<requestHandler name="/custom" class="me.val.plugins.CustomRequestHandler" />
```

<lib dir="${solr.install.dir:../../../..}/lib" regex=".*\.jar" />

# 4. Rifinire plugin solar con Maven (pom.xml)

# 4.5 Import Tensorflow in Apache Solr (classpath)

1. è stato disabilitato il Security Manager perché non consentiva di modificare il classpath

```cmd
IF NOT DEFINED SOLR_SECURITY_MANAGER_ENABLED (
  set SOLR_SECURITY_MANAGER_ENABLED=false
)
```

Sono stati aggiunti nella directory `solr-9.2.1\server\solr-webapp\webapp\WEB-INF\lib` le librerie per tensorflow:
* `libtensorflow-1.15.0-javadoc.jar`
* `libtensorflow-1.15.0-sources.jar`
* `libtensorflow-1.15.0.jar`
* `libtensorflow_jni-1.15.0.jar`

# 5. Import saved_model to Apache Solr plugin

Copiare temporaneamente `resources\saved_model` in una cartella temporanea. In particolare, in `.\solr\tmp`.
Dopodiché, si aggiunge il modello e si lavora con esso

# 7. Preprocessing 

1. Ridimensionamento: se l'immagine è di dimensione diversa da 224x224, viene portata a tale risoluzione
2. La funzione img_to_array converte un'immagine di tipo PIL (Python Imaging Library) in un array NumPy. In Java, la funzione `normalizeImage_f` prende in input un array multidimensionale `image` di tipo `float[][][][]`, che rappresenta un'immagine con dimensioni di batch, altezza, larghezza e canali. La funzione itera su tutti i pixel dell'immagine e applica la normalizzazione seguendo i passaggi seguenti:
    In poche parole, la funzione normalizza l'immagine sottraendo il valore medio e dividendo per il fattore di scala, mentre inverte i canali rosso e blu.

con `saved_model_cli` è stato visto l'output della rete



# 7. Make standard query with extracted feature vector



