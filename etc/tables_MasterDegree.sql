#----------------------------
# Table structure for MASTER_DEGREE_CANDIDATE
#----------------------------
drop table if exists MASTER_DEGREE_CANDIDATE;
create table MASTER_DEGREE_CANDIDATE (
   ID_INTERNAL integer(11) not null auto_increment,
   PERSON_KEY integer(11) not null,
   EXECUTION_DEGREE_KEY integer(11) not null default '0',
   CANDIDATE_NUMBER int(20),
   SPECIALIZATION int(11) not null, 
   MAJOR_DEGREE varchar(100),
   MAJOR_DEGREE_SCHOOL varchar(100),
   MAJOR_DEGREE_YEAR int(11),
   AVERAGE float,
   SPECIALIZATION_AREA varchar(250),
   SUBSTITUTE_ORDER INT(11) DEFAULT null,
   GIVEN_CREDITS FLOAT,
   GIVEN_CREDITS_REMARKS TEXT,
   primary key (ID_INTERNAL),
   unique u1 (PERSON_KEY, EXECUTION_DEGREE_KEY, SPECIALIZATION),
   unique u2 (CANDIDATE_NUMBER, EXECUTION_DEGREE_KEY, SPECIALIZATION))
   type=InnoDB;

#----------------------------
# Table structure for CANDIDATE_SITUATION
#----------------------------
drop table if exists CANDIDATE_SITUATION;
create table CANDIDATE_SITUATION (
   ID_INTERNAL integer(11) not null auto_increment,
   DATE date not null default '0001-01-01',
   REMARKS text,
   VALIDATION tinyint(4) not null default '1',
   CANDIDATE_KEY integer(11) not null,
   SITUATION integer(11) not null, 
   primary key (ID_INTERNAL)) type=InnoDB;

#----------------------------
# Table structure for PRICE
#----------------------------

drop table if exists PRICE; 
create table PRICE (
   ID_INTERNAL integer(11) not null auto_increment,
   GRADUATION_TYPE integer(11) not null, 
   DOCUMENT_TYPE integer(11) not null,
   DESCRIPTION varchar(200) not null,
   PRICE float(10,2) not null default '0.00',
   primary key (ID_INTERNAL),
   unique u1 (GRADUATION_TYPE,DOCUMENT_TYPE,DESCRIPTION))
   type=InnoDB;
   
#----------------------------
# Table structure for GUIDE
#----------------------------
drop table if exists GUIDE;
create table GUIDE (

   ID_INTERNAL integer(11) not null auto_increment,
   NUMBER integer(11) not null default '0',
   YEAR integer(4) not null default '0',
   KEY_CONTRIBUTOR integer(11) not null default '0',
   KEY_PERSON integer(11) not null default '0',
   TOTAL float(11,2) not null default '0.00',
   REMARKS text,
   GUIDE_REQUESTER integer(11) not null,
   KEY_EXECUTION_DEGREE integer(11) not null,
   PAYMENT_TYPE integer(11),
   CREATION_DATE date not null,
   VERSION integer(11) not null default '1',
   PAYMENT_DATE date,
   primary key (ID_INTERNAL),
   unique u1 (NUMBER, YEAR, VERSION))
   type=InnoDB;
   
#----------------------------
# Table structure for GUIDE_ENTRY
#----------------------------
drop table if exists GUIDE_ENTRY;
create table GUIDE_ENTRY(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_GUIDE integer(11) not null default '0',
   GRADUATION_TYPE integer(11), 
   DOCUMENT_TYPE integer(11) not null,
   DESCRIPTION varchar(200),
   PRICE float(10,2) not null default '0.00',
   QUANTITY integer(4) not null default '0',
   primary key (ID_INTERNAL),
   unique u1 (KEY_GUIDE,GRADUATION_TYPE,DOCUMENT_TYPE,DESCRIPTION))
   type=InnoDB;


#----------------------------
# Table structure for GUIDE_SITUATION
#----------------------------
drop table if exists GUIDE_SITUATION;
create table GUIDE_SITUATION(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_GUIDE integer(11) not null default '0',
   SITUATION integer(11) not null,
   DATE date not null default '0001-01-01',   
   REMARKS text,
   STATE integer(11) not null,
   primary key (ID_INTERNAL),
   unique u1 (KEY_GUIDE,SITUATION))
   type=InnoDB;


#----------------------------
# Table structure for REIMBURSEMENT_GUIDE
#----------------------------
drop table if exists REIMBURSEMENT_GUIDE;
create table REIMBURSEMENT_GUIDE (
   ID_INTERNAL integer(11) not null auto_increment,
   NUMBER integer(11) not null default '0',
   VALUE float(11,2) not null default '0.00',
   JUSTIFICATION text,
   KEY_GUIDE integer(11) not null,
   CREATION_DATE date not null,  
   primary key (ID_INTERNAL),
   unique U1 (NUMBER))
   type=InnoDB;
   
