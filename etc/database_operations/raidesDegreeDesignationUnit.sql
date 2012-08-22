alter table `DEGREE_DESIGNATION` add `EDUCATION_LEVEL` text;
create table `INSTITUTION_UNIT_DEGREE_DESIGNATION` (`OID_DEGREE_DESIGNATION` bigint unsigned, `OID_UNIT` bigint unsigned, primary key (OID_DEGREE_DESIGNATION, OID_UNIT), index (OID_DEGREE_DESIGNATION), index (OID_UNIT)) ENGINE=InnoDB, character set latin1;
