alter table `PARTY` add `OID_SANTANDER_PHOTO_ENTRY` bigint unsigned;
create table `SANTANDER_PHOTO_ENTRY` (`OID` bigint unsigned, `SEQUENCE_NUMBER` int(11), `OID_NEXT` bigint unsigned, `OID_DOMAIN_META_OBJECT` bigint unsigned, `OID_PERSON` bigint unsigned, `OID_PREVIOUS` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
alter table `SANTANDER_SEQUENCE_NUMBER_GENERATOR` add `PHOTO_SEQUENCE_NUMBER` int(11);
update SANTANDER_SEQUENCE_NUMBER_GENERATOR set PHOTO_SEQUENCE_NUMBER = 0;
alter table `SANTANDER_PHOTO_ENTRY` add `WHEN_GENERATED` timestamp NULL default NULL;
