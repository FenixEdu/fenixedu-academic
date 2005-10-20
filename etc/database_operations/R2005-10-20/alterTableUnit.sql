alter table UNIT add column TYPE varchar(80) default '';
alter table UNIT change column TYPE TYPE varchar(80) default 'UNKNOWN';

alter table UNIT drop column REAL_NAME;

alter table UNIT add column BEGIN_DATE date default NULL;
alter table UNIT add column END_DATE date default NULL;