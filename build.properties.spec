#------------------------------------------------------------------------------
# General Configuration - Application propertiess
#------------------------------------------------------------------------------
#
# @message = The name of the application
# @type = string
application.name=fenix

# @message = application virtual context path
# @type = string
application.virtual.context=${application.name}

# @message = Manager Filter Pattern
# @type = menu
# @options = {"Nothing"}
# @optionsValues = {"nothing"}
application.manager.filter.pattern=nothing

# @message = Default language for application
# @type = langString
application.language=pt

# @message = Default location for application
# @type = langLocation
# @languageProperty = application.language
# @dependency = application.language=*
application.location=PT

# @message = Default variant for application
# @type = langVariant
# @required = no
# @languageProperty = application.language
# @locationProperty = application.location
# @dependency = application.location=*
# @dependency = application.language=*
application.variant=

#------------------------------------------------------------------------------
# General Configuration - Institution propertiess
#------------------------------------------------------------------------------
#
# @message = Institution main url
# @type = url
# @validate = yes
institution.url=http://www.ist.utl.pt

# Example institution.project=/home/gedl/workspace/fenix-head
# @message = Institution project directory
# @type = path
# @required = no
# @validatePath = yes
institution.project=

#------------------------------------------------------------------------------
# Web Container Configuration
#------------------------------------------------------------------------------
#
#   app.path: relative path the application will be accesible under
#   manager.username: username of web container manager for installing the app
#   manager.password: the password of the web container manager
#   manager.url: absolute path to the web container managment interface
#
# @message = Relative path of the web application
# @type = default
webcontainer.virtual.root=/${application.virtual.context}

# @message = Tomcat manager's username
# @type = string
# @dependency = manager.url=*
webcontainer.manager.username=admin

# @message = Tomcat manager's password
# @type = password
# @dependency = manager.url=*
webcontainer.manager.password=admin

# TODO - Shouldn't this properties be made dependent on filter.hostnames?
# @message = Tomcat manager's url
# @type = url
# @required = no
# @validate = yes
webcontainer.manager.url=http://localhost:8080/manager


#------------------------------------------------------------------------------
# Data Repository Configuration
#------------------------------------------------------------------------------
#
# @message = Persistence support type
# @type = menu
# @options = {"OJB Persistence support","Versioned Objects Persistence Support","Delegate Persistence Support"}
# @optionsValues = {"net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB","net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsPersistenceSupport","net.sourceforge.fenixedu.persistenceTier.delegatedObjects.DelegatePersistenceSupport"}
repository.default.persistenceSupport=net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB

# @message = Fenix Database host
# @type = hostname
repository.db.host=localhost

# @message = Fenix Database name
# @type = integer
# @min = 1
# @max = 65535
repository.db.port=3306

# @message = Fenix Database name
# @type = string
repository.db.name=fenix

# @message = Database username
# @type = string
repository.db.user=fenix

# @message = Database password
# @type = string
# @persist = no
repository.db.password=fenix

# @message = Fenix database driver class name
# @type = default
repository.db.driver=com.mysql.jdbc.Driver

# @message = Database url additional parameters
# @type = string
repository.db.url.parameters=?useUnicode=true&amp;characterEncoding=latin1

# @message = Fenix database url protocol
# @type = string
repository.db.protocol=jdbc

# @message = Fenix database url subprotocol
# @type = string
repository.db.subprotocol=mysql

# @message = Database partial url
# @type = default
repository.db.alias=//${repository.db.host}:${repository.db.port}/${repository.db.name}${repository.db.url.parameters}

# @message = Database full url 
# @type = default
repository.db.url=${repository.db.protocol}:${repository.db.subprotocol}:${repository.db.alias}

#------------------------------------------------------------------------------
# Data Repository Configuration - Assiduousness
#------------------------------------------------------------------------------
#
# @message = Assiduousness database host
# @type = default
# @validate = yes
repository.assiduousness.db.host=localhost

# @message = Assiduousness database port
# @type = default
# @min = 1
# @max = 65535
repository.assiduousness.db.port=3306

# @message = Assiduousness database name
# @type = default
repository.assiduousness.db.name=AssiduidadeOracleTeste

# @message = Assiduousness database platform
# @type = default
repository.assiduousness.db.platform=MySQL

# @message = Assiduousness database driver class name
# @type = default
repository.assiduousness.db.driver=com.mysql.jdbc.Driver

# @message = Assiduousness database url protocol
# @type = default
repository.assiduousness.db.protocol=jdbc

# @message = Assiduousness database url subprotocol
# @type = default
repository.assiduousness.db.subprotocol=mysql

# @message = Assiduousness database username
# @type = default
repository.assiduousness.db,user=root

# @message = Assiduousness database password
# @type = default
repository.assiduousness.db.pass=

# @message = Assiduousness Database url additional parameters
# @type = nullableDefault
# @required = no
repository.assiduousness.db.url.parameters=

# @message = Assiduousness database alias
# @type = default
repository.assiduousness.db.alias=//${repository.assiduousness.db.host}:${repository.assiduousness.db.port}/${repository.assiduousness.db.name}

