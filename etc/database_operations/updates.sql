


-- Inserted at 2010-05-03T17:08:02.745+01:00

alter table `FILE` add column `OID_TEACHER_EVALUATION` bigint(20);
alter table `FILE` add column `TEACHER_EVALUATION_FILE_TYPE` text;
alter table `FILE` add index (`OID_TEACHER_EVALUATION`);
alter table `PARTY` add column `OID_TEACHER_EVALUATION_PROCESS_FROM_EVALUATOR` bigint(20);
alter table `PARTY` add column `OID_TEACHER_EVALUATION_PROCESS_FROM_EVALUEE` bigint(20);


create table `FACULTY_EVALUATION_PROCESS` (
  `AUTO_EVALUATION_INTERVAL` longtext,
  `EVALUATION_INTERVAL` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `TITLE` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `TEACHER_EVALUATION` (
  `AUTO_EVALUATION_LOCK` timestamp NULL default NULL,
  `AUTO_EVALUATION_MARK` longtext,
  `EVALUATION_LOCK` timestamp NULL default NULL,
  `EVALUATION_MARK` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_TEACHER_EVALUATION_PROCESS` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `TEACHER_EVALUATION_PROCESS` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OID` bigint(20),
  `OID_EVALUEE` bigint(20),
  `OID_EVALUTOR` bigint(20),
  `OID_FACULTY_EVALUATION_PROCESS` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_TEACHER_EVALUATION` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_FACULTY_EVALUATION_PROCESS),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;




-- Inserted at 2010-05-04T11:53:38.027+01:00

alter table `TEACHER_EVALUATION_PROCESS` add column `OID_EVALUATOR` bigint(20);
alter table `TEACHER_EVALUATION_PROCESS` add index (`OID_EVALUATOR`);
alter table `TEACHER_EVALUATION_PROCESS` add index (`OID_EVALUEE`);


