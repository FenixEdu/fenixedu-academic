alter table `ACADEMIC_SERVICE_REQUEST` add column `OID_REGISTRY_CODE` bigint(20), add column `OID_DIPLOMA_SUPPLEMENT` bigint(20), add column `OID_REGISTRY_DIPLOMA_REQUEST` bigint(20);
alter table `PARTY` add column `OID_REGISTRY_CODE_GENERATOR` bigint(20);

create table `INSTITUTION_REGISTRY_CODE_GENERATOR` (
  `CURRENT` int(11),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_INSTITUTION` bigint(20),
  `OID_CURRENT_BAG` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `REGISTRY_CODE` (
  `CODE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_REGISTRY_CODE_BAG` bigint(20),
  `OID_REGISTRY_CODE_GENERATOR` bigint(20),
  `OID_DOCUMENT_REQUEST` bigint(20),
  `OJB_CONCRETE_CLASS` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_REGISTRY_CODE_BAG),
  index (OID_REGISTRY_CODE_GENERATOR)
) type=InnoDB, character set latin1 ;

create table `REGISTRY_CODE_BAG` (
  `DATE` timestamp NULL default NULL,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_NEXT` bigint(20),
  `OID_PREVIOUS` bigint(20),
  `OID_REGISTRY_CODE_GENERATOR` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
