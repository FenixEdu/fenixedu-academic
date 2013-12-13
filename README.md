FenixEdu [![Build Status](https://travis-ci.org/FenixEdu/fenix.png?branch=develop)](https://travis-ci.org/FenixEdu/fenix)
==========

**Table of Contents**

- [About](#about)
- [Setup environment](#setup-environment)
- [Compiling](#compiling)
- [Bootstrapping](#bootstrapping)
- [Generate Fenix API Documentation](#generate-fenix-api-documentation)
- [Generate Fenix DML Zip](#generate-fenix-dml-zip)
- [Troubleshooting](#troubleshooting)
	- [Error compiling JasperReports](#error-compiling-jasperreports)
	
##About

Fenix


##Setup environment
1. `export JAVA_OPTS="-server -Xms256m -Xmx1024m -XX:PermSize=384m"`
2. `export MAVEN_OPTS="$JAVA_OPTS"`


##Compiling
  
To compile Fenix, simply run:

    mvn clean install
    
##Bootstrapping

To initialize an instalation of Fenix in an empty Database, see: 

https://fenix-ashes.ist.utl.pt/fenixWiki/FenixSetup

##Generate Fenix API Documentation

    mvn test -PGenerateFenixAPIDocs

creates `target/miredot/index.html`


##Generate Fenix DML Zip

    mvn test -PGenerateDMLZip

creates `target/fenix_dmls.zip`

It can be used to upload in [Fenix Domain Browser](https://fenix-ashes.ist.utl.pt/fdb)

	
## Troubleshooting

### Error compiling JasperReports

This is due to a race condition in the Reports compilation process. If this is happenning, try compiling with: 

    mvn -Djasper.threads=1 clean install
