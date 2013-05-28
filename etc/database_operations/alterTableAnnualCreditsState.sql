alter table `ANNUAL_CREDITS_STATE` add `SHARED_UNIT_CREDITS_INTERVAL` text, add `UNIT_CREDITS_INTERVAL` text;

create table `DEPARTMENT_CREDITS_POOL` (`OID` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_DEPARTMENT` bigint unsigned, `CREDITS_POOL` text, `OID_EXECUTION_YEAR` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_DEPARTMENT), index (OID_EXECUTION_YEAR), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
alter table `EXECUTION_COURSE` add `EFFORT_RATE` text;