# @message = Assiduousness database full url
# @type = default
repository.assiduousness.db.url.alias=${repository.assiduousness.db.protocol}:${repository.assiduousness.db.subprotocol}://${repository.assiduousness.db.host}:${repository.assiduousness.db.port}/${repository.assiduousness.db.name}${repository.assiduousness.db.url.parameters}

# @message = Assiduousness database validation query sql
# @type = default
repository.assiduousness.db.validationQuery=select 1

#------------------------------------------------------------------------------
# Data Repository Configuration - Project Management
#------------------------------------------------------------------------------
#
#
# @message = Project Management database host
# @type = default
# @validate = yes
repository.projectManagement.db.host=localhost

# @message = Project Management database port
# @type = default
# @min = 1
# @max = 65535
repository.projectManagement.db.port=3306

# @message = Project Management database name
# @type = default
repository.projectManagement.db.name=AssiduidadeOracleTeste

# @message = Project Management database platform
# @type = default
repository.projectManagement.db.platform=MySQL

# @message = Project Management database driver class name
# @type = default
repository.projectManagement.db.driver=com.mysql.jdbc.Driver

# @message = Project Management database url protocol
# @type = default
repository.projectManagement.db.protocol=jdbc

# @message = Project Management database url subprotocol
# @type = default
repository.projectManagement.db.subprotocol=mysql

# @message = Project Management database username
# @type = default
repository.projectManagement.db,user=root

# @message = Project Management database password
# @type = default
repository.projectManagement.db.pass=

# @message = Project Management Database url additional parameters
# @type = nullableDefault
# @required = no
repository.projectManagement.db.url.parameters=

# @message = Project Management database alias
# @type = default
repository.projectManagement.db.alias=//${repository.projectManagement.db.host}:${repository.projectManagement.db.port}/${repository.projectManagement.db.name}

# @message = Project Management database full url
# @type = default
repository.projectManagement.db.url.alias=${repository.projectManagement.db.protocol}:${repository.projectManagement.db.subprotocol}://${repository.projectManagement.db.host}:${repository.projectManagement.db.port}/${repository.projectManagement.db.name}${repository.projectManagement.db.url.parameters}

# @message = Project Management database validation query sql
# @type = default
repository.projectManagement.db.validationQuery=select 1

#------------------------------------------------------------------------------
# Data Repository Configuration - Person Filter
#------------------------------------------------------------------------------
#
#
#
# @message = Person Filter database host
# @type = default
# @validate = yes
repository.personFilter.db.host=localhost

# @message = Person Filter database port
# @type = default
# @min = 1
# @max = 65535
repository.personFilter.db.port=3306

# @message = Person Filter database name
# @type = default
repository.personFilter.db.name=personFilter

# @message = Person Filter database platform
# @type = default
repository.personFilter.db.platform=MySQL

# @message = Person Filter database driver class name
# @type = default
repository.personFilter.db.driver=com.mysql.jdbc.Driver

# @message = Person Filter database url protocol
# @type = default
repository.personFilter.db.protocol=jdbc

# @message = Person Filter database url subprotocol
# @type = default
repository.personFilter.db.subprotocol=mysql

# @message = Person Filter database username
# @type = default
repository.personFilter.db,user=root

# @message = Person Filter database password
# @type = default
repository.personFilter.db.pass=

# @message = Person Filter Database url additional parameters
# @type = nullableDefault
# @required = no
repository.personFilter.db.url.parameters=

# @message = Person Filter database alias
# @type = default
repository.personFilter.db.alias=//${repository.personFilter.db.host}:${repository.personFilter.db.port}/${repository.personFilter.db.name}

# @message = Person Filter database full url
# @type = default
repository.personFilter.db.url.alias=${repository.personFilter.db.protocol}:${repository.personFilter.db.subprotocol}://${repository.personFilter.db.host}:${repository.personFilter.db.port}/${repository.personFilter.db.name}${repository.personFilter.db.url.parameters}

# @message = Person Filter database validation query sql
# @type = default
repository.personFilter.db.validationQuery=select 1

# @message = Person Filter other ???? 
# @type = string
repository.personFilter.db.other=-1

# @message = Person Filter error file 
# @type = string
repository.personFilter.db.errorFile=FicheiroErros.txt

# @message = Person Filter Database log operations? 
# @type = boolean
# @yesOption=y
# @yesOptionValue=true
# @noOption=n
# @noOptionValue=false
repository.personFilter.db.log=true


#------------------------------------------------------------------------------
# Data Repository Configuration - Slide
#------------------------------------------------------------------------------
#
#
# @message = Slide database host
# @type = default
# @validate = yes
repository.slide.db.host=localhost

# @message = Slide database port
# @type = default
# @min = 1
# @max = 65535
repository.slide.db.port=3306

# @message = Slide database name
# @type = default
repository.slide.db.name=slide

# @message = Slide database platform
# @type = default
repository.slide.db.platform=MySQL

# @message = Slide database driver class name
# @type = default
repository.slide.db.driver=com.mysql.jdbc.Driver

# @message = Slide database url protocol
# @type = default
repository.slide.db.protocol=jdbc

# @message = Slide database url subprotocol
# @type = default
repository.slide.db.subprotocol=mysql

# @message = Slide database username
# @type = default
repository.slide.db,user=root

