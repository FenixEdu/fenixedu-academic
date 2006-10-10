CREATE TABLE `LOGIN_ALIAS` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `ALIAS` varchar(50) not NULL default '',  
  `KEY_LOGIN` int(11) unsigned NOT NULL default '0', 
  `TYPE` varchar(50) NOT NULL default '',
  `ROLE_TYPE`  varchar(50) NOT NULL default '',
  `KEY_ROOT_DOMAIN_OBJECT` int(11) NOT NULL default '1',
  PRIMARY KEY  (`ID_INTERNAL`),
  KEY `KEY_LOGIN` (`KEY_LOGIN`),
  KEY `KEY_ROOT_DOMAIN_OBJECT` (`KEY_ROOT_DOMAIN_OBJECT`)
) ENGINE=InnoDB;
