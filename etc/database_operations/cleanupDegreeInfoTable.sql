create table DEGREE_INFO_CANDIDACY
   select
      ID_INTERNAL,
      KEY_ROOT_DOMAIN_OBJECT,
      ID_INTERNAL as 'KEY_DEGREE_INFO',
      ACCESS_REQUISITES,
      CANDIDACY_DOCUMENTS,
      CANDIDACY_PERIOD,
      ENROLMENT_PERIOD,
      SELECTION_RESULT_DEADLINE,
      TEST_INGRESSION
   from DEGREE_INFO;

alter table DEGREE_INFO_CANDIDACY type = InnoDB;

alter table DEGREE_INFO_CANDIDACY
   add primary key (ID_INTERNAL),
   add index (KEY_ROOT_DOMAIN_OBJECT),
   add index(KEY_DEGREE_INFO);

alter table DEGREE_INFO add column KEY_DEGREE_INFO_CANDIDACY int(11) not null;
update DEGREE_INFO set KEY_DEGREE_INFO_CANDIDACY = ID_INTERNAL;


create table DEGREE_INFO_FUTURE
   select
      ID_INTERNAL,
      KEY_ROOT_DOMAIN_OBJECT,
      ID_INTERNAL as 'KEY_DEGREE_INFO',
      OBJECTIVES,
      DESIGNED_FOR,
      PROFESSIONAL_EXITS,
      QUALIFICATION_LEVEL,
      RECOGNITIONS,
      CLASSIFICATIONS
   from DEGREE_INFO;

alter table DEGREE_INFO_FUTURE type = InnoDB;

alter table DEGREE_INFO_FUTURE
   add primary key (ID_INTERNAL),
   add index (KEY_ROOT_DOMAIN_OBJECT),
   add index(KEY_DEGREE_INFO);

alter table DEGREE_INFO add column KEY_DEGREE_INFO_FUTURE int(11) not null;
update DEGREE_INFO set KEY_DEGREE_INFO_FUTURE = ID_INTERNAL;


alter table DEGREE_INFO
   drop column OLD_DESCRIPTION,
   drop column OLD_OBJECTIVES,
   drop column OLD_HISTORY,
   drop column OLD_PROFESSIONAL_EXITS,
   drop column OLD_ADDITIONAL_INFO,
   drop column OLD_LINKS,
   drop column OLD_TEST_INGRESSION,
   drop column OLD_CLASSIFICATIONS,
   drop column OLD_QUALIFICATION_LEVEL,
   drop column OLD_RECOGNITIONS,
   drop column OLD_DESIGNED_FOR,
   drop column OLD_OPERATIONAL_REGIME,
   drop column OLD_GRATUITY,
   drop column OLD_SCHOOL_CALENDAR,
   drop column OLD_CANDIDACY_PERIOD,
   drop column OLD_SELECTION_RESULT_DEADLINE,
   drop column OLD_ENROLMENT_PERIOD,
   drop column OLD_ACCESS_REQUISITES,
   drop column OLD_CANDIDACY_DOCUMENTS,
   drop column DESCRIPTION_EN,
   drop column OBJECTIVES_EN,
   drop column HISTORY_EN,
   drop column PROFESSIONAL_EXITS_EN,
   drop column ADDITIONAL_INFO_EN,
   drop column LINKS_EN,
   drop column TEST_INGRESSION_EN,
   drop column CLASSIFICATIONS_EN,
   drop column QUALIFICATION_LEVEL_EN,
   drop column RECOGNITIONS_EN,
   drop column DESIGNED_FOR_EN,
   drop column OPERATIONAL_REGIME_EN,
   drop column GRATUITY_EN,
   drop column SCHOOL_CALENDAR_EN,
   drop column CANDIDACY_PERIOD_EN,
   drop column SELECTION_RESULT_DEADLINE_EN,
   drop column ENROLMENT_PERIOD_EN,
   drop column ACCESS_REQUISITES_EN,
   drop column CANDIDACY_DOCUMENTS_EN,
   drop column ACCESS_REQUISITES,
   drop column CANDIDACY_DOCUMENTS,
   drop column CANDIDACY_PERIOD,
   drop column ENROLMENT_PERIOD,
   drop column SELECTION_RESULT_DEADLINE,
   drop column TEST_INGRESSION,
   drop column OBJECTIVES,
   drop column DESIGNED_FOR,
   drop column PROFESSIONAL_EXITS,
   drop column QUALIFICATION_LEVEL,
   drop column RECOGNITIONS,
   drop column CLASSIFICATIONS
;

alter table DEGREE_INFO type = InnoDB;
