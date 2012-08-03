alter table GROUPING add column `AUTOMATIC_ENROLMENT` tinyint(1) unsigned DEFAULT NULL;
update GROUPING set AUTOMATIC_ENROLMENT=0;
