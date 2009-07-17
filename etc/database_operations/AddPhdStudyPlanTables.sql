create table `PHD_STUDY_PLAN` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_DEGREE` int(11) not null,
  `KEY_PROCESS` int(11) not null,
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `OID` bigint(20) not null,
  `OID_DEGREE` bigint(20) not null,
  `OID_PROCESS` bigint(20) not null,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_DEGREE),
  index (OID_DEGREE),
  index (KEY_PROCESS),
  index (OID_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `PHD_STUDY_PLAN_ENTRY` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `COURSE_NAME` longtext,
  `KEY_COMPETENCE_COURSE` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11) not null,
  `KEY_STUDY_PLAN` int(11) not null,
  `OID` bigint(20) not null,
  `OID_COMPETENCE_COURSE` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) not null,
  `OID_STUDY_PLAN` bigint(20) not null,
  `TYPE` varchar(255),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_COMPETENCE_COURSE),
  index (OID_COMPETENCE_COURSE),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (KEY_STUDY_PLAN),
  index (OID_STUDY_PLAN)
) type=InnoDB, character set latin1 ;


alter table `PHD_STUDY_PLAN_ENTRY` add column OJB_CONCRETE_CLASS varchar(255) not null;
alter table `PROCESS` add column `KEY_STUDY_PLAN` int(11);
alter table `PROCESS` add column `OID_STUDY_PLAN` bigint(20);
alter table `PROCESS` add index (`KEY_STUDY_PLAN`), add index (`OID_STUDY_PLAN`);
