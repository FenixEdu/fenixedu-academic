create table `ERASMUS_VACANCY` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `NUMBER_OF_VACANCIES` int(11),
  `OID` bigint(20),
  `OID_CANDIDACY_PERIOD` bigint(20),
  `OID_DEGREE` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  `OID_UNIVERSITY_UNIT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (OID_CANDIDACY_PERIOD),
  index (OID_DEGREE),
  index (OID_ROOT_DOMAIN_OBJECT),
  index (OID_UNIVERSITY_UNIT)
) type=InnoDB, character set latin1 ;

alter table `ERASMUS_STUDENT_DATA` add column `OID_SELECTED_VACANCY` bigint(20);
alter table `ERASMUS_STUDENT_DATA` add index (`OID_SELECTED_VACANCY`);
