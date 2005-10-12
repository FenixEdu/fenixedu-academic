alter table UNIT add column TYPE varchar(80) default '';
alter table UNIT add column REAL_NAME varchar(100) default '';
alter table UNIT change column TYPE TYPE varchar(80) default 'UNKNOWN';