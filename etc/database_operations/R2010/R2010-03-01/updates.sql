


-- Inserted at 2010-03-01T11:34:55.561Z

alter table `ROOT_DOMAIN_OBJECT` add column `OID_IRS_DECLARATION_LINK` bigint(20);


create table `IRS_DECLARATION_LINK` (
  `AVAILABLE` tinyint(1),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `IRS_LINK` longtext,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `TITLE` longtext,
  primary key (ID_INTERNAL),
  index (OID)
) type=InnoDB, character set latin1 ;

