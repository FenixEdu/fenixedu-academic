


-- Inserted at 2010-04-23T13:36:02.772+01:00

alter table `ACCOUNTABILITY` add column `OID_ACCOUNTABILITY_IMPORT_REGISTER` bigint(20);
alter table `PARTY` add column `OID_PARTY_IMPORT_REGISTER` bigint(20);


create table `IMPORT_REGISTER` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_ACCOUNTABILITY` bigint(20),
  `OID_PARTY` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `REMOTE_EXTERNAL_OID` longtext,
  `OJB_CONCRETE_CLASS` mediumtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `IMPORT_REGISTER_LOG` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `INSTANT` timestamp NULL default NULL,
  `OID` bigint(20),
  `OID_IMPORT_REGISTER` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_IMPORT_REGISTER),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

