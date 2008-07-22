alter table PARTY add column OFFICIAL tinyint(1) null;
alter table PARTY add column CODE varchar(20) null;
alter table PARTY add column INSTITUTION_TYPE varchar(100) default null;