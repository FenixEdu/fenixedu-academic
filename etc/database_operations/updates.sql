-- Inserted at 2009-09-08T18:45:57.287+01:00

alter table `REGISTRATION` add column `KEY_MEASUREMENT_TEST_ROOM` int(11);
alter table `REGISTRATION` add column `OID_MEASUREMENT_TEST_ROOM` bigint(20);
alter table `REGISTRATION` add index (`KEY_MEASUREMENT_TEST_ROOM`), add index (`OID_MEASUREMENT_TEST_ROOM`);


create table `MEASUREMENT_TEST` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_CAMPUS` int(11) not null,
  `KEY_EXECUTION_YEAR` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `OID` bigint(20) not null,
  `OID_CAMPUS` bigint(20) not null,
  `OID_EXECUTION_YEAR` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_CAMPUS),
  index (OID_CAMPUS),
  index (KEY_EXECUTION_YEAR),
  index (OID_EXECUTION_YEAR),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `MEASUREMENT_TEST_ROOM` (
  `CAPACITY` int(11) not null,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `KEY_SHIFT` int(11) not null,
  `NAME` varchar(255) not null,
  `OID` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `OID_SHIFT` bigint(20) not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (KEY_SHIFT),
  index (OID_SHIFT),
  unique (KEY_SHIFT,NAME)
) type=InnoDB, character set latin1 ;

create table `MEASUREMENT_TEST_SHIFT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `KEY_TEST` int(11) not null,
  `NAME` varchar(255) not null,
  `OID` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `OID_TEST` bigint(20) not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (KEY_TEST),
  index (OID_TEST),
  unique (KEY_TEST,NAME)
) type=InnoDB, character set latin1 ;