#----------------------------
# Table structure for REIMBURSEMENT_GUIDE_SITUATION
#----------------------------
drop table if exists REIMBURSEMENT_GUIDE_SITUATION;
create table REIMBURSEMENT_GUIDE_SITUATION (
   ID_INTERNAL integer(11) not null auto_increment,
   STATE integer(11) not null default '0',
   KEY_REIMBURSEMENT_GUIDE integer(11) not null,
   REMARKS text,
   KEY_EMPLOYEE integer(11) not null,
   MODIFICATION_DATE date not null,  
   REIMBURSEMENT_GUIDE_STATE integer(11) not null,  
   primary key (ID_INTERNAL))
   type=InnoDB;   

#----------------------------
# Table structure for GRATUITY
#----------------------------
drop table if exists GRATUITY;
create table GRATUITY(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_STUDENT_CURRICULAR_PLAN integer(11) not null ,
   STATE integer(11),
   GRATUITY_STATE integer(11),
   DATE date, 
   REMARKS text,
   primary key (ID_INTERNAL))
   type=InnoDB;

#----------------------------
# Table structure for CANDIDATE_ENROLMENT
#----------------------------
drop table if exists CANDIDATE_ENROLMENT;
create table CANDIDATE_ENROLMENT(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_MASTER_DEGREE_CANDIDATE integer(11) not null ,
   KEY_CURRICULAR_COURSE_SCOPE integer(11) not null ,
   primary key (ID_INTERNAL),
   unique u1 (KEY_MASTER_DEGREE_CANDIDATE, KEY_CURRICULAR_COURSE_SCOPE))
   type=InnoDB;
 
#-----------------------------------------------
# Table structure for table Master Degree Thesis
#-----------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_THESIS;
CREATE TABLE MASTER_DEGREE_THESIS (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_STUDENT_CURRICULAR_PLAN int(11) not null, 
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT_CURRICULAR_PLAN)
) TYPE=InnoDB;

#------------------------------------------------------------
# Table structure for table Master Degree Thesis Data Version
#------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_THESIS_DATA_VERSION;
CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS int(11) not null,
  KEY_EMPLOYEE int(11) not null,
  DISSERTATION_TITLE varchar(255) not null,
  LAST_MODIFICATION timestamp,
  CURRENT_STATE int(11) not null,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#-------------------------------------------------------------------------------------
# Table structure for table Master Degree Thesis Data Version External Assistent Guider
#-------------------------------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_ASSISTENT_GUIDER;
CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_ASSISTENT_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) not null,
  KEY_EXTERNAL_PERSON int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_EXTERNAL_PERSON)
) TYPE=InnoDB;



#-------------------------------------------------------------------------------------
# Table structure for table Master Degree Thesis Data Version Assistent Guider
#-------------------------------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_THESIS_DATA_VERSION_ASSISTENT_GUIDER;
CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_ASSISTENT_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) not null,
  KEY_TEACHER int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_TEACHER)
) TYPE=InnoDB;


#-------------------------------------------------------------------------------------
# Table structure for table Master Degree Thesis Data Version Guider
#-------------------------------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_THESIS_DATA_VERSION_GUIDER;
CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) not null,
  KEY_TEACHER int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_TEACHER)
) TYPE=InnoDB;

#----------------------------
# Table structure for External Person
#----------------------------
DROP TABLE if exists EXTERNAL_PERSON;
CREATE TABLE EXTERNAL_PERSON (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   WORK_LOCATION varchar(50) not null,
   KEY_PERSON int(11) unsigned  not null, 
   primary key (ID_INTERNAL),
   UNIQUE KEY U1 (KEY_PERSON)
) TYPE=InnoDB;



#------------------------------------------------------------
# Table structure for table Master Degree Proof Version
#------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_PROOF_VERSION;
CREATE TABLE MASTER_DEGREE_PROOF_VERSION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS int(11) not null,
  KEY_EMPLOYEE int(11) not null,
  PROOF_DATE DATE,
  THESIS_DELIVERY_DATE DATE,
  FINAL_RESULT int(11) not null,
  ATTACHED_COPIES_NUMBER int(3),
  LAST_MODIFICATION timestamp,
  CURRENT_STATE int(11) not null,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#------------------------------------------------------------
# Table structure for table Master Degree Proof Version Jury
#------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_PROOF_VERSION_JURY;
CREATE TABLE MASTER_DEGREE_PROOF_VERSION_JURY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_PROOF_VERSION int(11) not null,
  KEY_TEACHER int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_PROOF_VERSION,KEY_TEACHER)
) TYPE=InnoDB;
  