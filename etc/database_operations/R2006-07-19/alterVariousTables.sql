alter table `FUNCTION` change column `KEY_UNIT` `KEY_UNIT` int(11) NOT NULL default '0';

alter table `ACCOUNTABILITY` change column `KEY_ACCOUNTABILITY_TYPE` `KEY_ACCOUNTABILITY_TYPE` int(11) NOT NULL default '0';

alter table `CONTRACT` change column `KEY_WORKING_UNIT` `KEY_WORKING_UNIT` int(11) NOT NULL default '0';
alter table `CONTRACT` change column `KEY_EMPLOYEE` `KEY_EMPLOYEE` int(11) NOT NULL default '0';
alter table `CONTRACT` change column `BEGIN_DATE_YEAR_MONTH_DAY` `BEGIN_DATE_YEAR_MONTH_DAY` date NOT NULL;

alter table `TEACHER_LEGAL_REGIMEM` change column `KEY_TEACHER` `KEY_TEACHER` int(11) NOT NULL default '0';
alter table `TEACHER_LEGAL_REGIMEM` change column `BEGIN_DATE_YEAR_MONTH_DAY` `BEGIN_DATE_YEAR_MONTH_DAY` date NOT NULL;