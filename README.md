FenixEdu [![Build Status](https://travis-ci.org/FenixEdu/fenix.png?branch=develop)](https://travis-ci.org/FenixEdu/fenix)
==========

FenixEdu is a modular software platform for academic and administrative management of higher education institutions. It provides an integrated solution that spans all levels of the academic management process: from high level management tasks to the daily communication between students, faculty and staff. Take a look at [FenixEdu.org](http://fenixedu.org/) for more information.

**Table of Contents**

- [Setup environment](#setup-environment)
- [Compiling](#compiling)
- [Bootstrapping](#bootstrapping)
- [Generate Fenix DML Zip](#generate-fenix-dml-zip)
- [Troubleshooting](#troubleshooting)
	- [Error compiling JasperReports](#error-compiling-jasperreports)


##Setup environment
1. `export JAVA_OPTS="-server -Xms256m -Xmx1024m -XX:PermSize=384m"`
2. `export MAVEN_OPTS="$JAVA_OPTS"`


##Compiling
  
To compile Fenix, simply run:

    mvn clean install
    
##Bootstrapping

To initialize an instalation of Fenix in an empty Database, see: 

http://fenixedu.org/dev/tutorials/install-fenix-edu/

##Generate Fenix DML Zip

    mvn ff:package-dmls

creates `target/fenix_dmls.zip`

It can be used to upload in [Fenix Domain Browser](https://fenix-ashes.ist.utl.pt/fdb)

	
## Troubleshooting

### Error compiling JasperReports

This is due to a race condition in the Reports compilation process. If this is happenning, try compiling with: 

    mvn -Djasper.threads=1 clean install
