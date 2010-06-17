create table `EXECUTED_ACTION` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OJB_CONCRETE_CLASS` text,
  `OID_APPROVED_LEARNING_AGREEMENT` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_WHO_MADE` bigint(20),
  `TYPE` text,
  `WHEN_OCCURED` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_APPROVED_LEARNING_AGREEMENT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (OID_WHO_MADE)
) type=InnoDB, character set latin1 ;

