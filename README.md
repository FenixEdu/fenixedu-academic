**Table of Contents**  *generated with [DocToc](http://doctoc.herokuapp.com/)*

- [FenixEdu](#fenixedu)
- [Setup environment](#setup-environment)
- [Compiling & Run Fenix](#compiling--run-fenix)
	- [compile and exploded war (faster cycle)](#compile-and-exploded-war-faster-cycle)
	- [compile and assemble a war file](#compile-and-assemble-a-war-file)
	- [create jar for fenix scripts (without access control)](#create-jar-for-fenix-scripts-without-access-control)
	- [compile and run with jetty](#compile-and-run-with-jetty)
			- [The port can be changed using](#the-port-can-be-changed-using)
	- [run SQLUpdateGenerator](#run-sqlupdategenerator)
	- [running jetty with debugger](#running-jetty-with-debugger)
	- [fenix bootstrap and initialization with an empty database](#fenix-bootstrap-and-initialization-with-an-empty-database)
	- [Generate Fenix API Documentation](#generate-fenix-api-documentation)
	- [Generate Fenix DML Zip](#generate-fenix-dml-zip)
- [Troubleshooting](#troubleshooting)
	- [Too many open files error when running jetty](#too-many-open-files-error-when-running-jetty)
	- [When compiling, maven hangs while compiling jasper reports](#when-compiling-maven-hangs-while-compiling-jasper-reports)


FenixEdu
==========

#Setup environment
1. copy `src/main/resources/configuration.properties.sample` to `src/main/resources/configuration.properties`
2. copy `src/main/resources/fenix-framework.properties.sample` to `src/main/resources/fenix-framework.properties`
3. change above files accordingly
4. `export JAVA_OPTS="-server -Xms256m -Xmx1024m -XX:PermSize=384m"`
5. `export MAVEN_OPTS=$JAVA_OPTS`

#Compiling & Run Fenix

##compile and exploded war (faster cycle)
able to run with jetty & tomcat

    mvn clean prepare-package war:exploded

creates `target/fenix-1.0-SNAPSHOT/`
* running with jetty
  *    `mvn jetty:start`
* running with tomcat
  *    `create symlink in <tomcat>/webapps linking to target/fenix-1.0-SNAPSHOT/`

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

##Generate Fenix API Documentation

    mvn test -PGenerateFenixAPIDocs

creates `target/miredot/index.html` 

##Generate Fenix DML Zip

    mvn test -PGenerateDMLZip

creates `target/fenix_dmls.zip`

It can be used to upload in [Fenix Domain Browser](https://fenix-ashes.ist.utl.pt/fdb)
	
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
