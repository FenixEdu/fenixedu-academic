----------------------------
-- Table structure for SHIFT_PROFESSORSHIP
----------------------------
drop table if exists SHIFT_PROFESSORSHIP;
create table SHIFT_PROFESSORSHIP (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_PROFESSOR_SHIP int(11) not null,
   KEY_SHIFT int(11) not null,
   PERCENTAGE float not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_PROFESSOR_SHIP, KEY_SHIFT))
   type=InnoDB;
   
----------------------------
-- Table structure for CREDITS
----------------------------
drop table if exists crd_CREDITS;
create table crd_CREDITS (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EXECUTION_PERIOD int(11) not null,
   KEY_TEACHER int(11) not null,
   SUPPORT_LESSONS float,
   INSTITUTION_WORK_TIME float,
   LESSONS float,
   DEGREE_FINAL_PROJECT_STUDENTS float,
   OTHER_TYPE_CREDITS float,
   CONTAINS_MANAGEMENT_POSITIONS tinyint(1),
   CONTAINS_SERVICE_EXEMPTIONS_SITUATIONS tinyint(1),
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_PERIOD, KEY_TEACHER))
   type=InnoDB;
   
drop table if exists crd_OTHER_TYPE_CREDIT_LINE;
create table crd_OTHER_TYPE_CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   CREDITS float not null,
   REASON TEXT not null,
   KEY_EXECUTION_PERIOD	int(11) not null,
   primary key (ID_INTERNAL)
)type=InnoDB;   

drop table if exists crd_MANAGEMENT_POSITION_CREDIT_LINE;
create table crd_MANAGEMENT_POSITION_CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   POSITION TEXT not null,
   START_DATE DATE not null,
   END_DATE DATE not null,
   primary key (ID_INTERNAL)
)type=InnoDB;   

drop table if exists crd_EXEMPTION_SITUATION_CREDIT_LINE;
create table crd_EXEMPTION_SITUATION_CREDIT_LINE(
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   TYPE int(11) not null,
   START_DATE DATE not null,
   END_DATE DATE not null,
   primary key (ID_INTERNAL)
)type=InnoDB;   


drop table if exists CREDIT_LINE;
create table CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   CREDITS float not null,
   CREDIT_LINE_TYPE int(11) not null,
   EXPLANATION varchar(250) not null,
   START_DATE date not null,
   END_DATE date not null,
   primary key (ID_INTERNAL)
)type=InnoDB;   

----------------------------
-- Table structure for CREDITS_MANAGER_DEPARTMENT
--  This table tells what departments that a person can manage the teacher credits.
----------------------------
drop table if exists crd_CREDITS_MANAGER_DEPARTMENT;
create table crd_CREDITS_MANAGER_DEPARTMENT (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
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
   ACK_OPT_LOCK int(11),
   CODE varchar(20) not null,
   LONG_NAME varchar(250) not null,
   SHORT_NAME varchar(50) not null,
   CAN_BE_EXECUTION_COURSE_RESPONSIBLE bit not null default 0,
   primary key (ID_INTERNAL),
   unique U1 (CODE))
   type=InnoDB;

drop table if exists crd_TEACHER_DEGREE_FINAL_PROJECT_STUDENT;
create table crd_TEACHER_DEGREE_FINAL_PROJECT_STUDENT (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   PERCENTAGE float not null default '100.0',
   KEY_STUDENT int(11) not null,
   KEY_TEACHER int(11) not null,   
   KEY_EXECUTION_PERIOD int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_TEACHER, KEY_EXECUTION_PERIOD)
)type=InnoDB;




drop table if exists crd_TEACHER_INSTITUTION_WORK_TIME;
create table crd_TEACHER_INSTITUTION_WORK_TIME (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   KEY_EXECUTION_PERIOD int(11) not null,
   WEEKDAY int(11) not null,
   START_TIME time,
   END_TIME time,   
   primary key (ID_INTERNAL),
   unique U1 (KEY_TEACHER, KEY_EXECUTION_PERIOD, WEEKDAY, START_TIME)
)type=InnoDB;
