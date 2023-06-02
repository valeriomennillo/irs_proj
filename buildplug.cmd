cd solr_class
javac -classpath libs/* CustomRequestHandler.java
jar cvf CustomHandler.jar CustomRequestHandler.class
cd ..
copy solr_class\CustomHandler.jar ..\solr-9.2.1\lib
..\solr-9.2.1\bin\solr.cmd start -f 