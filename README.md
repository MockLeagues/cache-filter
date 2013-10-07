To verify behaviour using standalone Apache Tomcat 7

####Generate sef-singed certificate####

    $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA

Use password _changeit_

####Enable SSL in Tomcat####

Uncomment SSL in _server.xml_


