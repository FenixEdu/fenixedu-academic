alter table EXECUTION_YEAR ADD BEGIN_DATE date NOT NULL default '0000-00-00';
alter table EXECUTION_YEAR ADD END_DATE date NOT NULL default '0000-00-00';

update EXECUTION_YEAR set BEGIN_DATE = '2002-08-01' , END_DATE = '2003-07-31' where id_internal = 1; -- 2002/2003
update EXECUTION_YEAR set BEGIN_DATE = '2003-08-01', END_DATE =  '2004-07-31' where id_internal = 2; -- 2003/2004


