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
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  NUMBER int(11) unsigned NOT NULL default '0',
  DATE_SEND_CGD timestamp(8) NOT NULL,
  CARD_COPY_NUMBER int(11) unsigned default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (NUMBER,KEY_PERSON)
) TYPE=InnoDB;



#
# Dumping data for table 'grant_owner'
#

