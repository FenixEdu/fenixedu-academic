
DROP TABLE SEMINAR_COMMISSION_ELEMENT;

alter table `PROCESS` add column `OID_THESIS_PROCESS` bigint(20);

create table `THESIS_JURY_ELEMENT` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OJB_CONCRETE_CLASS` VARCHAR(255) NOT NULL,
  `CREATION_DATE` datetime NULL default NULL,
  `NAME` longtext,
  `ADDRESS` longtext,
  `CATEGORY` longtext,
  `EMAIL` longtext,
  `INSTITUTION` longtext,
  `PHONE` longtext,
  `QUALIFICATION` longtext,
  `PRESIDENT` tinyint(1),
  `REPORTER` tinyint(1),
  `ELEMENT_ORDER` int(11), 
  `OID` bigint(20) NOT NULL,
  `OID_PERSON` bigint(20),
  `OID_PROCESS` bigint(20) NOT NULL,
  `OID_ROOT_DOMAIN_OBJECT` bigint(20) NOT NULL,
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_PERSON),
  index (OID_PROCESS),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
