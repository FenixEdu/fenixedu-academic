create table `PHD_ALERT` (
  `BODY` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_PROCESS` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT_FOR_ACTIVE_PHD_ALERTS` int(11) null,
  `OID` bigint(20) not null,
  `OID_PROCESS` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT_FOR_ACTIVE_PHD_ALERTS` bigint(20) null,
  `SEND_EMAIL` tinyint(1) not null,
  `SUBJECT` longtext not null,
  `TARGET_GROUP` longtext not null,
  `OJB_CONCRETE_CLASS` varchar(255) not null,
  `FIRE_DATE` varchar(10) null,
  `ACTIVE` tinyint(1) not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_PROCESS),
  index (OID_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (`KEY_ROOT_DOMAIN_OBJECT_FOR_ACTIVE_PHD_ALERTS`),
  index (`OID_ROOT_DOMAIN_OBJECT_FOR_ACTIVE_PHD_ALERTS`)
) type=InnoDB, character set latin1 ;


create table `PHD_ALERT_MESSAGE` (
  `BODY` longtext not null,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_PERSON` int(11) not null,
  `KEY_PROCESS` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `OID` bigint(20) not null,
  `OID_PERSON` bigint(20) not null,
  `OID_PROCESS` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `READED` tinyint(1) not null,
  `SUBJECT` longtext not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_PERSON),
  index (OID_PERSON),
  index (KEY_PROCESS),
  index (OID_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

