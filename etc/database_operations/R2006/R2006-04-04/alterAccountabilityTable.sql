alter table `ACCOUNTABILITY` add column `BEGIN_DATE` varchar(10) default NULL;
alter table `ACCOUNTABILITY` add column `END_DATE` varchar(10) default NULL;
alter table `ACCOUNTABILITY` add column `KEY_FUNCTION` int(11) default NULL;
alter table `ACCOUNTABILITY` add column `CREDITS` double default NULL;
alter table `ACCOUNTABILITY` add column `CLASS_NAME` varchar(255) NOT NULL default '';
alter table `ACCOUNTABILITY` add key `KEY_FUNCTION` (`KEY_FUNCTION`);

insert into ACCOUNTABILITY_TYPE values (2, 'MANAGEMENT_FUNCTION', 1);

update `ACCOUNTABILITY` set CLASS_NAME = 'net.sourceforge.fenixedu.domain.organizationalStructure.Accountability';

insert into `ACCOUNTABILITY` (BEGIN_DATE, END_DATE, KEY_FUNCTION, CREDITS, KEY_CHILD_PARTY, KEY_PARENT_PARTY)                             
       select PERSON_FUNCTION.BEGIN_DATE, PERSON_FUNCTION.END_DATE, PERSON_FUNCTION.KEY_FUNCTION,
              PERSON_FUNCTION.CREDITS, PERSON_FUNCTION.KEY_PERSON, FUNCTION.KEY_UNIT 
       from PERSON_FUNCTION inner join FUNCTION on FUNCTION.ID_INTERNAL = PERSON_FUNCTION.KEY_FUNCTION;

update ACCOUNTABILITY set KEY_ROOT_DOMAIN_OBJECT = 1;
update ACCOUNTABILITY set KEY_ACCOUNTABILITY_TYPE = 2 where KEY_FUNCTION is not NULL;
update ACCOUNTABILITY set CLASS_NAME = 'net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction' where KEY_ACCOUNTABILITY_TYPE = 2;              
