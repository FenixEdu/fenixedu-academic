set autocommit = 0;
begin;

alter table ACADEMIC_CALENDAR_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT_ENTRY bigint unsigned default null,
    add index (OID_PARENT_ENTRY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_ROOT_DOMAIN_OBJECT_FOR_DEFAULT_ROOT_ENTRY bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT_FOR_DEFAULT_ROOT_ENTRY),
    add column OID_ROOT_DOMAIN_OBJECT_FOR_ROOT_ENTRIES bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT_FOR_ROOT_ENTRIES),
    add column OID_TEMPLATE_ENTRY bigint unsigned default null,
    add index (OID_TEMPLATE_ENTRY);
alter table ACADEMIC_SERVICE_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACADEMIC_SERVICE_REQUEST_YEAR bigint unsigned default null,
    add index (OID_ACADEMIC_SERVICE_REQUEST_YEAR),
    add column OID_ADMINISTRATIVE_OFFICE bigint unsigned default null,
    add index (OID_ADMINISTRATIVE_OFFICE),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_EQUIVALENCE_PLAN_REQUEST bigint unsigned default null,
    add index (OID_EQUIVALENCE_PLAN_REQUEST),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_INSTITUTION bigint unsigned default null,
    add index (OID_INSTITUTION),
    add column OID_NEW_COURSE_GROUP bigint unsigned default null,
    add index (OID_NEW_COURSE_GROUP),
    add column OID_OLD_COURSE_GROUP bigint unsigned default null,
    add index (OID_OLD_COURSE_GROUP),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ACADEMIC_SERVICE_REQUEST_SITUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACADEMIC_SERVICE_REQUEST bigint unsigned default null,
    add index (OID_ACADEMIC_SERVICE_REQUEST),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ACADEMIC_SERVICE_REQUEST_YEAR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ACCOUNT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ACCOUNTABILITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACCOUNTABILITY_TYPE bigint unsigned default null,
    add index (OID_ACCOUNTABILITY_TYPE),
    add column OID_CHILD_PARTY bigint unsigned default null,
    add index (OID_CHILD_PARTY),
    add column OID_CURRICULAR_YEAR bigint unsigned default null,
    add index (OID_CURRICULAR_YEAR),
    add column OID_DELEGATE bigint unsigned default null,
    add index (OID_DELEGATE),
    add column OID_PARENT_PARTY bigint unsigned default null,
    add index (OID_PARENT_PARTY),
    add column OID_RESPONSIBLE bigint unsigned default null,
    add index (OID_RESPONSIBLE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SENDER bigint unsigned default null,
    add index (OID_SENDER);
alter table ACCOUNTABILITY_TYPE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT_INHERENT_FUNCTION bigint unsigned default null,
    add index (OID_PARENT_INHERENT_FUNCTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table ACCOUNTING_TRANSACTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ADJUSTED_TRANSACTION bigint unsigned default null,
    add index (OID_ADJUSTED_TRANSACTION),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_INSTALLMENT bigint unsigned default null,
    add index (OID_INSTALLMENT),
    add column OID_RESPONSIBLE_USER bigint unsigned default null,
    add index (OID_RESPONSIBLE_USER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TRANSACTION_DETAIL bigint unsigned default null,
    add index (OID_TRANSACTION_DETAIL);
alter table ACCOUNTING_TRANSACTION_DETAIL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TRANSACTION bigint unsigned default null,
    add index (OID_TRANSACTION);
alter table ADMINISTRATIVE_OFFICE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SERVICE_AGREEMENT_TEMPLATE bigint unsigned default null,
    add index (OID_SERVICE_AGREEMENT_TEMPLATE),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table ADMINISTRATIVE_OFFICE_PERMISSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ADMINISTRATIVE_OFFICE_PERMISSION_GROUP bigint unsigned default null,
    add index (OID_ADMINISTRATIVE_OFFICE_PERMISSION_GROUP),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ADMINISTRATIVE_OFFICE_PERMISSION_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ADMINISTRATIVE_OFFICE bigint unsigned default null,
    add index (OID_ADMINISTRATIVE_OFFICE),
    add column OID_CAMPUS bigint unsigned default null,
    add index (OID_CAMPUS),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ADVISE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_END_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_END_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_START_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_START_EXECUTION_PERIOD),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table AFFINITY_CYCLE_COURSE_GROUP
    add column OID_CYCLE_COURSE_GROUP_DESTINATION_AFFINITIES bigint unsigned default null,
    add index (OID_CYCLE_COURSE_GROUP_DESTINATION_AFFINITIES),
    add column OID_CYCLE_COURSE_GROUP_SOURCE_AFFINITIES bigint unsigned default null,
    add index (OID_CYCLE_COURSE_GROUP_SOURCE_AFFINITIES);
alter table ALLOWED_TO_UPLOAD_IN_UNIT
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table ALUMNI
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table ALUMNI_IDENTITY_CHECK_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ALUMNI bigint unsigned default null,
    add index (OID_ALUMNI),
    add column OID_OPERATOR bigint unsigned default null,
    add index (OID_OPERATOR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ANNOUNCEMENT_BOARD_BOOKMARK
    add column OID_ANNOUNCEMENT_BOARD bigint unsigned default null,
    add index (OID_ANNOUNCEMENT_BOARD),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON);
alter table ANNOUNCEMENT_CATEGORY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ANNOUNCEMENT_CATEGORY_ANNOUNCEMENT
    add column OID_ANNOUNCEMENT bigint unsigned default null,
    add index (OID_ANNOUNCEMENT),
    add column OID_ANNOUNCEMENT_CATEGORY bigint unsigned default null,
    add index (OID_ANNOUNCEMENT_CATEGORY);
alter table ANUAL_BONUS_INSTALLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ARTICLE_ASSOCIATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ARTICLE bigint unsigned default null,
    add index (OID_ARTICLE),
    add column OID_CREATOR bigint unsigned default null,
    add index (OID_CREATOR),
    add column OID_JOURNAL_ISSUE bigint unsigned default null,
    add index (OID_JOURNAL_ISSUE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_CAMPUS_HISTORY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_CAMPUS bigint unsigned default null,
    add index (OID_CAMPUS),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_CLOSED_MONTH
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS_STATUS_HISTORY bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS_STATUS_HISTORY),
    add column OID_CLOSED_MONTH bigint unsigned default null,
    add index (OID_CLOSED_MONTH),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_EXEMPTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_EXEMPTION_SHIFT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS_EXEMPTION bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS_EXEMPTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_EXTRA_WORK
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS_CLOSED_MONTH bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS_CLOSED_MONTH),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_WORK_SCHEDULE_TYPE bigint unsigned default null,
    add index (OID_WORK_SCHEDULE_TYPE);
alter table ASSIDUOUSNESS_RECORD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ANULATED_ASSIDUOUSNESS_RECORD bigint unsigned default null,
    add index (OID_ANULATED_ASSIDUOUSNESS_RECORD),
    add column OID_ANULATION bigint unsigned default null,
    add index (OID_ANULATION),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_CLOCK_UNIT bigint unsigned default null,
    add index (OID_CLOCK_UNIT),
    add column OID_JUSTIFICATION_MOTIVE bigint unsigned default null,
    add index (OID_JUSTIFICATION_MOTIVE),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_RECORD_ASSIDUOUSNESS_RECORD_MONTH_INDEX
    add column OID_ASSIDUOUSNESS_RECORD bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS_RECORD),
    add column OID_ASSIDUOUSNESS_RECORD_MONTH_INDEX bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS_RECORD_MONTH_INDEX);
