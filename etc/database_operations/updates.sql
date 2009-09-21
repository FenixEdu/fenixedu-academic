


-- Inserted at 2009-09-21T11:15:58.201+01:00

create table `CARD_GENERATION_REGISTER` (
  `EMISSION` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_PERSON` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `LINE_PREFIX` longtext,
  `OID` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `WITH_ACCOUNT_INFORMATION` tinyint(1),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_PERSON),
  index (OID_PERSON),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

