CREATE TABLE `TEACHER_SERVICE_ITEM` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_TEACHER_SERVICE` int(11) NOT NULL default '0',
  `CLASS_NAME` varchar(255) NOT NULL default '',
  `KEY_PROFESSORSHIP` int(11) default NULL,
  `KEY_SHIFT` int(11) default NULL,  
  `KEY_ADVISE` int(11) default NULL,  
  `PERCENTAGE` float NOT NULL default '0',  
  `CREDITS` float(10,2) default NULL,
  `HOURS` float default NULL,
  `REASON` text NOT NULL,
  `WEEKDAY` varchar(50) default NULL,
  `START_TIME` time default NULL,
  `END_TIME` time default NULL,
  `ACK_OPT_LOCK` int(11) default NULL,  
  PRIMARY KEY  (`ID_INTERNAL`)
)TYPE=InnoDB
 