#-----------------------------
# Table structure for METADATA
#-----------------------------

drop table if exists METADATA;
CREATE TABLE METADATA (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
  METADATA_FILE blob not null,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#----------------------------------
# Table structure for XML_DOCUMENTS
#----------------------------------

drop table if exists XML_DOCUMENTS;
CREATE TABLE XML_DOCUMENTS (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  XML_FILE mediumblob not null,
  XML_FILE_NAME varchar(100) not null default '',
  KEY_METADATA int(11) unsigned not null default '0',
  PRIMARY KEY  (ID_INTERNAL),
  unique U1 (KEY_METADATA, XML_FILE_NAME)
) TYPE=InnoDB;

#--------------------------
# Table structure for TESTS
#--------------------------

drop table if exists TESTS;
CREATE TABLE TESTS (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  TITLE text not null,
  INFORMATION text,
  NUMBER_OF_QUESTIONS int(2) not null default '0',
  CREATION_DATE timestamp not null default 'NOW()',
  LAST_MODIFICATION_DATE timestamp not null default 'NOW()',
  KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#----------------------------------
# Table structure for TEST_QUESTION
#----------------------------------

drop table if exists TEST_QUESTION;
CREATE TABLE TEST_QUESTION(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  TEST_QUESTION_ORDER int(2) not null default '1',
  TEST_QUESTION_VALUE int(2) not null default '0',
  KEY_XML_DOCUMENT int(11) unsigned not null default '0',
  KEY_TEST int(11) unsigned not null default '0',
  PRIMARY KEY  (ID_INTERNAL),
  unique U1 (KEY_XML_DOCUMENT, KEY_TEST)
) TYPE=InnoDB;