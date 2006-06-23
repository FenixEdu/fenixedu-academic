CREATE TABLE `DISTRICT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `ACK_OPT_LOCK` int(11) default NULL,
  `NAME` varchar(100) NOT NULL,  
  `CODE` varchar(10) NOT NULL,    
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `U1` (`NAME`)
) Type=InnoDB;