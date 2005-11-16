alter table QUALIFICATION add column `TYPE` varchar(100) NOT NULL;
alter table QUALIFICATION add column `YEAR` varchar(10) default NULL;
alter table QUALIFICATION change column `DEGREE` `DEGREE` varchar(200) default NULL;
alter table QUALIFICATION change column `MARK` `MARK` varchar(100) default NULL;
