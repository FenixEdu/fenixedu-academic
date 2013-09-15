alter table `PARTY` add `OID_SANTANDER_P_I_N` bigint unsigned;
create table `SANTANDER_P_I_N` (`ENCRYPTED_P_I_N` text, `OID` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_PERSON` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID)) ENGINE=InnoDB, character set utf8;
