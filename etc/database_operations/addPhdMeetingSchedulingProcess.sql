alter table `PHD_PROCESS_STATE` add `OID_MEETING_PROCESS` bigint unsigned, add index (OID_MEETING_PROCESS);
create table `PHD_MEETING` (`OID` bigint unsigned, `MEETING_PLACE` text, `MEETING_DATE` timestamp NULL default NULL, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `OID_MEETING_PROCESS` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT), index (OID_MEETING_PROCESS)) type=InnoDB, character set latin1;
alter table `PROCESS` add `OID_MEETING_PROCESS` bigint unsigned;
alter table `FILE` add `OID_PHD_MEETING` bigint unsigned;
