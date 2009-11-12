


-- Inserted at 2009-11-12T17:18:32.932Z

alter table `EMAIL` add column `OID_MESSAGE` bigint(20);
alter table `EMAIL` add index (`OID_MESSAGE`);

create table `MESSAGE_ID` (
  `ID` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_MESSAGE` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_MESSAGE),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
