alter table `PROCESS` add column `KEY_PHD_INDIVIDUAL_PROCESS_NUMBER` int(11) null;
alter table `PROCESS` add column `OID_PHD_INDIVIDUAL_PROCESS_NUMBER` bigint(20) null;
alter table `PROCESS` add index (`KEY_PHD_INDIVIDUAL_PROCESS_NUMBER`), add index (`OID_PHD_INDIVIDUAL_PROCESS_NUMBER`);

create table `PHD_INDIVIDUAL_PROGRAM_PROCESS_NUMBER` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_PROCESS` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `NUMBER` int(11) not null,
  `OID` bigint(20) not null,
  `OID_PROCESS` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `YEAR` int(11) not null,
  primary key (ID_INTERNAL),
  unique (NUMBER,YEAR),
  index (OID),
  index (KEY_PROCESS),
  index (OID_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

