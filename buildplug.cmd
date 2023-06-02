cd solr_plugin\my-solr-plugin
mvn clean install
cd ..
copy target\CustomHandler-1.jar ..\..\solr-9.2.1\lib
cd ..
..\solr-9.2.1\bin\solr.cmd start -f 