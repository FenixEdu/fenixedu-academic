create table `PHD_PROGRAM_FOCUS_AREA` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `NAME` longtext,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `WHEN_CREATED` datetime NULL default NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `PHD_PROGRAM_PHD_PROGRAM_FOCUS_AREA` (KEY_PHD_PROGRAM_FOCUS_AREA int(11) not null, KEY_PHD_PROGRAM int(11) not null, OID_PHD_PROGRAM_FOCUS_AREA bigint unsigned default null, OID_PHD_PROGRAM bigint unsigned default null,  primary key (KEY_PHD_PROGRAM_FOCUS_AREA, KEY_PHD_PROGRAM), key(KEY_PHD_PROGRAM_FOCUS_AREA), key(KEY_PHD_PROGRAM), key (OID_PHD_PROGRAM_FOCUS_AREA, OID_PHD_PROGRAM), key(OID_PHD_PROGRAM_FOCUS_AREA), key(OID_PHD_PROGRAM)) type=InnoDB;

alter table `PROCESS` add column `KEY_PHD_PROGRAM_FOCUS_AREA` int(11);
alter table `PROCESS` add column `OID_PHD_PROGRAM_FOCUS_AREA` bigint(20);
alter table `PROCESS` add index (`KEY_PHD_PROGRAM_FOCUS_AREA`), add index (`OID_PHD_PROGRAM_FOCUS_AREA`);

