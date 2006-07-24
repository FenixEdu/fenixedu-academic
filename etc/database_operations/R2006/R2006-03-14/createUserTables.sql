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

insert into USER select
	PARTY.ID_INTERNAL, PARTY.USERNAME, PARTY.IST_USERNAME, PARTY.IS_PASS_KERBEROS, PARTY.PASSWD,
	null, null, null, null, PARTY.ID_INTERNAL
from PARTY where PARTY.CLASS_NAME = 'net.sourceforge.fenixedu.domain.Person';

update PARTY set PARTY.KEY_USER = PARTY.ID_INTERNAL where PARTY.CLASS_NAME = 'net.sourceforge.fenixedu.domain.Person';

alter table PARTY drop column USERNAME;
alter table PARTY drop column IST_USERNAME;
alter table PARTY drop column IS_PASS_KERBEROS;
alter table PARTY drop column PASSWD;
