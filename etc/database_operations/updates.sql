create table `PHD_NOTIFICATION` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_CANDIDACY_PROCESS` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `NUMBER` int(11) not null,
  `OID` bigint(20) not null,
  `OID_CANDIDACY_PROCESS` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `TYPE` varchar(255) not null,
  `STATE` varchar(255) not null,
  `YEAR` int(11) not null,
  `CREATED_BY` varchar(255) not null,
  `WHEN_CREATED` datetime not null,
  primary key (ID_INTERNAL),
  unique(YEAR,NUMBER),
  index (OID),
  index (KEY_CANDIDACY_PROCESS),
  index (OID_CANDIDACY_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;


-- Inserted at 2009-08-11T11:22:36.476+01:00

alter table `EVENT` add column `KEY_PROCESS` int(11);
alter table `EVENT` add column `OID_PROCESS` bigint(20);
alter table `EVENT` add index (`KEY_PROCESS`), add index (`OID_PROCESS`);

alter table `PROCESS` add column `KEY_REGISTRATION_FEE` int(11);
alter table `PROCESS` add column `OID_REGISTRATION_FEE` bigint(20);
alter table `PROCESS` add index (`KEY_REGISTRATION_FEE`), add index (`OID_REGISTRATION_FEE`);

alter table `PAYMENT_CODE` add index (`KEY_PERSON`), add index (`OID_PERSON`);
