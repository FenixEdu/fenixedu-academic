alter table CONTRACT change column HOURS_NUMBER LESSON_HOURS_NUMBER int default NULL;
alter table CONTRACT add column TOTAL_HOURS_NUMBER double default NULL;
alter table CONTRACT add column LEGAL_REGIMEN_TYPE  varchar(100) default NULL;
alter table CONTRACT add column REGIMEN_TYPE  varchar(100) default NULL;
