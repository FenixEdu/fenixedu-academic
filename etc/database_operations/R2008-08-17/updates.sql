


-- Inserted at 2008-08-16T21:45:49.743+01:00

alter table ACADEMIC_SERVICE_REQUEST add index (KEY_ACADEMIC_SERVICE_REQUEST_YEAR);
alter table ACADEMIC_SERVICE_REQUEST add index (KEY_INSTITUTION);
alter table ACADEMIC_SERVICE_REQUEST_YEAR add index (KEY_ROOT_DOMAIN_OBJECT);
alter table ALUMNI add index (KEY_STUDENT);
alter table ALUMNI_IDENTITY_CHECK_REQUEST add index (KEY_ALUMNI);
alter table ALUMNI_IDENTITY_CHECK_REQUEST add index (KEY_OPERATOR);
alter table BUSINESS_AREA add index (KEY_PARENT_AREA);
alter table CRON_SCRIPT_STATE add column RUN_NOW tinyint(1);
alter table DEGREE_CONTEXT add column PERIOD text;
alter table EDUCATION_AREA add index (KEY_PARENT_AREA);
alter table EVENT add index (KEY_RESIDENCE_MONTH);
alter table JOB add index (KEY_BUSINESS_AREA);
alter table JOB add index (KEY_COUNTRY);
alter table JOB add index (KEY_PERSON);
alter table QUALIFICATION add index (KEY_EDUCATION_AREA);
alter table QUALIFICATION add index (KEY_INSTITUTION);
alter table RESIDENCE_MONTH add index (KEY_YEAR);
alter table RESIDENCE_YEAR add index (KEY_UNIT);
alter table STUDENT add index (KEY_ALUMNI);
alter table SUPPORT_REQUEST add index (KEY_ERROR_LOG);
alter table SUPPORT_REQUEST add index (KEY_REQUESTER);
alter table SUPPORT_REQUEST add index (KEY_REQUEST_CONTEXT);
alter table THESIS add index (KEY_LAST_LIBRARY_OPERATION);
alter table THESIS_LIBRARY_OPERATION add index (KEY_NEXT);
alter table THESIS_LIBRARY_OPERATION add index (KEY_PERFORMER);
alter table THESIS_LIBRARY_OPERATION add index (KEY_PREVIOUS);
alter table THESIS_LIBRARY_OPERATION add index (KEY_THESIS);


