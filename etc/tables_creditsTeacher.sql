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
   
----------------------------
-- Table structure for CREDITS_MANAGER_DEPARTMENT
--  This table tells what departments that a person can manage the teacher credits.
----------------------------
drop table if exists CREDITS_MANAGER_DEPARTMENT;
create table CREDITS_MANAGER_DEPARTMENT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_PERSON int(11) not null,
   KEY_DEPARTMENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_PERSON, KEY_DEPARTMENT))
   type=InnoDB;
   
----------------------------
-- Table structure for CATEGORY
----------------------------
drop table if exists CATEGORY;
create table CATEGORY (
   ID_INTERNAL int(11) not null auto_increment,
   CODE varchar(20) not null,
   LONG_NAME varchar(50) not null,
   SHORT_NAME varchar(20) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE))
   type=InnoDB;


-- DEGREE FINAL PROJECT TABLES

drop table if exists DEGREE_FINAL_PROJECT;
create table DEGREE_FINAL_PROJECT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_YEAR int(11) not null,   
   TITLE varchar(255),
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_YEAR, TITLE))type=InnoDB;
   
drop table if exists DEGREE_FINAL_PROJECT_ORIENTATION;
create table DEGREE_FINAL_PROJECT_ORIENTATION (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_DEGREE_FINAL_PROJECT int(11) not null,
   KEY_TEACHER int(11) not null,
   CO_ORIENTATION bit default '0',
   PERCENTAGE double (3,2),
   primary key (ID_INTERNAL),
   unique U1 (KEY_DEGREE_FINAL_PROJECT,KEY_TEACHER)
 )type=InnoDB;

drop table if exists STUDENT_CURRICULAR_PLAN_DEGREE_FINAL_PROJECT;
create table STUDENT_CURRICULAR_PLAN_DEGREE_FINAL_PROJECT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_DEGREE_FINAL_PROJECT int(11),
   KEY_STUDENT_CURRICULAR_PLAN int (11),
   primary key (ID_INTERNAL),
   unique U1 (KEY_DEGREE_FINAL_PROJECT, KEY_STUDENT_CURRICULAR_PLAN)
)type=InnoDB;
   