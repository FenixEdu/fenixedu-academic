


-- Inserted at 2010-02-17T12:26:09.549Z

alter table `EXECUTION_INTERVAL` add column `OID_ENROLMENT_INSTRUCTIONS` bigint(20);


create table `ENROLMENT_INSTRUCTIONS` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `INSTRUCTIONS` longtext,
  `OID` bigint(20),
  `OID_EXECUTION_SEMESTER` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

