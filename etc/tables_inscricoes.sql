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

#----------------------------------
# Table structure for degree_curricular_plan
#----------------------------------
DROP TABLE IF EXISTS DEGREE_CURRICULAR_PLAN;
CREATE TABLE IF NOT EXISTS DEGREE_CURRICULAR_PLAN (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(10) NOT NULL DEFAULT '' ,
  KEY_DEGREE int(11) NOT NULL DEFAULT '0' ,
  STATE int(11) DEFAULT '1' ,
  INITIAL_DATE date DEFAULT '0000-00-00' ,
  END_DATE date DEFAULT '0000-00-00' ,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,KEY_DEGREE)
) TYPE=InnoDB;
   
#------------------------------------------
# Table structure for STUDENT_CURRICULAR_PLAN
# FIXME : The current state should not belong the the unique of the class, should it?
#------------------------------------------
drop table if exists STUDENT_CURRICULAR_PLAN;
create table STUDENT_CURRICULAR_PLAN (
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_STUDENT integer(11) not null,
   KEY_DEGREE_CURRICULAR_PLAN integer(11) not null,
   CURRENT_STATE integer(11) not null,
   START_DATE date not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE))
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


#----------------------------
# Table structure for enrolment
# FIXME : This should associate students with execution courses, not with curricular courses.
#               The curricular course can be reached through the students curricular plan.
#----------------------------
drop table if exists ENROLMENT;
create table ENROLMENT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT_CURRICULAR_PLAN int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE))
   type=InnoDB;


