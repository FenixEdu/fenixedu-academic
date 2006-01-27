CREATE TABLE `RESEARCHER` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `KEY_PERSON` int(11) unsigned NOT NULL default '0',
  `ACK_OPT_LOCK` int(11) default NULL,
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `U1` (`KEY_PERSON`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1