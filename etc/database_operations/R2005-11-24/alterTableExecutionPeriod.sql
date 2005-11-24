alter table EXECUTION_PERIOD drop column LAST_MODIFICATION_DATE;
alter table EXECUTION_PERIOD drop column INQUIRY_RESPONSE_BEGIN;
alter table EXECUTION_PERIOD drop column INQUIRY_RESPONSE_END;

alter table EXECUTION_PERIOD add column LAST_MODIFICATION_DATE timestamp;
alter table EXECUTION_PERIOD add column INQUIRY_RESPONSE_BEGIN timestamp default 0;
alter table EXECUTION_PERIOD add column INQUIRY_RESPONSE_END timestamp default 0;
