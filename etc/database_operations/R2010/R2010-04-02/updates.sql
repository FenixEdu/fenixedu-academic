


-- Inserted at 2010-04-01T23:44:21.425+01:00

create table `CERIMONY_INQUIRY` (
  `BEGIN` timestamp NULL default NULL,
  `DESCRIPTION` longtext,
  `END` timestamp NULL default NULL,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `TEXT` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `CERIMONY_INQUIRY_ANSWER` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_CERIMONY_INQUIRY` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `TEXT` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_CERIMONY_INQUIRY),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `CERIMONY_INQUIRY_PERSON` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_CERIMONY_INQUIRY` bigint(20),
  `OID_CERIMONY_INQUIRY_ANSWER` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_CERIMONY_INQUIRY),
  index (OID_CERIMONY_INQUIRY_ANSWER),
  index (OID_PERSON),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;




-- Inserted at 2010-04-02T00:39:58.082+01:00

alter table `CERIMONY_INQUIRY_ANSWER` add column `ANSWER_ORDER` int(11);


