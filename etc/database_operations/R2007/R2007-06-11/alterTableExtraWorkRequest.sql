alter table EXTRA_WORK_REQUEST add column APPROVED tinyint(1) default NULL;
alter table EXTRA_WORK_REQUEST add column AMOUNT double default '0';
alter table EXTRA_WORK_REQUEST add HOURS_DONE_IN_PARTIAL_DATE text NOT NULL;
alter table EXTRA_WORK_REQUEST change column PARTIAL_DATE PARTIAL_PAYING_DATE text NOT NULL;
