alter table UNIT add column TYPE varchar(80) default '';
alter table UNIT change column TYPE TYPE varchar(80) default 'UNKNOWN';