drop table if exists CREDITS;
create table CREDITS (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_PERIOD int(11) not null,
   KEY_TEACHER int(11) not null,
   TFC_STUDENTS_NUMBER int(11) not null,
   CREDITS float,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_PERIOD, KEY_TEACHER))
   type=InnoDB;
   
#----------------------------
# Table structure for exam_student_room
#----------------------------
drop table if exists EXAM_STUDENT_ROOM;
create table EXAM_STUDENT_ROOM (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXAM int(11) not null,
   KEY_STUDENT int(11) not null,
   KEY_ROOM int(11) not null,   
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXAM, KEY_STUDENT, KEY_ROOM))
   type=InnoDB;

ALTER TABLE DEGREE_CURRICULAR_PLAN ADD MARK_TYPE integer (11);
ALTER TABLE DEGREE_CURRICULAR_PLAN ADD NEEDED_CREDITS float (11,2);
ALTER TABLE ATTEND ADD KEY_ENROLMENT int(11);