package com.example.solrextensions;

import org.apache.solr.common.SolrException;
import org.apache.solr.handler.SearchHandler;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;

public class CustomSearchHandler extends SearchHandler {

    @Override
    public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
        // Add custom logic here
        // For example, setting a static response
        rsp.add("message", "This is a static response from the custom search handler");
    }

    @Override
    public String getDescription() {
        return "Custom Search Handler";
    }
}
