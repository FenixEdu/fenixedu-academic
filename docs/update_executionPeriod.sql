
#--Add begin_date and end_date to the table EXECUTION_PERIOD
ALTER TABLE `EXECUTION_PERIOD` ADD `BEGIN_DATE` DATE NOT NULL AFTER `SEMESTER`;
ALTER TABLE `EXECUTION_PERIOD` ADD `END_DATE` DATE NOT NULL AFTER `BEGIN_DATE`;
update EXECUTION_PERIOD set begin_date='2002-09-16',end_date='2002-12-21' where id_internal=3;
update EXECUTION_PERIOD set begin_date='2003-02-19',end_date='2003-06-07' where id_internal=1;
update EXECUTION_PERIOD set begin_date='2003-09-15',end_date='2003-12-20' where id_internal=2;
create table COORDINATOR (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_DEGREE int(11) not null,
   KEY_TEACHER int(11) not null,
   RESPONSIBLE bit not null default '0',
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_DEGREE,KEY_TEACHER)
) type=InnoDB;
