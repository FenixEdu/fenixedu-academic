create table `EMPLOYEE_FUNCTIONS_ACCUMULATION` (
  `ANULATION_DATE` timestamp NULL default NULL,
  `BEGIN_DATE` text,
  `CREATION_DATE` timestamp NULL default NULL,
  `END_DATE` text,
  `HOURS` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `IMPORTATION_DATE` timestamp NULL default NULL,
  `KEY_EMPLOYEE` int(11), OID_EMPLOYEE bigint unsigned default null,
  `KEY_FUNCTIONS_ACCUMULATION` int(11), OID_FUNCTIONS_ACCUMULATION bigint unsigned default null,
  `KEY_PROFESSIONAL_REGIME` int(11), OID_PROFESSIONAL_REGIME bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `MODIFIED_DATE` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_EMPLOYEE),
  index (OID_EMPLOYEE),
  index (KEY_FUNCTIONS_ACCUMULATION),
  index (OID_FUNCTIONS_ACCUMULATION),
  index (KEY_PROFESSIONAL_REGIME),
  index (OID_PROFESSIONAL_REGIME),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `EMPLOYEE_PROFESSIONAL_EXEMPTION` (
  `ANULATION_DATE` timestamp NULL default NULL,
  `BEGIN_DATE` text,
  `CREATION_DATE` timestamp NULL default NULL,
  `END_DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `IMPORTATION_DATE` timestamp NULL default NULL,
  `KEY_COUNTRY` int(11), OID_COUNTRY bigint unsigned default null,
  `KEY_EMPLOYEE` int(11), OID_EMPLOYEE bigint unsigned default null,
  `KEY_GRANT_OWNER_EQUIVALENT` int(11), OID_GRANT_OWNER_EQUIVALENT bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `KEY_SERVICE_EXEMPTION` int(11), OID_SERVICE_EXEMPTION bigint unsigned default null,
  `LOCAL` longtext,
  `MODIFIED_DATE` timestamp NULL default NULL,
  `MOTIVE` longtext,
  `OJB_CONCRETE_CLASS` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_COUNTRY),
  index (OID_COUNTRY),
  index (KEY_EMPLOYEE),
  index (OID_EMPLOYEE),
  index (KEY_GRANT_OWNER_EQUIVALENT),
  index (OID_GRANT_OWNER_EQUIVALENT),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (KEY_SERVICE_EXEMPTION),
  index (OID_SERVICE_EXEMPTION)
) type=InnoDB, character set latin1 ;

create table `FUNCTIONS_ACCUMULATION` (
  `GIAF_ID` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `NAME` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `GRANT_OWNER_EQUIVALENT` (
  `GIAF_ID` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `NAME` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `SERVICE_EXEMPTION` (
  `GIAF_ID` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `NAME` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

