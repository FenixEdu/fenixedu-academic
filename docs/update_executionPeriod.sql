
#--Add begin_date and end_date to the table EXECUTION_PERIOD
ALTER TABLE `EXECUTION_PERIOD` ADD `BEGIN_DATE` DATE NOT NULL AFTER `SEMESTER`;
ALTER TABLE `EXECUTION_PERIOD` ADD `END_DATE` DATE NOT NULL AFTER `BEGIN_DATE`;

update EXECUTION_PERIOD set begin_date='2002-09-16',end_date='2002-12-21' where id_internal=3;
update EXECUTION_PERIOD set begin_date='2003-02-19',end_date='2003-06-07' where id_internal=1;
update EXECUTION_PERIOD set begin_date='2003-09-15',end_date='2003-12-20' where id_internal=2;
