FenixEdu
==========

#Setup environment
    copy src/main/resources/configuration.properties.sample to src/main/resources/configuration.properties
    export JAVA_OPTS="-server -Xms256m -Xmx1024m -XX:PermSize=384m"
    export MAVEN_OPTS=$JAVA_OPTS

#Compiling & Run Fenix

##compile and assemble a war file
    mvn clean package

creates `target/fenix-1.0-SNAPSHOT.war` and `target/fenix-1.0-SNAPSHOT/`

##create jar for fenix scripts (without access control)
    mvn clean test -Pjar

creates `deploy/fenix-1.0-SNAPSHOT.jar`

##compile and run with jetty
	mvn clean package jetty:start
	
jetty runs on (http://localhost:8080/fenix/)

####The port can be changed using 
	
	mvn -Djetty.port=XXXX jetty:start

##run SQLUpdateGenerator
	mvn clean test -PSQLUpdateGenerator
creates or updates `etc/database_operations/updates.sql`

##running jetty with debugger
	export MAVEN_OPTS="$MAVEN_OPTS -Xrunjdwp:transport=dt_socket,address=4000,server=y,suspend=n"
	mvn clean package jetty:start

1. change suspend to y if debugging startup process is needed.
2. change address to prefered debug port 8000.

##fenix bootstrap and initialization with an empty database 

https://fenix-ashes.ist.utl.pt/fenixWiki/FenixSetup
	
# Troubleshooting

## Too many open files error when running jetty

1. Change limit of open files to a higher level (man ulimit)
    * RedHat 
        * http://pro.benjaminste.in/post/318453669/increase-the-number-of-file-descriptors-on-centos-and
     
    * Ubuntu
        * http://posidev.com/blog/2009/06/04/set-ulimit-parameters-on-ubuntu/

## When compiling, maven hangs while compiling jasper reports

Please try to decrease the number of threads

    mvn -Djasper.threads=1 clean package
