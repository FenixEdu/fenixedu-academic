#-------------------------------------------
# Table structure for execution_degree
#-------------------------------------------
DROP TABLE IF EXISTS EXECUTION_DEGREE;
CREATE TABLE EXECUTION_DEGREE (
  ID_INTERNAL int(11) NOT NULL default '0' auto_increment,
  ACKOPTLOCK int(11),
  ACADEMIC_YEAR int(11) NOT NULL,
  KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
  KEY_TEACHER integer(11) not null,
  TEMPORARY_EXAM_MAP bit not null default 1,
  KEY_CAMPUS integer(11) not null,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (ACADEMIC_YEAR,KEY_DEGREE_CURRICULAR_PLAN, KEY_CAMPUS),
  index EXECUTION_DEGREE_KEY_EXECUTION_YEAR (ACADEMIC_YEAR),
  index EXECUTION_DEGREE_KEY_DEGREE_CURRICULAR_PLAN (KEY_DEGREE_CURRICULAR_PLAN)
) TYPE=InnoDB;
   
#------------------------------------------
# Table structure for country
#------------------------------------------
drop table if exists COUNTRY;
create table COUNTRY (
   ID_INTERNAL integer(11) not null auto_increment,
   ACKOPTLOCK int(11),
   NAME varchar(50) not null,
   NATIONALITY varchar(50) not null,
   CODE varchar(10) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME),
   unique U2 (NATIONALITY),
   unique U3 (CODE))
   type=InnoDB;

#----------------------------
# Table structure for QUALIFICATION
#----------------------------
drop table if exists QUALIFICATION;
create table QUALIFICATION(
   ID_INTERNAL integer(11) not null auto_increment,
   ACKOPTLOCK int(11),
   KEY_PERSON integer(11) not null ,
   YEAR integer(11) not null,
   MARK varchar(200),
   SCHOOL varchar(200) not null,
   TITLE varchar(200),
   DEGREE varchar(200),
   LAST_MODIFICATION_DATE timestamp(14) not null,   
   primary key (ID_INTERNAL),
   unique U1 (YEAR,SCHOOL,KEY_PERSON))
   type=InnoDB;

#------------------------------------------
# Table structure for execution_course
#------------------------------------------
DROP TABLE IF EXISTS EXECUTION_COURSE;
CREATE TABLE EXECUTION_COURSE (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACKOPTLOCK int(11),
  NAME varchar(100) default NULL,
  CODE varchar(100) NOT NULL default '',
  THEORETICAL_HOURS double default NULL,
  PRATICAL_HOURS double default NULL,
  THEO_PRAT_HOURS double default NULL,
  LAB_HOURS double default NULL,
  SEMESTER int(11) default '2',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL,
  COMMENT text,  
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (CODE, KEY_EXECUTION_PERIOD),
  index EXECUTION_COURSE_KEY_EXECUTION_PERIOD (KEY_EXECUTION_PERIOD)
) TYPE=InnoDB;

#---------------------------------------------------------------
# Table structure for curricular_course_execution_course
#---------------------------------------------------------------
drop table if exists CURRICULAR_COURSE_EXECUTION_COURSE;
create table CURRICULAR_COURSE_EXECUTION_COURSE (
   INTERNAL_CODE integer(11) not null auto_increment,
   ACKOPTLOCK int(11),
   KEY_CURRICULAR_COURSE integer(11) not null,
   KEY_EXECUTION_COURSE integer(11) not null,
   primary key (INTERNAL_CODE),
   unique U1 (KEY_CURRICULAR_COURSE, KEY_EXECUTION_COURSE),
   index CURRICULAR_COURSE_EXECUTION_COURSE_KEY_CURRICULAR_COURSE (KEY_CURRICULAR_COURSE)
) type=InnoDB;


