CREATE TABLE `TEACHER_LEGAL_REGIMEM` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_CATEGORY` int(11) default NULL,  
  `KEY_TEACHER` int(11) default NULL,
  `BEGIN_DATE` date default NULL,
  `END_DATE` date default NULL,
  `PERCENTAGE` int(3) default NULL,
  `LESSON_HOURS_NUMBER` int(11) default NULL,
  `TOTAL_HOURS_NUMBER` double default NULL,
  `LEGAL_REGIMEN_TYPE` varchar(100) default NULL,
  `REGIMEN_TYPE` varchar(100) default NULL,
  `ACK_OPT_LOCK` int(11) default NULL,
  PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
