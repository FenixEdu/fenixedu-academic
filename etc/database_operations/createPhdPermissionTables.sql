
alter table `ADMINISTRATIVE_OFFICE` add column `OID_PHD_PROCESSES_MANAGER` bigint(20);

create table `PHD_PROCESSES_MANAGER` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20) NOT NULL,
  `OID_ADMINISTRATIVE_OFFICE` bigint(20) NOT NULL,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) NOT NULL,
  `WHEN_CREATED` datetime NOT NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `PHD_PERMISSION` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20) NOT NULL,
  `OID_PHD_PROCESSES_MANAGER` bigint(20) NOT NULL,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) NOT NULL,
  `TYPE` text NOT NULL,
  `WHEN_CREATED` datetime NOT NULL,
  `MEMBERS` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_PHD_PROCESSES_MANAGER),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
