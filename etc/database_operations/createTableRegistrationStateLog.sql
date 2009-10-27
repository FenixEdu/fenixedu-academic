
create table `REGISTRATION_STATE_LOG` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) NOT NULL,
  `OID_REGISTRATION` bigint(20) NOT NULL,
  `WHEN_CREATED` datetime NOT NULL,
  `STATE_DATE` datetime NOT NULL,
  `STATE_TYPE` longtext NOT NULL,
  `ACTION` int(11) NOT NULL,
  `WHO` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_REGISTRATION),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
