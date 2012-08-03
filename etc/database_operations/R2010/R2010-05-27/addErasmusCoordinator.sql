


-- Inserted at 2010-05-27T10:13:13.169+01:00




create table `ERASMUS_COORDINATOR` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_DEGREE` bigint(20),
  `OID_PROCESS` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_TEACHER` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_DEGREE),
  index (OID_PROCESS),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (OID_TEACHER)
) type=InnoDB, character set latin1 ;