# @message = Slide database password
# @type = default
repository.slide.db.pass=

# @message = Slide Database url additional parameters
# @type = nullableDefault
# @required = no
repository.slide.db.url.parameters=

# @message = Slide database alias
# @type = default
repository.slide.db.alias=//${repository.slide.db.host}:${repository.slide.db.port}/${repository.slide.db.name}

# @message = Slide database full url
# @type = default
repository.slide.db.url.alias=${repository.slide.db.protocol}:${repository.slide.db.subprotocol}://${repository.slide.db.host}:${repository.slide.db.port}/${repository.slide.db.name}${repository.slide.db.url.parameters}

# @message = Slide database validation query sql
# @type = default
repository.slide.db.validationQuery=select 1

# @message = Slide database configuration file
# @type = file
# @validate = yes
repository.slide.db.config=build/WEB-INF/classes/Domain.xml

#------------------------------------------------------------------------------
# Data Repository Configuration - scripts
#------------------------------------------------------------------------------
#
# @message = Should the repository be checked for alivedness for fenix database
# @type = boolean
# @yesOption=y
# @yesOptionValue=true
# @noOption=n 
# @noOptionValue=false
# @persist = true
repository.script.isAlive.check.db.fenix=false

# @message = Should the repository be checked for alivedness for slide database
# @type = boolean
# @yesOption=y
# @yesOptionValue=true
# @noOption=n 
# @noOptionValue=false
# @persist = true
repository.script.isAlive.check.db.slide=false

#----------------------------------------
# Database Installer  - install & upgrade
#----------------------------------------
#
# @message = Install or upgrade fenix and slide databases
# @type = menu
# @options={"Install from scratch","Upgrade existing"}
# @optionsValues={"install","upgrade"}
# @required = false
dbinstaller.installOrUpgrade = 

# @message = Database Driver Class for connection to Fenix and Slide databases
# @type = menu
# @options={"MySQL Driver class"}
# @optionsValues={"com.mysql.jdbc.Driver"}
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.driverClassName = com.mysql.jdbc.Driver

# @message = Database URL Schema for connecting to Fenix and Slide databases and execute
# multiple DDL statements
# @type = string
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.connectionUrlSchema = jdbc;mysql://${host}:${port}/${dbname}?allowMultiQueries=true&amp;jdbcCompliantTruncation=false

# @message = Fenix database name
# @type = default
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.fenixDBName = ${repository.db.name}

# @message = Slide database name
# @type = default
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.slideDBName = ${repository.slide.db.name}

# @message = Main database name (to grant permissions and create users
# @type = string
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.baseDBName = mysql

# @message = DDL Schema to create database
# @type = string
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.createDatabaseDDLSchema = create database if not exists ${dbname}

# @message = DDL Schema to create users and give them privileges to the fenix and slide databases
# @type = string
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.createUserDDLSchema = grant select, update, delete, insert, create on ${dbname}.* to '${username}'@'%' identified by '${password}'; flush privileges;

# @message = DDL Schema to create full privileges on the fenix and slide databases to the administration user
# @type = string
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.grantFullPrivsUserDDLSchema = grant ALL on ${dbname}.* to '${username}'@'%' identified by '${password}'; flush privileges;

# @message = The database host
# @type = default
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.dbHost = ${repository.db.host}

# @message = The database port
# @type = default
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.dbPort = ${repository.db.port}

# @message = The database administrator user (must have permissions to create databases, users and privileges)
# @type = string
# @persist = false
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.dbAdminUser = root

# @message = The database administrator user's password
# @type = password
# @persist = false
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.dbAdminPassword = 

# @message = The fenix database user for application access
# @type = string
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.fenixDBUser = ${repository.db.user}

# @message = The fenix database user's password for application access
# @type = password
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.fenixDBPassword = ${repository.db.password}

# @message = The slide database user for application access
# @type = string
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.slideDBUser = ${repository.slide.db.user}

# @message = The slide database user's password for application access
# @type = password
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.slideDBPassword = ${repository.slide.db.user}

# @message = The fenix database name
# @type = default
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=*
dbinstaller.dbNameFenix = ${repository.db.name}

# @message = The slide database name
# @type = default
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.dbNameSlide = ${repository.slide.db.name}

# @message = The base catalog database name 
# @type = string
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.dbNameBase = mysql

# @message = Create also one Fenix Management user
# @type = boolean
# @yesOption=y
# @yesOptionValue=true
# @noOption=n 
# @noOptionValue=false
# @persist = true
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.createFenixManagerUser = true

# @message = Fenix Management username (for login)
# @type = string
# @persist = false
# @dependency=dbinstaller.createFenixManagerUser=true
dbinstaller.fenixManagerUser = admin

# @message = Fenix Management user's password
# @type = password
# @persist = false
# @dependency=dbinstaller.createFenixManagerUser=true
dbinstaller.fenixManagerUserPassword = admin

# @message = Fenix Management user's full name 
# @type = string
# @persist = false
# @dependency=dbinstaller.createFenixManagerUser=true
dbinstaller.fenixManagerUserName = Administrator

