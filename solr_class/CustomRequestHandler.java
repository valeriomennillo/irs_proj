import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.security.AuthorizationContext;
import org.apache.solr.security.PermissionNameProvider;
import org.apache.solr.common.util.NamedList;

import java.util.Random;


public class CustomRequestHandler extends RequestHandlerBase {

    @Override
    public void init(NamedList args) {
        super.init(args);
        
        System.out.println("OINDAOGNADNGDOAI\n\n\n");
    }

    @Override
    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
        Random random = new Random();
        double randomDouble = random.nextDouble();
        rsp.add("message", "Custom request handler3 is working!"+randomDouble);
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
