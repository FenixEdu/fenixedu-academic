
create table `CONTRACT_SITUATION` (
  `END_SITUATION` tinyint(1),
  `GIAF_ID` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `NAME` text,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `EMPLOYEE_CONTRACT_SITUATION` (
  `ANULATION_DATE` timestamp NULL default NULL,
  `BEGIN_DATE` text,
  `CREATION_DATE` timestamp NULL default NULL,
  `END_DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `IMPORTATION_DATE` timestamp NULL default NULL,
  `KEY_CONTRACT_SITUATION` int(11), OID_CONTRACT_SITUATION bigint unsigned default null,
  `KEY_EMPLOYEE` int(11), OID_EMPLOYEE bigint unsigned default null,
  `KEY_PROFESSIONAL_CATEGORY` int(11), OID_PROFESSIONAL_CATEGORY bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `MODIFIED_DATE` timestamp NULL default NULL,
  `STEP` text,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_CONTRACT_SITUATION),
  index (OID_CONTRACT_SITUATION),
  index (KEY_EMPLOYEE),
  index (OID_EMPLOYEE),
  index (KEY_PROFESSIONAL_CATEGORY),
  index (OID_PROFESSIONAL_CATEGORY),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `EMPLOYEE_PROFESSIONAL_CATEGORY` (
  `ANULATION_DATE` timestamp NULL default NULL,
  `BEGIN_DATE` text,
  `CREATION_DATE` timestamp NULL default NULL,
  `END_DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `IMPORTATION_DATE` timestamp NULL default NULL,
  `KEY_EMPLOYEE` int(11), OID_EMPLOYEE bigint unsigned default null,
  `KEY_PROFESSIONAL_CATEGORY` int(11), OID_PROFESSIONAL_CATEGORY bigint unsigned default null,
  `KEY_PROFESSIONAL_REGIME` int(11), OID_PROFESSIONAL_REGIME bigint unsigned default null,
  `KEY_PROFESSIONAL_RELATION` int(11), OID_PROFESSIONAL_RELATION bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `LEVEL` text,
  `MODIFIED_DATE` timestamp NULL default NULL,
  `STEP` text,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_EMPLOYEE),
  index (OID_EMPLOYEE),
  index (KEY_PROFESSIONAL_CATEGORY),
  index (OID_PROFESSIONAL_CATEGORY),
  index (KEY_PROFESSIONAL_REGIME),
  index (OID_PROFESSIONAL_REGIME),
  index (KEY_PROFESSIONAL_RELATION),
  index (OID_PROFESSIONAL_RELATION),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `EMPLOYEE_PROFESSIONAL_CONTRACT` (
  `ANULATION_DATE` timestamp NULL default NULL,
  `BEGIN_DATE` text,
  `CREATION_DATE` timestamp NULL default NULL,
  `END_DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `IMPORTATION_DATE` timestamp NULL default NULL,
  `KEY_CONTRACT_SITUATION` int(11), OID_CONTRACT_SITUATION bigint unsigned default null,
  `KEY_EMPLOYEE` int(11), OID_EMPLOYEE bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `MODIFIED_DATE` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_CONTRACT_SITUATION),
  index (OID_CONTRACT_SITUATION),
  index (KEY_EMPLOYEE),
  index (OID_EMPLOYEE),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `EMPLOYEE_PROFESSIONAL_REGIME` (
  `ANULATION_DATE` timestamp NULL default NULL,
  `BEGIN_DATE` text,
  `CREATION_DATE` timestamp NULL default NULL,
  `END_DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `IMPORTATION_DATE` timestamp NULL default NULL,
  `KEY_EMPLOYEE` int(11), OID_EMPLOYEE bigint unsigned default null,
  `KEY_PROFESSIONAL_REGIME` int(11), OID_PROFESSIONAL_REGIME bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `MODIFIED_DATE` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_EMPLOYEE),
  index (OID_EMPLOYEE),
  index (KEY_PROFESSIONAL_REGIME),
  index (OID_PROFESSIONAL_REGIME),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `EMPLOYEE_PROFESSIONAL_RELATION` (
  `ANULATION_DATE` timestamp NULL default NULL,
  `BEGIN_DATE` text,
  `CREATION_DATE` timestamp NULL default NULL,
  `END_DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `IMPORTATION_DATE` timestamp NULL default NULL,
  `KEY_EMPLOYEE` int(11), OID_EMPLOYEE bigint unsigned default null,
  `KEY_PROFESSIONAL_CATEGORY` int(11), OID_PROFESSIONAL_CATEGORY bigint unsigned default null,
  `KEY_PROFESSIONAL_RELATION` int(11), OID_PROFESSIONAL_RELATION bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `MODIFIED_DATE` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_EMPLOYEE),
  index (OID_EMPLOYEE),
  index (KEY_PROFESSIONAL_CATEGORY),
  index (OID_PROFESSIONAL_CATEGORY),
  index (KEY_PROFESSIONAL_RELATION),
  index (OID_PROFESSIONAL_RELATION),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `PROFESSIONAL_CATEGORY` (
  `CATEGORY_TYPE` text,
  `GIAF_ID` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `NAME` text,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `PROFESSIONAL_REGIME` (
  `FULL_TIME_EQUIVALENT` text,
  `GIAF_ID` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `NAME` text,
  `WEIGHTING` int(11),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `PROFESSIONAL_RELATION` (
  `FULL_TIME_EQUIVALENT` tinyint(1),
  `GIAF_ID` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11), OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
  `NAME` text,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

