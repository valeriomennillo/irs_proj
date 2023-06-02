cd solr_class
javac -classpath solr-core-9.2.1.jar;solr-solrj-9.2.1.jar;solr-solrj-streaming-9.2.1.jar;solr-solrj-zookeeper-9.2.1.jar CustomRequestHandler.java
jar cvf custom-handler.jar CustomRequestHandler.class
..\..\solr-9.2.1\bin\solr.cmd start -f 