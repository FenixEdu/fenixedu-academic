#----------------------------------
# Table structure for table student
#----------------------------------
DROP TABLE IF EXISTS STUDENT;
CREATE TABLE STUDENT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NUMBER smallint(10) unsigned NOT NULL default '0',
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  STATE int(11) unsigned NOT NULL default '1',
  DEGREE_TYPE integer(11) not null,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NUMBER, DEGREE_TYPE),
  UNIQUE KEY U2 (DEGREE_TYPE, KEY_PERSON)
) TYPE=InnoDB;

#----------------------------------
# Table structure for degree
#----------------------------------
drop table if exists DEGREE;
create table DEGREE (
   ID_INTERNAL int(11) not null,
   CODE varchar(100) not null,
   NAME varchar(100) not null,
   TYPE_DEGREE integer(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE), 
   unique U2 (NAME))
   type=InnoDB;

#-------------------------------------------
# Table structure for execution_degree
#-------------------------------------------
drop table if exists EXECUTION_DEGREE;
create table EXECUTION_DEGREE (
   ID_INTERNAL int(11) not null,
   ACADEMIC_YEAR varchar(100) not null,
   KEY_DEGREE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (ACADEMIC_YEAR, KEY_DEGREE))
   type=InnoDB;
   
#------------------------------------------
# Table structure for degree_curricular_plan
#------------------------------------------
drop table if exists DEGREE_CURRICULAR_PLAN;
create table DEGREE_CURRICULAR_PLAN (
   ID_INTERNAL integer(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   KEY_DEGREE integer(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, CODE, KEY_DEGREE))
   type=InnoDB;   
   
   
#------------------------------------------
# Table structure for STUDENT_CURRICULAR_PLAN
#------------------------------------------
drop table if exists STUDENT_CURRICULAR_PLAN;
create table STUDENT_CURRICULAR_PLAN (
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_STUDENT integer(11) not null,
   KEY_COURSE_CURRICULAR_PLAN integer(11) not null,
   CURRENT_STATE integer(11) not null,
   START_DATE date not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_COURSE_CURRICULAR_PLAN, CURRENT_STATE))
   type=InnoDB;
   