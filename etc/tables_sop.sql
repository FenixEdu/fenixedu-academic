#----------------------------
# Table structure for room
#----------------------------
drop table if exists ROOM;
create table ROOM (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   NAME varchar(100) not null,
   BUILDING varchar(50) not null,
   FLOOR int(11) not null,
   TYPE int(11) not null,
   NORMAL_CAPACITY int(11) not null,
   EXAM_CAPACITY int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME))
   type=InnoDB;

#----------------------------
# Table structure for room occupation
#----------------------------
drop table if exists ROOM_OCCUPATION;
create table ROOM_OCCUPATION (
   ID_INTERNAL int(11) not null auto_increment,
   START_TIME time not null,
   END_TIME time not null,
   WEEKDAY int(11) not null,
   KEY_ROOM int(11) not null,
   KEY_PERIOD int(11) not null,
   primary key (ID_INTERNAL))
   type=InnoDB;

#----------------------------
# Table structure for period
#----------------------------
drop table if exists PERIOD;
create table PERIOD (
   ID_INTERNAL int(11) not null auto_increment,
   START_DATE date not null,
   END_DATE date not null,
   primary key (ID_INTERNAL))
   type=InnoDB;

#----------------------------
# Table structure for writtenEvaluation_roomOccupation
#----------------------------
drop table if exists WRITTEN_EVALUATION_ROOM_OCCUPATION;
create table WRITTEN_EVALUATION_ROOM_OCCUPATION (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_WRITTEN_EVALUATION int(11) not null,
   KEY_ROOM_OCCUPATION int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_ROOM_OCCUPATION, KEY_WRITTEN_EVALUATION))
   type=InnoDB;



#----------------------------
# Table structure for class
#----------------------------
DROP TABLE IF EXISTS CLASS;
CREATE TABLE CLASS (
  ID_INTERNAL int(11) NOT NULL default '0' auto_increment,
  ACK_OPT_LOCK int(11),
  NAME varchar(50) NOT NULL default '',
  SEMESTER int(11),
  CURRICULAR_YEAR int(11) NOT NULL,
  KEY_EXECUTION_DEGREE int(11) NOT NULL,
  KEY_DEGREE int(11),
  KEY_EXECUTION_PERIOD int (11) not null,  
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME, KEY_EXECUTION_PERIOD, KEY_EXECUTION_DEGREE)
) TYPE=InnoDB;

#----------------------------
# Table structure for shift
#----------------------------
drop table if exists SHIFT;
create table SHIFT (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   NAME varchar(50) not null,
   KEY_EXECUTION_COURSE int(11) not null,
   TYPE int(11) not null,
   CAPACITY int(11) not null,
   AVAILLABILITY_FINAL int(11),
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_EXECUTION_COURSE),
   index SHIFT_KEY_EXECUTION_COURSE (KEY_EXECUTION_COURSE)
) type=InnoDB;

#----------------------------
# Table structure for lesson
#----------------------------
drop table if exists LESSON;
create table LESSON (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   WEEKDAY int(11) not null,
   START_TIME time not null,
   END_TIME time not null,
   KEY_ROOM int(11) not null,
   KEY_EXECUTION_COURSE int(11) not null,
   TYPE int(11) not null,
   KEY_EXECUTION_PERIOD int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (WEEKDAY, START_TIME, END_TIME, KEY_ROOM, KEY_EXECUTION_PERIOD))
   type=InnoDB;

#----------------------------
# Table structure for class_shift
#----------------------------
drop table if exists CLASS_SHIFT;
create table CLASS_SHIFT (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_CLASS int(11) not null,
   KEY_SHIFT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_CLASS, KEY_SHIFT),
   index CLASS_SHIFT_KEY_CLASS (KEY_CLASS),
   index CLASS_SHIFT_KEY_SHIFT (KEY_SHIFT)
) type=InnoDB;

#----------------------------
# Table structure for shift_student
#----------------------------
drop table if exists SHIFT_STUDENT;
create table SHIFT_STUDENT (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_SHIFT int(11) not null,
   KEY_STUDENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_SHIFT, KEY_STUDENT))
   type=InnoDB;

