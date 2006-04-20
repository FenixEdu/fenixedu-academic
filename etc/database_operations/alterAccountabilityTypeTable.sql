alter table ACCOUNTABILITY_TYPE add column `CLASS_NAME` varchar(255) NOT NULL default '';
alter table ACCOUNTABILITY_TYPE add column `NAME` varchar(100) default '';
alter table ACCOUNTABILITY_TYPE add column `FUNCTION_TYPE` varchar(80) default '';
alter table ACCOUNTABILITY_TYPE add column `BEGIN_DATE` date default NULL;
alter table ACCOUNTABILITY_TYPE add column `END_DATE` date default NULL;
alter table ACCOUNTABILITY_TYPE add column `KEY_UNIT` int(11) unsigned default NULL;
alter table ACCOUNTABILITY_TYPE add column `KEY_INHERENT_PARENT_FUNCTION` int(11) default NULL;
alter table ACCOUNTABILITY_TYPE add key `KEY_UNIT` (`KEY_UNIT`);
alter table ACCOUNTABILITY_TYPE add key `KEY_INHERENT_PARENT_FUNCTION` (`KEY_INHERENT_PARENT_FUNCTION`);

insert into ACCOUNTABILITY_TYPE (`ID_INTERNAL`, `NAME`, `KEY_INHERENT_PARENT_FUNCTION`, `FUNCTION_TYPE`, `BEGIN_DATE`, `END_DATE`, `KEY_UNIT`) 
    select `ID_INTERNAL` + 2, `NAME`, `KEY_INHERENT_PARENT_FUNCTION`, `TYPE`, `BEGIN_DATE`, `END_DATE`, `KEY_UNIT`
           from FUNCTION; 

update ACCOUNTABILITY set KEY_ACCOUNTABILITY_TYPE = KEY_FUNCTION + 2 where KEY_FUNCTION is not null;
alter table ACCOUNTABILITY drop column KEY_FUNCTION;

update ACCOUNTABILITY_TYPE set NAME = TYPE where NAME is null or NAME like '';
update ACCOUNTABILITY_TYPE set CLASS_NAME = 'net.sourceforge.fenixedu.domain.organizationalStructure.Function';
update ACCOUNTABILITY_TYPE set CLASS_NAME = 'net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType' where ID_INTERNAL in (1,2,3);
update ACCOUNTABILITY_TYPE set TYPE = 'MANAGEMENT_FUNCTION' where CLASS_NAME = 'net.sourceforge.fenixedu.domain.organizationalStructure.Function';
 