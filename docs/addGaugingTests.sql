# MySQL-Front Dump 2.5
#
# Host: localhost   Database: ciapl
# --------------------------------------------------------
# Server version 3.23.53-max-nt
#
# Table structure for table 'GAUGING_TESTS_RESULTS'
#

DROP TABLE IF EXISTS GAUGING_TESTS_RESULTS;

CREATE TABLE GAUGING_TEST_RESULT (  
  ID_INTERNAL int(10) unsigned NOT NULL auto_increment,
  KEY_STUDENT int(11) default NULL,
  TEST int(11) default NULL,
  P1 char(5) default NULL,
  P2 char(5) default NULL,
  P3 char(5) default NULL, 
  P4 char(5) default NULL,
  P5 char(5) default NULL,
  P6 char(5) default NULL,
  P7 char(5) default NULL,
  P8 char(5) default NULL,
  P9 char(5) default NULL,
  P10 char(5) default NULL,
  P11 char(5) default NULL,
  P12 char(5) default NULL,
  P13 char(5) default NULL, 
  P14 char(5) default NULL, 
  P15 char(5) default NULL, 
  P16 char(5) default NULL, 
  P17 char(5) default NULL,  
  P18 char(5) default NULL,  
  P19 char(5) default NULL,  
  P20 char(5) default NULL, 
  P21 char(5) default NULL, 
  P22 char(5) default NULL, 
  P23 char(5) default NULL,  
  P24 char(5) default NULL,  
  P25 char(5) default NULL,  
  P26 char(5) default NULL,  
  P27 char(5) default NULL,  
  P28 char(5) default NULL,  
  UNANSWERED int(11) default NULL,
  CORRECT int(11) default NULL, 
  WRONG int(11) default NULL, 
  CF float default NULL,  
  PRIMARY KEY  (ID_INTERNAL))
  type=InnoDB;