# @message = Fenix Management user's gender
# @type = menu
# @options = {"Female","Male"}
# @optionsValues = {"FEMALE","MALE"}
# @persist = false
# @dependency=dbinstaller.createFenixManagerUser=true
dbinstaller.fenixManagerUserGender = MALE

# @message = Slide database creation ddl sql file
# @type = file
# @validateFile = yes
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.slideDDLFile = etc/slide.sql

# @message = Fenix OJB tables creation ddl sql file
# @type = file
# @validateFile = yes
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.ojbDDLFile = etc/ojb.sql

# @message = Fenix DML tables creation ddl sql file
# @type = file
# @validateFile = yes
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.dmlDDLFile = etc/dml.sql

# @message = Fenix base tables creation ddl sql file
# @type = file
# @validateFile = yes
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.fenixBaseDDLFile = etc/fenix-base.sql

# @message = Fenix application tables creation ddl sql file
# @type = file
# @validateFile = yes
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.fenixDDLFile = etc/fenix.sql

# @message = Fenix version table creation ddl sql file
# @type = file
# @validateFile = yes
# @dependency=dbinstaller.installOrUpgrade=install
dbinstaller.fenixVersionDDLFile = etc/fenix-date-version-create.sql

# @message = Fenix version date retrieval sql file
# @type = file
# @validateFile = yes
# @dependency=dbinstaller.installOrUpgrade=upgrade
dbinstaller.fenixVersionRetrieveSQLFile = etc/fenix-date-version-retrieve.sql

# @message = database direct client command line simple execution
# @type = string
# @dependency=dbinstaller.installOrUpgrade=upgrade
dbinstaller.dbDDLClientSchemaExecute = mysql -u${username} -f -p${password} ${dbname} &lt; ${file}

# @message = database direct client command line execution with temporary file
# @type = string
# @dependency=dbinstaller.installOrUpgrade=upgrade
dbinstaller.dbDDLClientSchemaExecuteToTempFile = mysql -u${username} -f -p${password} ${dbname} &lt; ${file} &gt; ${tempfile};

# @message = keep the generated temporary sql files
# @type = boolean
# @yesOption = y 
# @yesOptionValue = true
# @noOption = y
# @noOptionValue = false
# @dependency=dbinstaller.installOrUpgrade=upgrade
dbinstaller.keepDDLTempFile = false

# @message = database upgrades base directory
# @type = path
# @validatePath = yes
# @dependency=dbinstaller.installOrUpgrade=upgrade
dbinstaller.upgradesDirectory=etc/database_operations


#------------------------------------------------------------------------------
# FTP Configuration's
#------------------------------------------------------------------------------
#
#   ftp.IST.server.user: 
#   ftp.IST.server.pass: 
#   ftp.IST.server.address: 
#	ftp.IST.scp.command:
#	ftp.IST.scp.args:
#   ftp.degree.grades.server.user: 
#   ftp.degree.grades.server.pass: 
#   ftp.degree.grades.server.address: 
#
# @message = The FTP hostnames to ??? TODO - comment on this one
# @type = string
# @validate = true
# @required=true
# @persist=true
# @validate=true
# @generated.1.message=Please choose the username for FTP Access to host ${value}
# @generated,1.type=string
# @generated.1.required=true
# @generated.1.persist=true
# @generated.1.key=ftp.${value}.server.user
# @generated.1.defaultValue=ciist
# @generated.2.message=Please choose the password for FTP Access to host ${value}
# @generated,2.type=password
# @generated.2.required=true
# @generated.2.persist=true
# @generated.2.key=ftp.${value}.server.pass
# @generated.2.defaultValue=desenvolvimento
# @generated.3.message=Please choose the server address for FTP Access to host ${value}
# @generated,3.type=hostname
# @generated.3.required=true
# @generated.3.persist=true
# @generated.3.key=ftp.${value}.server.address
# @generated.3.defaultValue=saladd2.ist.utl.pt
# @generated.4.message=SCP (Secure Copy - ssh) command line to host ${value}
# @generated,4.type=string
# @generated.4.required=true
# @generated.4.persist=true
# @generated.4.key=ftp.${value}.scp.command
# @generated.4.defaultValue=
# @generated.5.message=SCP (Secure Copy - ssh) command line arguments to host ${value}
# @generated,5.type=string
# @generated.5.required=true
# @generated.5.persist=true
# @generated.5.key=ftp.${value}.scp.args
# @generated.5.defaultValue=
ftp.hostnames=IST,degree.grades

