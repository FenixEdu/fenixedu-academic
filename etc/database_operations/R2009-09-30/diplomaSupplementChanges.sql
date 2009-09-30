alter table `ACADEMIC_SERVICE_REQUEST` add column `FAMILY_NAMES` text;
alter table `ACADEMIC_SERVICE_REQUEST` add column `GIVEN_NAMES` text;
alter table `EXTERNAL_ENROLMENT` add column `REGIME` text;

create table `ECTS_CONVERSION_TABLE` (
  `CYCLE` text,
  `ECTS_TABLE` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_COMPETENCE_COURSE` int(11),
  `KEY_CURRICULAR_YEAR` int(11),
  `KEY_DEGREE` int(11),
  `KEY_SCHOOL` int(11),
  `OID` bigint(20),
  `OID_COMPETENCE_COURSE` bigint(20),
  `OID_CURRICULAR_YEAR` bigint(20),
  `OID_DEGREE` bigint(20),
  `OID_SCHOOL` bigint(20),
  `PERCENTAGES` longtext,
  `YEAR` longtext,
  `OJB_CONCRETE_CLASS` longtext,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_COMPETENCE_COURSE),
  index (OID_COMPETENCE_COURSE),
  index (KEY_CURRICULAR_YEAR),
  index (OID_CURRICULAR_YEAR),
  index (KEY_DEGREE),
  index (OID_DEGREE),
  index (KEY_SCHOOL),
  index (OID_SCHOOL)
) type=InnoDB, character set latin1 ;

create table `EXTRA_CURRICULAR_ACTIVITY` (
  `ACTIVITY_INTERVAL` longtext,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_FEATURED_DIPLOMA` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_STUDENT` int(11),
  `KEY_TYPE` int(11),
  `OID` bigint(20),
  `OID_FEATURED_DIPLOMA` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_STUDENT` bigint(20),
  `OID_TYPE` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_FEATURED_DIPLOMA),
  index (OID_FEATURED_DIPLOMA),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (KEY_STUDENT),
  index (OID_STUDENT),
  index (KEY_TYPE),
  index (OID_TYPE)
) type=InnoDB, character set latin1 ;

create table `EXTRA_CURRICULAR_ACTIVITY_TYPE` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `NAME` longtext,
  `OID` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;

create table `DEGREE_OFFICIAL_PUBLICATION` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_DEGREE` int(11),
  `DGES_REFERENCE` text,
  `UNIVERSITY_REFERENCE` text,
  `OFFICIAL_REFERENCE` text not null,
  `OID` bigint(20),
  `OID_DEGREE` bigint(20),
  `PUBLICATION` text not null,
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_DEGREE),
  index (OID_DEGREE)
) type=InnoDB, character set latin1 ;


