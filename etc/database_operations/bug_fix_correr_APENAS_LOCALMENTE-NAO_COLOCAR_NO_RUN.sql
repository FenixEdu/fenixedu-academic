alter table EVALUATION change column BEGIN_DATE_TIME PROJECT_BEGIN_DATE_TIME  timestamp NOT NULL default '0000-00-00 00:00:00';
alter table EVALUATION change column END_DATE_TIME PROJECT_END_DATE_TIME  timestamp NOT NULL default '0000-00-00 00:00:00';
