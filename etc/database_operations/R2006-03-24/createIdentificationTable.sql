update PARTY set KEY_USER = NULL where PARTY.KEY_EXTERNAL_PERSON is not NULL 
   and PARTY.KEY_USER in (select USER.ID_INTERNAL from USER where USER.USERNAME like 'e%');

delete from USER where USER.USERNAME like 'e%';

CREATE TABLE `IDENTIFICATION` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `CLASS_NAME` varchar(255) NOT NULL default '',
  `BEGIN_DATE` varchar(10),
  `END_DATE` varchar(10),
  `ACTIVE` int(11) unsigned default NULL,  
  `USERNAME` varchar(50) NOT NULL,
  `PASSWORD` varchar(40) default NULL,
  `IS_PASS_KERBEROS` tinyint(1) NOT NULL default '0',    
  `KEY_USER` int(11) unsigned default NULL,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) unsigned default '1',
  PRIMARY KEY  (`ID_INTERNAL`),
  UNIQUE (`USERNAME`),
  KEY `KEY_USER` (`KEY_USER`),
  KEY `KEY_ROOT_DOMAIN_OBJECT` (`KEY_ROOT_DOMAIN_OBJECT`)
) ENGINE=InnoDB;

insert into `IDENTIFICATION` (KEY_USER, USERNAME, PASSWORD, IS_PASS_KERBEROS) 
   select USER.ID_INTERNAL, USER.USERNAME, USER.PASSWORD, USER.IS_PASS_KERBEROS from USER;
          
update `IDENTIFICATION` set CLASS_NAME = 'net.sourceforge.fenixedu.domain.Login';          
update `IDENTIFICATION` set BEGIN_DATE = '2006-03-24';
update `IDENTIFICATION` set ACTIVE = 1;
update `IDENTIFICATION` set KEY_ROOT_DOMAIN_OBJECT = 1;

alter table USER drop column `USERNAME`;
alter table USER drop column `PASSWORD`;
alter table USER drop column `IS_PASS_KERBEROS`;
alter table USER change column `IST_USERNAME` `USER_UID` varchar(100) default NULL;

drop table PARTY_PARTY;
