


-- Inserted at 2008-11-03T14:13:00.225Z

alter table ACADEMIC_SERVICE_REQUEST add column LANGUAGE text;
alter table COUNTRY add column COUNTRY_NATIONALITY text;
alter table PARTY_CONTACT add column VISIBLE_TO_ALUMNI tinyint(1);


create table PROFILE_REPORT (
  `DATE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `REPORT` longtext,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