#------------------------------------------------------------------------------
# Cache Configuration
#------------------------------------------------------------------------------
#
#   ObjectCacheClass: cache implementation to use. Possible values are:
#                        - org.apache.ojb.broker.cache.ObjectCacheDefaultImpl
#                        - org.apache.ojb.broker.cache.ObjectCacheEmptyImpl
#                        - org.apache.ojb.broker.cache.ObjectCachePerBrokerImpl
#                        - org.apache.ojb.broker.cache.ObjectCacheJCSPerClassImpl
#                        - org.apache.ojb.broker.cache.ObjectCachePerClassImpl
#                        - net.sourceforge.fenixedu.persistenceTier.cache.FenixObjectCacheDefaultImpl
#                        - net.sourceforge.fenixedu.persistenceTier.cache.ObjectCacheOSCacheImpl
#   cache.event.listeners: set this to:
#       com.opensymphony.oscache.plugins.clustersupport.JavaGroupsBroadcastingListener
#                          to use javagroups clustering. This must be set for use on 
#                          distributed systems. Also, ObjectCacheOSCacheImpl must be
#                          used for the distributed cache to work.
#   cache.cluster.multicast.ip: multicast ip address to use with javagroups clustering.
#
#
# @message = Object Caching implementation class
# For clustering to work you need to select [Fenix Object Cache OS Impl]
# @type = menu
# @options = {"OJB Default Cache Impl","OJB Empty Cache Impl","OJB Cache per Broker Impl","OJB JCS Cache per Class Impl","OJB Cache per Class Impl","Fenix Default Cache Impl","Fenix Object Cache OS Impl"}
# @optionsValues = {"org.apache.ojb.broker.cache.ObjectCacheDefaultImpl","org.apache.ojb.broker.cache.ObjectCacheEmptyImpl","org.apache.ojb.broker.cache.ObjectCachePerBrokerImpl","org.apache.ojb.broker.cache.ObjectCacheJCSPerClassImpl","org.apache.ojb.broker.cache.ObjectCachePerClassImpl","net.sourceforge.fenixedu.persistenceTier.cache.FenixObjectCacheDefaultImpl","net.sourceforge.fenixedu.persistenceTier.cache.ObjectCacheOSCacheImpl"}
cache.objectcache.class=net.sourceforge.fenixedu.persistenceTier.cache.FenixCacheWrapper

# @message = Object cache event listeners class type
# For clustering to work you need to select [JavaGroups Broadcasting Listener Support]
# @type = menu
# @required = no
# @options={"JavaGroups Broadcasting Listener Support"}
# @optionsValues={"com.opensymphony.oscache.plugins.clustersupport.JavaGroupsBroadcastingListener"}
cache.event.listeners=

# @message = Multicast Clustering ip address
# @type = string
# @required = no
cache.cluster.multicast.ip=231.12.21.132

#------------------------------------------------------------------------------
# HTTP Response Cache Configuration
#------------------------------------------------------------------------------
#
#	response.cache.timeout : public pages cache timeout; subject to change in the manager interface
#
# @message = Public pages cache timeout (subject to change in the manager interface)
# @type = integer
# @min = 1
http.response.cache.timeout=600


#------------------------------------------------------------------------------
# SMS Configuration
#------------------------------------------------------------------------------
#
#   sms.gateway.host:
#   sms.gateway.port:
#   sms.gateway.uri:
#   sms.gateway.protocol:
#   sms.gateway.username:
#   sms.gateway.password:
#   sms.delivery.host:
#   sms.delivery.port:
#   sms.delivery.uri:
#   sms.delivery.protocol:
#   sms.delivery.username:
#   sms.delivery.password:
#
# @message = SMS gateway hostname
# @type = hostname
# @validate = yes
sms.gateway.host=localhost

# @message = SMS gateway host port
# @type = integer
# @min=1
# @max=65535
sms.gateway.port=13013

# @message = SMS gateway host URI
# @type = url
sms.gateway.uri=/cgi-bin/sendsms

# @message = SMS gateway host protocol
# @type = string
sms.gateway.protocol=http

# @message = SMS gateway host username
# @type = string
# @required = no
sms.gateway.username=

# @message = SMS gateway host password
# @type = password
# @required = no
sms.gateway.password=

# @message = SMS delivery hostname
# @type = hostname
# @validate = yes
sms.delivery.host=localhost

# @type = integer
# @min=1
# @max=65535
sms.delivery.port=8080

# @message = SMS gateway host URI
# @type = url
sms.delivery.uri=/ciapl/publico/smsController.do

# @message = SMS gateway host protocol
# @type = string
sms.delivery.protocol=http

# @message = SMS gateway host username
# @type = string
# @required = no
sms.delivery.username=

# @message = SMS gateway host password
# @type = password
# @required = no
sms.delivery.password=

#------------------------------------------------------------------------------
# Auto Task Configuration
#------------------------------------------------------------------------------
#
#   gratuity.situation.creator.task.hour: Hour at which the task will be
#                                         launched to create the gratuity
#                                         situations. set to -1 to deactivate.
# @message = Hour at which the task will be
#            launched to create the gratuity
#            situations. set to -1 to deactivate.
# @type = integer
# @min = -1
# @max = 65535
gratuity.situation.creator.task.hour=-1


#------------------------------------------------------------------------------
# FAQ Configuration
#------------------------------------------------------------------------------
#
#   enrollment.faq.url: url to the enrollment FAQ
#
# @message = URL to the enrollment FAQ
# @type = url
# @validate = yes
enrollment.faq.url=http://fenix-ashes.ist.utl.pt:8080/confluence/display/FAQ/FAQ-Inscri%25E7%25F5es


#------------------------------------------------------------------------------
# Logging Configuration
#------------------------------------------------------------------------------
#
#   log.profile.dir: name of dir where profiling log will be stored
#   log.profile.filename: name of file where profiling log will be stored
#   log.image.directory: directory to where dynamic images for log reports
#                        are generated. It must end with the applications
#                        context followed by images/logs
#   log4j.email.smtp.host: 
#   log4j.email.smtp.from: 
#   log4j.email.smtp.to: 
#   log4j.email.smtp.subject: 
#
# @message = Directory for profile logging files
# @type = path
# @validatePath = yes
log.profile.dir=/tmp

