CREATE TABLE `UNIT_EXTRA_WORK_AMOUNT` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `KEY_UNIT` int(11) NOT NULL default '0',
  `YEAR` int(4) NOT NULL default 0,
  `SPENT` double default 0,
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE KEY `u1` (`KEY_UNIT`,`YEAR`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `UNIT_EXTRA_WORK_MOVEMENT` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `KEY_UNIT_EXTRA_WORK_AMOUNT` int(11) NOT NULL default '0',
  `DATE` varchar(50) NOT NULL default '',
  `AMOUNT` double default 0,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) NOT NULL default '1',
  PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;