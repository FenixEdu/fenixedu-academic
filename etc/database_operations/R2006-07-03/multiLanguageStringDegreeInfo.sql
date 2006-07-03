alter table DEGREE_INFO change column DESCRIPTION OLD_DESCRIPTION text;
update DEGREE_INFO set OLD_DESCRIPTION = '__xpto__' where OLD_DESCRIPTION is null or OLD_DESCRIPTION = '';
update DEGREE_INFO set DESCRIPTION_EN = '__xpto__' where DESCRIPTION_EN is null or DESCRIPTION_EN = '';
alter table DEGREE_INFO add column DESCRIPTION longtext;
update DEGREE_INFO set DEGREE_INFO.DESCRIPTION = concat('pt', length(replace(DEGREE_INFO.OLD_DESCRIPTION, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_DESCRIPTION, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.DESCRIPTION_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.DESCRIPTION_EN, "__xpto__", "")); 
update DEGREE_INFO set DESCRIPTION = NULL WHERE DESCRIPTION = "pt0:en0:";
update DEGREE_INFO set DESCRIPTION = replace(DEGREE_INFO.DESCRIPTION, "pt0:", "");
update DEGREE_INFO set DESCRIPTION = replace(DEGREE_INFO.DESCRIPTION, "en0:", "");

alter table DEGREE_INFO change column HISTORY OLD_HISTORY text;
update DEGREE_INFO set OLD_HISTORY = '__xpto__' where OLD_HISTORY is null or OLD_HISTORY = '';
update DEGREE_INFO set HISTORY_EN = '__xpto__' where HISTORY_EN is null or HISTORY_EN = '';
alter table DEGREE_INFO add column HISTORY longtext;
update DEGREE_INFO set DEGREE_INFO.HISTORY = concat('pt', length(replace(DEGREE_INFO.OLD_HISTORY, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_HISTORY, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.HISTORY_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.HISTORY_EN, "__xpto__", "")); 
update DEGREE_INFO set HISTORY = NULL WHERE HISTORY = "pt0:en0:";
update DEGREE_INFO set HISTORY = replace(DEGREE_INFO.HISTORY, "pt0:", "");
update DEGREE_INFO set HISTORY = replace(DEGREE_INFO.HISTORY, "en0:", "");

alter table DEGREE_INFO change column OBJECTIVES OLD_OBJECTIVES text;
update DEGREE_INFO set OLD_OBJECTIVES = '__xpto__' where OLD_OBJECTIVES is null or OLD_OBJECTIVES = '';
update DEGREE_INFO set OBJECTIVES_EN = '__xpto__' where OBJECTIVES_EN is null or OBJECTIVES_EN = '';
alter table DEGREE_INFO add column OBJECTIVES longtext;
update DEGREE_INFO set DEGREE_INFO.OBJECTIVES = concat('pt', length(replace(DEGREE_INFO.OLD_OBJECTIVES, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_OBJECTIVES, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.OBJECTIVES_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.OBJECTIVES_EN, "__xpto__", "")); 
update DEGREE_INFO set OBJECTIVES = NULL WHERE OBJECTIVES = "pt0:en0:";
update DEGREE_INFO set OBJECTIVES = replace(DEGREE_INFO.OBJECTIVES, "pt0:", "");
update DEGREE_INFO set OBJECTIVES = replace(DEGREE_INFO.OBJECTIVES, "en0:", "");

alter table DEGREE_INFO change column DESIGNED_FOR OLD_DESIGNED_FOR text;
update DEGREE_INFO set OLD_DESIGNED_FOR = '__xpto__' where OLD_DESIGNED_FOR is null or OLD_DESIGNED_FOR = '';
update DEGREE_INFO set DESIGNED_FOR_EN = '__xpto__' where DESIGNED_FOR_EN is null or DESIGNED_FOR_EN = '';
alter table DEGREE_INFO add column DESIGNED_FOR longtext;
update DEGREE_INFO set DEGREE_INFO.DESIGNED_FOR = concat('pt', length(replace(DEGREE_INFO.OLD_DESIGNED_FOR, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_DESIGNED_FOR, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.DESIGNED_FOR_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.DESIGNED_FOR_EN, "__xpto__", "")); 
update DEGREE_INFO set DESIGNED_FOR = NULL WHERE DESIGNED_FOR = "pt0:en0:";
update DEGREE_INFO set DESIGNED_FOR = replace(DEGREE_INFO.DESIGNED_FOR, "pt0:", "");
update DEGREE_INFO set DESIGNED_FOR = replace(DEGREE_INFO.DESIGNED_FOR, "en0:", "");

alter table DEGREE_INFO change column PROFESSIONAL_EXITS OLD_PROFESSIONAL_EXITS text;
update DEGREE_INFO set OLD_PROFESSIONAL_EXITS = '__xpto__' where OLD_PROFESSIONAL_EXITS is null or OLD_PROFESSIONAL_EXITS = '';
update DEGREE_INFO set PROFESSIONAL_EXITS_EN = '__xpto__' where PROFESSIONAL_EXITS_EN is null or PROFESSIONAL_EXITS_EN = '';
alter table DEGREE_INFO add column PROFESSIONAL_EXITS longtext;
update DEGREE_INFO set DEGREE_INFO.PROFESSIONAL_EXITS = concat('pt', length(replace(DEGREE_INFO.OLD_PROFESSIONAL_EXITS, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_PROFESSIONAL_EXITS, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.PROFESSIONAL_EXITS_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.PROFESSIONAL_EXITS_EN, "__xpto__", "")); 
update DEGREE_INFO set PROFESSIONAL_EXITS = NULL WHERE PROFESSIONAL_EXITS = "pt0:en0:";
update DEGREE_INFO set PROFESSIONAL_EXITS = replace(DEGREE_INFO.PROFESSIONAL_EXITS, "pt0:", "");
update DEGREE_INFO set PROFESSIONAL_EXITS = replace(DEGREE_INFO.PROFESSIONAL_EXITS, "en0:", "");

alter table DEGREE_INFO change column OPERATIONAL_REGIME OLD_OPERATIONAL_REGIME text;
update DEGREE_INFO set OLD_OPERATIONAL_REGIME = '__xpto__' where OLD_OPERATIONAL_REGIME is null or OLD_OPERATIONAL_REGIME = '';
update DEGREE_INFO set OPERATIONAL_REGIME_EN = '__xpto__' where OPERATIONAL_REGIME_EN is null or OPERATIONAL_REGIME_EN = '';
alter table DEGREE_INFO add column OPERATIONAL_REGIME longtext;
update DEGREE_INFO set DEGREE_INFO.OPERATIONAL_REGIME = concat('pt', length(replace(DEGREE_INFO.OLD_OPERATIONAL_REGIME, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_OPERATIONAL_REGIME, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.OPERATIONAL_REGIME_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.OPERATIONAL_REGIME_EN, "__xpto__", "")); 
update DEGREE_INFO set OPERATIONAL_REGIME = NULL WHERE OPERATIONAL_REGIME = "pt0:en0:";
update DEGREE_INFO set OPERATIONAL_REGIME = replace(DEGREE_INFO.OPERATIONAL_REGIME, "pt0:", "");
update DEGREE_INFO set OPERATIONAL_REGIME = replace(DEGREE_INFO.OPERATIONAL_REGIME, "en0:", "");

alter table DEGREE_INFO change column GRATUITY OLD_GRATUITY text;
update DEGREE_INFO set OLD_GRATUITY = '__xpto__' where OLD_GRATUITY is null or OLD_GRATUITY = '';
update DEGREE_INFO set GRATUITY_EN = '__xpto__' where GRATUITY_EN is null or GRATUITY_EN = '';
alter table DEGREE_INFO add column GRATUITY longtext;
update DEGREE_INFO set DEGREE_INFO.GRATUITY = concat('pt', length(replace(DEGREE_INFO.OLD_GRATUITY, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_GRATUITY, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.GRATUITY_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.GRATUITY_EN, "__xpto__", "")); 
update DEGREE_INFO set GRATUITY = NULL WHERE GRATUITY = "pt0:en0:";
update DEGREE_INFO set GRATUITY = replace(DEGREE_INFO.GRATUITY, "pt0:", "");
update DEGREE_INFO set GRATUITY = replace(DEGREE_INFO.GRATUITY, "en0:", "");

alter table DEGREE_INFO change column SCHOOL_CALENDAR OLD_SCHOOL_CALENDAR text;
update DEGREE_INFO set OLD_SCHOOL_CALENDAR = '__xpto__' where OLD_SCHOOL_CALENDAR is null or OLD_SCHOOL_CALENDAR = '';
update DEGREE_INFO set SCHOOL_CALENDAR_EN = '__xpto__' where SCHOOL_CALENDAR_EN is null or SCHOOL_CALENDAR_EN = '';
alter table DEGREE_INFO add column SCHOOL_CALENDAR longtext;
update DEGREE_INFO set DEGREE_INFO.SCHOOL_CALENDAR = concat('pt', length(replace(DEGREE_INFO.OLD_SCHOOL_CALENDAR, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_SCHOOL_CALENDAR, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.SCHOOL_CALENDAR_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.SCHOOL_CALENDAR_EN, "__xpto__", "")); 
update DEGREE_INFO set SCHOOL_CALENDAR = NULL WHERE SCHOOL_CALENDAR = "pt0:en0:";
update DEGREE_INFO set SCHOOL_CALENDAR = replace(DEGREE_INFO.SCHOOL_CALENDAR, "pt0:", "");
update DEGREE_INFO set SCHOOL_CALENDAR = replace(DEGREE_INFO.SCHOOL_CALENDAR, "en0:", "");

alter table DEGREE_INFO change column CANDIDACY_PERIOD OLD_CANDIDACY_PERIOD text;
update DEGREE_INFO set OLD_CANDIDACY_PERIOD = '__xpto__' where OLD_CANDIDACY_PERIOD is null or OLD_CANDIDACY_PERIOD = '';
update DEGREE_INFO set CANDIDACY_PERIOD_EN = '__xpto__' where CANDIDACY_PERIOD_EN is null or CANDIDACY_PERIOD_EN = '';
alter table DEGREE_INFO add column CANDIDACY_PERIOD longtext;
update DEGREE_INFO set DEGREE_INFO.CANDIDACY_PERIOD = concat('pt', length(replace(DEGREE_INFO.OLD_CANDIDACY_PERIOD, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_CANDIDACY_PERIOD, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.CANDIDACY_PERIOD_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.CANDIDACY_PERIOD_EN, "__xpto__", "")); 
update DEGREE_INFO set CANDIDACY_PERIOD = NULL WHERE CANDIDACY_PERIOD = "pt0:en0:";
update DEGREE_INFO set CANDIDACY_PERIOD = replace(DEGREE_INFO.CANDIDACY_PERIOD, "pt0:", "");
update DEGREE_INFO set CANDIDACY_PERIOD = replace(DEGREE_INFO.CANDIDACY_PERIOD, "en0:", "");

alter table DEGREE_INFO change column SELECTION_RESULT_DEADLINE OLD_SELECTION_RESULT_DEADLINE text;
update DEGREE_INFO set OLD_SELECTION_RESULT_DEADLINE = '__xpto__' where OLD_SELECTION_RESULT_DEADLINE is null or OLD_SELECTION_RESULT_DEADLINE = '';
update DEGREE_INFO set SELECTION_RESULT_DEADLINE_EN = '__xpto__' where SELECTION_RESULT_DEADLINE_EN is null or SELECTION_RESULT_DEADLINE_EN = '';
alter table DEGREE_INFO add column SELECTION_RESULT_DEADLINE longtext;
update DEGREE_INFO set DEGREE_INFO.SELECTION_RESULT_DEADLINE = concat('pt', length(replace(DEGREE_INFO.OLD_SELECTION_RESULT_DEADLINE, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_SELECTION_RESULT_DEADLINE, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.SELECTION_RESULT_DEADLINE_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.SELECTION_RESULT_DEADLINE_EN, "__xpto__", "")); 
update DEGREE_INFO set SELECTION_RESULT_DEADLINE = NULL WHERE SELECTION_RESULT_DEADLINE = "pt0:en0:";
update DEGREE_INFO set SELECTION_RESULT_DEADLINE = replace(DEGREE_INFO.SELECTION_RESULT_DEADLINE, "pt0:", "");
update DEGREE_INFO set SELECTION_RESULT_DEADLINE = replace(DEGREE_INFO.SELECTION_RESULT_DEADLINE, "en0:", "");

alter table DEGREE_INFO change column ENROLMENT_PERIOD OLD_ENROLMENT_PERIOD text;
update DEGREE_INFO set OLD_ENROLMENT_PERIOD = '__xpto__' where OLD_ENROLMENT_PERIOD is null or OLD_ENROLMENT_PERIOD = '';
update DEGREE_INFO set ENROLMENT_PERIOD_EN = '__xpto__' where ENROLMENT_PERIOD_EN is null or ENROLMENT_PERIOD_EN = '';
alter table DEGREE_INFO add column ENROLMENT_PERIOD longtext;
update DEGREE_INFO set DEGREE_INFO.ENROLMENT_PERIOD = concat('pt', length(replace(DEGREE_INFO.OLD_ENROLMENT_PERIOD, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_ENROLMENT_PERIOD, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.ENROLMENT_PERIOD_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.ENROLMENT_PERIOD_EN, "__xpto__", "")); 
update DEGREE_INFO set ENROLMENT_PERIOD = NULL WHERE ENROLMENT_PERIOD = "pt0:en0:";
update DEGREE_INFO set ENROLMENT_PERIOD = replace(DEGREE_INFO.ENROLMENT_PERIOD, "pt0:", "");
update DEGREE_INFO set ENROLMENT_PERIOD = replace(DEGREE_INFO.ENROLMENT_PERIOD, "en0:", "");

alter table DEGREE_INFO change column ADDITIONAL_INFO OLD_ADDITIONAL_INFO text;
update DEGREE_INFO set OLD_ADDITIONAL_INFO = '__xpto__' where OLD_ADDITIONAL_INFO is null or OLD_ADDITIONAL_INFO = '';
update DEGREE_INFO set ADDITIONAL_INFO_EN = '__xpto__' where ADDITIONAL_INFO_EN is null or ADDITIONAL_INFO_EN = '';
alter table DEGREE_INFO add column ADDITIONAL_INFO longtext;
update DEGREE_INFO set DEGREE_INFO.ADDITIONAL_INFO = concat('pt', length(replace(DEGREE_INFO.OLD_ADDITIONAL_INFO, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_ADDITIONAL_INFO, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.ADDITIONAL_INFO_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.ADDITIONAL_INFO_EN, "__xpto__", "")); 
update DEGREE_INFO set ADDITIONAL_INFO = NULL WHERE ADDITIONAL_INFO = "pt0:en0:";
update DEGREE_INFO set ADDITIONAL_INFO = replace(DEGREE_INFO.ADDITIONAL_INFO, "pt0:", "");
update DEGREE_INFO set ADDITIONAL_INFO = replace(DEGREE_INFO.ADDITIONAL_INFO, "en0:", "");

alter table DEGREE_INFO change column LINKS OLD_LINKS text;
update DEGREE_INFO set OLD_LINKS = '__xpto__' where OLD_LINKS is null or OLD_LINKS = '';
update DEGREE_INFO set LINKS_EN = '__xpto__' where LINKS_EN is null or LINKS_EN = '';
alter table DEGREE_INFO add column LINKS longtext;
update DEGREE_INFO set DEGREE_INFO.LINKS = concat('pt', length(replace(DEGREE_INFO.OLD_LINKS, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_LINKS, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.LINKS_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.LINKS_EN, "__xpto__", "")); 
update DEGREE_INFO set LINKS = NULL WHERE LINKS = "pt0:en0:";
update DEGREE_INFO set LINKS = replace(DEGREE_INFO.LINKS, "pt0:", "");
update DEGREE_INFO set LINKS = replace(DEGREE_INFO.LINKS, "en0:", "");

alter table DEGREE_INFO change column TEST_INGRESSION OLD_TEST_INGRESSION text;
update DEGREE_INFO set OLD_TEST_INGRESSION = '__xpto__' where OLD_TEST_INGRESSION is null or OLD_TEST_INGRESSION = '';
update DEGREE_INFO set TEST_INGRESSION_EN = '__xpto__' where TEST_INGRESSION_EN is null or TEST_INGRESSION_EN = '';
alter table DEGREE_INFO add column TEST_INGRESSION longtext;
update DEGREE_INFO set DEGREE_INFO.TEST_INGRESSION = concat('pt', length(replace(DEGREE_INFO.OLD_TEST_INGRESSION, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_TEST_INGRESSION, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.TEST_INGRESSION_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.TEST_INGRESSION_EN, "__xpto__", "")); 
update DEGREE_INFO set TEST_INGRESSION = NULL WHERE TEST_INGRESSION = "pt0:en0:";
update DEGREE_INFO set TEST_INGRESSION = replace(DEGREE_INFO.TEST_INGRESSION, "pt0:", "");
update DEGREE_INFO set TEST_INGRESSION = replace(DEGREE_INFO.TEST_INGRESSION, "en0:", "");

alter table DEGREE_INFO change column CLASSIFICATIONS OLD_CLASSIFICATIONS text;
update DEGREE_INFO set OLD_CLASSIFICATIONS = '__xpto__' where OLD_CLASSIFICATIONS is null or OLD_CLASSIFICATIONS = '';
update DEGREE_INFO set CLASSIFICATIONS_EN = '__xpto__' where CLASSIFICATIONS_EN is null or CLASSIFICATIONS_EN = '';
alter table DEGREE_INFO add column CLASSIFICATIONS longtext;
update DEGREE_INFO set DEGREE_INFO.CLASSIFICATIONS = concat('pt', length(replace(DEGREE_INFO.OLD_CLASSIFICATIONS, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_CLASSIFICATIONS, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.CLASSIFICATIONS_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.CLASSIFICATIONS_EN, "__xpto__", "")); 
update DEGREE_INFO set CLASSIFICATIONS = NULL WHERE CLASSIFICATIONS = "pt0:en0:";
update DEGREE_INFO set CLASSIFICATIONS = replace(DEGREE_INFO.CLASSIFICATIONS, "pt0:", "");
update DEGREE_INFO set CLASSIFICATIONS = replace(DEGREE_INFO.CLASSIFICATIONS, "en0:", "");

alter table DEGREE_INFO change column ACCESS_REQUISITES OLD_ACCESS_REQUISITES text;
update DEGREE_INFO set OLD_ACCESS_REQUISITES = '__xpto__' where OLD_ACCESS_REQUISITES is null or OLD_ACCESS_REQUISITES = '';
update DEGREE_INFO set ACCESS_REQUISITES_EN = '__xpto__' where ACCESS_REQUISITES_EN is null or ACCESS_REQUISITES_EN = '';
alter table DEGREE_INFO add column ACCESS_REQUISITES longtext;
update DEGREE_INFO set DEGREE_INFO.ACCESS_REQUISITES = concat('pt', length(replace(DEGREE_INFO.OLD_ACCESS_REQUISITES, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_ACCESS_REQUISITES, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.ACCESS_REQUISITES_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.ACCESS_REQUISITES_EN, "__xpto__", "")); 
update DEGREE_INFO set ACCESS_REQUISITES = NULL WHERE ACCESS_REQUISITES = "pt0:en0:";
update DEGREE_INFO set ACCESS_REQUISITES = replace(DEGREE_INFO.ACCESS_REQUISITES, "pt0:", "");
update DEGREE_INFO set ACCESS_REQUISITES = replace(DEGREE_INFO.ACCESS_REQUISITES, "en0:", "");

alter table DEGREE_INFO change column CANDIDACY_DOCUMENTS OLD_CANDIDACY_DOCUMENTS text;
update DEGREE_INFO set OLD_CANDIDACY_DOCUMENTS = '__xpto__' where OLD_CANDIDACY_DOCUMENTS is null or OLD_CANDIDACY_DOCUMENTS = '';
update DEGREE_INFO set CANDIDACY_DOCUMENTS_EN = '__xpto__' where CANDIDACY_DOCUMENTS_EN is null or CANDIDACY_DOCUMENTS_EN = '';
alter table DEGREE_INFO add column CANDIDACY_DOCUMENTS longtext;
update DEGREE_INFO set DEGREE_INFO.CANDIDACY_DOCUMENTS = concat('pt', length(replace(DEGREE_INFO.OLD_CANDIDACY_DOCUMENTS, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_CANDIDACY_DOCUMENTS, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.CANDIDACY_DOCUMENTS_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.CANDIDACY_DOCUMENTS_EN, "__xpto__", "")); 
update DEGREE_INFO set CANDIDACY_DOCUMENTS = NULL WHERE CANDIDACY_DOCUMENTS = "pt0:en0:";
update DEGREE_INFO set CANDIDACY_DOCUMENTS = replace(DEGREE_INFO.CANDIDACY_DOCUMENTS, "pt0:", "");
update DEGREE_INFO set CANDIDACY_DOCUMENTS = replace(DEGREE_INFO.CANDIDACY_DOCUMENTS, "en0:", "");

alter table DEGREE_INFO change column QUALIFICATION_LEVEL OLD_QUALIFICATION_LEVEL text;
update DEGREE_INFO set OLD_QUALIFICATION_LEVEL = '__xpto__' where OLD_QUALIFICATION_LEVEL is null or OLD_QUALIFICATION_LEVEL = '';
update DEGREE_INFO set QUALIFICATION_LEVEL_EN = '__xpto__' where QUALIFICATION_LEVEL_EN is null or QUALIFICATION_LEVEL_EN = '';
alter table DEGREE_INFO add column QUALIFICATION_LEVEL longtext;
update DEGREE_INFO set DEGREE_INFO.QUALIFICATION_LEVEL = concat('pt', length(replace(DEGREE_INFO.OLD_QUALIFICATION_LEVEL, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_QUALIFICATION_LEVEL, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.QUALIFICATION_LEVEL_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.QUALIFICATION_LEVEL_EN, "__xpto__", "")); 
update DEGREE_INFO set QUALIFICATION_LEVEL = NULL WHERE QUALIFICATION_LEVEL = "pt0:en0:";
update DEGREE_INFO set QUALIFICATION_LEVEL = replace(DEGREE_INFO.QUALIFICATION_LEVEL, "pt0:", "");
update DEGREE_INFO set QUALIFICATION_LEVEL = replace(DEGREE_INFO.QUALIFICATION_LEVEL, "en0:", "");

alter table DEGREE_INFO change column RECOGNITIONS OLD_RECOGNITIONS text;
update DEGREE_INFO set OLD_RECOGNITIONS = '__xpto__' where OLD_RECOGNITIONS is null or OLD_RECOGNITIONS = '';
update DEGREE_INFO set RECOGNITIONS_EN = '__xpto__' where RECOGNITIONS_EN is null or RECOGNITIONS_EN = '';
alter table DEGREE_INFO add column RECOGNITIONS longtext;
update DEGREE_INFO set DEGREE_INFO.RECOGNITIONS = concat('pt', length(replace(DEGREE_INFO.OLD_RECOGNITIONS, "__xpto__", "")), ':', replace(DEGREE_INFO.OLD_RECOGNITIONS, "__xpto__", ""), 'en', length(replace(DEGREE_INFO.RECOGNITIONS_EN, "__xpto__", "")), ':', replace(DEGREE_INFO.RECOGNITIONS_EN, "__xpto__", "")); 
update DEGREE_INFO set RECOGNITIONS = NULL WHERE RECOGNITIONS = "pt0:en0:";
update DEGREE_INFO set RECOGNITIONS = replace(DEGREE_INFO.RECOGNITIONS, "pt0:", "");
update DEGREE_INFO set RECOGNITIONS = replace(DEGREE_INFO.RECOGNITIONS, "en0:", "");
