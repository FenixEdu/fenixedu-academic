create table `TEACHER_CREDITS` (
  `BALANCE_OF_CREDITS` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `INSTITUTION_WORKING_HOURS` longtext,
  `LAST_MODIFIED_DATE` timestamp NULL default NULL,
  `MANAGEMENT_CREDITS` longtext,
  `MANDATORY_LESSON_HOURS` longtext,
  `MASTER_DEGREE_CREDITS` longtext,
  `OID` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_TEACHER` bigint(20),
  `OID_TEACHER_CREDITS_STATE` bigint(20),
  `OTHER_CREDITS` longtext,
  `PAST_SERVICE_CREDITS` longtext,
  `SERVICE_EXEMPTION_CREDITS` longtext,
  `SUPPORT_LESSON_HOURS` longtext,
  `TEACHING_DEGREE_CREDITS` longtext,
  `TFC_ADVISE_CREDITS` longtext,
  `THESES_CREDITS` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_PERSON),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (OID_TEACHER_CREDITS_STATE)
) type=InnoDB, character set latin1 ;

create table `TEACHER_CREDITS_STATE` (
  `CREDIT_STATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `LAST_MODIFIED_DATE` timestamp NULL default NULL,
  `OID` bigint(20),
  `OID_EXECUTION_SEMESTER` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_PERSON),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;




-- Inserted at 2010-04-13T11:59:30.437+01:00

alter table `TEACHER_CREDITS_STATE` add index (`OID_PERSON`);
alter table `TEACHER_CREDITS` add index (`OID_PERSON`);
alter table `TEACHER_CREDITS` add index (`OID_TEACHER`);





-- Inserted at 2010-04-14T11:46:53.898+01:00







-- Inserted at 2010-04-15T10:38:13.144+01:00

alter table `EXECUTION_INTERVAL` add column `OID_TEACHER_CREDITS_STATE` bigint(20);
alter table `EXECUTION_INTERVAL` add index (`OID_TEACHER_CREDITS_STATE`);





-- Inserted at 2010-04-15T16:28:43.766+01:00

alter table `TEACHER_CREDITS_STATE` add index (`OID_EXECUTION_SEMESTER`);





-- Inserted at 2010-04-15T16:35:47.083+01:00







-- Inserted at 2010-04-15T17:08:49.895+01:00

alter table `EXECUTION_INTERVAL` add column `OID_TEACHER_CREDITS_QUEUE_JOB` bigint(20);
alter table `QUEUE_JOB` add column `OID_EXECUTION_SEMESTER` bigint(20);





-- Inserted at 2010-04-22T10:52:47.243+01:00

alter table `QUEUE_JOB` add index (`OID_EXECUTION_SEMESTER`);


