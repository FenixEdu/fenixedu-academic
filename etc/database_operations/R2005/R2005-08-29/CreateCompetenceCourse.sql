CREATE TABLE `COMPETENCE_COURSE` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `ACK_OPT_LOCK` int(11) default NULL,
  `CODE` varchar(50) NOT NULL default '',
  `NAME` varchar(100) NOT NULL default '',
  `KEY_DEPARTMENT` int(11) default NULL,
  PRIMARY KEY  (`ID_INTERNAL`)
);