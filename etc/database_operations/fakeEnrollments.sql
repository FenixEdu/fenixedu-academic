create table `FAKE_SHIFT` (`OID` bigint unsigned, `NAME` text, `CAPACITY` int(11), `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;
alter table `FAKE_ENROLLMENT` add `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, add index (OID_ROOT_DOMAIN_OBJECT);
