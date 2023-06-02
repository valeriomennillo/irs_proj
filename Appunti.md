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

# 5. Import model.h5 and keras modules integrated in plugin .jar file

# 6. CNN_Model create and fix interaction with main CustomRequestHandler.java 
convert python feature extractor.py to java

# 7. Make standard query with extracted feature vector



