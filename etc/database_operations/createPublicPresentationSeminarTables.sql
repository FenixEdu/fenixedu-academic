


-- Inserted at 2009-08-27T11:49:24.964+01:00

alter table `PROCESS` add column `KEY_SEMINAR_PROCESS` int(11);
alter table `PROCESS` add column `OID_SEMINAR_PROCESS` bigint(20);
alter table `PROCESS` add column `PRESENTATION_DATE` text;
alter table `PROCESS` add index (`KEY_SEMINAR_PROCESS`), add index (`OID_SEMINAR_PROCESS`);


create table `SEMINAR_COMMISSION_ELEMENT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `CREATION_DATE` datetime NULL default NULL,
  `ADDRESS` longtext,
  `EMAIL` longtext,
  `INSTITUTION` longtext,
  `KEY_PERSON` int(11),
  `KEY_PROCESS` int(11),
  `NAME` longtext,
  `OID` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_PROCESS` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `CATEGORY` longtext,
  `QUALIFICATION` longtext,
  `PHONE` longtext,
  `POSITION` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_PERSON),
  index (OID_PERSON),
  index (KEY_PROCESS),
  index (OID_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
