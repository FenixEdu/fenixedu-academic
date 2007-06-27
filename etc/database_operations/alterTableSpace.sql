update EXECUTION_DEGREE set KEY_CAMPUS = 2177 where KEY_CAMPUS = 1;
update EXECUTION_DEGREE set KEY_CAMPUS = 2178 where KEY_CAMPUS = 2;

alter table RESOURCE add column LESSON_OCCUPATIONS_ACCESS_GROUP text;
alter table RESOURCE add column WRITTEN_EVALUATION_OCCUPATIONS_ACCESS_GROUP text;
alter table RESOURCE add column GENERIC_EVENT_OCCUPATIONS_ACCESS_GROUP text;

alter table RESOURCE drop column CAPACIDADE_NORMAL;
alter table RESOURCE drop column CAPACIDADE_EXAME;
alter table RESOURCE drop column TIPO;
alter table RESOURCE drop column KEY_OLD_CAMPUS;
alter table RESOURCE drop column KEY_BUILDING;
alter table RESOURCE drop column KEY_CAMPUS;

alter table RESOURCE_ALLOCATION add column `START_TIME_DATE_HOUR_MINUTE_SECOND` time default '00:00:00';
alter table RESOURCE_ALLOCATION add column `END_TIME_DATE_HOUR_MINUTE_SECOND` time default '00:00:00';
alter table RESOURCE_ALLOCATION add column `DAY_OF_WEEK` int(11) default NULL;
alter table RESOURCE_ALLOCATION add column `KEY_PERIOD` int(11) default NULL;
alter table RESOURCE_ALLOCATION add column `WEEK_OF_QUINZENAL_START` int(11) default NULL;
alter table RESOURCE_ALLOCATION add column `KEY_WRITTEN_EVALUATION` int(11) default NULL;
alter table RESOURCE_ALLOCATION add column `KEY_LESSON` int(11) default NULL;
alter table RESOURCE_ALLOCATION add column `KEY_GENERIC_EVENT` int(11) default NULL;
alter table RESOURCE_ALLOCATION add column `DAILY_FREQUENCY_MARK_SUNDAY` tinyint(1) default '1';
alter table RESOURCE_ALLOCATION add column `DAILY_FREQUENCY_MARK_SATURDAY` tinyint(1) default '1';

alter table RESOURCE_ALLOCATION add key `KEY_PERIOD` (`KEY_PERIOD`);
alter table RESOURCE_ALLOCATION add key `KEY_WRITTEN_EVALUATION` (`KEY_WRITTEN_EVALUATION`);
alter table RESOURCE_ALLOCATION add key `KEY_LESSON` (`KEY_LESSON`);
alter table RESOURCE_ALLOCATION add key `KEY_GENERIC_EVENT` (`KEY_GENERIC_EVENT`);

delete from RESOURCE where OJB_CONCRETE_CLASS like '%OldRoom%';
delete from RESOURCE where OJB_CONCRETE_CLASS like '%OldBuilding%';

drop table CAMPUS;
drop table BUILDING;
drop table ROOM;