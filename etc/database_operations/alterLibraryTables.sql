alter table `FILE` add `OID_LIBRARY_DOCUMENT` bigint unsigned;
create table `LIBRARY_DOCUMENT` (`OID_CARD_DOCUMENT` bigint unsigned, `OID` bigint unsigned, `OID_LETTER_DOCUMENT` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) type=InnoDB, character set latin1;
