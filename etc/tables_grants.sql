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
   ACK_OPT_LOCK int(11),
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  NUMBER int(11) unsigned NOT NULL default '0',
  DATE_SEND_CGD date default NULL,
  CARD_COPY_NUMBER int(11) unsigned default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (NUMBER,KEY_PERSON)
) TYPE=InnoDB;


#
# Table structure for table 'grant_contract'
#

DROP TABLE IF EXISTS GRANT_CONTRACT;
CREATE TABLE GRANT_CONTRACT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  NUMBER int(11) unsigned NOT NULL default '0',
  DATE_BEGIN_CONTRACT date default NULL,
  DATE_END_CONTRACT date default NULL,
  DATE_SEND_DISPATCH_CC date default NULL,
  DATE_DISPATCH_CC date default NULL,
  DATE_SEND_DISPATCH_CD date default NULL,
  DATE_DISPATCH_CD date default NULL,
  DATE_ACCEPT_TERM date default NULL,
  END_CONTRACT_MOTIVE varchar(255) default NULL,
  KEY_GRANT_OWNER int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TYPE int(11) unsigned NOT NULL default '0',
--  KEY_GRANT_CONTEST int(11) unsigned default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (NUMBER,KEY_GRANT_OWNER)
) TYPE=InnoDB;


#
# Table structure for table 'grant_type'
#

DROP TABLE IF EXISTS GRANT_TYPE;
CREATE TABLE GRANT_TYPE (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  NAME varchar(50) NOT NULL,
  SIGLA varchar(50) NOT NULL,
  MIN_PERIOD_DAYS int(2) unsigned,
  MAX_PERIOD_DAYS int(2) unsigned,
  INDICATIVE_VALUE double(10,2),
  SOURCE varchar(10),
  STATE date default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (SIGLA)
) TYPE=InnoDB;


#
# Table structure for table 'grant_responsible_teacher'
#

DROP TABLE IF EXISTS GRANT_RESPONSIBLE_TEACHER;
CREATE TABLE GRANT_RESPONSIBLE_TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  DATE_BEGIN date NOT NULL,
  DATE_END date default NULL,
  KEY_GRANT_CONTRACT int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TEACHER int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (DATE_BEGIN,DATE_END,KEY_GRANT_CONTRACT,KEY_GRANT_TEACHER)
) TYPE=InnoDB;


#
# Table structure for table 'grant_orientation_teacher'
#

DROP TABLE IF EXISTS GRANT_ORIENTATION_TEACHER;
CREATE TABLE GRANT_ORIENTATION_TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  DATE_BEGIN date NOT NULL,
  DATE_END date default NULL,
  KEY_GRANT_CONTRACT int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TEACHER int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (DATE_BEGIN,DATE_END,KEY_GRANT_CONTRACT,KEY_GRANT_TEACHER)
) TYPE=InnoDB;


#
# Table structure for table 'grant_subsidy'
#

DROP TABLE IF EXISTS GRANT_SUBSIDY;
CREATE TABLE GRANT_SUBSIDY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  VALUE_FULL_NAME varchar(255),
  VALUE DOUBLE(11,2) unsigned default '0.00',
  TOTAL_COST DOUBLE(11,2) unsigned default '0,00',
  KEY_GRANT_CONTRACT int(11) unsigned,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

#
# Table structure for table 'grant_part'
#

DROP TABLE IF EXISTS GRANT_PART;
CREATE TABLE GRANT_PART (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  PERCENTAGE int(11) unsigned,
  KEY_GRANT_SUBSIDY int(11) unsigned,
  KEY_GRANT_PAYMENT_ENTITY int(11) unsigned,
  KEY_TEACHER int(11) unsigned,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (KEY_GRANT_SUBSIDY, KEY_GRANT_PAYMENT_ENTITY)
) TYPE=InnoDB;


#
# Table structure for table 'grant_payment_entity'
#

DROP TABLE IF EXISTS GRANT_PAYMENT_ENTITY;
CREATE TABLE GRANT_PAYMENT_ENTITY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
   ACK_OPT_LOCK int(11),
  CLASS_NAME varchar(250) NOT NULL,
  NUMBER int(11) unsigned NOT NULL,
  DESIGNATION varchar(50) NOT NULL,
  KEY_TEACHER int(11) unsigned,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;
