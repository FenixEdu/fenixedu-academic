alter table `ROOT_DOMAIN_OBJECT` add `OID_LIBRARY_CARD_SYSTEM` bigint unsigned;
create table `LIBRARY_CARD_SYSTEM` (`HIGHER_CLEARENCE_GROUP` text, `OID` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID)) ENGINE=InnoDB, character set latin1;

