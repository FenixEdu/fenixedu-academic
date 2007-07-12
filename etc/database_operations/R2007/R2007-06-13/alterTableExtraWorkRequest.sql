alter table EXTRA_WORK_REQUEST add column NORMAL_VACATIONS_DAYS int(2) default '0';
alter table EXTRA_WORK_REQUEST add column NORMAL_VACATIONS_AMOUNT double default '0';
alter table EXTRA_WORK_REQUEST add column ACCUMULATED_NORMAL_VACATIONS_AMOUNT double default '0';
alter table EXTRA_WORK_REQUEST add column NIGHT_VACATIONS_DAYS int(2) default '0';
alter table EXTRA_WORK_REQUEST add column NIGHT_VACATIONS_AMOUNT double default '0';
alter table EXTRA_WORK_REQUEST add column ACCUMULATED_NIGHT_VACATIONS_AMOUNT double default '0';
