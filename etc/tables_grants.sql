# MySQL-Front Dump 2.5
#
# Host: localhost   Database: ciapl
# --------------------------------------------------------
# Server version 4.0.15-nt


#
# Table structure for table 'grant_owner'
#

DROP TABLE IF EXISTS GRANT_OWNER;
CREATE TABLE GRANT_OWNER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACKOPTLOCK int(11),
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  NUMBER int(11) unsigned NOT NULL default '0',
  DATE_SEND_CGD date default NULL,
  CARD_COPY_NUMBER int(11) unsigned default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (NUMBER,KEY_PERSON)
) TYPE=InnoDB;

#
# Dumping data for table 'grant_owner'
#


#
# Table structure for table 'grant_contract'
#

DROP TABLE IF EXISTS GRANT_CONTRACT;
CREATE TABLE GRANT_CONTRACT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACKOPTLOCK int(11),
  NUMBER int(11) unsigned NOT NULL default '0',
  DATE_BEGIN_CONTRACT date default NULL,
  DATE_END_CONTRACT date default NULL,
  END_CONTRACT_MOTIVE varchar(255) default NULL,
  KEY_GRANT_OWNER int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TYPE int(11) unsigned NOT NULL default '0',
--  KEY_GRANT_CONTEST int(11) unsigned default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (NUMBER,KEY_GRANT_OWNER)
) TYPE=InnoDB;

#
# Dumping data for table 'grant_contract'
#


#
# Table structure for table 'grant_type'
#

DROP TABLE IF EXISTS GRANT_TYPE;
CREATE TABLE GRANT_TYPE (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACKOPTLOCK int(11),
  NAME varchar(50) NOT NULL default '',
  SIGLA varchar(50) NOT NULL default '',
  MIN_PERIOD_DAYS int(2) unsigned NOT NULL default '0',
  MAX_PERIOD_DAYS int(2) unsigned NOT NULL default '0',
  INDICATIVE_VALUE double(10,2) NOT NULL default '0.00',
  SOURCE varchar(10) NOT NULL default '',
  STATE date default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (SIGLA)
) TYPE=InnoDB;


#
# Dumping data for table 'grant_type'
#


#
# Table structure for table 'grant_responsible_teacher'
#

DROP TABLE IF EXISTS GRANT_RESPONSIBLE_TEACHER;
CREATE TABLE GRANT_RESPONSIBLE_TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACKOPTLOCK int(11),
  DATE_BEGIN date NOT NULL,
  DATE_END date default NULL,
  KEY_GRANT_CONTRACT int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TEACHER int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (DATE_BEGIN,DATE_END,KEY_GRANT_CONTRACT,KEY_GRANT_TEACHER)
) TYPE=InnoDB;


#
# Dumping data for table 'grant_responsible_teacher'
#


#
# Table structure for table 'grant_orientation_teacher'
#

DROP TABLE IF EXISTS GRANT_ORIENTATION_TEACHER;
CREATE TABLE GRANT_ORIENTATION_TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACKOPTLOCK int(11),
  DATE_BEGIN date NOT NULL,
  DATE_END date default NULL,
  KEY_GRANT_CONTRACT int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TEACHER int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (DATE_BEGIN,DATE_END,KEY_GRANT_CONTRACT,KEY_GRANT_TEACHER)
) TYPE=InnoDB;


#
# Dumping data for table 'grant_orientation_teacher'
#


#
# Table structure for table 'payment_entity'
#
#
#DROP TABLE IF EXISTS PAYMENT_ENTITY;
#CREATE TABLE GRANT_PAYMENT_ENTITY (
#  ID_INTERNAL int(11) not null auto_increment,
#   ACKOPTLOCK int(11),
#  KEY_COST_CENTER int(11) default NULL,
#  KEY_PROJECT int(11) default NULL,
#  PRIMARY KEY  (ID_INTERNAL)
#) TYPE=InnoDB;
#
#
# Dumping data for table 'payment_entity'
#


#
# Table structure for table 'cost_center'
#
#
#CREATE TABLE COST_CENTER (
#  ID_INTERNAL int(11) not null auto_increment,
#   ACKOPTLOCK int(11),
#  NUMBER int(11) NOT NULL default '0',
#  DESIGNATION varchar(50) NOT NULL default '',
#  PRIMARY KEY  (ID_INTERNAL),
#  UNIQUE KEY UNIQUE1 (NUMBER)
#) TYPE=InnoDB;
#
#
# Dumping data for table 'cost_center'
#


#
# Table structure for table 'project'
#
#
#CREATE TABLE PROJECT (
#  ID_INTERNAL int(11) unsigned not null auto_increment,
#   ACKOPTLOCK int(11),
#  NUMBER varchar(255) default NULL,
#  DESIGNATION varchar(255) default NULL,
#  KEY_TEACHER int(11) default NULL,
#  PRIMARY KEY  (ID_INTERNAL)
#) TYPE=InnoDB;
#
#
# Dumping data for table 'project'
#