# @message = Filename for profile logging
# @type = file
# @validateFile = no
log.profile.filename=profileing.log

# @message = Image directory for profile logging
# @type = path
# @validatePath = yes
log.image.directory=/home/gedl/workspace/fenix-head/build/standalone/ciapl/images/logs

# @message = Log4j logging smtp hostname
# @type = hostname
# @validate = yes
log.log4j.email.smtp.host=localhost

# @message = Log4j logging 'From' line
# @type = string
log.log4j.email.smtp.from=Me

# @message = Log4j logging 'To' line
# @type = string
log.log4j.email.smtp.to=You

# @message = Log4j logging 'Subject' line
# @type = string
log.log4j.email.smtp.subject=Logging information


#------------------------------------------------------------------------------
# ServiceManager Configuration
#------------------------------------------------------------------------------
#
#   application.filterBroker: name of the filter broker that will be used in the application
#		Possible values are:	- pt.utl.ist.berserk.logic.filterManager.FilterBroker
#								- net.sourceforge.fenixedu.applicationTier.Filtro.FenixFilterBroker
# @message = Filter broker that will be used in the application
# @type = menu
# @options = {"Berserk Filter Broker","Fenix Filter Broker"}
# @optionsValues = {"pt.utl.ist.berserk.logic.filterManager.FilterBroker","net.sourceforge.fenixedu.applicationTier.Filtro.FenixFilterBroker"}
application.filterBroker=pt.utl.ist.berserk.logic.filterManager.FilterBroker


#------------------------------------------------------------------------------
# Validation/Verification Configuration
#------------------------------------------------------------------------------
#
# @message = Struts Validator doctype url
# @type = url
# @validate = yes
validation.struts.validator.doctype.url=file:///home/gedl/workspace/fenix-head/metadata/dtds/validator_1_0.dtd

#-----------------------------------------------------------------------------
# Cms Configuration
#-----------------------------------------------------------------------------
#
# @message = Content Management system mail host
# @type = hostname
# @validate = yes
cms.mail.smtp.host=localhost

# @message = Content Management system mail host name alias
# @type = string
cms.mail.smtp.name=localhost

# @message = Content Management system mailing list hostname
# @type = hostname
# @validate = yes
cms.mailingList.host.name=fenix.ist.utl.pt

#------------------------------------------------------------------------------
# Security Configuration - Portal Filter patterns
#------------------------------------------------------------------------------
#
#  filter.hostnames: comma seperated list of hostnames. The specified hostname
#                    can is expected to be anything following the http:// 
#                    string.
#  filter.hostname.<hostname>: comma seperated list of RoleTypes that are to
#                              be provided by the server <hostname>.
#
# @message = The hostnames under which this application will be made available for portal filters availability
# @type = hostnameList
# @validate = true
# @required=true
# @persist=true
# @validate=true
# @generated.1.message=Please choose the available portals for hostname ${value}
# @generated,1.type=roleType
# @generated.1.required=true
# @generated.1.persist=true
# @generated.1.key=security.filter.hostname.${value}
# @generated.1.defaultValue=*
# @generated.2.message=Application index page link on host ${value}
# @generated.2.type=string
# @generated.2.required=true
# @generated.2.persist=true
# @generated.2.key=application.index.html.link.${value}
# @generated.2.defaultValue=application.index.html.link.${value}
# @generated.3.message=Application login page on host ${value}
# @generated.3.type=url
# @generated.3.required=true
# @generated.3.persist=true
# @generated.3.key=application.login.page.${value}
# @generated.3.defaultValue=https://${value}:8080/${application.virtual.context}/privado
# @generated.4.message=CAS Service URL on host ${value}
# @generated.4.type=url
# @generated.4.required=true
# @generated.4.persist=true
# @generated.4.key=security.cas.serviceUrl.${value}
# @generated.4.defaultValue=https://${value}:8443/${application.virtual.context}/loginCAS.do
# @generated.4.dependency=security.cas.enabled=true
security.filter.hostnames=localhost,localhost.localdomain

#----------------------------------------------
# Security Configuration - Auth
#----------------------------------------------
#
# @message = Authentication service
# @type = menu
# @options = {"Default authentication service"}
# @optionsValues = {"net.sourceforge.fenixedu.applicationTier.Servico.Authenticate"}
security.auth.authenticationService.class=net.sourceforge.fenixedu.applicationTier.Servico.Authenticate

# @message = Change password service
# @type = menu
# @options = {"Default change password service"}
# @optionsValues = {"net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePassword"}
security.auth.changePassService.class=net.sourceforge.fenixedu.applicationTier.Servico.person.ChangePassword

# @message = Password Generator class
# @type = menu
# @options = {"Default Password Generator"}
# @optionsValues = {"net.sourceforge.fenixedu.applicationTier.utils.GeneratePasswordBase"}
security.auth,passwordGenerator.class=net.sourceforge.fenixedu.applicationTier.utils.GeneratePasswordBase

