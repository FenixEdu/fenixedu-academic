/*
Mascon Dump
Source Host:           localhost
Source Server Version: 3.23.53-max
Source Database:       ciapl
Date:                  2003-03-11 16:50:53
*/

#----------------------------
# Table structure for announcement
#----------------------------
drop table if exists ANNOUNCEMENT;
create table ANNOUNCEMENT (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   TITLE varchar(100),
   CREATION_DATE timestamp(8),
   LAST_MODIFICATION_DATE timestamp(8),
   INFORMATION text,
   KEY_SITE int(11) unsigned not null default '0',
   primary key (ID_INTERNAL),
   UNIQUE KEY U1( KEY_SITE,TITLE,CREATION_DATE))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for curriculum
#----------------------------
drop table if exists CURRICULUM;
create table CURRICULUM (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_CURRICULAR_COURSE int(11) not null default '0',
   GENERAL_OBJECTIVES text,
   OPERACIONAL_OBJECTIVES text,
   PROGRAM text,
   GENERAL_OBJECTIVES_EN text,
   OPERACIONAL_OBJECTIVES_EN text,
   PROGRAM_EN text,
   LAST_MODIFICATION_DATE timestamp(14) not null,
   KEY_PERSON_WHO_ALTERED int(11) not null,
   primary key (ID_INTERNAL))
   type=InnoDB ;

#----------------------------
# Table structure for item
#----------------------------
drop table if exists ITEM;
create table ITEM (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   NAME varchar(100),
   ITEM_ORDER int(11) unsigned,
   INFORMATION text,
   URGENT int(11) unsigned,
   KEY_SECTION int(11) unsigned not null default '0',
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (NAME,KEY_SECTION))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for responsiblefor
#----------------------------
drop table if exists RESPONSIBLEFOR;
create table RESPONSIBLEFOR (
  INTERNAL_CODE integer(11) not null auto_increment,
  ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) unsigned not null default '0',
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   primary key (INTERNAL_CODE),
    UNIQUE KEY U1(KEY_TEACHER, KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for professorships
#   Column CREDITS only applies to MASTER DEGREE execution courses.
#----------------------------
drop table if exists PROFESSORSHIPS;
create table PROFESSORSHIPS (
   INTERNAL_CODE integer(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) unsigned not null default '0',
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   CREDITS float(11, 2) default '0',
   primary key (INTERNAL_CODE),
    UNIQUE KEY U1(KEY_TEACHER, KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for section
#----------------------------
drop table if exists SECTION;
create table SECTION (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   NAME varchar(100),
   SECTION_ORDER int(11) unsigned,
   KEY_SITE int(10) unsigned not null default '0',
   KEY_SUPERIOR_SECTION int(10) unsigned,
   LAST_MODIFIED_DATE date,
   primary key (ID_INTERNAL),
   UNIQUE KEY U1(NAME, KEY_SITE, KEY_SUPERIOR_SECTION))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for site
#----------------------------
drop table if exists SITE;
create table SITE (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   KEY_INITIAL_SECTION int(11),
   ALTERNATIVE_SITE varchar(255),
   MAIL varchar(50),
   INITIAL_STATEMENT text,
   INTRODUCTION 	text,
   STYLE varchar(255) ,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (ID_INTERNAL, KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for bibliographic_reference
#----------------------------
drop table if exists BIBLIOGRAPHIC_REFERENCE;
create table BIBLIOGRAPHIC_REFERENCE (
   ID_INTERNAL int(50) not null auto_increment,
   ACK_OPT_LOCK int(11),
   TITLE varchar(50) not null,
   AUTHORS varchar(50) not null,
   REFERENCE varchar(50) not null,
   `YEAR` varchar(50) not null,
   OPTIONAL int(11) not null default '0',
   KEY_EXECUTION_COURSE int(11) not null default '0',
   primary key (ID_INTERNAL),
   unique U1 (TITLE, AUTHORS, REFERENCE, `YEAR`))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for support_lesson
#----------------------------
drop table if exists SUPPORT_LESSON;
create table SUPPORT_LESSON (
   ID_INTERNAL int(50) not null auto_increment,
   ACK_OPT_LOCK int(11),
   WEEKDAY varchar(50) not null,
   START_TIME TIME not null,
   END_TIME TIME not null,
   PLACE varchar(50) not null,
   KEY_PROFESSORSHIP int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_PROFESSORSHIP, WEEKDAY, START_TIME, END_TIME))   
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for EvaluationMethod
#----------------------------
drop table if exists EVALUATION_METHOD;
create table EVALUATION_METHOD (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_CURRICULAR_COURSE int(11) unsigned not null default '0',
   EVALUATION_ELEMENTS text,
   EVALUATION_ELEMENTS_EN text,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (KEY_CURRICULAR_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for Summary
#----------------------------
drop table if exists SUMMARY;
create table SUMMARY (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   TITLE text,
   SUMMARY_DATE date,
   SUMMARY_HOUR time,
   LAST_MODIFICATION_DATE timestamp not null default 'NOW()',
   SUMMARY_TEXT text,
   SUMMARY_TYPE int,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_COURSE, SUMMARY_DATE,SUMMARY_HOUR))
   type=InnoDB ;

#----------------------------
# Table structure for StudentGroup
#----------------------------
drop table if exists STUDENT_GROUP;
create table STUDENT_GROUP (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   GROUP_NUMBER int(11) unsigned not null,
   KEY_SHIFT int(11) unsigned not null,
   KEY_GROUP_PROPERTIES int(11) unsigned not null,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (GROUP_NUMBER, KEY_GROUP_PROPERTIES))
   type=InnoDB comment="InnoDB free: 372736 kB";
   
#----------------------------
# Table structure for StudentGroupAttend
#----------------------------
drop table if exists STUDENT_GROUP_ATTEND;
create table STUDENT_GROUP_ATTEND (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_ATTEND int(11) unsigned not null,
   KEY_STUDENT_GROUP int(11) unsigned not null,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (KEY_ATTEND, KEY_STUDENT_GROUP))
   type=InnoDB comment="InnoDB free: 372736 kB";
   
   
#----------------------------
# Table structure for GroupProperties
#----------------------------
drop table if exists GROUP_PROPERTIES;
create table GROUP_PROPERTIES (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   MAXIMUM_CAPACITY int(11),
   MINIMUM_CAPACITY int(11),
   IDEAL_CAPACITY int(11),
   ENROLMENT_POLICY int(11),
   GROUP_MAXIMUM_NUMBER int(11),
   NAME varchar(50) not null,
   SHIFT_TYPE int(11),
   ENROLMENT_BEGIN_DAY date,
   ENROLMENT_END_DAY date,
   PROJECT_DESCRIPTION text,
   KEY_EXECUTION_COURSE int(11) unsigned not null ,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (NAME,KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for CourseReport
#----------------------------
drop table if exists COURSE_REPORT;
create table COURSE_REPORT (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_EXECUTION_COURSE int(11) unsigned not null ,
   REPORT text,
   LAST_MODIFICATION_DATE timestamp(14) not null,   
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";
      