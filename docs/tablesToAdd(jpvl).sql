ALTER TABLE TEACHER_DEGREE_FINAL_PROJECT_STUDENT change KEY_EXECUTION_YEAR KEY_EXECUTION_PERIOD int(11) not null;

update TEACHER_DEGREE_FINAL_PROJECT_STUDENT set KEY_EXECUTION_PERIOD = 2;

drop table if exists TEACHER_SHIFT_PERCENTAGE;

drop table if exists CREDITS;
create table CREDITS (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EXECUTION_PERIOD int(11) not null,
   KEY_TEACHER int(11) not null,
   SUPPORT_LESSONS float,
   INSTITUTION_WORK_TIME float,
   LESSONS float,
   DEGREE_FINAL_PROJECT_STUDENTS float,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_PERIOD, KEY_TEACHER))
   type=InnoDB;