#----------------------------------------------
# Security Configuration - Host access control, filter configuration
#----------------------------------------------
#
# Configure the PathAccessControlfilter and the host control mechanism.
#
# All properties with the form 
#
#	security.host.control.path.${path}=${host list}
#
# will be used as the filter configuration. The host list indicates
# the hosts that are allowed to access the given servlet paths. IPs
# are also allowed.
#
# Properties with the form
#
#   security.host.control.name.${name}=${host list}
#
# will be used in the generic mechains for host control were ${name}
# is associated with the ${host list}. A request can be verified
# against the ${host list} with HostAccessControl.isAllowed(name, request).
#
# Example:
# 	security.host.control.path./login.do=localhost,trusted.example.net
# 	security.host.control.name.net.sourceforge.fenixedu.presentationTier.Action.CheckPasswordKerberosAction=localhost,trusted.example.net
# 	security.host.control.name.checkPassword=localhost,trusted.example.net
#
# @message = The Struts Actions classes to check host access lists on (Comma separated list)
# @type = string
# @required = false
# @generated.1.message = Hosts with permission for Action ${namevalue}
# @generated.1.type = hostnameList
# @generated.1.key = security.host.control.name.${value}
# @generated.1.defaultValue = localhost
security.host.controllable.actions=net.sourceforge.fenixedu.presentationTier.Action.CheckPasswordKerberosAction,net.sourceforge.fenixedu.presentationTier.Action.externalServices.RetrieveUserInformation,net.sourceforge.fenixedu.presentationTier.Action.externalServices.UserPermissionCheck,net.sourceforge.fenixedu.presentationTier.Action.externalServices.GroupCheck,net.sourceforge.fenixedu.presentationTier.Action.messaging.ExecutionCourseAliasExpandingAction

# @message = The Struts Paths to check host access lists on (Comma separated list)
# @type = string
# @required = false
# @generated.1.message = Hosts with permission for Path ${value}
# @generated.1.key = security.host.control.path.${value}
# @generated.1.type = hostnameList
# @generated.1.defaultValue = localhost
security.host.controllable.paths=

#----------------------------------------------
# Security Configuration - CAS 
#----------------------------------------------
#
# @message = Use CAS for Security Checking?
# @type = boolean
# @yesOption = y
# @yesOptionValue = true
# @noOption = n
# @noOptionValue = false
security.cas.enabled=false

# @message = CAS Login URL
# @type = url
# @validate = true
# @dependency = security.cas.enabled=true
security.cas.loginUrl=https://localhost:8443/cas/login

# @message = CAS Login URL
# @type = url
# @validate = true
# @dependency = security.cas.enabled=true
security.cas.validateUrl=https://localhost:8443/cas/serviceValidate

# @message = CAS Logout URL
# @type = url
# @validate = true
# @dependency = security.cas.enabled=true
security.cas.logoutUrl=https://localhost:8443/cas/logout

#------------------------------------------------------------------------------
# Miscellaneous 
#------------------------------------------------------------------------------
## Characters that can't be used directly in ant build files
# @message = line separator char
# @type=default
misc.line.seperator=\n

# @message = less then char
# @type=default
misc.character.lessthan=<

# @message = dollar char
# @type=default
misc.character.dollar=$

## Complex constructions that contain characters that's can't
## be used directly in ant build files
# @message = empty - TODO
# @type=default
misc.allowed-roles.build.file.constructor.header=<?xml version="1.0" encoding="ISO-8859-15"?>\n \
		\t<project default="generate-properties-file" name="Auto-Generated-Build-File">\n \
			\t\t<property file="build.properties"/>\n \
			\t\t<target name="generate-properties-file"> \n\n

# @message = empty - TODO
# @type=default
misc.allowed-roles.build.file.constructor.tail=\n\n\t\t</target>\n\t</project>

# @message = empty - TODO
# @type=default
misc.allowed-roles.newline.instruction=${line.seperator}

#----------------------------------------------------------------
# Miscellaneous - Printers
#----------------------------------------------------------------
#
# @message = empty - TODO
# @type=nullableDefault
misc.markSheet.printers=

#----------------------------------------------------------------
# File Manager
#----------------------------------------------------------------
#
# @message = File Manager Implementation Class
# @type = menu
# @options = {"DSpace File Manager"}
# @optionsValues = {"pt.utl.ist.fenix.tools.file.dspace.DspaceFileManager"}
filemanager.implementation.class=pt.utl.ist.fenix.tools.file.dspace.DspaceFileManager


#----------------------------------------------------------------
# File Manager - Dspace Configuration
#----------------------------------------------------------------
#
# @message = DSpace underlying transport class
# @type = menu
# @options = {"HTTP","RMI"}
# @optionsValues = {"pt.utl.ist.fenix.tools.file.dspace.DspaceHttpClient","pt.utl.ist.fenix.tools.file.dspace.DspaceRmiClient"}
# @dependency = filemanager.implementation.class=pt.utl.ist.fenix.tools.file.dspace.DspaceFileManager
filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceHttpClient

# @message = DSpace Server URL
# @type = url
# @validate = yes
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceHttpClient
filemanager.dspace.serverUrl=https://localhost:8443/dspace

