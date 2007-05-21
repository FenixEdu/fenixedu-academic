alter table USER add column KEY_LOGIN_REQUEST int(11);

CREATE TABLE `LOGIN_REQUEST` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `KEY_USER` int(11) NOT NULL,
  `HASH` varchar(255),
  `KEY_ROOT_DOMAIN_OBJECT` int(11) NOT NULL default '1',
  PRIMARY KEY  (`ID_INTERNAL`)
) ENGINE=InnoDB;

