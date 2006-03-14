alter table `PARTY` add column `KEY_USER` int(11) default NULL;
alter table `PARTY` add key `KEY_USER` (`KEY_USER`);

CREATE TABLE `USER` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `USERNAME` varchar(50) NOT NULL default '',
  `IST_USERNAME` varchar(100) default NULL,
  `IS_PASS_KERBEROS` tinyint(1) NOT NULL default '0',
  `PASSWORD` varchar(40) default NULL,
  `LAST_LOGIN_HOST` varchar(100) default '',
  `CURRENT_LOGIN_HOST` varchar(100) default '',
  `LAST_LOGIN_DATE_TIME` timestamp default 0,
  `CURRENT_LOGIN_DATE_TIME` timestamp default 0,
  `KEY_PERSON` int(11) unsigned default NULL,
  PRIMARY KEY  (`ID_INTERNAL`),
  KEY `KEY_PERSON` (`KEY_PERSON`)
) ENGINE=InnoDB;
