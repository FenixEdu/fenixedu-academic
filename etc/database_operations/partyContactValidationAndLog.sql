alter table `FILE` add `OID_PYHSICAL_ADDRESS_VALIDATION` bigint unsigned;
alter table `PARTY` add `NUMBER_OF_VALIDATION_REQUESTS` int(11), add `LAST_VALIDATION_REQUEST_DATE` timestamp NULL default NULL;
create table `PARTY_CONTACT_VALIDATION` (`OID` bigint unsigned, `OID_FILE` bigint unsigned, `STATE` text, `DESCRIPTION` text, `LAST_CHANGE_DATE` timestamp NULL default NULL, `OID_PARTY_CONTACT` bigint unsigned, `TRIES` int(11), `REQUEST_DATE` timestamp NULL default NULL, `OJB_CONCRETE_CLASS` varchar(255) NOT NULL DEFAULT '', `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `TOKEN` text, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;
alter table `PARTY_CONTACT` add `OID_PREV_PARTY_CONTACT` bigint unsigned, add `OID_PARTY_CONTACT_VALIDATION` bigint unsigned, add `OID_CURRENT_PARTY_CONTACT` bigint unsigned, add `ACTIVE` tinyint(1) default 1;