#----------------------------
# Table structure for shift_lesson
#----------------------------
drop table if exists SHIFT_LESSON;
create table SHIFT_LESSON (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_SHIFT int(11) unsigned not null,
   KEY_LESSON int(11) unsigned not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_SHIFT, KEY_LESSON),
   index SHIFT_LESSON_KEY_LESSON (KEY_LESSON),
   index SHIFT_LESSON_KEY_SHIFT (KEY_SHIFT)
) type=InnoDB;

#----------------------------
# Table structure for attend
#----------------------------
drop table if exists ATTEND;
create table ATTEND (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_STUDENT int(11) not null,
   KEY_EXECUTION_COURSE int(11) not null,
   KEY_ENROLMENT int(11),
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_EXECUTION_COURSE))
   type=InnoDB;

#----------------------------
# Table structure for EXECUTION_PERIOD
# State : C = Current; O= Open; NO= Not open; CL= Closed
#----------------------------
drop table if exists EXECUTION_PERIOD;
create table EXECUTION_PERIOD (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   NAME varchar(50) not null,
   KEY_EXECUTION_YEAR int(11) not null,
   STATE varchar(3) not null default "NO",
   SEMESTER int (11) not null,
   BEGIN_DATE date not null,
   END_DATE date not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_EXECUTION_YEAR))
   type=InnoDB comment="InnoDB free: 373760 kB";
   
   
#----------------------------
# Table structure for EXECUTION_YEAR
#----------------------------
drop table if exists EXECUTION_YEAR;
create table EXECUTION_YEAR (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   `YEAR` varchar(9) not null,
   STATE varchar(3) not null default "NO",   
   BEGIN_DATE date NOT NULL default '0000-00-00',
   END_DATE date NOT NULL default '0000-00-00',     
   primary key (ID_INTERNAL),
   unique U1 (`YEAR`))
   type=InnoDB comment="InnoDB free: 373760 kB";

#----------------------------
# Table structure for evaluation
#----------------------------
DROP TABLE IF EXISTS EVALUATION;
CREATE TABLE EVALUATION (
  ID_INTERNAL int(11) not null auto_increment,
  ACK_OPT_LOCK int(11),
  CLASS_NAME varchar(250) NOT NULL,
  DAY date,
  BEGINNING time,
  END time,
  SEASON int(11) not null,
  ENROLLMENT_BEGIN_DAY date,
  ENROLLMENT_BEGIN_TIME time,
  ENROLLMENT_END_DAY date,  
  ENROLLMENT_END_TIME time,
  PUBLISHMENT_MESSAGE varchar (120),
  PRIMARY KEY (ID_INTERNAL)
) TYPE=InnoDB;

#----------------------------
# Table structure for exam_executionCourse
#----------------------------
drop table if exists EXAM_EXECUTION_COURSE;
create table EXAM_EXECUTION_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EVALUATION int(11) not null,
   KEY_EXECUTION_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EVALUATION, KEY_EXECUTION_COURSE))
   type=InnoDB;

#----------------------------
# Table structure for writtenEvaluation_curricularCourseScope
#----------------------------
drop table if exists WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE;
create table WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_WRITTEN_EVALUATION int(11) not null,
   KEY_CURRICULAR_COURSE_SCOPE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_WRITTEN_EVALUATION, KEY_CURRICULAR_COURSE_SCOPE))
   type=InnoDB;

#----------------------------
# Table structure for exam_room
#----------------------------
drop table if exists EXAM_ROOM;
create table EXAM_ROOM (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EVALUATION int(11) not null,
   KEY_ROOM int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EVALUATION, KEY_ROOM))
   type=InnoDB;

#----------------------------
# Table structure for exam_student
#----------------------------
drop table if exists EXAM_STUDENT;
create table EXAM_STUDENT (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EVALUATION int(11) not null,
   KEY_STUDENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EVALUATION, KEY_STUDENT))
   type=InnoDB;

#----------------------------
# Table structure for exam_student_room
#----------------------------
drop table if exists EXAM_STUDENT_ROOM;
create table EXAM_STUDENT_ROOM (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EVALUATION int(11) not null,
   KEY_STUDENT int(11) not null,
   KEY_ROOM int(11) ,   
   primary key (ID_INTERNAL),
   unique U1 (KEY_EVALUATION, KEY_STUDENT))
   type=InnoDB;