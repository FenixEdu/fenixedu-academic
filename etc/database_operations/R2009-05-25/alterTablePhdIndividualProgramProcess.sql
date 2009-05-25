
alter table `PROCESS` add column `KEY_GUIDING` int(11);
alter table `PROCESS` add column `OID_GUIDING` bigint(20);
alter table `PROCESS` add column `THESIS_TITLE` text;
alter table `PROCESS` add index (`KEY_GUIDING`), add index (`OID_GUIDING`);

create table `PHD_PROGRAM_GUIDING` (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `OJB_CONCRETE_CLASS` longtext,
  `NAME` longtext,
  `QUALIFICATION` longtext,
  `CATEGORY` longtext,
  `WORK_LOCATION` longtext,
  `ADDRESS` longtext,
  `EMAIL` longtext,
  `PHONE` longtext,
  `KEY_ASSISTANT_PHD_INDIVIDUAL_PROGRAM_PROCESS` int(11),
  `KEY_PERSON` int(11),
  `KEY_PHD_INDIVIDUAL_PROGRAM_PROCESS` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11) NOT NULL DEFAULT 1,
  `OID` bigint(20),
  `OID_ASSISTANT_PHD_INDIVIDUAL_PROGRAM_PROCESS` bigint(20),
  `OID_PERSON` bigint(20),
  `OID_PHD_INDIVIDUAL_PROGRAM_PROCESS` bigint(20),
  `OID_ROOT_DOMAIN_OBJECT` bigint(20),
  primary key (ID_INTERNAL),
  index (OID),
  index (KEY_ASSISTANT_PHD_INDIVIDUAL_PROGRAM_PROCESS),
  index (OID_ASSISTANT_PHD_INDIVIDUAL_PROGRAM_PROCESS),
  index (KEY_PERSON),
  index (OID_PERSON),
  index (KEY_PHD_INDIVIDUAL_PROGRAM_PROCESS),
  index (OID_PHD_INDIVIDUAL_PROGRAM_PROCESS),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (OID_ROOT_DOMAIN_OBJECT)
) type=InnoDB, character set latin1 ;
