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