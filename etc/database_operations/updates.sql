drop table if exists QUEUE_JOB_CONTEXT;

create table QUEUE_JOB (
  `CONTENT` longblob,
  `CONTENT_TYPE` text,
  `DONE` tinyint(1),
  `FAILED_COUNTER` int(11),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `JOB_END_TIME` timestamp NULL default NULL,
  `JOB_START_TIME` timestamp NULL default NULL,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_ROOT_DOMAIN_OBJECT_QUEUE_UNDONE` int(11),
  `KEY_USER` int(11),
  `OJB_CONCRETE_CLASS` text,
  `REQUEST_DATE` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_ROOT_DOMAIN_OBJECT_QUEUE_UNDONE),
  index (KEY_USER)
) type=InnoDB ;




-- Inserted at 2009-01-11T17:41:12.995Z

alter table EXECUTION_INTERVAL add column KEY_EUR_ACE_QUEUE_JOB_FILE int(11);
alter table EXECUTION_INTERVAL add index (KEY_EUR_ACE_QUEUE_JOB_FILE);
alter table QUEUE_JOB add column DEGREE_TYPE text;
alter table QUEUE_JOB add column KEY_EXECUTION_YEAR int(11);
alter table QUEUE_JOB add column REPORT_NAME text;
alter table QUEUE_JOB add column TYPE text;
alter table QUEUE_JOB add index (KEY_EXECUTION_YEAR);





-- Inserted at 2009-01-16T12:11:10.949Z




