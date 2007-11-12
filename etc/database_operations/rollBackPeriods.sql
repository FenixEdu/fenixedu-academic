
alter table EXECUTION_PERIOD add column BEGIN_DATE_YEAR_MONTH_DAY varchar(10) NOT NULL default '';
alter table EXECUTION_PERIOD add column END_DATE_YEAR_MONTH_DAY varchar(10) NOT NULL default '';
alter table EXECUTION_PERIOD add column SEMESTER  int(1) default 0;


alter table EXECUTION_PERIOD add column KEY_NEXT_EXECUTION_PERIOD int(11) not null default 0;
alter table EXECUTION_PERIOD add column KEY_PREVIOUS_EXECUTION_PERIOD int(11) not null default 0;


alter table EXECUTION_YEAR add column BEGIN_DATE_YEAR_MONTH_DAY varchar(10) NOT NULL default '';
alter table EXECUTION_YEAR add column END_DATE_YEAR_MONTH_DAY varchar(10) NOT NULL default '';
