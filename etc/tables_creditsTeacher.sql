----------------------------
-- Table structure for TEACHER_SHIFT_PERCENTAGE
----------------------------
drop table if exists TEACHER_SHIFT_PERCENTAGE;
create table TEACHER_SHIFT_PERCENTAGE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_PROFESSOR_SHIP int(11) not null,
   KEY_SHIFT int(11) not null,
   PERCENTAGE float not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_PROFESSOR_SHIP, KEY_SHIFT))
   type=InnoDB;
   
----------------------------
-- Table structure for CREDITS
----------------------------
drop table if exists CREDITS;
create table CREDITS (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_PERIOD int(11) not null,
   KEY_TEACHER int(11) not null,
   TFC_STUDENTS_NUMBER int(11) not null,
   CREDITS float,
   ADDITIONAL_CREDITS float,
   ADDITIONAL_CREDITS_JUSTIFICATION varchar (250),
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_PERIOD, KEY_TEACHER))
   type=InnoDB;
   
   