


-- Inserted at 2008-01-15T14:24:51.066Z

alter table AVAILABILITY_POLICY add index (KEY_CONTENT);
alter table CONTENT add index (KEY_DEGREE);
alter table CONTENT add index (KEY_LOGO);
alter table CONTENT add index (KEY_MODULE_ROOT_DOMAIN_OBJECT);
alter table CONTENT add index (KEY_PERSON);
alter table CONTENT add index (KEY_PORTAL);
alter table CONTENT add index (KEY_PORTAL_ROOT_DOMAIN_OBJECT);
alter table CONTENT add index (KEY_UNIT);
alter table CURRICULUM_MODULE add index (KEY_CONCLUSION_PROCESS_RESPONSIBLE);
alter table EXECUTION_PATH add primary key (ID_INTERNAL);
alter table FUNCTIONALITY_PARAMETER add index (KEY_FUNCTIONALITY);
alter table FUNCTIONALITY_PARAMETER add index (KEY_ROOT_DOMAIN_OBJECT);
alter table FUNCTIONALITY_PARAMETER add index (KEY_TYPE);
--alter table GAUGING_TEST_RESULT add column CF_STRING text;
alter table META_DOMAIN_OBJECT add index (KEY_ROOT_DOMAIN_OBJECT);
alter table NODE add index (KEY_CHILD);
alter table NODE add index (KEY_PARENT);
alter table REGISTRATION add index (KEY_CONCLUSION_PROCESS_RESPONSIBLE);
alter table ROOT_DOMAIN_OBJECT add index (KEY_ROOT_MODULE);
alter table ROOT_DOMAIN_OBJECT add index (KEY_ROOT_PORTAL);
alter table TEACHER_SERVICE_DISTRIBUTION add index (KEY_PARENT);
alter table TEACHER_SERVICE_DISTRIBUTION add index (KEY_ROOT_DOMAIN_OBJECT);
alter table TEACHER_SERVICE_DISTRIBUTION add index (KEY_T_S_D_PROCESS_PHASE);
alter table T_S_D_COURSE add index (KEY_COMPETENCE_COURSE);
alter table T_S_D_COURSE add index (KEY_CURRICULAR_COURSE);
alter table T_S_D_COURSE add index (KEY_EXECUTION_PERIOD);
alter table T_S_D_COURSE add index (KEY_ROOT_DOMAIN_OBJECT);
alter table T_S_D_COURSE add index (KEY_T_S_D_CURRICULAR_COURSE_GROUP);
alter table T_S_D_CURRICULAR_LOAD add index (KEY_T_S_D_COURSE);
alter table T_S_D_PROCESS add index (KEY_CREATOR);
alter table T_S_D_PROCESS add index (KEY_DEPARTMENT);
alter table T_S_D_PROCESS add index (KEY_ROOT_DOMAIN_OBJECT);
alter table T_S_D_PROCESS_PHASE add index (KEY_NEXT_T_S_D_PROCESS_PHASE);
alter table T_S_D_PROCESS_PHASE add index (KEY_PREVIOUS_T_S_D_PROCESS_PHASE);
alter table T_S_D_PROCESS_PHASE add index (KEY_ROOT_DOMAIN_OBJECT);
alter table T_S_D_PROCESS_PHASE add index (KEY_T_S_D_PROCESS);
alter table T_S_D_PROFESSORSHIP add index (KEY_ROOT_DOMAIN_OBJECT);
alter table T_S_D_PROFESSORSHIP add index (KEY_T_S_D_COURSE);
alter table T_S_D_PROFESSORSHIP add index (KEY_T_S_D_TEACHER);
alter table T_S_D_TEACHER add index (KEY_CATEGORY);
alter table T_S_D_TEACHER add index (KEY_ROOT_DOMAIN_OBJECT);
alter table T_S_D_TEACHER add index (KEY_TEACHER);


