create table `CYCLE_COURSE_GROUP_INFORMATION` (
  `GRADUATED_TITLE` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_CYCLE_COURSE_GROUP` bigint(20),
  `OID_EXECUTION_YEAR` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_CYCLE_COURSE_GROUP),
  index (OID_EXECUTION_YEAR),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

