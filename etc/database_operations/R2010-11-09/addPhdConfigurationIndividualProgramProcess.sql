alter table `QUEUE_JOB` add index (OID_ERASMUS_CANDIDACY_PROCESS);
create table `PHD_CONFIGURATION_INDIVIDUAL_PROGRAM_PROCESS` (`OID` bigint unsigned, `OID_PHD_INDIVIDUAL_PROGRAM_PROCESS` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `GENERATE_ALERT` tinyint(1), `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) type=InnoDB, character set latin1;
alter table `PROCESS` add `OID_PHD_CONFIGURATION_INDIVIDUAL_PROGRAM_PROCESS` bigint unsigned;
