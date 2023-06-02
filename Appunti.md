# IRS APPUNTI

```xml
<str name="sharedLib">${solr.sharedLib:}</str>
```

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


# 3. Configurare Solr

```cmd
bin\solr.cmd stop -p 8983
bin\solr.cmd start 5000 -f
```

1. Andare in Core Admin, creare core copiando nella cartella i file da _default andando a specificare configurazioni particolari di `managed-schema.xml` e `solrconfig.xml`. Nel nostro caso un `RequestHandler`

Da adesso in poi, ci basta cambiare il file `solrconfig.xml` e riavviare solr per effettuare dei cambiamenti.

# 3. Caricare plugin su solr 
Il jar dovrà essere caricati nella cartella `.\solr\lib`.

Inoltre, si specifica che si utilizzerà la classe come `RequestHandler`, nel file del configset `solrconfig.xml`:

```xml
<requestHandler name="/custom" class="CustomRequestHandler" />
```

<lib dir="${solr.install.dir:../../../..}/lib" regex=".*\.jar" />
