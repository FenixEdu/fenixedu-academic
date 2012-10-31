create table `SANTANDER_BATCH_REQUESTER` (`OID_SANTANDER_BATCH` bigint unsigned, `OID` bigint unsigned, `OID_PERSON` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;

create table `SANTANDER_ENTRY` (`OID` bigint unsigned, `OID_SANTANDER_BATCH` bigint unsigned, `OID_PERSON` bigint unsigned, `LINE` text, `CREATED` timestamp NULL default NULL, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_SANTANDER_BATCH), index (OID_PERSON), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;

alter table `PARTY` add `OID_SANTANDER_BATCH_REQUESTERS` bigint unsigned, add `OID_SANTANDER_BATCH_SENDERS` bigint unsigned;

create table `SANTANDER_BATCH_SENDER` (`OID_SANTANDER_BATCH` bigint unsigned, `OID` bigint unsigned, `OID_PERSON` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;

create table `SANTANDER_BATCH` (`OID_SANTANDER_BATCH_REQUESTER` bigint unsigned, `OID` bigint unsigned, `SEQUENCE_NUMBER` int(11), `SENT` timestamp NULL default NULL, `OID_SANTANDER_BATCH_SENDER` bigint unsigned, `GENERATED` timestamp NULL default NULL, `OID_EXECUTION_YEAR` bigint unsigned, `CREATED` timestamp NULL default NULL, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `OID_SANTANDER_SEQUENCE_NUMBER_GENERATOR` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_EXECUTION_YEAR), index (OID_ROOT_DOMAIN_OBJECT), index (OID_SANTANDER_SEQUENCE_NUMBER_GENERATOR)) ENGINE=InnoDB, character set utf8;

create table `SANTANDER_SEQUENCE_NUMBER_GENERATOR` (`OID` bigint unsigned, `SEQUENCE_NUMBER` int(11), `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;

create table `SANTANDER_PROBLEM` (`OID` bigint unsigned, `OID_SANTANDER_BATCH` bigint unsigned, `ARG` text, `DESCRIPTION_KEY` text, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_SANTANDER_BATCH), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set utf8;
