#----------------------------
# Table structure for room
#----------------------------
drop table if exists ROOM;
create table ROOM (
   ID_INTERNAL int(11) not null auto_increment,
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
# Table structure for class
#----------------------------
DROP TABLE IF EXISTS CLASS;
CREATE TABLE CLASS (
  ID_INTERNAL int(11) NOT NULL default '0',
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
   NAME varchar(50) not null,
   KEY_EXECUTION_COURSE int(11) not null,
   TYPE int(11) not null,
   CAPACITY int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_EXECUTION_COURSE))
   type=InnoDB;

#----------------------------
# Table structure for lesson
#----------------------------
drop table if exists LESSON;
create table LESSON (
   ID_INTERNAL int(11) not null auto_increment,
   WEEKDAY int(11) not null,
   START_TIME time not null,
   END_TIME time not null,
   KEY_ROOM int(11) not null,
   KEY_EXECUTION_COURSE int(11) not null,
   TYPE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (WEEKDAY, START_TIME, END_TIME, KEY_ROOM))
   type=InnoDB;

#----------------------------
# Table structure for class_shift
#----------------------------
drop table if exists CLASS_SHIFT;
create table CLASS_SHIFT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CLASS int(11) not null,
   KEY_SHIFT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_CLASS, KEY_SHIFT))
   type=InnoDB;

#----------------------------
# Table structure for shift_student
#----------------------------
drop table if exists SHIFT_STUDENT;
create table SHIFT_STUDENT (
   ID_INTERNAL int(11) not null auto_increment,
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
   KEY_SHIFT int(11) unsigned not null,
   KEY_LESSON int(11) unsigned not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_SHIFT, KEY_LESSON))
   type=InnoDB;

#----------------------------
# Table structure for attend
#----------------------------
drop table if exists ATTEND;
create table ATTEND (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT int(11) not null,
   KEY_EXECUTION_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_EXECUTION_COURSE))
   type=InnoDB;

#----------------------------
# Table structure for EXECUTION_PERIOD
#----------------------------
drop table if exists EXECUTION_PERIOD;
create table EXECUTION_PERIOD (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   KEY_EXECUTION_YEAR int(11) not null default '0',
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_EXECUTION_YEAR))
   type=InnoDB comment="InnoDB free: 373760 kB";
   
   
#----------------------------
# Table structure for EXECUTION_YEAR
#----------------------------
drop table if exists EXECUTION_YEAR;
create table EXECUTION_YEAR (
   ID_INTERNAL int(11) not null auto_increment,
   `YEAR` varchar(9) not null,
   primary key (ID_INTERNAL),
   unique U1 (`YEAR`))
   type=InnoDB comment="InnoDB free: 373760 kB";
   