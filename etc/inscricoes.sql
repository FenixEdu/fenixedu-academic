#------------------------------------------
# Table structure for country
#------------------------------------------
drop table if exists COUNTRY;
create table COUNTRY (
   ID_INTERNAL integer(11) not null auto_increment,
   NAME varchar(50) not null,
   NATIONALITY varchar(50) not null,
   CODE varchar(10) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME),
   unique U2 (NATIONALITY),
   unique U3 (CODE))
   type=InnoDB;

#------------------------------------------
# Table structure for curricular_course
# key_department should be not null
#   ainda não se tem a disciplina departamento
#------------------------------------------
drop table if exists CURRICULAR_COURSE;
create table CURRICULAR_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_DEPARTMENT_COURSE int(11),
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
   CREDITS double,
   THEORETICAL_HOURS double,
   PRATICAL_HOURS double,
   THEO_PRAT_HOURS double,
   LAB_HOURS double,
   CURRICULAR_YEAR int(11),
   SEMESTER int(11),
   NAME varchar(100),
   CODE varchar(50) not null,
   primary key (ID_INTERNAL ),
   unique U1 (CODE, NAME,SEMESTER, CURRICULAR_YEAR,  KEY_DEGREE_CURRICULAR_PLAN))
   type=InnoDB;

#------------------------------------------
# Table structure for execution_course
#------------------------------------------
DROP TABLE IF EXISTS EXECUTION_COURSE;
CREATE TABLE EXECUTION_COURSE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(100) default NULL,
  CODE varchar(50) NOT NULL default '',
  KEY_EXECUTION_DEGREE int(11),
  KEY_TEACHER_IN_CHARGE int(11) NOT NULL default '0',
  PROGRAM text,
  THEORETICAL_HOURS double default NULL,
  PRATICAL_HOURS double default NULL,
  THEO_PRAT_HOURS double default NULL,
  LAB_HOURS double default NULL,
  SEMESTER int(11) default '2',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (ID_INTERNAL, CODE, KEY_EXECUTION_PERIOD)
) TYPE=InnoDB;

#---------------------------------------------------------------
# Table structure for curricular_course_execution_course
#---------------------------------------------------------------
drop table if exists CURRICULAR_COURSE_EXECUTION_COURSE;
create table CURRICULAR_COURSE_EXECUTION_COURSE (
   INTERNAL_CODE integer(11) not null auto_increment,
   KEY_CURRICULAR_COURSE integer(11) not null,
   KEY_EXECUTION_COURSE integer(11) not null,
   primary key (INTERNAL_CODE),
   unique U1 (KEY_CURRICULAR_COURSE, KEY_EXECUTION_COURSE))
   type=InnoDB;


#------------------------------------------
# Table structure for department
#------------------------------------------
drop table if exists DEPARTMENT;
create table DEPARTMENT (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME), 
   unique U2 (CODE))
   type=InnoDB;

#------------------------------------------
# Table structure for DEPARTMENT_COURSE
#------------------------------------------
drop table if exists DEPARTMENT_COURSE;
create table DEPARTMENT_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   KEY_DEPARTMENT integer(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, CODE))
   type=InnoDB;