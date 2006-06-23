alter table EVALUATION add column DESCRIPTION varchar(120) default NULL;

alter table EVALUATION add column NAME varchar(100) NOT NULL default '';
alter table EVALUATION add column PROJECT_BEGIN datetime  NOT NULL default '0000-00-00 00:00:00';
alter table EVALUATION add column PROJECT_END datetime  NOT NULL default '0000-00-00 00:00:00';