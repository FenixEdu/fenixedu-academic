# MySQL-Front Dump 2.5
#
# Host: localhost   Database: ciapl
# --------------------------------------------------------
# Server version 3.23.51-max-debug

USE ciapl;


#
# Table structure for table 'ANNOUNCEMENT'
#

CREATE TABLE `ANNOUNCEMENT` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `TITLE` varchar(100) default NULL,
  `CREATION_DATE` date default NULL,
  `LAST_MODIFICATION_DATE` date default NULL,
  `INFORMATION` varchar(100) default NULL,
  `KEY_SITE` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ID_INTERNAL`)
) TYPE=InnoDB;



#
# Table structure for table 'ITEM'
#

CREATE TABLE `ITEM` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `NAME` varchar(100) default NULL,
  `ORDER` int(11) unsigned default NULL,
  `INFORMATION` text,
  `URGENT` int(11) unsigned default NULL,
  `KEY_SECTION` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `ID_INTERNAL` (`ID_INTERNAL`)
) TYPE=InnoDB;



#
# Table structure for table 'SECTION'
#

CREATE TABLE `SECTION` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `NAME` varchar(100) default NULL,
  `ORDER` int(11) unsigned default NULL,
  `KEY_SITE` int(10) unsigned NOT NULL default '0',
  `KEY_SUPERIOR_SECTION` int(10) unsigned NOT NULL default '0',
  `LAST_MODIFIED_DATE` date default NULL,
  PRIMARY KEY  (`ID_INTERNAL`)
) TYPE=InnoDB;



#
# Table structure for table 'SITE'
#

CREATE TABLE `SITE` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `KEY_EXECUTION_COURSE` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `ID_INTERNAL` (`ID_INTERNAL`,`KEY_EXECUTION_COURSE`)
) TYPE=MyISAM;

