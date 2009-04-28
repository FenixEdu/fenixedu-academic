


-- Inserted at 2009-01-26T11:43:06.909Z

alter table QUEUE_JOB add column KEY_PERSON int(11);
alter table QUEUE_JOB add index (KEY_PERSON);
alter table QUEUE_JOB drop column KEY_USER;
alter table QUEUE_JOB drop column REPORT_NAME;
alter table EXECUTION_INTERVAL drop column KEY_EUR_ACE_QUEUE_JOB_FILE;




