CREATE TABLE `AUTHORSHIP` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `ACK_OPT_LOCK` int(11) default NULL,
  `KEY_PUBLICATION` int(11) default NULL,
  `KEY_AUTHOR` int(11) default NULL,
  `AUTHOR_ORDER` int(11) default NULL,
  PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;