----------------------------
-- Table structure for SHIFT_PROFESSORSHIP
----------------------------
drop table if exists SHIFT_PROFESSORSHIP;
create table SHIFT_PROFESSORSHIP (
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
   CREDITS float,
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
   LONG_NAME varchar(250) not null,
   SHORT_NAME varchar(50) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE))
   type=InnoDB;

drop table if exists TEACHER_DEGREE_FINAL_PROJECT_STUDENT;
create table TEACHER_DEGREE_FINAL_PROJECT_STUDENT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT int(11) not null,
   KEY_TEACHER int(11) not null,   
   KEY_EXECUTION_YEAR int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_TEACHER, KEY_EXECUTION_YEAR)
)type=InnoDB;

----------------------------
-- Table structure for CREDIT_LINE
-- Types : Sabática, Outro, Dispensa de Serviço Docente, Cargos de Gestão
----------------------------
drop table if exists CREDIT_LINE;
create table CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_TEACHER int(11) not null,
   CREDITS float not null,
	 CREDIT_LINE_TYPE int(11) not null,
   EXPLANATION varchar(250) not null,
   START_DATE date not null,
   END_DATE date not null,
   primary key (ID_INTERNAL),
)type=InnoDB;


drop table if exists TEACHER_INSTITUTION_WORK_TIME;
create table TEACHER_INSTITUTION_WORK_TIME (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_TEACHER int(11) not null,
   KEY_EXECUTION_PERIOD int(11) not null,
   WEEKDAY int(11) not null,
   START_TIME time,
   END_TIME time,   
   primary key (ID_INTERNAL),
   unique U1 (KEY_TEACHER, KEY_EXECUTION_PERIOD, WEEKDAY, START_TIME)
)type=InnoDB;
