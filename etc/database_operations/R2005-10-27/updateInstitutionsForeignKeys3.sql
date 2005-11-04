alter table EXTERNAL_PERSON change KEY_WORK_LOCATION KEY_INSTITUTION int(11) unsigned NOT NULL default 0;
drop table WORK_LOCATION;