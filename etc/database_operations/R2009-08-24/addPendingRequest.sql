create table `PENDING_REQUEST` (
  `GENERATION_DATE` timestamp NULL default NULL,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `URL` text,
  `POST` tinyint(1),
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `PENDING_REQUEST_PARAMETER` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `PARAMETER_KEY` longtext,
  `KEY_PENDING_REQUEST` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `OID` bigint(20),
  `OID_PENDING_REQUEST` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `PARAMETER_VALUE` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_PENDING_REQUEST),
  index (OID_PENDING_REQUEST),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
