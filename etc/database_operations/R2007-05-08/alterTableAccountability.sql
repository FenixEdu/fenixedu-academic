update ACCOUNTABILITY set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeWorkingContract' where CONTRACT_TYPE like 'WORKING';
update ACCOUNTABILITY set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeMailingContract' where CONTRACT_TYPE like 'MAILING';

alter table ACCOUNTABILITY drop column CONTRACT_TYPE;

insert into ACCOUNTABILITY_TYPE (TYPE, OJB_CONCRETE_CLASS, NAME, KEY_ROOT_DOMAIN_OBJECT) values ('RESEARCHER_CONTRACT', 'net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityType', 'RESEARCHER_CONTRACT', 1);