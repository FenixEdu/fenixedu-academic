
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

delete from BRANCH where id_internal=93;
update BRANCH set branch_code='' where branch_name='';
update STUDENT_CURRICULAR_PLAN set key_branch=8272 where key_branch=93;

alter table EXECUTION_YEAR ADD BEGIN_DATE date NOT NULL default '0000-00-00';
alter table EXECUTION_YEAR ADD END_DATE date NOT NULL default '0000-00-00';

update EXECUTION_YEAR set BEGIN_DATE = '2002-08-01' , END_DATE = '2003-07-31' where id_internal = 1; -- 2002/2003
update EXECUTION_YEAR set BEGIN_DATE = '2003-08-01', END_DATE =  '2004-07-31' where id_internal = 2; -- 2003/2004

-- ---------------------------------
--Table structure for DEGREE_INFO
-- ---------------------------------
drop table if exists CAMPUS;
create table CAMPUS(
	ID_INTERNAL int(11) not null auto_increment,
	KEY_DEGREE int(11) not null,
	NAME varchar(50) not null,
	primary key (ID_INTERNAL),
	unique U1 (NAME, KEY_DEGREE)
) type=InnoDB;

insert CAMPUS values(1, 1,"Alameda");
insert CAMPUS values(2, 2,"Alameda");
insert CAMPUS values(3, 3,"Alameda");
insert CAMPUS values(4, 4,"Alameda");
insert CAMPUS values(5, 5,"Alameda");
insert CAMPUS values(6, 6,"Alameda");
insert CAMPUS values(7, 7,"Alameda");
insert CAMPUS values(8, 8,"Alameda");
insert CAMPUS values(9, 9,"Alameda");
insert CAMPUS values(10, 10,"Alameda");
insert CAMPUS values(11, 11,"Alameda");
insert CAMPUS values(12, 12,"Alameda");
insert CAMPUS values(13, 13,"Alameda");
insert CAMPUS values(14, 14,"Alameda");
insert CAMPUS values(15, 15,"Alameda");
insert CAMPUS values(16, 16,"Alameda");
insert CAMPUS values(17, 17,"Alameda");
insert CAMPUS values(18, 18,"Alameda");
insert CAMPUS values(19, 19,"TagusPark");
insert CAMPUS values(20, 20,"Alameda");
insert CAMPUS values(21, 21,"Alameda");
insert CAMPUS values(22, 22,"TagusPark");
insert CAMPUS values(23, 23,"Alameda");
insert CAMPUS values(24, 24,"Alameda");
insert CAMPUS values(25, 25,"Alameda");
insert CAMPUS values(26, 26,"Alameda");
insert CAMPUS values(27, 27,"Alameda");
insert CAMPUS values(28, 28,"Alameda");
insert CAMPUS values(29, 29,"Alameda");
insert CAMPUS values(30, 30,"Alameda");
insert CAMPUS values(31, 31,"Alameda");
insert CAMPUS values(32, 32,"Alameda");
insert CAMPUS values(33, 33,"Alameda");
insert CAMPUS values(34, 34,"Alameda");
insert CAMPUS values(35, 35,"Alameda");
insert CAMPUS values(36, 36,"Alameda");
insert CAMPUS values(37, 37,"Alameda");
insert CAMPUS values(38, 38,"Alameda");
insert CAMPUS values(39, 39,"Alameda");
insert CAMPUS values(40, 40,"Alameda");
insert CAMPUS values(41, 41,"Alameda");
insert CAMPUS values(42, 42,"Alameda");
insert CAMPUS values(43, 43,"Alameda");
insert CAMPUS values(44, 44,"Alameda");
insert CAMPUS values(45, 45,"Alameda");
insert CAMPUS values(46, 46,"Alameda");
insert CAMPUS values(47, 47,"Alameda");
insert CAMPUS values(48, 48,"Alameda");
insert CAMPUS values(49, 49,"Alameda");
insert CAMPUS values(51, 51,"TagusPark");


-- ---------------------------------
--Table structure for DEGREE_INFO
-- ---------------------------------
drop table if exists DEGREE_INFO;
create table DEGREE_INFO(
	ID_INTERNAL int(11) not null auto_increment,
	KEY_DEGREE int(11) not null,
	DESCRIPTION text,
	OBJECTIVES text,
	HISTORY text,
	PROFESSIONAL_EXITS text,
	ADDITIONAL_INFO text,
	LINKS text,
	TEST_INGRESSION text,
	DRIFTS_INITIAL int(11),
	DRIFTS_FIRST int(11),
	DRIFTS_SECOND int(11),
	CLASSIFICATIONS text,
	MARK_MIN float(10,2) ,
	MARK_MAX float(10,2) ,
	MARK_AVERAGE float(10,2),
	DESCRIPTION_EN text,
	OBJECTIVES_EN text,
	HISTORY_EN text,
	PROFESSIONAL_EXITS_EN text,
	ADDITIONAL_INFO_EN text,
	LINKS_EN text,
	TEST_INGRESSION_EN text,	
	CLASSIFICATIONS_EN text,
	LAST_MODIFICATION_DATE datetime,
	primary key (ID_INTERNAL),
	unique U1 (KEY_DEGREE, LAST_MODIFICATION_DATE)
) type=InnoDB;
