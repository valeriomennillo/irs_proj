cd solr_class
javac -classpath libs/* CustomRequestHandler.java
jar cvf CustomHandler.jar CustomRequestHandler.class
cd ..