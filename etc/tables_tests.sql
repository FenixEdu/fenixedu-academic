#-----------------------------
# Table structure for METADATA
#-----------------------------

drop table if exists METADATA;
CREATE TABLE METADATA (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
  METADATA_FILE blob not null,
  DESCRIPTION text,
  DIFFICULTY text,
  LEARNINGTIME time,
  LEVEL text,
  MAIN_SUBJECT text,
  SECONDARY_SUBJECT text,
  AUTHOR text,
  NUMBER_OF_MEMBERS int(5) not null default '1',
  VISIBILITY bit not null default '1',
  PRIMARY KEY  (ID_INTERNAL),
  index INDEX1 (KEY_EXECUTION_COURSE, VISIBILITY)
) TYPE=InnoDB;

#----------------------------------
# Table structure for XML_DOCUMENTS
#----------------------------------

drop table if exists XML_DOCUMENTS;
CREATE TABLE XML_DOCUMENTS (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  XML_FILE mediumblob not null,
  XML_FILE_NAME varchar(100) not null default '',
  KEY_METADATA int(11) unsigned not null default '0',
  VISIBILITY bit not null default '1',
  PRIMARY KEY  (ID_INTERNAL),
  unique U1 (KEY_METADATA, XML_FILE_NAME)
) TYPE=InnoDB;

#--------------------------
# Table structure for TESTS
#--------------------------

drop table if exists TESTS;
CREATE TABLE TESTS (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  TITLE text not null,
  INFORMATION text,
  NUMBER_OF_QUESTIONS int(2) not null default '0',
  CREATION_DATE timestamp not null default 'NOW()',
  LAST_MODIFICATION_DATE timestamp not null default 'NOW()',
  KEY_TEST_SCOPE int(11) unsigned not null default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#----------------------------------
# Table structure for TEST_QUESTION
#----------------------------------

drop table if exists TEST_QUESTION;
CREATE TABLE TEST_QUESTION(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  TEST_QUESTION_ORDER int(2) not null default '1',
  TEST_QUESTION_VALUE double not null default '0',
  KEY_XML_DOCUMENT int(11) unsigned not null default '0',
  KEY_TEST int(11) unsigned not null default '0',
  CORRECTION_FORMULA int(1) not null default 1,
  PRIMARY KEY  (ID_INTERNAL),
  unique U1 (KEY_XML_DOCUMENT, KEY_TEST)
) TYPE=InnoDB;

#--------------------------------------
# Table structure for DISTRIBUTED_TESTS
#--------------------------------------

drop table if exists DISTRIBUTED_TESTS;
CREATE TABLE DISTRIBUTED_TESTS(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  TITLE text not null,
  TEST_INFORMATION text,
  TEST_BEGIN_DATE date,
  TEST_BEGIN_HOUR time,
  TEST_END_DATE date,
  TEST_END_HOUR time,
  TEST_TYPE int(1) not null default '0',
  CORRECTION_AVAILABILITY int(1) not null default '0',
  IMS_FEEDBACK int(1) not null default '0',
  NUMBER_OF_QUESTIONS int(2) not null default '0',
  KEY_TEST_SCOPE int(11) unsigned not null default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#------------------------------------------
# Table structure for STUDENT_TEST_QUESTION
#------------------------------------------
drop table if exists STUDENT_TEST_QUESTION;
CREATE TABLE STUDENT_TEST_QUESTION(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  KEY_STUDENT int(11) unsigned not null default '0',
  KEY_DISTRIBUTED_TEST int(11) unsigned not null default '0',
  KEY_XML_DOCUMENT int(11) unsigned not null default '0',
  TEST_QUESTION_ORDER int(2) not null default '1',
  TEST_QUESTION_VALUE double not null default '0',
  TEST_QUESTION_MARK double not null default '0',
  OLD_RESPONSE int not null default '0',
  RESPONSE mediumblob,
  OPTION_SHUFFLE text,
  CORRECTION_FORMULA int(1) not null default 1,
  PRIMARY KEY  (ID_INTERNAL),
  unique U1 (KEY_STUDENT, KEY_DISTRIBUTED_TEST, KEY_XML_DOCUMENT),
  index INDEX1 (TEST_QUESTION_ORDER),
  index INDEX2 (KEY_DISTRIBUTED_TEST)
) TYPE=InnoDB;

#-------------------------------------
# Table structure for STUDENT_TEST_LOG
#-------------------------------------
drop table if exists STUDENT_TEST_LOG;
CREATE TABLE STUDENT_TEST_LOG(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  KEY_STUDENT int(11) unsigned not null default '0',
  KEY_DISTRIBUTED_TEST int(11) unsigned not null default '0',
  DATE timestamp not null default 'NOW()',
  EVENT text,
  PRIMARY KEY  (ID_INTERNAL),
  index INDEX1 (KEY_STUDENT, KEY_DISTRIBUTED_TEST, DATE)
) TYPE=InnoDB;

#-------------------------------
# Table structure for TEST_SCOPE
#-------------------------------
drop table if exists TEST_SCOPE;
CREATE TABLE TEST_SCOPE(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  CLASS_NAME varchar(250) NOT NULL,
  KEY_CLASS int(11) unsigned,
  ACK_OPT_LOCK int(11),
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#----------------------------------------------
# Table structure for DISTRIBUTED_TEST_ADVISORY
#----------------------------------------------
drop table if exists DISTRIBUTED_TEST_ADVISORY;
CREATE TABLE DISTRIBUTED_TEST_ADVISORY(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  KEY_DISTRIBUTED_TEST int(11) unsigned not null,
  KEY_ADVISORY int(11) unsigned not null,
  ACK_OPT_LOCK int(11),
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;