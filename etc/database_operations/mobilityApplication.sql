-- 1st Data change (new domain entities, renamed EramsusCandidacyPeriod, ErasmusCandidacyProcess, ErasmusCoordinator)
create table `MOBILITY_AGREEMENT` (`OID_MOBILITY_PROGRAM` bigint unsigned, `OID` bigint unsigned, `OID_UNIVERSITY_UNIT` bigint unsigned, `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_MOBILITY_PROGRAM), index (OID_UNIVERSITY_UNIT), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;
alter table `QUEUE_JOB` change `OID_ERASMUS_CANDIDACY_PROCESS` `OID_MOBILITY_APPLICATION_PROCESS` bigint unsigned, add index (OID_MOBILITY_APPLICATION_PROCESS);
alter table `EXECUTED_ACTION` change `OID_ERASMUS_CANDIDACY_PROCESS` `OID_MOBILITY_APPLICATION_PROCESS` bigint unsigned, add index (OID_MOBILITY_APPLICATION_PROCESS);


create table `MOBILITY_QUOTA` (`OID` bigint unsigned, `OID_DEGREE` bigint unsigned, `OID_APPLICATION_PERIOD` bigint unsigned, `NUMBER_OF_OPENINGS` int(11), `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `OID_MOBILITY_AGREEMENT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_DEGREE), index (OID_APPLICATION_PERIOD), index (OID_ROOT_DOMAIN_OBJECT), index (OID_MOBILITY_AGREEMENT)) ENGINE=InnoDB, character set latin1;


create table `PROGRAM` (`OID` bigint unsigned, `REGISTRATION_AGREEMENT` text, `OJB_CONCRETE_CLASS` varchar(255) NOT NULL DEFAULT '', `OID_ROOT_DOMAIN_OBJECT` bigint unsigned, `OID_LEARNING_AGREEMENT` bigint unsigned, `ID_INTERNAL` int(11) NOT NULL auto_increment, primary key (ID_INTERNAL), index (OID), index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;

alter table `ERASMUS_STUDENT_DATA` add `OID_SELECTED_OPENING` bigint unsigned, add index (OID_SELECTED_OPENING);


update PROCESS set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess' where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCandidacyProcess';

update CANDIDACY_PERIOD set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod' where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod';

alter table `ERASMUS_COORDINATOR` change `OID_PROCESS` `OID_MOBILITY_APPLICATION_PROCESS` bigint unsigned, add index (OID_MOBILITY_APPLICATION_PROCESS);
-- ERASMUS_COORDINATOR might not have a column called OJB_CONCRETE_CLASS
delimiter '//'

CREATE PROCEDURE condUpdate() BEGIN
IF EXISTS(
	select * from information_schema.COLUMNS 
	where TABLE_SCHEMA = 'fenix' and TABLE_NAME = 'ERASMUS_COORDINATOR' and COLUMN_NAME = 'OJB_CONCRETE_CLASS'
	)
	THEN
		update `ERASMUS_COORDINATOR` set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityCoordinator' where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinator';

END IF;
END;
//

delimiter ';'

CALL condUpdate();

DROP PROCEDURE condUpdate;

update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityCoordinator' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusCoordinator';
rename table `ERASMUS_COORDINATOR` to `MOBILITY_COORDINATOR`;



-- 2nd Data change (renamed EramusIndividualCandidacyProcess, EramsusIndividualCandidacy)
alter table `FILE` change `OID_ERASMUS_INDIVIDUAL_CANDIDACY` `OID_MOBILITY_INDIVIDUAL_APPLICATION` bigint unsigned, add index (OID_MOBILITY_INDIVIDUAL_APPLICATION);
alter table `ERASMUS_STUDENT_DATA` change `OID_ERASMUS_INDIVIDUAL_CANDIDACY` `OID_MOBILITY_INDIVIDUAL_APPLICATION` bigint unsigned, add index (OID_MOBILITY_INDIVIDUAL_APPLICATION);
alter table `PROCESS` change `VALIDATED_BY_ERASMUS_COORDINATOR` `VALIDATED_BY_MOBILITY_COORDINATOR` tinyint(1);
alter table `EXECUTED_ACTION` change `OID_ERASMUS_INDIVIDUAL_CANDIDACY_PROCESS` `OID_MOBILITY_INDIVIDUAL_APPLICATION_PROCESS` bigint unsigned, add index (OID_MOBILITY_INDIVIDUAL_APPLICATION_PROCESS);

alter table `ERASMUS_INDIVIDUAL_CANDIDACY_CURRICULAR_COURSES` change `OID_ERASMUS_INDIVIDUAL_CANDIDACY` `OID_MOBILITY_INDIVIDUAL_APPLICATION` bigint unsigned, add index (OID_MOBILITY_INDIVIDUAL_APPLICATION);
rename table `ERASMUS_INDIVIDUAL_CANDIDACY_CURRICULAR_COURSES`to `MOBILITY_INDIVIDUAL_APPLICATION_CURRICULAR_COURSES`;

alter table `ERASMUS_CANDIDACY_EXECUTED_ACTION_ERASMUS_INDIVIDUAL_PROCESS` change `OID_ERASMUS_INDIVIDUAL_CANDIDACY_PROCESS` `OID_MOBILITY_INDIVIDUAL_APPLICATION_PROCESS` bigint unsigned, add index (OID_MOBILITY_INDIVIDUAL_APPLICATION_PROCESS);
rename table `ERASMUS_CANDIDACY_EXECUTED_ACTION_ERASMUS_INDIVIDUAL_PROCESS`to `ERASMUS_CANDIDACY_EXECUTED_ACTION_MOBILITY_INDIVIDUAL_PROCESS`;

update PROCESS set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess' where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess';

update INDIVIDUAL_CANDIDACY set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplication' where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacy';
update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplication' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacy';



-- 3rd Data change (renamed ErasmusStudentData)
alter table `INDIVIDUAL_CANDIDACY` change `OID_ERASMUS_STUDENT_DATA` `OID_MOBILITY_STUDENT_DATA` bigint unsigned, add index (OID_MOBILITY_STUDENT_DATA);

delimiter '//'

CREATE PROCEDURE condUpdate() BEGIN
IF EXISTS(
	select * from information_schema.COLUMNS 
	where TABLE_SCHEMA = 'fenix' and TABLE_NAME = 'ERASMUS_STUDENT_DATA' and COLUMN_NAME = 'OJB_CONCRETE_CLASS'
	)
	THEN
		update `ERASMUS_STUDENT_DATA` set OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData' 			where OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusStudentData';

END IF;
END;
//

delimiter ';'

CALL condUpdate();

DROP PROCEDURE condUpdate;

update FF$DOMAIN_CLASS_INFO set DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData' where DOMAIN_CLASS_NAME = 'net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusStudentData';

rename table `ERASMUS_STUDENT_DATA` to `MOBILITY_STUDENT_DATA`;

create table `MOBILITY_EMAIL_TEMPLATE` (
	`OID` bigint unsigned, 
	`OID_ROOT_DOMAIN_OBJECT` bigint unsigned,
	`OID_MOBILITY_PROGRAM` bigint unsigned,
	`TYPE` varchar(200),
	`SUBJECT` longtext,
	`OID_PERIOD` bigint unsigned,
	`BODY` longtext, 
	`ID_INTERNAL` int(11) NOT NULL auto_increment, 
	primary key (ID_INTERNAL), 
	index (OID), 
	index (OID_MOBILITY_PROGRAM),
	index (OID_PERIOD), 
	index (OID_ROOT_DOMAIN_OBJECT)) ENGINE=InnoDB, character set latin1;
