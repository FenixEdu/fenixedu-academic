#---------------------------------------------------------------
# Table structure for person
#---------------------------------------------------------------
drop table if exists PERSON;
create table PERSON (
   ID_INTERNAL integer(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   DOCUMENT_ID_NUMBER varchar(50) not null,
   EMISSION_LOCATION_OF_DOCUMENT_ID varchar(100),
   EMISSION_DATE_OF_DOCUMENT_ID date,
   EXPERATION_DATE_OF_DOCUMENT_ID date,
   NAME varchar(100),
   DATE_OF_BIRTH date,
   NAME_OF_FATHER varchar(100),
   NAME_OF_MOTHER varchar(100),
   NATIONALITY varchar(50),
   PARISH_OF_BIRTH varchar(100),
   DISTRICT_SUBDIVISION_OF_BIRTH varchar(100),
   DISTRICT_OF_BIRTH varchar(100),
   ADDRESS varchar(100),
   AREA varchar(100),
   AREA_CODE varchar(8),
   AREA_OF_AREA_CODE varchar(100),
   PARISH_OF_RESIDENCE varchar(100),
   DISTRICT_SUBDIVISION_OF_RESIDENCE varchar(100),
   DISTRICT_OF_RESIDENCE varchar(100),
   PHONE varchar(50),
   MOBILE varchar(50),
   EMAIL varchar(100),
   WEB_ADRDRESS varchar(200),
   SOCIAL_SECURITY_NUMBER varchar(50),
   PROFESSION varchar(100),
   USERNAME varchar(50) not null,
   PASSWD varchar(40),
   KEY_COUNTRY int(11),
   FISCAL_CODE varchar(50),
   TYPE_ID_DOCUMENT int(11) not null,
   SEX int(11),
   MARITAL_STATUS int(11),
   primary key (ID_INTERNAL),
   unique U1 (DOCUMENT_ID_NUMBER, TYPE_ID_DOCUMENT),
   UNIQUE U2 (USERNAME)
   )type=InnoDB;

#----------------------------------
# Table structure for table ROLE
#----------------------------------
drop table if exists ROLE;
create table ROLE(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  ROLE_TYPE int (11) not null,
  PORTAL_SUB_APPLICATION varchar (100),
  PAGE varchar (100),
  PAGE_NAME_PROPERTY varchar(100),
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE U1 (ROLE_TYPE)
)type=InnoDB;

#----------------------------------
# Table structure for table PERSON_ROLE
#----------------------------------
drop table if exists PERSON_ROLE;
create table PERSON_ROLE(
  ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
  KEY_ROLE int(11) not null,
  KEY_PERSON int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE U1 (KEY_ROLE, KEY_PERSON)
)type=InnoDB;

#----------------------------------
# Table structure for table student
#----------------------------------
DROP TABLE IF EXISTS STUDENT;
CREATE TABLE STUDENT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACK_OPT_LOCK int(11),
  NUMBER smallint(10) unsigned NOT NULL default '0',
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  STATE int(11) unsigned NOT NULL default '1',
  DEGREE_TYPE integer(11) not null,
  KEY_STUDENT_KIND int(11) unsigned NOT NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NUMBER, DEGREE_TYPE),
  UNIQUE KEY U2 (DEGREE_TYPE, KEY_PERSON)
) TYPE=InnoDB;

#----------------------------
# Table structure for TEACHER
#----------------------------
drop table if exists TEACHER;
create table TEACHER (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   TEACHER_NUMBER int(10) unsigned,
   KEY_PERSON int(11) unsigned  not null,
   KEY_CATEGORY int(11) unsigned, 
   primary key (ID_INTERNAL),
   UNIQUE KEY U1 (TEACHER_NUMBER,KEY_PERSON))
   type=InnoDB comment="InnoDB free: 372736 kB";
