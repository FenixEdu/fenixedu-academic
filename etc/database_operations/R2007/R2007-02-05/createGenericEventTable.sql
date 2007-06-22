CREATE TABLE `GENERIC_EVENT` (
  `ID_INTERNAL` int(11) unsigned NOT NULL auto_increment,
  `TITLE` longtext,
  `DESCRIPTION` longtext,
  `FREQUENCY` varchar(100) default '',
  `KEY_ROOT_DOMAIN_OBJECT` int(11) NOT NULL default '1',
  PRIMARY KEY  (`ID_INTERNAL`),
  KEY `KEY_ROOT_DOMAIN_OBJECT` (`KEY_ROOT_DOMAIN_OBJECT`)
) ENGINE=InnoDB;

alter table `ROOM_OCCUPATION` add column `KEY_GENERIC_EVENT` int(11) default NULL;
update `ROOM_OCCUPATION` set `FREQUENCY` = null where `FREQUENCY` = 1;