# @message = DSpace Server download URI Format 
# @type = string
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceHttpClient
filemanager.dspace.downloadUriFormat=bitstream

# @message = DSpace Server username
# @type = string
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceHttpClient
filemanager.dspace.username=ist12628

# @message = DSpace Server password
# @type = password
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceHttpClient
filemanager.dspace.password=pass


# @message = JNDI Initial Context configuration property file
# @type = file
# @validateFile = no
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceRmiClient
filemanager.dspace.initial.context.properties=dspace.client.jndi.properties

# @message = JNDI Remote DSpace Manager bind name
# @type = string
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceRmiClient
filemanager.dspace.rmi.server.jndi.name=DSpaceRMIRemoteManager

# @message = DSpace - Use RMI over SSL?
# @type = boolean
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceRmiClient
filemanager.dspace.client.rmi.use.ssl=true

# @message = DSpace - SSL Client keystore
# @type = file
# @validateFile = no
# @dependency = filemanager.dspace.client.rmi.use.ssl=true
filemanager.dspace.client.rmi.ssl.keystore=DSpaceRMIClient.keystore

# @message = DSpace - Minimum data packet size (bytes)
# @type = integer
# @min = 1
# @max = 65535
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceRmiClient
filemanager.dspace.rmi.stream.bytes.min=1024

# @message = DSpace - Maximum data packet size (bytes)
# @type = integer
# @min = 1
# @max = 65535
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceRmiClient
filemanager.dspace.rmi.stream.bytes.max=8192

# @message = DSpace - Block size increase/decrease for best packet size (bytes)
# @type = integer
# @min = 1
# @max = 65535
# @dependency = filemanager.dspace.underlying.transport.class=pt.utl.ist.fenix.tools.file.dspace.DspaceRmiClient
filemanager.dspace.rmi.stream.bytes.block=512


#------------------------------------------------------------------------------
# Testing Configuration
#------------------------------------------------------------------------------
#
#   JdbcAccessClass: implementation to use for accessing the data repository.
#                    the default value is
#                         org.apache.ojb.broker.accesslayer.JdbcAccessImpl
#                    For testing the application with ClickUnit the following
#                    implementation should be used:
#                         net.sourceforge.fenixedu.persistence.persistenceTiercAccessRollbackImpl
#                    This latter implementation stores the inverse operations
#                    performed on the database, so that the database state can
#                    be roled back to the initial state. In production env's
#                    the default implementation must be used!!! Using this 
#                    latter implementation in production is a major security
#                    hole!
#   roleback.filename: The filename to where the 
#                         net.sourceforge.fenixedu.persistenceTier.AccessLayer.JdbcAccessRollbackImpl
#                      implementation will write the inverse sql operations. This
#                      file is generated in cronilogical order. When reverting 
#                      database state, the sql instructions should be executed
#                      starting from the end of the file.
#
# @message = Implementation to use for accessing the data repository.
#                    For testing the application with ClickUnit the [Fenix Jdbc Rollback Impl] 
#                    should be selected.
#                    This latter implementation stores the inverse operations
#                    performed on the database, so that the database state can
#                    be roled back to the initial state. In production env's
#                    the default implementation must be used!!! Using this 
#                    latter implementation in production is a major security
#                    hole!
# @type = menu
# @options = {"Default OJB jdbc access class","Fenix Jdbc Rollback Impl"}
# @optionsValues = {"org.apache.ojb.broker.accesslayer.JdbcAccessImpl","net.sourceforge.fenixedu.persistenceTier.AccessLayer.JdbcAccessRollbackImpl"}
test.jdbc.access.class=org.apache.ojb.broker.accesslayer.JdbcAccessImpl

# @message = Implementation to use for accessing the data repository.
#                    For testing the application with ClickUnit the [Fenix Jdbc Rollback Impl] 
#                    should be selected.
#                    This latter implementation stores the inverse operations
#                    performed on the database, so that the database state can
#                    be roled back to the initial state. In production env's
#                    the default implementation must be used!!! Using this 
#                    latter implementation in production is a major security
#                    hole!
# @type = file
# @validateFile=false
# @required=false
# @depency=test.jdbc.access.class=net.sourceforge.fenixedu.persistenceTier.AccessLayer.JdbcAccessRollbackImpl
test.rollback.filename=/tmp/roleback.sql


#------------------------------------------------------------------------------
# Tests Configuration - ClickUnit
#------------------------------------------------------------------------------
#
## @message = The host to test by click unit testing
## @type = hostname
#### @validate = yes
#test.clickunit.http.host=localhost
	
## @message = The http port of the host to test by click unit testing
## @type = integer
#### @min = 1
## @max = 65535
#test.clickunit.http.port=8080

## @message = The https (secure) port of the host to test by click unit testing
## @type = integer
#### @min = 1
## @max = 65535
#test.clickunit.https.port=443

## @message = Login page url of the host to test by click unit testing
## @type = url
#### @validate = yes
#test.clickunit.login.page=https://${clickunit.http.host}:${clickunit.https.port}/${application.virtual.context}/privado

#
#------------------------------------------------------------------------------
# End of build.properties.sample file
#------------------------------------------------------------------------------
