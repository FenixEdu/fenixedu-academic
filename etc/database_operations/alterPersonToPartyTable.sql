rename table `PERSON` to `PARTY`;
alter table `PARTY` add column `PARTY_TYPE` varchar(80) default 'UNKNOWN';
alter table `PARTY` add column `CLASS_NAME` varchar(255) NOT NULL default '';
update PARTY set CLASS_NAME = 'net.sourceforge.fenixedu.domain.Person';

alter table `PARTY` add column `BEGIN_DATE` date default NULL;
alter table `PARTY` add column `END_DATE` date default NULL;
alter table `PARTY` add column `KEY_DEPARTMENT` int(11) default NULL;
alter table `PARTY` add column `KEY_DEGREE` int(11) default NULL;
alter table `PARTY` add column `COST_CENTER_CODE` int(11) default NULL;

alter table `PARTY` add key `KEY_DEGREE` (`KEY_DEGREE`);
alter table `PARTY` add key `KEY_DEPARTMENT` (`KEY_DEPARTMENT`);

alter table PARTY drop key `U1`;
alter table PARTY drop key `U2`;