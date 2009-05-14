


-- Inserted at 2009-05-05T19:07:15.828+01:00

alter table `FILE` add column `KEY_JOB` int(11);
alter table `FILE` add index (`KEY_INDIVIDUAL_CANDIDACY`), add index (`OID_INDIVIDUAL_CANDIDACY`);
alter table `FILE` add index (`KEY_JOB`), add index (`OID_JOB`);
alter table `INDIVIDUAL_CANDIDACY_PERSONAL_DETAILS` add index (`KEY_COUNTRY_OF_RESIDENCE`), add index (`OID_COUNTRY_OF_RESIDENCE`);
alter table `INDIVIDUAL_CANDIDACY_PERSONAL_DETAILS` add index (`KEY_NATIONALITY`), add index (`OID_NATIONALITY`);
alter table `PROCESS` add index (`KEY_CANDIDACY_HASH_CODE`), add index (`OID_CANDIDACY_HASH_CODE`);
alter table `PUBLIC_CANDIDACY_HASH_CODE` add index (`KEY_INDIVIDUAL_CANDIDACY_PROCESS`), add index (`OID_INDIVIDUAL_CANDIDACY_PROCESS`);
alter table `PUBLIC_CANDIDACY_HASH_CODE` add index (`KEY_ROOT_DOMAIN_OBJECT`), add index (`OID_ROOT_DOMAIN_OBJECT`);
alter table `QUALIFICATION` add index (`KEY_CONCLUSION_EXECUTION_YEAR`), add index (`OID_CONCLUSION_EXECUTION_YEAR`);
alter table `QUALIFICATION` add index (`KEY_INDIVIDUAL_CANDIDACY`), add index (`OID_INDIVIDUAL_CANDIDACY`);
alter table `QUEUE_JOB` add column `KEY_FILE` int(11), add column `OID_FILE` bigint unsigned default null;
alter table `QUEUE_JOB` add index (`KEY_FILE`), add index (`OID_FILE`);


create table `FF$PERSISTENT_ROOT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `ROOT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID)
) type=InnoDB, character set latin1 ;




-- Inserted at 2009-05-13T18:08:53.784+01:00

alter table `FILE` add index (`KEY_INDIVIDUAL_CANDIDACY`), add index (`OID_INDIVIDUAL_CANDIDACY`);
alter table `FILE` add index (`KEY_JOB`), add index (`OID_JOB`);
alter table `INDIVIDUAL_CANDIDACY_PERSONAL_DETAILS` add index (`KEY_COUNTRY_OF_RESIDENCE`), add index (`OID_COUNTRY_OF_RESIDENCE`);
alter table `INDIVIDUAL_CANDIDACY_PERSONAL_DETAILS` add index (`KEY_NATIONALITY`), add index (`OID_NATIONALITY`);
alter table `PROCESS` add column `PROCESS_CODE` text;
alter table `PROCESS` add index (`KEY_CANDIDACY_HASH_CODE`), add index (`OID_CANDIDACY_HASH_CODE`);
alter table `PUBLIC_CANDIDACY_HASH_CODE` add index (`KEY_INDIVIDUAL_CANDIDACY_PROCESS`), add index (`OID_INDIVIDUAL_CANDIDACY_PROCESS`);
alter table `PUBLIC_CANDIDACY_HASH_CODE` add index (`KEY_ROOT_DOMAIN_OBJECT`), add index (`OID_ROOT_DOMAIN_OBJECT`);
alter table `QUALIFICATION` add index (`KEY_CONCLUSION_EXECUTION_YEAR`), add index (`OID_CONCLUSION_EXECUTION_YEAR`);
alter table `QUALIFICATION` add index (`KEY_INDIVIDUAL_CANDIDACY`), add index (`OID_INDIVIDUAL_CANDIDACY`);
alter table `QUEUE_JOB` add column `KEY_FILE` int(11), add column `OID_FILE` bigint unsigned default null;
alter table `QUEUE_JOB` add index (`KEY_FILE`), add index (`OID_FILE`);
alter table `ROOT_DOMAIN_OBJECT` add column `KEY_SYSTEM_SENDER` int(11), add column `OID_SYSTEM_SENDER` bigint unsigned default null;
alter table `ROOT_DOMAIN_OBJECT` add index (`KEY_SYSTEM_SENDER`), add index (`OID_SYSTEM_SENDER`);
alter table `SENDER` add column `KEY_SYSTEM_ROOT_DOMAIN_OBJECT` int(11), add column `OID_SYSTEM_ROOT_DOMAIN_OBJECT` bigint unsigned default null;
alter table `SENDER` add index (`KEY_SYSTEM_ROOT_DOMAIN_OBJECT`), add index (`OID_SYSTEM_ROOT_DOMAIN_OBJECT`);


create table `FF$PERSISTENT_ROOT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment, OID bigint unsigned default null,
  `ROOT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID)
) type=InnoDB, character set latin1 ;

