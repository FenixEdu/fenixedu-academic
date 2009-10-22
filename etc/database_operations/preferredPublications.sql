alter table `RESEARCH_RESULT` add column `OID_PERSON_THAT_PREFERS` bigint(20);

create table `PREFERRED_PUBLICATION` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_PERSON_THAT_PREFERS` bigint(20),
  `OID_PREFERRED_PUBLICATION` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `PRIORITY` text,
  primary key (ID_INTERNAL),
  index (OID)
) type=InnoDB, character set latin1 ;