alter table ASSIDUOUSNESS_RECORD_MONTH_INDEX
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_STATUS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_STATUS_HISTORY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_ASSIDUOUSNESS_STATUS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS_STATUS),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ASSIDUOUSNESS_VACATIONS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ATTENDS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ALUNO bigint unsigned default null,
    add index (OID_ALUNO),
    add column OID_DISCIPLINA_EXECUCAO bigint unsigned default null,
    add index (OID_DISCIPLINA_EXECUCAO),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table AVAILABILITY_POLICY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONTENT bigint unsigned default null,
    add index (OID_CONTENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table BIBLIOGRAPHIC_REFERENCE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table BLUEPRINT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BLUEPRINT_FILE bigint unsigned default null,
    add index (OID_BLUEPRINT_FILE),
    add column OID_CREATION_PERSON bigint unsigned default null,
    add index (OID_CREATION_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SPACE bigint unsigned default null,
    add index (OID_SPACE);
alter table BRANCH
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table BUSINESS_AREA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT_AREA bigint unsigned default null,
    add index (OID_PARENT_AREA),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CANDIDACY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY_PROCESS bigint unsigned default null,
    add index (OID_CANDIDACY_PROCESS),
    add column OID_COUNTRY_OF_RESIDENCE bigint unsigned default null,
    add index (OID_COUNTRY_OF_RESIDENCE),
    add column OID_DFA_CANDIDACY_EVENT bigint unsigned default null,
    add index (OID_DFA_CANDIDACY_EVENT),
    add column OID_DISTRICT_SUBDIVISION_OF_RESIDENCE bigint unsigned default null,
    add index (OID_DISTRICT_SUBDIVISION_OF_RESIDENCE),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_PRECEDENT_DEGREE_INFORMATION bigint unsigned default null,
    add index (OID_PRECEDENT_DEGREE_INFORMATION),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCHOOL_TIME_DISTRICT_SUB_DIVISION_OF_RESIDENCE bigint unsigned default null,
    add index (OID_SCHOOL_TIME_DISTRICT_SUB_DIVISION_OF_RESIDENCE);
alter table CANDIDACY_DOCUMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY bigint unsigned default null,
    add index (OID_CANDIDACY),
    add column OID_FILE bigint unsigned default null,
    add index (OID_FILE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CANDIDACY_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_INTERVAL bigint unsigned default null,
    add index (OID_EXECUTION_INTERVAL),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CANDIDACY_PRECEDENT_DEGREE_INFORMATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY bigint unsigned default null,
    add index (OID_CANDIDACY),
    add column OID_COUNTRY bigint unsigned default null,
    add index (OID_COUNTRY),
    add column OID_INSTITUTION bigint unsigned default null,
    add index (OID_INSTITUTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table CANDIDACY_SITUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY bigint unsigned default null,
    add index (OID_CANDIDACY),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CANDIDATE_ENROLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_MASTER_DEGREE_CANDIDATE bigint unsigned default null,
    add index (OID_MASTER_DEGREE_CANDIDATE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CANDIDATE_SITUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_MASTER_DEGREE_CANDIDATE bigint unsigned default null,
    add index (OID_MASTER_DEGREE_CANDIDATE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CARD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CARD_GENERATION_BATCH
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CARD_GENERATION_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CARD_GENERATION_BATCH bigint unsigned default null,
    add index (OID_CARD_GENERATION_BATCH),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CARD_GENERATION_PROBLEM
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CARD_GENERATION_BATCH bigint unsigned default null,
    add index (OID_CARD_GENERATION_BATCH),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CAREER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CATEGORY bigint unsigned default null,
    add index (OID_CATEGORY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table CASE_STUDY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SEMINARY_THEME bigint unsigned default null,
    add index (OID_SEMINARY_THEME);
alter table CASE_STUDY_CHOICE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY bigint unsigned default null,
    add index (OID_CANDIDACY),
    add column OID_CASE_STUDY bigint unsigned default null,
    add index (OID_CASE_STUDY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CATEGORY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CLOCK_UNIT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CLOSED_MONTH
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CLOSED_MONTH_DOCUMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CLOSED_MONTH bigint unsigned default null,
    add index (OID_CLOSED_MONTH),
    add column OID_CLOSED_MONTH_FILE bigint unsigned default null,
    add index (OID_CLOSED_MONTH_FILE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CLOSED_MONTH_JUSTIFICATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS_CLOSED_MONTH bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS_CLOSED_MONTH),
    add column OID_JUSTIFICATION_MOTIVE bigint unsigned default null,
    add index (OID_JUSTIFICATION_MOTIVE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COMPETENCE_COURSE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COMPETENCE_COURSE_GROUP_UNIT bigint unsigned default null,
    add index (OID_COMPETENCE_COURSE_GROUP_UNIT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COMPETENCE_COURSE_DEPARTMENT
    add column OID_COMPETENCE_COURSE bigint unsigned default null,
    add index (OID_COMPETENCE_COURSE),
    add column OID_DEPARTMENT bigint unsigned default null,
    add index (OID_DEPARTMENT);
alter table COMPETENCE_COURSE_INFORMATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COMPETENCE_COURSE bigint unsigned default null,
    add index (OID_COMPETENCE_COURSE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COMPETENCE_COURSE_INFORMATION_CHANGE_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ANALIZED_BY bigint unsigned default null,
    add index (OID_ANALIZED_BY),
    add column OID_COMPETENCE_COURSE bigint unsigned default null,
    add index (OID_COMPETENCE_COURSE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_REQUESTER bigint unsigned default null,
    add index (OID_REQUESTER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COMPETENCE_COURSE_LOAD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COMPETENCE_COURSE_INFORMATION bigint unsigned default null,
    add index (OID_COMPETENCE_COURSE_INFORMATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CONCLUSION_PROCESS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONCLUSION_YEAR bigint unsigned default null,
    add index (OID_CONCLUSION_YEAR),
    add column OID_CYCLE bigint unsigned default null,
    add index (OID_CYCLE),
    add column OID_LAST_VERSION bigint unsigned default null,
    add index (OID_LAST_VERSION),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CONCLUSION_PROCESS_VERSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONCLUSION_PROCESS bigint unsigned default null,
    add index (OID_CONCLUSION_PROCESS),
    add column OID_CONCLUSION_YEAR bigint unsigned default null,
    add index (OID_CONCLUSION_YEAR),
    add column OID_DISSERTATION_ENROLMENT bigint unsigned default null,
    add index (OID_DISSERTATION_ENROLMENT),
    add column OID_INGRESSION_YEAR bigint unsigned default null,
    add index (OID_INGRESSION_YEAR),
    add column OID_LAST_VERSION_CONCLUSION_PROCESS bigint unsigned default null,
    add index (OID_LAST_VERSION_CONCLUSION_PROCESS),
    add column OID_MASTER_DEGREE_THESIS bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS),
    add column OID_RESPONSIBLE bigint unsigned default null,
    add index (OID_RESPONSIBLE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CONNECTION_RULE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACCOUNTABILITY_TYPE bigint unsigned default null,
    add index (OID_ACCOUNTABILITY_TYPE),
    add column OID_ALLOWED_CHILD_PARTY_TYPE bigint unsigned default null,
    add index (OID_ALLOWED_CHILD_PARTY_TYPE),
    add column OID_ALLOWED_PARENT_PARTY_TYPE bigint unsigned default null,
    add index (OID_ALLOWED_PARENT_PARTY_TYPE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CONTENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_AVAILABILITY_POLICY bigint unsigned default null,
    add index (OID_AVAILABILITY_POLICY),
    add column OID_CAMPUS bigint unsigned default null,
    add index (OID_CAMPUS),
    add column OID_CREATOR bigint unsigned default null,
    add index (OID_CREATOR),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_EXECUTION_PATH_VALUE bigint unsigned default null,
    add index (OID_EXECUTION_PATH_VALUE),
    add column OID_FILE bigint unsigned default null,
    add index (OID_FILE),
    add column OID_FUNCTIONALITY bigint unsigned default null,
    add index (OID_FUNCTIONALITY),
    add column OID_INITIAL_CONTENT bigint unsigned default null,
    add index (OID_INITIAL_CONTENT),
    add column OID_LOGO bigint unsigned default null,
    add index (OID_LOGO),
    add column OID_META_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_META_DOMAIN_OBJECT),
    add column OID_MODULE_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_MODULE_ROOT_DOMAIN_OBJECT),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_PORTAL bigint unsigned default null,
    add index (OID_PORTAL),
    add column OID_PORTAL_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_PORTAL_ROOT_DOMAIN_OBJECT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SITE_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_SITE_EXECUTION_COURSE),
    add column OID_THESIS bigint unsigned default null,
    add index (OID_THESIS),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table CONTEXT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BEGIN_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_BEGIN_EXECUTION_PERIOD),
    add column OID_CHILD_DEGREE_MODULE bigint unsigned default null,
    add index (OID_CHILD_DEGREE_MODULE),
    add column OID_CURRICULAR_PERIOD bigint unsigned default null,
    add index (OID_CURRICULAR_PERIOD),
    add column OID_END_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_END_EXECUTION_PERIOD),
    add column OID_PARENT_COURSE_GROUP bigint unsigned default null,
    add index (OID_PARENT_COURSE_GROUP),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CONTRIBUTOR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COOPERATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table COORDINATOR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COST_CENTER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COUNTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COURSE_EQUIVALENCY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_MODALITY bigint unsigned default null,
    add index (OID_MODALITY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SEMINARY bigint unsigned default null,
    add index (OID_SEMINARY);
alter table COURSE_EQUIVALENCY_THEME
    add column OID_COURSE_EQUIVALENCY bigint unsigned default null,
    add index (OID_COURSE_EQUIVALENCY),
    add column OID_THEME bigint unsigned default null,
    add index (OID_THEME);
alter table COURSE_HISTORIC
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COURSE_LOAD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table COURSE_LOAD_REQUEST_ENROLMENT
    add column OID_COURSE_LOAD_REQUEST bigint unsigned default null,
    add index (OID_COURSE_LOAD_REQUEST),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT);
alter table COURSE_LOAD_SHIFT
    add column OID_COURSE_LOAD bigint unsigned default null,
    add index (OID_COURSE_LOAD),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT);
alter table COURSE_REPORT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CREDITS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table CREDITS_DISMISSAL_NO_ENROL_CURRICULAR_COURSES
    add column OID_CREDITS_DISMISSAL bigint unsigned default null,
    add index (OID_CREDITS_DISMISSAL),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE);
alter table CREDITS_IN_ANY_SECUNDARY_AREA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table CREDITS_IN_SCIENTIFIC_AREA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCIENTIFIC_AREA bigint unsigned default null,
    add index (OID_SCIENTIFIC_AREA),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table CREDIT_LINE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table CREDIT_NOTE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_RECEIPT bigint unsigned default null,
    add index (OID_RECEIPT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CREDIT_NOTE_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACCOUNTING_ENTRY bigint unsigned default null,
    add index (OID_ACCOUNTING_ENTRY),
    add column OID_ADJUSTMENT_ACCOUNTING_ENTRY bigint unsigned default null,
    add index (OID_ADJUSTMENT_ACCOUNTING_ENTRY),
    add column OID_CREDIT_NOTE bigint unsigned default null,
    add index (OID_CREDIT_NOTE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CRON_REGISTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CRON_SCRIPT_INVOCATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CRON_SCRIPT_STATE bigint unsigned default null,
    add index (OID_CRON_SCRIPT_STATE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CRON_SCRIPT_STATE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CURRICULAR_COURSE_EQUIVALENCE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_EQUIVALENT_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_EQUIVALENT_CURRICULAR_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CURRICULAR_COURSE_EXECUTION_COURSE
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE);
alter table CURRICULAR_COURSE_SCOPE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BRANCH bigint unsigned default null,
    add index (OID_BRANCH),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_CURRICULAR_SEMESTER bigint unsigned default null,
    add index (OID_CURRICULAR_SEMESTER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CURRICULAR_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_PARENT bigint unsigned default null,
    add index (OID_PARENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CURRICULAR_RULE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BEGIN bigint unsigned default null,
    add index (OID_BEGIN),
    add column OID_CONTEXT_COURSE_GROUP bigint unsigned default null,
    add index (OID_CONTEXT_COURSE_GROUP),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_DEGREE_MODULE_TO_APPLY_RULE bigint unsigned default null,
    add index (OID_DEGREE_MODULE_TO_APPLY_RULE),
    add column OID_DEPARTMENT_UNIT bigint unsigned default null,
    add index (OID_DEPARTMENT_UNIT),
    add column OID_END bigint unsigned default null,
    add index (OID_END),
    add column OID_EXCLUSIVE_DEGREE_MODULE bigint unsigned default null,
    add index (OID_EXCLUSIVE_DEGREE_MODULE),
    add column OID_NOT_RULE bigint unsigned default null,
    add index (OID_NOT_RULE),
    add column OID_PARENT_COMPOSITE_RULE bigint unsigned default null,
    add index (OID_PARENT_COMPOSITE_RULE),
    add column OID_PRECEDENCE_DEGREE_MODULE bigint unsigned default null,
    add index (OID_PRECEDENCE_DEGREE_MODULE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_WRAPPED_RULE bigint unsigned default null,
    add index (OID_WRAPPED_RULE);
alter table CURRICULAR_SEMESTER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_YEAR bigint unsigned default null,
    add index (OID_CURRICULAR_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CURRICULAR_YEAR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CURRICULUM
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_PERSON_WHO_ALTERED bigint unsigned default null,
    add index (OID_PERSON_WHO_ALTERED),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table CURRICULUM_LINE_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_MODULE bigint unsigned default null,
    add index (OID_DEGREE_MODULE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_OPTIONAL_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_OPTIONAL_CURRICULAR_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table CURRICULUM_MODULE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONCLUSION_PROCESS bigint unsigned default null,
    add index (OID_CONCLUSION_PROCESS),
    add column OID_CREDITS bigint unsigned default null,
    add index (OID_CREDITS),
    add column OID_CURRICULUM_GROUP bigint unsigned default null,
    add index (OID_CURRICULUM_GROUP),
    add column OID_DEGREE_MODULE bigint unsigned default null,
    add index (OID_DEGREE_MODULE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_OPTIONAL_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_OPTIONAL_CURRICULAR_COURSE),
    add column OID_PARENT_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_PARENT_STUDENT_CURRICULAR_PLAN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table DEGREE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SENDER bigint unsigned default null,
    add index (OID_SENDER),
    add column OID_SITE bigint unsigned default null,
    add index (OID_SITE),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table DEGREE_CONTEXT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DEGREE_CURRICULAR_PLAN
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_DEGREE_STRUCTURE bigint unsigned default null,
    add index (OID_DEGREE_STRUCTURE),
    add column OID_EQUIVALENCE_PLAN bigint unsigned default null,
    add index (OID_EQUIVALENCE_PLAN),
    add column OID_ROOT bigint unsigned default null,
    add index (OID_ROOT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SERVICE_AGREEMENT_TEMPLATE bigint unsigned default null,
    add index (OID_SERVICE_AGREEMENT_TEMPLATE);
alter table DEGREE_INFO
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_DEGREE_INFO_CANDIDACY bigint unsigned default null,
    add index (OID_DEGREE_INFO_CANDIDACY),
    add column OID_DEGREE_INFO_FUTURE bigint unsigned default null,
    add index (OID_DEGREE_INFO_FUTURE),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DEGREE_INFO_CANDIDACY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_INFO bigint unsigned default null,
    add index (OID_DEGREE_INFO),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DEGREE_INFO_FUTURE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_INFO bigint unsigned default null,
    add index (OID_DEGREE_INFO),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DEGREE_MODULE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COMPETENCE_COURSE bigint unsigned default null,
    add index (OID_COMPETENCE_COURSE),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_PARENT_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_PARENT_DEGREE_CURRICULAR_PLAN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCIENTIFIC_AREA bigint unsigned default null,
    add index (OID_SCIENTIFIC_AREA),
    add column OID_STUDENT_COURSE_REPORT bigint unsigned default null,
    add index (OID_STUDENT_COURSE_REPORT),
    add column OID_UNIVERSITY bigint unsigned default null,
    add index (OID_UNIVERSITY);
alter table DELEGATE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DELEGATE_FUNCTION bigint unsigned default null,
    add index (OID_DELEGATE_FUNCTION),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DELEGATE_ELECTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY_PERIOD bigint unsigned default null,
    add index (OID_CANDIDACY_PERIOD),
    add column OID_CURRICULAR_YEAR bigint unsigned default null,
    add index (OID_CURRICULAR_YEAR),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_ELECTED_STUDENT bigint unsigned default null,
    add index (OID_ELECTED_STUDENT),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_VOTING_PERIOD bigint unsigned default null,
    add index (OID_VOTING_PERIOD);
alter table DELEGATE_ELECTION_CANDIDATES_STUDENT
    add column OID_DELEGATE_ELECTION bigint unsigned default null,
    add index (OID_DELEGATE_ELECTION),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table DELEGATE_ELECTION_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DELEGATE_ELECTION bigint unsigned default null,
    add index (OID_DELEGATE_ELECTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DELEGATE_ELECTION_STUDENT
    add column OID_DELEGATE_ELECTION bigint unsigned default null,
    add index (OID_DELEGATE_ELECTION),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table DELEGATE_ELECTION_VOTE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DELEGATE_ELECTION bigint unsigned default null,
    add index (OID_DELEGATE_ELECTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table DELEGATE_ELECTION_VOTING_STUDENT
    add column OID_DELEGATE_ELECTION bigint unsigned default null,
    add index (OID_DELEGATE_ELECTION),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table DELETE_FILE_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DELETE_FILE_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DEPARTMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEPARTMENT_UNIT bigint unsigned default null,
    add index (OID_DEPARTMENT_UNIT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DEPARTMENT_DEGREE
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_DEPARTMENT bigint unsigned default null,
    add index (OID_DEPARTMENT);
alter table DEPLOY_NOTIFIER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DISCOUNT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DISTRIBUTED_TEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ONLINE_TEST bigint unsigned default null,
    add index (OID_ONLINE_TEST),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEST_SCOPE bigint unsigned default null,
    add index (OID_TEST_SCOPE);
alter table DISTRICT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DISTRICT_SUBDIVISION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DISTRICT bigint unsigned default null,
    add index (OID_DISTRICT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DOMAIN_OBJECT_ACTION_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table DOMAIN_OBJECT_ACTION_LOG_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DOMAIN_OBJECT_ACTION_LOG bigint unsigned default null,
    add index (OID_DOMAIN_OBJECT_ACTION_LOG),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EDUCATION_AREA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT_AREA bigint unsigned default null,
    add index (OID_PARENT_AREA),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EMAIL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EMPLOYEE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EMPLOYEE_BONUS_INSTALLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ANUAL_BONUS_INSTALLMENT bigint unsigned default null,
    add index (OID_ANUAL_BONUS_INSTALLMENT),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EMPLOYEE_EXTRA_WORK_AUTHORIZATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_EXTRA_WORK_AUTHORIZATION bigint unsigned default null,
    add index (OID_EXTRA_WORK_AUTHORIZATION),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EMPLOYEE_HISTORIC
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_MAILING_COST_CENTER bigint unsigned default null,
    add index (OID_MAILING_COST_CENTER),
    add column OID_RESPONSABLE_EMPLOYEE bigint unsigned default null,
    add index (OID_RESPONSABLE_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SALARY_COST_CENTER bigint unsigned default null,
    add index (OID_SALARY_COST_CENTER),
    add column OID_WORKING_PLACE_COST_CENTER bigint unsigned default null,
    add index (OID_WORKING_PLACE_COST_CENTER);
alter table EMPLOYEE_MONTHLY_BONUS_INSTALLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE_BONUS_INSTALLMENT bigint unsigned default null,
    add index (OID_EMPLOYEE_BONUS_INSTALLMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ENROLMENT_EVALUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_IMPROVEMENT_OF_APPROVED_ENROLMENT_EVENT bigint unsigned default null,
    add index (OID_IMPROVEMENT_OF_APPROVED_ENROLMENT_EVENT),
    add column OID_MARK_SHEET bigint unsigned default null,
    add index (OID_MARK_SHEET),
    add column OID_PERSON_RESPONSIBLE_FOR_GRADE bigint unsigned default null,
    add index (OID_PERSON_RESPONSIBLE_FOR_GRADE),
    add column OID_RECTIFICATION bigint unsigned default null,
    add index (OID_RECTIFICATION),
    add column OID_RECTIFIED bigint unsigned default null,
    add index (OID_RECTIFIED),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ENROLMENT_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE);
alter table ENROLMENT_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ENROLMENT_WRAPPER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CREDITS bigint unsigned default null,
    add index (OID_CREDITS),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACCOUNT bigint unsigned default null,
    add index (OID_ACCOUNT),
    add column OID_ACCOUNTING_TRANSACTION bigint unsigned default null,
    add index (OID_ACCOUNTING_TRANSACTION),
    add column OID_ADJUSTMENT_CREDIT_NOTE_ENTRY bigint unsigned default null,
    add index (OID_ADJUSTMENT_CREDIT_NOTE_ENTRY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EQUIVALENCE_PLAN
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_OLD_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_OLD_STUDENT_CURRICULAR_PLAN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SOURCE_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_SOURCE_DEGREE_CURRICULAR_PLAN);
alter table EQUIVALENCE_PLAN_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EQUIVALENCE_PLAN bigint unsigned default null,
    add index (OID_EQUIVALENCE_PLAN),
    add column OID_PREVIOUS_COURSE_GROUP_FOR_NEW_DEGREE_MODULES bigint unsigned default null,
    add index (OID_PREVIOUS_COURSE_GROUP_FOR_NEW_DEGREE_MODULES),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EQUIVALENCE_PLAN_ENTRY_NEW_DEGREE_MODULE
    add column OID_DEGREE_MODULE bigint unsigned default null,
    add index (OID_DEGREE_MODULE),
    add column OID_EQUIVALENCE_PLAN_ENTRY bigint unsigned default null,
    add index (OID_EQUIVALENCE_PLAN_ENTRY);
alter table EQUIVALENCE_PLAN_ENTRY_OLD_DEGREE_MODULE
    add column OID_DEGREE_MODULE bigint unsigned default null,
    add index (OID_DEGREE_MODULE),
    add column OID_EQUIVALENCE_PLAN_ENTRY bigint unsigned default null,
    add index (OID_EQUIVALENCE_PLAN_ENTRY);
alter table ERROR_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXCEPTION bigint unsigned default null,
    add index (OID_EXCEPTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EVALUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DISTRIBUTED_TEST bigint unsigned default null,
    add index (OID_DISTRIBUTED_TEST),
    add column OID_GROUPING bigint unsigned default null,
    add index (OID_GROUPING),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EVALUATION_EXECUTION_COURSE
    add column OID_EVALUATION bigint unsigned default null,
    add index (OID_EVALUATION),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE);
alter table EVALUATION_METHOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EVENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACADEMIC_SERVICE_REQUEST bigint unsigned default null,
    add index (OID_ACADEMIC_SERVICE_REQUEST),
    add column OID_ADMINISTRATIVE_OFFICE bigint unsigned default null,
    add index (OID_ADMINISTRATIVE_OFFICE),
    add column OID_CANDIDACY bigint unsigned default null,
    add index (OID_CANDIDACY),
    add column OID_CANDIDACY_PROCESS bigint unsigned default null,
    add index (OID_CANDIDACY_PROCESS),
    add column OID_EMPLOYEE_RESPONSIBLE_FOR_CANCEL bigint unsigned default null,
    add index (OID_EMPLOYEE_RESPONSIBLE_FOR_CANCEL),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_GRATUITY_PAYMENT_PLAN bigint unsigned default null,
    add index (OID_GRATUITY_PAYMENT_PLAN),
    add column OID_INDIVIDUAL_CANDIDACY bigint unsigned default null,
    add index (OID_INDIVIDUAL_CANDIDACY),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_RESIDENCE_MONTH bigint unsigned default null,
    add index (OID_RESIDENCE_MONTH),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table EVENT_CONFERENCE_ARTICLES_ASSOCIATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONFERENCE_ARTICLE bigint unsigned default null,
    add index (OID_CONFERENCE_ARTICLE),
    add column OID_EVENT_EDITION bigint unsigned default null,
    add index (OID_EVENT_EDITION),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EVENT_EDITION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXAM_COORDINATOR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table EXAM_COORDINATOR_MANAGES_VIGILANT_GROUP
    add column OID_EXAM_COORDINATOR bigint unsigned default null,
    add index (OID_EXAM_COORDINATOR),
    add column OID_VIGILANT_GROUP bigint unsigned default null,
    add index (OID_VIGILANT_GROUP);
alter table EXAM_DATE_CERTIFICATE_REQUEST_ENROLMENT
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_EXAM_DATE_CERTIFICATE_REQUEST bigint unsigned default null,
    add index (OID_EXAM_DATE_CERTIFICATE_REQUEST);
alter table EXAM_DATE_CERTIFICATE_REQUEST_EXAM
    add column OID_EXAM bigint unsigned default null,
    add index (OID_EXAM),
    add column OID_EXAM_DATE_CERTIFICATE_REQUEST bigint unsigned default null,
    add index (OID_EXAM_DATE_CERTIFICATE_REQUEST);
alter table EXCEPTION_TYPE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXECUTION_COURSE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BOARD bigint unsigned default null,
    add index (OID_BOARD),
    add column OID_COURSE_REPORT bigint unsigned default null,
    add index (OID_COURSE_REPORT),
    add column OID_EVALUATION_METHOD bigint unsigned default null,
    add index (OID_EVALUATION_METHOD),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SENDER bigint unsigned default null,
    add index (OID_SENDER),
    add column OID_SITE bigint unsigned default null,
    add index (OID_SITE),
    add column OID_VIGILANT_GROUP bigint unsigned default null,
    add index (OID_VIGILANT_GROUP);
alter table EXECUTION_DEGREE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CAMPUS bigint unsigned default null,
    add index (OID_CAMPUS),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_GRATUITY_VALUES bigint unsigned default null,
    add index (OID_GRATUITY_VALUES),
    add column OID_PERIOD_EXAMS_FIRST_SEMESTER bigint unsigned default null,
    add index (OID_PERIOD_EXAMS_FIRST_SEMESTER),
    add column OID_PERIOD_EXAMS_SECOND_SEMESTER bigint unsigned default null,
    add index (OID_PERIOD_EXAMS_SECOND_SEMESTER),
    add column OID_PERIOD_EXAMS_SPECIAL_SEASON bigint unsigned default null,
    add index (OID_PERIOD_EXAMS_SPECIAL_SEASON),
    add column OID_PERIOD_GRADE_SUBMISSION_NORMAL_SEASON_FIRST_SEMESTER bigint unsigned default null,
    add index (OID_PERIOD_GRADE_SUBMISSION_NORMAL_SEASON_FIRST_SEMESTER),
    add column OID_PERIOD_GRADE_SUBMISSION_NORMAL_SEASON_SECOND_SEMESTER bigint unsigned default null,
    add index (OID_PERIOD_GRADE_SUBMISSION_NORMAL_SEASON_SECOND_SEMESTER),
    add column OID_PERIOD_GRADE_SUBMISSION_SPECIAL_SEASON bigint unsigned default null,
    add index (OID_PERIOD_GRADE_SUBMISSION_SPECIAL_SEASON),
    add column OID_PERIOD_LESSONS_FIRST_SEMESTER bigint unsigned default null,
    add index (OID_PERIOD_LESSONS_FIRST_SEMESTER),
    add column OID_PERIOD_LESSONS_SECOND_SEMESTER bigint unsigned default null,
    add index (OID_PERIOD_LESSONS_SECOND_SEMESTER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCHEDULING bigint unsigned default null,
    add index (OID_SCHEDULING);
alter table EXECUTION_INTERVAL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_INSURANCE_VALUE bigint unsigned default null,
    add index (OID_INSURANCE_VALUE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_ROOT_DOMAIN_OBJECT_FOR_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT_FOR_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT_FOR_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT_FOR_EXECUTION_YEAR),
    add column OID_SHIFT_DISTRIBUTION bigint unsigned default null,
    add index (OID_SHIFT_DISTRIBUTION);
alter table EXECUTION_PATH
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FUNCTIONALITY bigint unsigned default null,
    add index (OID_FUNCTIONALITY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXEMPTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_EXEMPTION_JUSTIFICATION bigint unsigned default null,
    add index (OID_EXEMPTION_JUSTIFICATION),
    add column OID_INSTALLMENT bigint unsigned default null,
    add index (OID_INSTALLMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXEMPTION_JUSTIFICATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXEMPTION bigint unsigned default null,
    add index (OID_EXEMPTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXPECTATION_EVALUATION_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_APPRAISER bigint unsigned default null,
    add index (OID_APPRAISER),
    add column OID_EVALUATED bigint unsigned default null,
    add index (OID_EVALUATED),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXPORT_GROUPING
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_GROUPING bigint unsigned default null,
    add index (OID_GROUPING),
    add column OID_RECEIVER_PERSON bigint unsigned default null,
    add index (OID_RECEIVER_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SENDER_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_SENDER_EXECUTION_COURSE),
    add column OID_SENDER_PERSON bigint unsigned default null,
    add index (OID_SENDER_PERSON);
alter table EXTERNAL_ACTIVITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table EXTERNAL_CURRICULAR_COURSE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table EXTERNAL_ENROLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_EXTERNAL_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_EXTERNAL_CURRICULAR_COURSE),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXTERNAL_ENROLMENT_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE
    add column OID_EXTERNAL_ENROLMENT bigint unsigned default null,
    add index (OID_EXTERNAL_ENROLMENT),
    add column OID_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE);
alter table EXTERNAL_REGISTRATION_DATA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_INSTITUTION bigint unsigned default null,
    add index (OID_INSTITUTION),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table EXTRA_CURRICULAR_CERTIFICATE_REQUEST_ENROLMENT
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_EXTRA_CURRICULAR_CERTIFICATE_REQUEST bigint unsigned default null,
    add index (OID_EXTRA_CURRICULAR_CERTIFICATE_REQUEST);
alter table EXTRA_WORK_AUTHORIZATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_PAYING_UNIT bigint unsigned default null,
    add index (OID_PAYING_UNIT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_WORKING_UNIT bigint unsigned default null,
    add index (OID_WORKING_UNIT);
alter table EXTRA_WORK_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table FILE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ABSTRACT_THESIS bigint unsigned default null,
    add index (OID_ABSTRACT_THESIS),
    add column OID_ADDRESSEE bigint unsigned default null,
    add index (OID_ADDRESSEE),
    add column OID_ATTACHMENT bigint unsigned default null,
    add index (OID_ATTACHMENT),
    add column OID_BACKGROUND_BANNER bigint unsigned default null,
    add index (OID_BACKGROUND_BANNER),
    add column OID_BLUEPRINT bigint unsigned default null,
    add index (OID_BLUEPRINT),
    add column OID_CANDIDACY_DOCUMENT bigint unsigned default null,
    add index (OID_CANDIDACY_DOCUMENT),
    add column OID_CLOSED_MONTH_DOCUMENT bigint unsigned default null,
    add index (OID_CLOSED_MONTH_DOCUMENT),
    add column OID_DISSERTATION_THESIS bigint unsigned default null,
    add index (OID_DISSERTATION_THESIS),
    add column OID_JOB bigint unsigned default null,
    add index (OID_JOB),
    add column OID_LOCAL_CONTENT bigint unsigned default null,
    add index (OID_LOCAL_CONTENT),
    add column OID_MAIN_BANNER bigint unsigned default null,
    add index (OID_MAIN_BANNER),
    add column OID_NEW_PARKING_DOCUMENT bigint unsigned default null,
    add index (OID_NEW_PARKING_DOCUMENT),
    add column OID_OPERATOR bigint unsigned default null,
    add index (OID_OPERATOR),
    add column OID_PROJECT_SUBMISSION bigint unsigned default null,
    add index (OID_PROJECT_SUBMISSION),
    add column OID_PROTOCOL bigint unsigned default null,
    add index (OID_PROTOCOL),
    add column OID_RESULT bigint unsigned default null,
    add index (OID_RESULT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SOURCE bigint unsigned default null,
    add index (OID_SOURCE),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT),
    add column OID_UNIT_SITE bigint unsigned default null,
    add index (OID_UNIT_SITE),
    add column OID_UPLOADER bigint unsigned default null,
    add index (OID_UPLOADER);
alter table FILE_LOCAL_CONTENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FILE bigint unsigned default null,
    add index (OID_FILE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table FILE_LOCAL_CONTENT_METADATA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONTENT bigint unsigned default null,
    add index (OID_CONTENT);
alter table FILE_TAGGING
    add column OID_UNIT_FILE bigint unsigned default null,
    add index (OID_UNIT_FILE),
    add column OID_UNIT_FILE_TAG bigint unsigned default null,
    add index (OID_UNIT_FILE_TAG);
alter table FINAL_DEGREE_WORK_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_PROPOSAL_ATTRIBUTED bigint unsigned default null,
    add index (OID_PROPOSAL_ATTRIBUTED),
    add column OID_PROPOSAL_ATTRIBUTED_BY_TEACHER bigint unsigned default null,
    add index (OID_PROPOSAL_ATTRIBUTED_BY_TEACHER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table FORUM_SUBSCRIPTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FORUM bigint unsigned default null,
    add index (OID_FORUM),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table FUNCTIONALITY_PARAMETER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FUNCTIONALITY bigint unsigned default null,
    add index (OID_FUNCTIONALITY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TYPE bigint unsigned default null,
    add index (OID_TYPE);
alter table FUNCTIONALITY_PRINTERS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table F_A_Q_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT_SECTION bigint unsigned default null,
    add index (OID_PARENT_SECTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table F_A_Q_SECTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT_SECTION bigint unsigned default null,
    add index (OID_PARENT_SECTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GAUGING_TEST_RESULT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table GENERAL_CLASS_PROPERTY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GENERIC_EVENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PUNCTUAL_ROOMS_OCCUPATION_REQUEST bigint unsigned default null,
    add index (OID_PUNCTUAL_ROOMS_OCCUPATION_REQUEST),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GLOSSARY_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_CONTRACT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_COST_CENTER bigint unsigned default null,
    add index (OID_GRANT_COST_CENTER),
    add column OID_GRANT_INSURANCE bigint unsigned default null,
    add index (OID_GRANT_INSURANCE),
    add column OID_GRANT_OWNER bigint unsigned default null,
    add index (OID_GRANT_OWNER),
    add column OID_GRANT_TYPE bigint unsigned default null,
    add index (OID_GRANT_TYPE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_CONTRACT_MOVEMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_CONTRACT bigint unsigned default null,
    add index (OID_GRANT_CONTRACT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_CONTRACT_REGIME
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_CONTRACT bigint unsigned default null,
    add index (OID_GRANT_CONTRACT),
    add column OID_GRANT_COST_CENTER bigint unsigned default null,
    add index (OID_GRANT_COST_CENTER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table GRANT_INSURANCE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_CONTRACT bigint unsigned default null,
    add index (OID_GRANT_CONTRACT),
    add column OID_GRANT_PAYMENT_ENTITY bigint unsigned default null,
    add index (OID_GRANT_PAYMENT_ENTITY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_ORIENTATION_TEACHER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_CONTRACT bigint unsigned default null,
    add index (OID_GRANT_CONTRACT),
    add column OID_ORIENTATION_TEACHER bigint unsigned default null,
    add index (OID_ORIENTATION_TEACHER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_OWNER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_PART
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_PAYMENT_ENTITY bigint unsigned default null,
    add index (OID_GRANT_PAYMENT_ENTITY),
    add column OID_GRANT_SUBSIDY bigint unsigned default null,
    add index (OID_GRANT_SUBSIDY),
    add column OID_RESPONSIBLE_TEACHER bigint unsigned default null,
    add index (OID_RESPONSIBLE_TEACHER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_PAYMENT_ENTITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_COST_CENTER bigint unsigned default null,
    add index (OID_GRANT_COST_CENTER),
    add column OID_RESPONSIBLE_TEACHER bigint unsigned default null,
    add index (OID_RESPONSIBLE_TEACHER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_SUBSIDY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRANT_CONTRACT bigint unsigned default null,
    add index (OID_GRANT_CONTRACT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRANT_TYPE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GRATUITY_SITUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_GRATUITY_VALUES bigint unsigned default null,
    add index (OID_GRATUITY_VALUES),
    add column OID_PAYMENT_CODE bigint unsigned default null,
    add index (OID_PAYMENT_CODE),
    add column OID_PENALTY_EXEMPTION_EMPLOYEE bigint unsigned default null,
    add index (OID_PENALTY_EXEMPTION_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table GRATUITY_VALUES
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GROUPING
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GROUPING_ATTENDS
    add column OID_ATTENDS bigint unsigned default null,
    add index (OID_ATTENDS),
    add column OID_GROUPING bigint unsigned default null,
    add index (OID_GROUPING);
alter table GROUP_PROPOSAL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FINAL_DEGREE_DEGREE_WORK_GROUP bigint unsigned default null,
    add index (OID_FINAL_DEGREE_DEGREE_WORK_GROUP),
    add column OID_FINAL_DEGREE_WORK_PROPOSAL bigint unsigned default null,
    add index (OID_FINAL_DEGREE_WORK_PROPOSAL),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GROUP_STUDENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FINAL_DEGREE_DEGREE_WORK_GROUP bigint unsigned default null,
    add index (OID_FINAL_DEGREE_DEGREE_WORK_GROUP),
    add column OID_FINAL_DEGREE_WORK_PROPOSAL_CONFIRMATION bigint unsigned default null,
    add index (OID_FINAL_DEGREE_WORK_PROPOSAL_CONFIRMATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table GUIDE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONTRIBUTOR bigint unsigned default null,
    add index (OID_CONTRIBUTOR),
    add column OID_CONTRIBUTOR_PARTY bigint unsigned default null,
    add index (OID_CONTRIBUTOR_PARTY),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GUIDE_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GUIDE bigint unsigned default null,
    add index (OID_GUIDE),
    add column OID_PAYMENT_TRANSACTION bigint unsigned default null,
    add index (OID_PAYMENT_TRANSACTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table GUIDE_SITUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GUIDE bigint unsigned default null,
    add index (OID_GUIDE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table HOLIDAY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_LOCALITY bigint unsigned default null,
    add index (OID_LOCALITY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table IDENTIFICATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CARD bigint unsigned default null,
    add index (OID_CARD),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_USER bigint unsigned default null,
    add index (OID_USER);
alter table ID_DOCUMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ID_DOCUMENT_TYPE bigint unsigned default null,
    add index (OID_ID_DOCUMENT_TYPE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ID_DOCUMENT_TYPE_OBJECT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table INDIVIDUAL_CANDIDACY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACCEPTED_DEGREE bigint unsigned default null,
    add index (OID_ACCEPTED_DEGREE),
    add column OID_CANDIDACY_PROCESS bigint unsigned default null,
    add index (OID_CANDIDACY_PROCESS),
    add column OID_COUNTRY_OF_RESIDENCE bigint unsigned default null,
    add index (OID_COUNTRY_OF_RESIDENCE),
    add column OID_DISTRICT_SUBDIVISION_OF_RESIDENCE bigint unsigned default null,
    add index (OID_DISTRICT_SUBDIVISION_OF_RESIDENCE),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_PERSONAL_DETAILS bigint unsigned default null,
    add index (OID_PERSONAL_DETAILS),
    add column OID_PRECEDENT_DEGREE_INFORMATION bigint unsigned default null,
    add index (OID_PRECEDENT_DEGREE_INFORMATION),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCHOOL_TIME_DISTRICT_SUB_DIVISION_OF_RESIDENCE bigint unsigned default null,
    add index (OID_SCHOOL_TIME_DISTRICT_SUB_DIVISION_OF_RESIDENCE),
    add column OID_SELECTED_DEGREE bigint unsigned default null,
    add index (OID_SELECTED_DEGREE);
alter table INDIVIDUAL_CANDIDACY_PERSONAL_DETAILS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY bigint unsigned default null,
    add index (OID_CANDIDACY),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON);
alter table INQUIRIES_COURSE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_EXECUTION_DEGREE_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE_COURSE),
    add column OID_EXECUTION_DEGREE_STUDENT bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE_STUDENT),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_SCHOOL_CLASS bigint unsigned default null,
    add index (OID_STUDENT_SCHOOL_CLASS);
alter table INQUIRIES_REGISTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table INQUIRIES_ROOM
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_INQUIRIES_COURSE bigint unsigned default null,
    add index (OID_INQUIRIES_COURSE),
    add column OID_ROOM bigint unsigned default null,
    add index (OID_ROOM),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table INQUIRIES_STUDENT_EXECUTION_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table INQUIRIES_TEACHER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_INQUIRIES_COURSE bigint unsigned default null,
    add index (OID_INQUIRIES_COURSE),
    add column OID_NON_AFFILIATED_TEACHER bigint unsigned default null,
    add index (OID_NON_AFFILIATED_TEACHER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table INQUIRY_RESPONSE_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table INSTALLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PAYMENT_PLAN bigint unsigned default null,
    add index (OID_PAYMENT_PLAN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table INSURANCE_VALUE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table INTERNSHIP_CANDIDACY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COUNTRY_OF_BIRTH bigint unsigned default null,
    add index (OID_COUNTRY_OF_BIRTH),
    add column OID_FIRST_DESTINATION bigint unsigned default null,
    add index (OID_FIRST_DESTINATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SECOND_DESTINATION bigint unsigned default null,
    add index (OID_SECOND_DESTINATION),
    add column OID_THIRD_DESTINATION bigint unsigned default null,
    add index (OID_THIRD_DESTINATION),
    add column OID_UNIVERSITY bigint unsigned default null,
    add index (OID_UNIVERSITY);
alter table JOB
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BUSINESS_AREA bigint unsigned default null,
    add index (OID_BUSINESS_AREA),
    add column OID_COUNTRY bigint unsigned default null,
    add index (OID_COUNTRY),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table JOURNAL_ISSUE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCIENTIFIC_JOURNAL bigint unsigned default null,
    add index (OID_SCIENTIFIC_JOURNAL);
alter table JUSTIFICATION_MOTIVE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table LESSON
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_LESSON_SPACE_OCCUPATION bigint unsigned default null,
    add index (OID_LESSON_SPACE_OCCUPATION),
    add column OID_PERIOD bigint unsigned default null,
    add index (OID_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT);
alter table LESSON_INSTANCE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COURSE_LOAD bigint unsigned default null,
    add index (OID_COURSE_LOAD),
    add column OID_LESSON bigint unsigned default null,
    add index (OID_LESSON),
    add column OID_LESSON_INSTANCE_SPACE_OCCUPATION bigint unsigned default null,
    add index (OID_LESSON_INSTANCE_SPACE_OCCUPATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SUMMARY bigint unsigned default null,
    add index (OID_SUMMARY);
alter table LESSON_PLANNING
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table LIBRARY_CARD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table LIBRARY_MISSING_CARDS_DOCUMENTS
    add column OID_LIBRARY_CARD bigint unsigned default null,
    add index (OID_LIBRARY_CARD),
    add column OID_LIBRARY_MISSING_CARDS_DOCUMENT bigint unsigned default null,
    add index (OID_LIBRARY_MISSING_CARDS_DOCUMENT);
alter table LIBRARY_MISSING_LETTERS_DOCUMENTS
    add column OID_LIBRARY_CARD bigint unsigned default null,
    add index (OID_LIBRARY_CARD),
    add column OID_LIBRARY_MISSING_LETTERS_DOCUMENT bigint unsigned default null,
    add index (OID_LIBRARY_MISSING_LETTERS_DOCUMENT);
alter table LOCALITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table LOGIN_ALIAS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_LOGIN bigint unsigned default null,
    add index (OID_LOGIN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table LOGIN_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_LOGIN bigint unsigned default null,
    add index (OID_LOGIN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table LOGIN_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_USER bigint unsigned default null,
    add index (OID_USER);
alter table MANAGEMENT_GROUPS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MARK
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ATTEND bigint unsigned default null,
    add index (OID_ATTEND),
    add column OID_EVALUATION bigint unsigned default null,
    add index (OID_EVALUATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MARK_SHEET
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONFIRMATION_EMPLOYEE bigint unsigned default null,
    add index (OID_CONFIRMATION_EMPLOYEE),
    add column OID_CREATION_EMPLOYEE bigint unsigned default null,
    add index (OID_CREATION_EMPLOYEE),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_RESPONSIBLE_TEACHER bigint unsigned default null,
    add index (OID_RESPONSIBLE_TEACHER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MASTER_DEGREE_CANDIDATE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_GUIDER bigint unsigned default null,
    add index (OID_GUIDER),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MASTER_DEGREE_PROOF_VERSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_MASTER_DEGREE_THESIS bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS),
    add column OID_RESPONSIBLE_EMPLOYEE bigint unsigned default null,
    add index (OID_RESPONSIBLE_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MASTER_DEGREE_PROOF_VERSION_EXTERNAL_JURY
    add column OID_EXTERNAL_CONTRACT bigint unsigned default null,
    add index (OID_EXTERNAL_CONTRACT),
    add column OID_MASTER_DEGREE_PROOF_VERSION bigint unsigned default null,
    add index (OID_MASTER_DEGREE_PROOF_VERSION);
alter table MASTER_DEGREE_PROOF_VERSION_JURY
    add column OID_MASTER_DEGREE_PROOF_VERSION bigint unsigned default null,
    add index (OID_MASTER_DEGREE_PROOF_VERSION),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table MASTER_DEGREE_THESIS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table MASTER_DEGREE_THESIS_DATA_VERSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_MASTER_DEGREE_THESIS bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS),
    add column OID_RESPONSIBLE_EMPLOYEE bigint unsigned default null,
    add index (OID_RESPONSIBLE_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MASTER_DEGREE_THESIS_DATA_VERSION_ASSISTENT_GUIDER
    add column OID_MASTER_DEGREE_THESIS_DATA_VERSION bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS_DATA_VERSION),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_ASSISTENT_GUIDER
    add column OID_EXTERNAL_CONTRACT bigint unsigned default null,
    add index (OID_EXTERNAL_CONTRACT),
    add column OID_MASTER_DEGREE_THESIS_DATA_VERSION bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS_DATA_VERSION);
alter table MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_GUIDER
    add column OID_EXTERNAL_CONTRACT bigint unsigned default null,
    add index (OID_EXTERNAL_CONTRACT),
    add column OID_MASTER_DEGREE_THESIS_DATA_VERSION bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS_DATA_VERSION);
alter table MASTER_DEGREE_THESIS_DATA_VERSION_GUIDER
    add column OID_MASTER_DEGREE_THESIS_DATA_VERSION bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS_DATA_VERSION),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table MEAL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MESSAGE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_ROOT_DOMAIN_OBJECT_FROM_PENDING_RELATION bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT_FROM_PENDING_RELATION),
    add column OID_SENDER bigint unsigned default null,
    add index (OID_SENDER);
alter table METADATA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table META_DOMAIN_OBJECT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table MODALITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NEW_ANSWER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ATOMIC_QUESTION bigint unsigned default null,
    add index (OID_ATOMIC_QUESTION),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NEW_CORRECTOR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ATOMIC_QUESTION bigint unsigned default null,
    add index (OID_ATOMIC_QUESTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NEW_GROUP_ELEMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CHILD bigint unsigned default null,
    add index (OID_CHILD),
    add column OID_PARENT bigint unsigned default null,
    add index (OID_PARENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NEW_PARKING_DOCUMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARKING_FILE bigint unsigned default null,
    add index (OID_PARKING_FILE),
    add column OID_PARKING_PARTY bigint unsigned default null,
    add index (OID_PARKING_PARTY),
    add column OID_PARKING_REQUEST bigint unsigned default null,
    add index (OID_PARKING_REQUEST),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_VEHICLE bigint unsigned default null,
    add index (OID_VEHICLE);
alter table NEW_PERMISSION_UNIT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_QUESTION bigint unsigned default null,
    add index (OID_QUESTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NEW_PRESENTATION_MATERIAL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PICTURE_MATERIAL_FILE bigint unsigned default null,
    add index (OID_PICTURE_MATERIAL_FILE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEST_ELEMENT bigint unsigned default null,
    add index (OID_TEST_ELEMENT);
alter table NEW_TEST_ELEMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BAG bigint unsigned default null,
    add index (OID_BAG),
    add column OID_BAG_MODEL bigint unsigned default null,
    add index (OID_BAG_MODEL),
    add column OID_CREATOR bigint unsigned default null,
    add index (OID_CREATOR),
    add column OID_MULTIPLE_CHOICE_QUESTION bigint unsigned default null,
    add index (OID_MULTIPLE_CHOICE_QUESTION),
    add column OID_OWNER bigint unsigned default null,
    add index (OID_OWNER),
    add column OID_PARENT_GROUP bigint unsigned default null,
    add index (OID_PARENT_GROUP),
    add column OID_QUESTION bigint unsigned default null,
    add index (OID_QUESTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SECTION bigint unsigned default null,
    add index (OID_SECTION),
    add column OID_TEST_GROUP bigint unsigned default null,
    add index (OID_TEST_GROUP);
alter table NEW_TEST_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CREATOR bigint unsigned default null,
    add index (OID_CREATOR),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NODE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CHILD bigint unsigned default null,
    add index (OID_CHILD),
    add column OID_PARENT bigint unsigned default null,
    add index (OID_PARENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NON_AFFILIATED_TEACHER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_INSTITUTION_UNIT bigint unsigned default null,
    add index (OID_INSTITUTION_UNIT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table NON_AFFILIATED_TEACHER_EXECUTION_COURSE
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_NON_AFFILIATED_TEACHER bigint unsigned default null,
    add index (OID_NON_AFFILIATED_TEACHER);
alter table NOT_NEED_TO_ENROLL_IN_CURRICULAR_COURSE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN);
alter table OCCUPATION_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_NEXT_PERIOD bigint unsigned default null,
    add index (OID_NEXT_PERIOD),
    add column OID_PREVIOUS_PERIOD bigint unsigned default null,
    add index (OID_PREVIOUS_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table OLD_CURRICULAR_COURSE_CURRICULAR_COURSE_EQUIVALENCE
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_CURRICULAR_COURSE_EQUIVALENCE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE_EQUIVALENCE);
alter table OLD_INQUIRIES_COURSES_RES
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table OLD_INQUIRIES_SUMMARY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table OLD_INQUIRIES_TEACHERS_RES
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table OLD_PUBLICATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table ORIENTATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table OVER23_INDIVIDUAL_CANDIDACY_DEGREE_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_OVER23_INDIVIDUAL_CANDIDACY bigint unsigned default null,
    add index (OID_OVER23_INDIVIDUAL_CANDIDACY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARKING_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARKING_PARTY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DRIVER_LICENSE_DOCUMENT bigint unsigned default null,
    add index (OID_DRIVER_LICENSE_DOCUMENT),
    add column OID_PARKING_GROUP bigint unsigned default null,
    add index (OID_PARKING_GROUP),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARKING_PARTY_HISTORY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARKING_GROUP bigint unsigned default null,
    add index (OID_PARKING_GROUP),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARKING_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DRIVER_LICENSE_DOCUMENT bigint unsigned default null,
    add index (OID_DRIVER_LICENSE_DOCUMENT),
    add column OID_PARKING_PARTY bigint unsigned default null,
    add index (OID_PARKING_PARTY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARKING_REQUEST_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARTIAL_REGIME_INSTALLMENT_EXECUTION_SEMESTER
    add column OID_EXECUTION_SEMESTER bigint unsigned default null,
    add index (OID_EXECUTION_SEMESTER),
    add column OID_PARTIAL_REGIME_INSTALLMENT bigint unsigned default null,
    add index (OID_PARTIAL_REGIME_INSTALLMENT);
alter table PARTICIPATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COOPERATION bigint unsigned default null,
    add index (OID_COOPERATION),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_EVENT_EDITION bigint unsigned default null,
    add index (OID_EVENT_EDITION),
    add column OID_JOURNAL_ISSUE bigint unsigned default null,
    add index (OID_JOURNAL_ISSUE),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCIENTIFIC_JOURNAL bigint unsigned default null,
    add index (OID_SCIENTIFIC_JOURNAL);
alter table PARTY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ADMINISTRATIVE_OFFICE bigint unsigned default null,
    add column OID_ASSOCIATED_PERSON_ACCOUNT bigint unsigned default null,
    add column OID_CAMPUS bigint unsigned default null,
    add column OID_COUNTRY_OF_BIRTH bigint unsigned default null,
    add column OID_DEGREE bigint unsigned default null,
    add column OID_DEPARTMENT bigint unsigned default null,
    add column OID_EMPLOYEE bigint unsigned default null,
    add column OID_GRANT_OWNER bigint unsigned default null,
    add column OID_HOMEPAGE bigint unsigned default null,
    add column OID_INCOMPATIBLE_PERSON bigint unsigned default null,
    add column OID_INCOMPATIBLE_VIGILANT bigint unsigned default null,
    add column OID_LIBRARY_CARD bigint unsigned default null,
    add column OID_NATIONALITY bigint unsigned default null,
    add column OID_PARKING_PARTY bigint unsigned default null,
    add column OID_PARTY_SOCIAL_SECURITY_NUMBER bigint unsigned default null,
    add column OID_PARTY_TYPE bigint unsigned default null,
    add column OID_PERSONAL_PHOTO bigint unsigned default null,
    add column OID_PERSON_NAME bigint unsigned default null,
    add column OID_PHD_PROGRAM bigint unsigned default null,
    add column OID_QUESTION_BANK bigint unsigned default null,
    add column OID_REPLY_TO bigint unsigned default null,
    add column OID_RESEARCHER bigint unsigned default null,
    add column OID_RESIDENCE_PRICE_TABLE bigint unsigned default null,
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add column OID_ROOT_DOMAIN_OBJECT_FOR_EARTH_UNIT bigint unsigned default null,
    add column OID_ROOT_DOMAIN_OBJECT_FOR_EXTERNAL_INSTITUTION_UNIT bigint unsigned default null,
    add column OID_ROOT_DOMAIN_OBJECT_FOR_INSTITUTION_UNIT bigint unsigned default null,
    add column OID_ROOT_DOMAIN_OBJECT_FOR_RESIDENCE_UNIT bigint unsigned default null,
    add column OID_SENDER bigint unsigned default null,
    add column OID_SITE bigint unsigned default null,
    add column OID_STUDENT bigint unsigned default null,
    add column OID_TEACHER bigint unsigned default null,
    add column OID_UNIT_ACRONYM bigint unsigned default null,
    add column OID_UNIT_COST_CENTER_CODE bigint unsigned default null,
    add column OID_UNIT_NAME bigint unsigned default null,
    add column OID_UNIT_SERVICE_AGREEMENT_TEMPLATE bigint unsigned default null,
    add column OID_USER bigint unsigned default null;
alter table PARTY_CONTACT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COUNTRY_OF_RESIDENCE bigint unsigned default null,
    add index (OID_COUNTRY_OF_RESIDENCE),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_RESEARCHER bigint unsigned default null,
    add index (OID_RESEARCHER),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARTY_SOCIAL_SECURITY_NUMBER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PARTY_TYPE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PAYMENT_CODE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ACCOUNTING_EVENT bigint unsigned default null,
    add index (OID_ACCOUNTING_EVENT),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_GRATUITY_SITUATION bigint unsigned default null,
    add index (OID_GRATUITY_SITUATION),
    add column OID_INSTALLMENT bigint unsigned default null,
    add index (OID_INSTALLMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table PAYMENT_CODE_MAPPING
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_INTERVAL bigint unsigned default null,
    add index (OID_EXECUTION_INTERVAL),
    add column OID_NEW_PAYMENT_CODE bigint unsigned default null,
    add index (OID_NEW_PAYMENT_CODE),
    add column OID_OLD_PAYMENT_CODE bigint unsigned default null,
    add index (OID_OLD_PAYMENT_CODE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PAYMENT_PHASE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GRATUITY_VALUES bigint unsigned default null,
    add index (OID_GRATUITY_VALUES),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PAYMENT_PLAN
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SERVICE_AGREEMENT bigint unsigned default null,
    add index (OID_SERVICE_AGREEMENT),
    add column OID_SERVICE_AGREEMENT_TEMPLATE bigint unsigned default null,
    add index (OID_SERVICE_AGREEMENT_TEMPLATE);
alter table PERIODICITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PERSISTENT_GROUP_MEMBERS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table PERSISTENT_GROUP_MEMBERS_PERSON
    add column OID_PERSISTENT_GROUP_MEMBERS bigint unsigned default null,
    add index (OID_PERSISTENT_GROUP_MEMBERS),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON);
alter table PERSONAL_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PERSON_ACCOUNT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PERSON_DEPARTMENT
    add column OID_DEPARTMENT bigint unsigned default null,
    add index (OID_DEPARTMENT),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON);
alter table PERSON_EXTENSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXTENSION bigint unsigned default null,
    add index (OID_EXTENSION),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON);
alter table PERSON_NAME
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PERSON_NAME_PART
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PERSON_NAME_PERSON_NAME_PART
    add column OID_PERSON_NAME bigint unsigned default null,
    add index (OID_PERSON_NAME),
    add column OID_PERSON_NAME_PART bigint unsigned default null,
    add index (OID_PERSON_NAME_PART);
alter table PERSON_ROLE
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROLE bigint unsigned default null,
    add index (OID_ROLE);
alter table PHD_PROGRAM
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_PHD_PROGRAM_UNIT bigint unsigned default null,
    add index (OID_PHD_PROGRAM_UNIT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PHOTOGRAPH
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_NEXT bigint unsigned default null,
    add index (OID_NEXT),
    add column OID_PENDING_HOLDER bigint unsigned default null,
    add index (OID_PENDING_HOLDER),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_PREVIOUS bigint unsigned default null,
    add index (OID_PREVIOUS),
    add column OID_REJECTOR bigint unsigned default null,
    add index (OID_REJECTOR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PHOTOGRAPH_CONTENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PHOTOGRAPH bigint unsigned default null,
    add index (OID_PHOTOGRAPH);
alter table POSTING_RULE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SERVICE_AGREEMENT_TEMPLATE bigint unsigned default null,
    add index (OID_SERVICE_AGREEMENT_TEMPLATE);
alter table PRECEDENCE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PRECEDENT_DEGREE_INFORMATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COUNTRY bigint unsigned default null,
    add index (OID_COUNTRY),
    add column OID_INSTITUTION bigint unsigned default null,
    add index (OID_INSTITUTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT),
    add column OID_STUDENT_CANDIDACY bigint unsigned default null,
    add index (OID_STUDENT_CANDIDACY);
alter table PRICE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PRIZE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_RESEARCH_RESULT bigint unsigned default null,
    add index (OID_RESEARCH_RESULT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PRIZE_WINNERS
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_PRIZE bigint unsigned default null,
    add index (OID_PRIZE);
alter table PROCESS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CANDIDACY bigint unsigned default null,
    add index (OID_CANDIDACY),
    add column OID_CANDIDACY_PERIOD bigint unsigned default null,
    add index (OID_CANDIDACY_PERIOD),
    add column OID_CANDIDACY_PROCESS bigint unsigned default null,
    add index (OID_CANDIDACY_PROCESS),
    add column OID_EVENT bigint unsigned default null,
    add index (OID_EVENT),
    add column OID_INDIVIDUAL_PROGRAM_PROCESS bigint unsigned default null,
    add index (OID_INDIVIDUAL_PROGRAM_PROCESS),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_PHD_PROGRAM bigint unsigned default null,
    add index (OID_PHD_PROGRAM),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROCESS_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PROCESS bigint unsigned default null,
    add index (OID_PROCESS),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROFESSIONAL_SITUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CATEGORY bigint unsigned default null,
    add index (OID_CATEGORY),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROFESSORSHIP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHING_INQUIRY bigint unsigned default null,
    add index (OID_TEACHING_INQUIRY);
alter table PROFILE_REPORT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROGRAM_CERTIFICATE_REQUEST_ENROLMENT
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_PROGRAM_CERTIFICATE_REQUEST bigint unsigned default null,
    add index (OID_PROGRAM_CERTIFICATE_REQUEST);
alter table PROJECT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROJECT_ACCESS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROJECT_EVENT_ASSOCIATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EVENT_EDITION bigint unsigned default null,
    add index (OID_EVENT_EDITION),
    add column OID_PROJECT bigint unsigned default null,
    add index (OID_PROJECT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROJECT_PARTICIPATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_PROJECT bigint unsigned default null,
    add index (OID_PROJECT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROJECT_SUBMISSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ATTENDS bigint unsigned default null,
    add index (OID_ATTENDS),
    add column OID_PROJECT bigint unsigned default null,
    add index (OID_PROJECT),
    add column OID_PROJECT_SUBMISSION_FILE bigint unsigned default null,
    add index (OID_PROJECT_SUBMISSION_FILE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_GROUP bigint unsigned default null,
    add index (OID_STUDENT_GROUP);
alter table PROJECT_SUBMISSION_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ATTENDS bigint unsigned default null,
    add index (OID_ATTENDS),
    add column OID_PROJECT bigint unsigned default null,
    add index (OID_PROJECT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_GROUP bigint unsigned default null,
    add index (OID_STUDENT_GROUP);
alter table PROPOSAL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COORIENTATOR bigint unsigned default null,
    add index (OID_COORIENTATOR),
    add column OID_GROUP_ATTRIBUTED bigint unsigned default null,
    add index (OID_GROUP_ATTRIBUTED),
    add column OID_GROUP_ATTRIBUTED_BY_TEACHER bigint unsigned default null,
    add index (OID_GROUP_ATTRIBUTED_BY_TEACHER),
    add column OID_ORIENTATOR bigint unsigned default null,
    add index (OID_ORIENTATOR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SCHEDULEING bigint unsigned default null,
    add index (OID_SCHEDULEING);
alter table PROPOSAL_BRANCH
    add column OID_BRANCH bigint unsigned default null,
    add index (OID_BRANCH),
    add column OID_PROPOSAL bigint unsigned default null,
    add index (OID_PROPOSAL);
alter table PROTOCOL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROTOCOL_HISTORY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PROTOCOL bigint unsigned default null,
    add index (OID_PROTOCOL),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PROTOCOL_PARTNER
    add column OID_PROTOCOL bigint unsigned default null,
    add index (OID_PROTOCOL),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table PROTOCOL_RESPONSIBLE
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_PROTOCOL bigint unsigned default null,
    add index (OID_PROTOCOL);
alter table PROTOCOL_RESPONSIBLE_FUNCTION
    add column OID_FUNCTION bigint unsigned default null,
    add index (OID_FUNCTION),
    add column OID_PROTOCOL bigint unsigned default null,
    add index (OID_PROTOCOL);
alter table PROTOCOL_RESPONSIBLE_PARTNER
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_PROTOCOL bigint unsigned default null,
    add index (OID_PROTOCOL);
alter table PROTOCOL_UNIT
    add column OID_PROTOCOL bigint unsigned default null,
    add index (OID_PROTOCOL),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table PUBLICATIONS_NUMBER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table PUBLICATION_COLLABORATORS
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_RESEARCH_UNIT bigint unsigned default null,
    add index (OID_RESEARCH_UNIT);
alter table PUNCTUAL_ROOMS_OCCUPATION_COMMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_OWNER bigint unsigned default null,
    add index (OID_OWNER),
    add column OID_REQUEST bigint unsigned default null,
    add index (OID_REQUEST),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PUNCTUAL_ROOMS_OCCUPATION_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_OWNER bigint unsigned default null,
    add index (OID_OWNER),
    add column OID_REQUESTOR bigint unsigned default null,
    add index (OID_REQUESTOR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table PUNCTUAL_ROOMS_OCCUPATION_STATE_INSTANT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_REQUEST bigint unsigned default null,
    add index (OID_REQUEST),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table QUALIFICATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COUNTRY bigint unsigned default null,
    add index (OID_COUNTRY),
    add column OID_EDUCATION_AREA bigint unsigned default null,
    add index (OID_EDUCATION_AREA),
    add column OID_INSTITUTION bigint unsigned default null,
    add index (OID_INSTITUTION),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table QUESTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_METADATA bigint unsigned default null,
    add index (OID_METADATA),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table QUEUE_JOB
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_FILE bigint unsigned default null,
    add index (OID_FILE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_ROOT_DOMAIN_OBJECT_QUEUE_UNDONE bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT_QUEUE_UNDONE);
alter table RECEIPT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CONTRIBUTOR_PARTY bigint unsigned default null,
    add index (OID_CONTRIBUTOR_PARTY),
    add column OID_CREATOR_UNIT bigint unsigned default null,
    add index (OID_CREATOR_UNIT),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_OWNER_UNIT bigint unsigned default null,
    add index (OID_OWNER_UNIT),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RECEIPT_ENTRY
    add column OID_ENTRY bigint unsigned default null,
    add index (OID_ENTRY),
    add column OID_RECEIPT bigint unsigned default null,
    add index (OID_RECEIPT);
alter table RECEIPT_PRINT_VERSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_RECEIPT bigint unsigned default null,
    add index (OID_RECEIPT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RECIPIENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REGISTRATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSOCIATED_GAUGING_TEST_RESULT bigint unsigned default null,
    add index (OID_ASSOCIATED_GAUGING_TEST_RESULT),
    add column OID_CONCLUSION_PROCESS bigint unsigned default null,
    add index (OID_CONCLUSION_PROCESS),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_DFA_REGISTRATION_EVENT bigint unsigned default null,
    add index (OID_DFA_REGISTRATION_EVENT),
    add column OID_EXTERNAL_REGISTRATION_DATA bigint unsigned default null,
    add index (OID_EXTERNAL_REGISTRATION_DATA),
    add column OID_INDIVIDUAL_CANDIDACY bigint unsigned default null,
    add index (OID_INDIVIDUAL_CANDIDACY),
    add column OID_PHD_INDIVIDUAL_PROGRAM_PROCESS bigint unsigned default null,
    add index (OID_PHD_INDIVIDUAL_PROGRAM_PROCESS),
    add column OID_PRECEDENT_DEGREE_INFORMATION bigint unsigned default null,
    add index (OID_PRECEDENT_DEGREE_INFORMATION),
    add column OID_REGISTRATION_NUMBER bigint unsigned default null,
    add index (OID_REGISTRATION_NUMBER),
    add column OID_REGISTRATION_YEAR bigint unsigned default null,
    add index (OID_REGISTRATION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SENIOR bigint unsigned default null,
    add index (OID_SENIOR),
    add column OID_SOURCE_REGISTRATION bigint unsigned default null,
    add index (OID_SOURCE_REGISTRATION),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT),
    add column OID_STUDENT_CANDIDACY bigint unsigned default null,
    add index (OID_STUDENT_CANDIDACY);
alter table REGISTRATION_DATA_BY_EXECUTION_YEAR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REGISTRATION_NUMBER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REGISTRATION_REGIME
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REGISTRATION_STATE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_RESPONSIBLE_PERSON bigint unsigned default null,
    add index (OID_RESPONSIBLE_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REIMBURSEMENT_GUIDE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GUIDE bigint unsigned default null,
    add index (OID_GUIDE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REIMBURSEMENT_GUIDE_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GUIDE_ENTRY bigint unsigned default null,
    add index (OID_GUIDE_ENTRY),
    add column OID_REIMBURSEMENT_GUIDE bigint unsigned default null,
    add index (OID_REIMBURSEMENT_GUIDE),
    add column OID_REIMBURSEMENT_TRANSACTION bigint unsigned default null,
    add index (OID_REIMBURSEMENT_TRANSACTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REIMBURSEMENT_GUIDE_SITUATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_REIMBURSEMENT_GUIDE bigint unsigned default null,
    add index (OID_REIMBURSEMENT_GUIDE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REPLY_TO
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SENDER bigint unsigned default null,
    add index (OID_SENDER);
alter table REQUEST_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ERROR_LOG bigint unsigned default null,
    add index (OID_ERROR_LOG),
    add column OID_MAPPING bigint unsigned default null,
    add index (OID_MAPPING),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table REQUEST_MAPPING
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESEARCHER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESEARCH_EVENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESEARCH_INTEREST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESEARCH_RESULT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ARTICLE_ASSOCIATION bigint unsigned default null,
    add index (OID_ARTICLE_ASSOCIATION),
    add column OID_COUNTRY bigint unsigned default null,
    add index (OID_COUNTRY),
    add column OID_CREATOR bigint unsigned default null,
    add index (OID_CREATOR),
    add column OID_EVENT_CONFERENCE_ARTICLES_ASSOCIATION bigint unsigned default null,
    add index (OID_EVENT_CONFERENCE_ARTICLES_ASSOCIATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_THESIS bigint unsigned default null,
    add index (OID_THESIS);
alter table RESIDENCE_CANDIDACIES
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_DATA_BY_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_STUDENT_DATA_BY_EXECUTION_YEAR);
alter table RESIDENCE_MONTH
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_YEAR bigint unsigned default null,
    add index (OID_YEAR);
alter table RESIDENCE_PRICE_TABLE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESIDENCE_YEAR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table RESOURCE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SUROUNDING_SPACE bigint unsigned default null,
    add index (OID_SUROUNDING_SPACE);
alter table RESOURCE_ALLOCATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GENERIC_EVENT bigint unsigned default null,
    add index (OID_GENERIC_EVENT),
    add column OID_LESSON bigint unsigned default null,
    add index (OID_LESSON),
    add column OID_MATERIAL bigint unsigned default null,
    add index (OID_MATERIAL),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_REQUESTOR bigint unsigned default null,
    add index (OID_REQUESTOR),
    add column OID_RESOURCE bigint unsigned default null,
    add index (OID_RESOURCE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table RESOURCE_RESPONSIBILITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARTY bigint unsigned default null,
    add index (OID_PARTY),
    add column OID_RESOURCE bigint unsigned default null,
    add index (OID_RESOURCE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESTRICTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PRECEDENCE bigint unsigned default null,
    add index (OID_PRECEDENCE),
    add column OID_PRECEDENT_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_PRECEDENT_CURRICULAR_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESULT_PARTICIPATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_RESULT bigint unsigned default null,
    add index (OID_RESULT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table RESULT_TEACHER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_RESULT bigint unsigned default null,
    add index (OID_RESULT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table RESULT_UNIT_ASSOCIATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_RESULT bigint unsigned default null,
    add index (OID_RESULT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table ROLE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ROOM_CLASSIFICATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT_ROOM_CLASSIFICATION bigint unsigned default null,
    add index (OID_PARENT_ROOM_CLASSIFICATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table ROOT_DOMAIN_OBJECT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEFAULT_ACADEMIC_CALENDAR bigint unsigned default null,
    add index (OID_DEFAULT_ACADEMIC_CALENDAR),
    add column OID_DEPLOY_NOTIFIER bigint unsigned default null,
    add index (OID_DEPLOY_NOTIFIER),
    add column OID_EARTH_UNIT bigint unsigned default null,
    add index (OID_EARTH_UNIT),
    add column OID_EXTERNAL_INSTITUTION_UNIT bigint unsigned default null,
    add index (OID_EXTERNAL_INSTITUTION_UNIT),
    add column OID_INSTITUTION_UNIT bigint unsigned default null,
    add index (OID_INSTITUTION_UNIT),
    add column OID_RESIDENCE_MANAGEMENT_UNIT bigint unsigned default null,
    add index (OID_RESIDENCE_MANAGEMENT_UNIT),
    add column OID_ROOT_MODULE bigint unsigned default null,
    add index (OID_ROOT_MODULE),
    add column OID_ROOT_PORTAL bigint unsigned default null,
    add index (OID_ROOT_PORTAL),
    add column OID_USERNAME_COUNTER bigint unsigned default null,
    add index (OID_USERNAME_COUNTER);
alter table SCHEDULE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ASSIDUOUSNESS bigint unsigned default null,
    add index (OID_ASSIDUOUSNESS),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SCHEDULEING
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SCHEDULE_WORK_SCHEDULE
    add column OID_SCHEDULE bigint unsigned default null,
    add index (OID_SCHEDULE),
    add column OID_WORK_SCHEDULE bigint unsigned default null,
    add index (OID_WORK_SCHEDULE);
alter table SCHOOL_CLASS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SCIENTIFIC_AREA
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SCIENTIFIC_COMMISSION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SCIENTIFIC_JOURNAL
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SECRETARY_ENROLMENT_STUDENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table SEMINARY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SEMINARY_CANDIDACY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_MODALITY bigint unsigned default null,
    add index (OID_MODALITY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SEMINARY bigint unsigned default null,
    add index (OID_SEMINARY),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT),
    add column OID_THEME bigint unsigned default null,
    add index (OID_THEME);
alter table SENDER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COURSE bigint unsigned default null,
    add index (OID_COURSE),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_PERSON_FUNCTION bigint unsigned default null,
    add index (OID_PERSON_FUNCTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table SENIOR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table SERVICE_AGREEMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SERVICE_AGREEMENT_TEMPLATE bigint unsigned default null,
    add index (OID_SERVICE_AGREEMENT_TEMPLATE);
alter table SERVICE_AGREEMENT_TEMPLATE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ADMINISTRATIVE_OFFICE bigint unsigned default null,
    add index (OID_ADMINISTRATIVE_OFFICE),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table SERVICE_PROVIDER_REGIME
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table SHIFT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SHIFT_DISTRIBUTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SHIFT_DISTRIBUTION_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT),
    add column OID_SHIFT_DISTRIBUTION bigint unsigned default null,
    add index (OID_SHIFT_DISTRIBUTION);
alter table SHIFT_ENROLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT);
alter table SHIFT_PROFESSORSHIP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PROFESSORSHIP bigint unsigned default null,
    add index (OID_PROFESSORSHIP),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT);
alter table SHIFT_SCHOOL_CLASS
    add column OID_SCHOOL_CLASS bigint unsigned default null,
    add index (OID_SCHOOL_CLASS),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT);
alter table SHIFT_STUDENT
    add column OID_REGISTRATION bigint unsigned default null,
    add index (OID_REGISTRATION),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT);
alter table SIBS_PAYMENT_FILE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SIBS_PAYMENT_FILE_ENTRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SIBS_PAYMENT_FILE bigint unsigned default null,
    add index (OID_SIBS_PAYMENT_FILE);
alter table SIBS_PAYMENT_FILE_PROCESS_REPORT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SPACE_INFORMATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_LOCALITY bigint unsigned default null,
    add index (OID_LOCALITY),
    add column OID_ROOM_CLASSIFICATION bigint unsigned default null,
    add index (OID_ROOM_CLASSIFICATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SPACE bigint unsigned default null,
    add index (OID_SPACE);
alter table SPECIAL_SEASON_CODE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table STANDALONE_ENROLMENT_CERTIFICATE_REQUEST_ENROLMENT
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_STANDALONE_ENROLMENT_CERTIFICATE_REQUEST bigint unsigned default null,
    add index (OID_STANDALONE_ENROLMENT_CERTIFICATE_REQUEST);
alter table STANDALONE_INDIVIDUAL_CANDIDACY_CURRICULAR_COURSES
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_STANDALONE_INDIVIDUAL_CANDIDACY bigint unsigned default null,
    add index (OID_STANDALONE_INDIVIDUAL_CANDIDACY);
alter table STUDENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ALUMNI bigint unsigned default null,
    add index (OID_ALUMNI),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table STUDENT_COURSE_REPORT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table STUDENT_CURRICULAR_PLAN
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BRANCH bigint unsigned default null,
    add index (OID_BRANCH),
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_EMPLOYEE bigint unsigned default null,
    add index (OID_EMPLOYEE),
    add column OID_EQUIVALENCE_PLAN bigint unsigned default null,
    add index (OID_EQUIVALENCE_PLAN),
    add column OID_MASTER_DEGREE_THESIS bigint unsigned default null,
    add index (OID_MASTER_DEGREE_THESIS),
    add column OID_ROOT bigint unsigned default null,
    add index (OID_ROOT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table STUDENT_CURRICULAR_PLAN_EQUIVALENCE_PLAN_ENTRY_TO_REMOVE
    add column OID_EQUIVALENCE_PLAN_ENTRY bigint unsigned default null,
    add index (OID_EQUIVALENCE_PLAN_ENTRY),
    add column OID_STUDENT_CURRICULAR_PLAN_EQUIVALENCE_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN_EQUIVALENCE_PLAN);
alter table STUDENT_DATA_BY_EXECUTION_YEAR
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_RESIDENCE_CANDIDACY bigint unsigned default null,
    add index (OID_RESIDENCE_CANDIDACY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table STUDENT_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_GROUPING bigint unsigned default null,
    add index (OID_GROUPING),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT);
alter table STUDENT_GROUP_ATTEND
    add column OID_ATTENDS bigint unsigned default null,
    add index (OID_ATTENDS),
    add column OID_STUDENT_GROUP bigint unsigned default null,
    add index (OID_STUDENT_GROUP);
alter table STUDENT_INQUIRIES_COURSE_RESULT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table STUDENT_INQUIRIES_TEACHING_RESULT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_DEGREE bigint unsigned default null,
    add index (OID_EXECUTION_DEGREE),
    add column OID_PROFESSORSHIP bigint unsigned default null,
    add index (OID_PROFESSORSHIP),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table STUDENT_STATUTE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BEGIN_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_BEGIN_EXECUTION_PERIOD),
    add column OID_END_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_END_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table STUDENT_TEST_LOG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DISTRIBUTED_TEST bigint unsigned default null,
    add index (OID_DISTRIBUTED_TEST),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table STUDENT_TEST_QUESTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DISTRIBUTED_TEST bigint unsigned default null,
    add index (OID_DISTRIBUTED_TEST),
    add column OID_QUESTION bigint unsigned default null,
    add index (OID_QUESTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table SUMMARY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_LESSON_INSTANCE bigint unsigned default null,
    add index (OID_LESSON_INSTANCE),
    add column OID_PROFESSORSHIP bigint unsigned default null,
    add index (OID_PROFESSORSHIP),
    add column OID_ROOM bigint unsigned default null,
    add index (OID_ROOM),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table SUPPORT_LESSON
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PROFESSORSHIP bigint unsigned default null,
    add index (OID_PROFESSORSHIP),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table SUPPORT_REQUEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ERROR_LOG bigint unsigned default null,
    add index (OID_ERROR_LOG),
    add column OID_REQUESTER bigint unsigned default null,
    add index (OID_REQUESTER),
    add column OID_REQUEST_CONTEXT bigint unsigned default null,
    add index (OID_REQUEST_CONTEXT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table TEACHER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SERVICE_PROVIDER_REGIME bigint unsigned default null,
    add index (OID_SERVICE_PROVIDER_REGIME),
    add column OID_WEEKLY_OCUPATION bigint unsigned default null,
    add index (OID_WEEKLY_OCUPATION);
alter table TEACHER_DEGREE_FINAL_PROJECT_STUDENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table TEACHER_INSTITUTION_WORK_TIME
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table TEACHER_PERSONAL_EXPECTATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table TEACHER_PERSONAL_EXPECTATION_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEPARTMENT bigint unsigned default null,
    add index (OID_DEPARTMENT),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table TEACHER_SERVICE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table TEACHER_SERVICE_DISTRIBUTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARENT bigint unsigned default null,
    add index (OID_PARENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_T_S_D_PROCESS_PHASE bigint unsigned default null,
    add index (OID_T_S_D_PROCESS_PHASE);
alter table TEACHER_SERVICE_ITEM
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ADVISE bigint unsigned default null,
    add index (OID_ADVISE),
    add column OID_PROFESSORSHIP bigint unsigned default null,
    add index (OID_PROFESSORSHIP),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SHIFT bigint unsigned default null,
    add index (OID_SHIFT),
    add column OID_TEACHER_SERVICE bigint unsigned default null,
    add index (OID_TEACHER_SERVICE);
alter table TEACHING_INQUIRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PROFESSORSHIP bigint unsigned default null,
    add index (OID_PROFESSORSHIP),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table TEST
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEST_SCOPE bigint unsigned default null,
    add index (OID_TEST_SCOPE);
alter table TEST_CHECKSUM
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table TEST_PERSON
    add column OID_NEW_TEST bigint unsigned default null,
    add index (OID_NEW_TEST),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON);
alter table TEST_QUESTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_QUESTION bigint unsigned default null,
    add index (OID_QUESTION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEST bigint unsigned default null,
    add index (OID_TEST);
alter table TEST_SCOPE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table THEME
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table THESIS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DEGREE bigint unsigned default null,
    add index (OID_DEGREE),
    add column OID_DISSERTATION bigint unsigned default null,
    add index (OID_DISSERTATION),
    add column OID_ENROLMENT bigint unsigned default null,
    add index (OID_ENROLMENT),
    add column OID_EXTENDED_ABSTRACT bigint unsigned default null,
    add index (OID_EXTENDED_ABSTRACT),
    add column OID_LAST_LIBRARY_OPERATION bigint unsigned default null,
    add index (OID_LAST_LIBRARY_OPERATION),
    add column OID_PUBLICATION bigint unsigned default null,
    add index (OID_PUBLICATION),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SITE bigint unsigned default null,
    add index (OID_SITE);
alter table THESIS_EVALUATION_PARTICIPANT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_THESIS bigint unsigned default null,
    add index (OID_THESIS);
alter table THESIS_LIBRARY_OPERATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_NEXT bigint unsigned default null,
    add index (OID_NEXT),
    add column OID_PERFORMER bigint unsigned default null,
    add index (OID_PERFORMER),
    add column OID_PREVIOUS bigint unsigned default null,
    add index (OID_PREVIOUS),
    add column OID_THESIS bigint unsigned default null,
    add index (OID_THESIS);
alter table TRANSACTION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_GRATUITY_SITUATION bigint unsigned default null,
    add index (OID_GRATUITY_SITUATION),
    add column OID_GUIDE_ENTRY bigint unsigned default null,
    add index (OID_GUIDE_ENTRY),
    add column OID_PERSON_ACCOUNT bigint unsigned default null,
    add index (OID_PERSON_ACCOUNT),
    add column OID_REIMBURSEMENT_GUIDE_ENTRY bigint unsigned default null,
    add index (OID_REIMBURSEMENT_GUIDE_ENTRY),
    add column OID_RESPONSIBLE_PERSON bigint unsigned default null,
    add index (OID_RESPONSIBLE_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);
alter table TUTORSHIP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_STUDENT_CURRICULAR_PLAN),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table T_S_D_COURSE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_COMPETENCE_COURSE bigint unsigned default null,
    add index (OID_COMPETENCE_COURSE),
    add column OID_CURRICULAR_COURSE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE),
    add column OID_EXECUTION_PERIOD bigint unsigned default null,
    add index (OID_EXECUTION_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_T_S_D_CURRICULAR_COURSE_GROUP bigint unsigned default null,
    add index (OID_T_S_D_CURRICULAR_COURSE_GROUP);
alter table T_S_D_COURSE_TEACHER_SERVICE_DISTRIBUTION
    add column OID_TEACHER_SERVICE_DISTRIBUTION bigint unsigned default null,
    add index (OID_TEACHER_SERVICE_DISTRIBUTION),
    add column OID_T_S_D_COURSE bigint unsigned default null,
    add index (OID_T_S_D_COURSE);
alter table T_S_D_CURRICULAR_LOAD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_T_S_D_COURSE bigint unsigned default null,
    add index (OID_T_S_D_COURSE);
alter table T_S_D_PROCESS
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CREATOR bigint unsigned default null,
    add index (OID_CREATOR),
    add column OID_DEPARTMENT bigint unsigned default null,
    add index (OID_DEPARTMENT),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table T_S_D_PROCESS_EXECUTION_PERIOD
    add column OID_EXECUTION_SEMESTER bigint unsigned default null,
    add index (OID_EXECUTION_SEMESTER),
    add column OID_T_S_D_PROCESS bigint unsigned default null,
    add index (OID_T_S_D_PROCESS);
alter table T_S_D_PROCESS_PHASE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_NEXT_T_S_D_PROCESS_PHASE bigint unsigned default null,
    add index (OID_NEXT_T_S_D_PROCESS_PHASE),
    add column OID_PREVIOUS_T_S_D_PROCESS_PHASE bigint unsigned default null,
    add index (OID_PREVIOUS_T_S_D_PROCESS_PHASE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_T_S_D_PROCESS bigint unsigned default null,
    add index (OID_T_S_D_PROCESS);
alter table T_S_D_PROFESSORSHIP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_T_S_D_COURSE bigint unsigned default null,
    add index (OID_T_S_D_COURSE),
    add column OID_T_S_D_TEACHER bigint unsigned default null,
    add index (OID_T_S_D_TEACHER);
alter table T_S_D_TEACHER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_CATEGORY bigint unsigned default null,
    add index (OID_CATEGORY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table T_S_D_TEACHER_TEACHER_SERVICE_DISTRIBUTION
    add column OID_TEACHER_SERVICE_DISTRIBUTION bigint unsigned default null,
    add index (OID_TEACHER_SERVICE_DISTRIBUTION),
    add column OID_T_S_D_TEACHER bigint unsigned default null,
    add index (OID_T_S_D_TEACHER);
alter table T_S_D_VIRTUAL_COURSE_GROUP_DEGREE_CURRICULAR_PLAN
    add column OID_DEGREE_CURRICULAR_PLAN bigint unsigned default null,
    add index (OID_DEGREE_CURRICULAR_PLAN),
    add column OID_T_S_D_VIRTUAL_COURSE_GROUP bigint unsigned default null,
    add index (OID_T_S_D_VIRTUAL_COURSE_GROUP);
alter table UNAVAILABLE_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table UNIT_ACRONYM
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table UNIT_COST_CENTER_CODE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table UNIT_EXTRA_WORK_AMOUNT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table UNIT_EXTRA_WORK_MOVEMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT_EXTRA_WORK_AMOUNT bigint unsigned default null,
    add index (OID_UNIT_EXTRA_WORK_AMOUNT);
alter table UNIT_FILE_TAG
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table UNIT_FUNCTIONALITY_PRINTERS
    add column OID_FUNCTIONALITY_PRINTERS bigint unsigned default null,
    add index (OID_FUNCTIONALITY_PRINTERS),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table UNIT_NAME
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table UNIT_NAME_PART
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table UNIT_NAME_UNIT_NAME_PART
    add column OID_UNIT_NAME bigint unsigned default null,
    add index (OID_UNIT_NAME),
    add column OID_UNIT_NAME_PART bigint unsigned default null,
    add index (OID_UNIT_NAME_PART);
alter table UNIT_SITE_BANNER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_BACKGROUND_IMAGE bigint unsigned default null,
    add index (OID_BACKGROUND_IMAGE),
    add column OID_MAIN_IMAGE bigint unsigned default null,
    add index (OID_MAIN_IMAGE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SITE bigint unsigned default null,
    add index (OID_SITE);
alter table UNIT_SITE_LINK
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FOOTER_UNIT_SITE bigint unsigned default null,
    add index (OID_FOOTER_UNIT_SITE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TOP_UNIT_SITE bigint unsigned default null,
    add index (OID_TOP_UNIT_SITE);
alter table UNIT_SITE_MANAGERS
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_UNIT_SITE bigint unsigned default null,
    add index (OID_UNIT_SITE);
alter table UNIVERSITY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table USER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_LOGIN_REQUEST bigint unsigned default null,
    add index (OID_LOGIN_REQUEST),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table USERNAME_COUNTER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table UTIL_EMAIL_MESSAGE_UTIL_EMAIL_RECIPIENT
    add column OID_MESSAGE bigint unsigned default null,
    add index (OID_MESSAGE),
    add column OID_RECIPIENT bigint unsigned default null,
    add index (OID_RECIPIENT);
alter table UTIL_EMAIL_REPLY_TO_UTIL_EMAIL_MESSAGE
    add column OID_MESSAGE bigint unsigned default null,
    add index (OID_MESSAGE),
    add column OID_REPLY_TO bigint unsigned default null,
    add index (OID_REPLY_TO);
alter table UTIL_EMAIL_SENDER_UTIL_EMAIL_RECIPIENT
    add column OID_RECIPIENT bigint unsigned default null,
    add index (OID_RECIPIENT),
    add column OID_SENDER bigint unsigned default null,
    add index (OID_SENDER);
alter table VEHICLE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PARKING_PARTY bigint unsigned default null,
    add index (OID_PARKING_PARTY),
    add column OID_PARKING_REQUEST bigint unsigned default null,
    add index (OID_PARKING_REQUEST),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table VIGILANCY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_VIGILANT_WRAPPER bigint unsigned default null,
    add index (OID_VIGILANT_WRAPPER),
    add column OID_WRITTEN_EVALUATION bigint unsigned default null,
    add index (OID_WRITTEN_EVALUATION);
alter table VIGILANT_GROUP
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_UNIT bigint unsigned default null,
    add index (OID_UNIT);
alter table VIGILANT_WRAPPER
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERSON bigint unsigned default null,
    add index (OID_PERSON),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_VIGILANT_GROUP bigint unsigned default null,
    add index (OID_VIGILANT_GROUP);
alter table WEEKLY_OCUPATION
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_TEACHER bigint unsigned default null,
    add index (OID_TEACHER);
alter table WEEKLY_WORK_LOAD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ATTENDS bigint unsigned default null,
    add index (OID_ATTENDS),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table WORK_PERIOD
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table WORK_SCHEDULE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_PERIODICITY bigint unsigned default null,
    add index (OID_PERIODICITY),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_WORK_SCHEDULE_TYPE bigint unsigned default null,
    add index (OID_WORK_SCHEDULE_TYPE);
alter table WORK_SCHEDULE_TYPE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_FIXED_WORK_PERIOD bigint unsigned default null,
    add index (OID_FIXED_WORK_PERIOD),
    add column OID_MEAL bigint unsigned default null,
    add index (OID_MEAL),
    add column OID_MODIFIED_BY bigint unsigned default null,
    add index (OID_MODIFIED_BY),
    add column OID_NORMAL_WORK_PERIOD bigint unsigned default null,
    add index (OID_NORMAL_WORK_PERIOD),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table WRITTEN_EVALUATION_CONTEXT
    add column OID_CONTEXT bigint unsigned default null,
    add index (OID_CONTEXT),
    add column OID_WRITTEN_EVALUATION bigint unsigned default null,
    add index (OID_WRITTEN_EVALUATION);
alter table WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE
    add column OID_CURRICULAR_COURSE_SCOPE bigint unsigned default null,
    add index (OID_CURRICULAR_COURSE_SCOPE),
    add column OID_WRITTEN_EVALUATION bigint unsigned default null,
    add index (OID_WRITTEN_EVALUATION);
alter table WRITTEN_EVALUATION_ENROLMENT
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_ROOM bigint unsigned default null,
    add index (OID_ROOM),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT),
    add column OID_WRITTEN_EVALUATION bigint unsigned default null,
    add index (OID_WRITTEN_EVALUATION);
alter table WRITTEN_EVALUATION_WRITTEN_EVALUATION_SPACE_OCCUPATION
    add column OID_WRITTEN_EVALUATION bigint unsigned default null,
    add index (OID_WRITTEN_EVALUATION),
    add column OID_WRITTEN_EVALUATION_SPACE_OCCUPATION bigint unsigned default null,
    add index (OID_WRITTEN_EVALUATION_SPACE_OCCUPATION);
alter table YEAR_DELEGATE_COURSE_INQUIRY
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_DELEGATE bigint unsigned default null,
    add index (OID_DELEGATE),
    add column OID_EXECUTION_COURSE bigint unsigned default null,
    add index (OID_EXECUTION_COURSE),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT);
alter table YEAR_STUDENT_SPECIAL_SEASON_CODE
    add column OID bigint unsigned default null,
    add index (OID),
    add column OID_EXECUTION_YEAR bigint unsigned default null,
    add index (OID_EXECUTION_YEAR),
    add column OID_ROOT_DOMAIN_OBJECT bigint unsigned default null,
    add index (OID_ROOT_DOMAIN_OBJECT),
    add column OID_SPECIAL_SEASON_CODE bigint unsigned default null,
    add index (OID_SPECIAL_SEASON_CODE),
    add column OID_STUDENT bigint unsigned default null,
    add index (OID_STUDENT);

commit;
