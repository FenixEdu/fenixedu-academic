[![FenixEdu Academic](https://cloud.githubusercontent.com/assets/132118/4919837/94ecfedc-64fd-11e4-9b02-2aad3a1b39c3.png)](https://fenixedu.org)

[![Build Status](https://travis-ci.org/FenixEdu/fenixedu-academic.png?branch=master)](https://travis-ci.org/FenixEdu/fenix)
==========

FenixEdu Academic is a open source Student Information System. It provides the back office system where schools manage students, teachers, degrees and courses. Take a look at [FenixEdu.org](http://fenixedu.org/) for more information.

**Table of Contents**

- [Issue Tracking](#issue-tracking)
- [Setup environment](#setup-environment)
- [Compiling](#compiling)
- [Bootstrapping](#bootstrapping)
- [Generate Fenix DML Zip](#generate-fenix-dml-zip)

##Issue Tracking

Issue Tracking for the FenixEdu Academic project is done in the [FenixEdu JIRA](https://jira.fenixedu.org/browse/ACADEMIC).

##Setup environment
1. `export JAVA_OPTS="-server -Xms256m -Xmx1024m -XX:PermSize=384m"`
2. `export MAVEN_OPTS="$JAVA_OPTS"`


##Compiling
  
To compile Fenix, simply run:

    mvn clean install
    
##Bootstrapping

To initialize an instalation of Fenix in an empty Database, see: 

https://confluence.fenixedu.org/display/ACADEMIC/Installation

##Generate Fenix DML Zip

    mvn ff:package-dmls

creates `target/fenix_dmls.zip`

It can be used to upload in [Fenix Domain Browser](https://fenix-ashes.ist.utl.pt/fdb)

