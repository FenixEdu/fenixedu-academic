alter table `FILE` add column `OID_GIAF_INTERFACE_DOCUMENT` bigint(20);

create table `GIAF_INTERFACE_DOCUMENT` (
  `CREATED_WHEN` timestamp NULL default NULL,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_GIAF_INTERFACE_FILE` bigint(20),
  `OID_MODIFIED_BY` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_MODIFIED_BY),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
