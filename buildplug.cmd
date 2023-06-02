cd solr_plugin
mvn clean install
copy target\CustomHandler-1.jar ..\solr-9.2.1\lib
..\solr-9.2.1\bin\solr.cmd start -f 