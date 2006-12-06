alter table RESULT change column MODIFYED_BY MODIFIED_BY varchar(250) default NULL;
alter table RESULT modify column LAST_MODIFICATION_DATE datetime;
alter table RESULT add column YEAR_BEGIN int(11);
alter table RESULT add column MONTH_BEGIN varchar(50);
