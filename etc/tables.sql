-- MySQL dump 9.09
--
-- Host: localhost    Database: ciapl
---------------------------------------------------------
-- Server version	4.0.15-Max-log

--
-- Table structure for table `ADVISORY`
--

CREATE TABLE ADVISORY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  SENDER varchar(100) NOT NULL default '',
  CREATED timestamp(8) NOT NULL,
  SUBJECT varchar(200) NOT NULL default '',
  MESSAGE text NOT NULL,
  EXPIRES date NOT NULL default '0000-00-00',
  ONLY_SHOW_ONCE tinyint(1) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `ANNOUNCEMENT`
--

CREATE TABLE ANNOUNCEMENT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  TITLE varchar(100) default NULL,
  CREATION_DATE timestamp(14) NOT NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  INFORMATION text,
  KEY_SITE int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (CREATION_DATE,KEY_SITE,TITLE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB; InnoDB free: 372736 kB';

--
-- Table structure for table `ATTEND`
--

CREATE TABLE ATTEND (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT int(11) NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) NOT NULL default '0',
  KEY_ENROLMENT int(11) default NULL,
  KEY_REGISTRATION_YEAR INT(11) UNSIGNED,
  PAYED_TUITION BIT NOT NULL DEFAULT '0',
  ENROLLMENT_FORBIDDEN BIT NOT NULL DEFAULT '0',
  ENTRY_PHASE INT(11) NOT NULL DEFAULT '1',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT,KEY_EXECUTION_COURSE),
  KEY ATTEND_KEY_EXECUTION_COURSE (KEY_EXECUTION_COURSE)
) TYPE=InnoDB;

--
-- Table structure for table `Abreviaturas`
--

CREATE TABLE Abreviaturas (
  abrv varchar(50) default NULL,
  expansao varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `AtribuicaoDisciplinas`
--

CREATE TABLE AtribuicaoDisciplinas (
  NumEpoca smallint(5) default NULL,
  Turma varchar(8) default NULL,
  Disciplina varchar(8) default NULL,
  TipoAula char(3) default NULL,
  NumDivisao smallint(5) default NULL,
  HorasMax double default NULL,
  HorasDadas smallint(5) default NULL,
  HorasAula double default NULL,
  AlunosDivisao smallint(5) default NULL,
  SalaPreferencial varchar(8) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  KEY NumEpoca (NumEpoca)
) TYPE=InnoDB;

--
-- Table structure for table `Aulas`
--

CREATE TABLE Aulas (
  id int(11) NOT NULL auto_increment,
  NumEpoca smallint(5) default NULL,
  Turma varchar(8) default NULL,
  Dia smallint(5) default NULL,
  Hora smallint(5) default NULL,
  Disciplina varchar(8) default NULL,
  TipoAula char(3) default NULL,
  NumTurno smallint(5) default NULL,
  Sala varchar(8) default NULL,
  Fixa tinyint(3) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (id)
) TYPE=InnoDB;

--
-- Table structure for table `BIBLIOGRAPHIC_REFERENCE`
--

CREATE TABLE BIBLIOGRAPHIC_REFERENCE (
  ID_INTERNAL int(50) NOT NULL auto_increment,
  TITLE text NOT NULL,
  AUTHORS text NOT NULL,
  REFERENCE text NOT NULL,
  YEAR text NOT NULL,
  OPTIONAL int(11) NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `BRANCH`
--

CREATE TABLE BRANCH (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  BRANCH_CODE varchar(50) NOT NULL default '',
  BRANCH_NAME varchar(255) NOT NULL default '',
  KEY_DEGREE_CURRICULAR_PLAN int(11) NOT NULL default '0',
  BRANCH_ACRONYM varchar(50) default NULL,
  SPECIALIZATION_CREDITS int(11) default NULL,
  SECONDARY_CREDITS int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  BRANCH_TYPE varchar(6) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (BRANCH_CODE,KEY_DEGREE_CURRICULAR_PLAN)
) TYPE=InnoDB;

--
-- Table structure for table `CAMPUS`
--

CREATE TABLE CAMPUS (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME)
) TYPE=InnoDB;

--
-- Table structure for table `CANDIDATE_ENROLMENT`
--

CREATE TABLE CANDIDATE_ENROLMENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_MASTER_DEGREE_CANDIDATE int(11) NOT NULL default '0',
  KEY_CURRICULAR_COURSE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CURRICULAR_COURSE,KEY_MASTER_DEGREE_CANDIDATE)
) TYPE=InnoDB;

--
-- Table structure for table `CANDIDATE_SITUATION`
--

CREATE TABLE CANDIDATE_SITUATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  DATE date NOT NULL default '0000-00-00',
  REMARKS text,
  VALIDATION tinyint(4) NOT NULL default '1',
  CANDIDATE_KEY int(11) NOT NULL default '0',
  SITUATION int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `CAREER`
--

CREATE TABLE CAREER (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL default '',
  BEGIN_YEAR int(10) default NULL,
  END_YEAR int(10) default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  ENTITY varchar(50) default NULL,
  FUNCTION varchar(50) default NULL,
  COURSE_OR_POSITION varchar(100) default NULL,
  KEY_CATEGORY int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `CATEGORY`
--

CREATE TABLE CATEGORY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CODE varchar(20) NOT NULL default '',
  LONG_NAME varchar(250) NOT NULL default '',
  SHORT_NAME varchar(50) NOT NULL default '',
  CAN_BE_EXECUTION_COURSE_RESPONSIBLE tinyint(1) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (CODE)
) TYPE=InnoDB;

--
-- Table structure for table `CLASS`
--

CREATE TABLE CLASS (
  ID_INTERNAL int(11) NOT NULL default '0',
  NAME varchar(50) NOT NULL default '',
  SEMESTER int(11) default NULL,
  CURRICULAR_YEAR int(11) NOT NULL default '0',
  KEY_EXECUTION_DEGREE int(11) NOT NULL default '0',
  KEY_DEGREE int(11) default NULL,
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,KEY_EXECUTION_PERIOD,KEY_EXECUTION_DEGREE)
) TYPE=InnoDB;

--
-- Table structure for table `CLASS_SHIFT`
--

CREATE TABLE CLASS_SHIFT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CLASS int(11) NOT NULL default '0',
  KEY_SHIFT int(11) NOT NULL default '0',
  ack_opt_lock int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CLASS,KEY_SHIFT),
  KEY CLASS_SHIFT_KEY_CLASS (KEY_CLASS),
  KEY CLASS_SHIFT_KEY_SHIFT (KEY_SHIFT)
) TYPE=InnoDB;

--
-- Table structure for table `CONTRIBUTOR`
--

CREATE TABLE CONTRIBUTOR (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CONTRIBUTOR_NUMBER int(11) NOT NULL default '0',
  CONTRIBUTOR_NAME varchar(100) NOT NULL default '',
  CONTRIBUTOR_ADDRESS varchar(200) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY u1 (CONTRIBUTOR_NUMBER)
) TYPE=InnoDB;

--
-- Table structure for table `COORDINATOR`
--

CREATE TABLE COORDINATOR (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_EXECUTION_DEGREE int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  RESPONSIBLE tinyint(1) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_DEGREE,KEY_TEACHER)
) TYPE=InnoDB;

--
-- Table structure for table `COUNTRY`
--

CREATE TABLE COUNTRY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  NATIONALITY varchar(50) NOT NULL default '',
  CODE varchar(10) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U3 (CODE),
  UNIQUE KEY U2 (NATIONALITY),
  UNIQUE KEY U1 (NAME)
) TYPE=InnoDB;

--
-- Table structure for table `COURSE_HISTORIC`
--

CREATE TABLE COURSE_HISTORIC (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_CURRICULAR_COURSE int(11) unsigned NOT NULL default '0',
  ENROLLED int(11) default NULL,
  EVALUATED int(11) default NULL,
  APPROVED int(11) default NULL,
  CURRICULAR_YEAR varchar(50) default NULL,
  SEMESTER int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CURRICULAR_COURSE,CURRICULAR_YEAR,SEMESTER)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `COURSE_REPORT`
--

CREATE TABLE COURSE_REPORT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  REPORT text,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_COURSE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `CREDITS`
--

CREATE TABLE CREDITS (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  SUPPORT_LESSONS float default NULL,
  INSTITUTION_WORK_TIME float default NULL,
  LESSONS float default NULL,
  DEGREE_FINAL_PROJECT_STUDENTS float default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_PERIOD,KEY_TEACHER)
) TYPE=MyISAM;

--
-- Table structure for table `CREDITS_IN_ANY_SECUNDARY_AREA`
--

CREATE TABLE CREDITS_IN_ANY_SECUNDARY_AREA (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT_CURRICULAR_PLAN int(11) NOT NULL default '0',
  KEY_ENROLMENT int(11) NOT NULL default '0',
  GIVEN_CREDITS int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT_CURRICULAR_PLAN,KEY_ENROLMENT)
) TYPE=InnoDB;

--
-- Table structure for table `CREDITS_IN_SCIENTIFIC_AREA`
--

CREATE TABLE CREDITS_IN_SCIENTIFIC_AREA (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT_CURRICULAR_PLAN int(11) NOT NULL default '0',
  KEY_SCIENTIFIC_AREA int(11) NOT NULL default '0',
  KEY_ENROLMENT int(11) NOT NULL default '0',
  GIVEN_CREDITS int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT_CURRICULAR_PLAN,KEY_SCIENTIFIC_AREA,KEY_ENROLMENT)
) TYPE=InnoDB;

--
-- Table structure for table `CREDITS_MANAGER_DEPARTMENT`
--

CREATE TABLE CREDITS_MANAGER_DEPARTMENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PERSON int(11) NOT NULL default '0',
  KEY_DEPARTMENT int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_PERSON,KEY_DEPARTMENT)
) TYPE=MyISAM;

--
-- Table structure for table `CREDIT_LINE`
--

CREATE TABLE CREDIT_LINE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_TEACHER int(11) NOT NULL default '0',
  CREDITS float NOT NULL default '0',
  CREDIT_LINE_TYPE int(11) NOT NULL default '0',
  EXPLANATION varchar(250) NOT NULL default '',
  START_DATE date NOT NULL default '0000-00-00',
  END_DATE date NOT NULL default '0000-00-00',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=MyISAM;

--
-- Table structure for table `CURRICULAR_COURSE`
--

CREATE TABLE CURRICULAR_COURSE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_DEPARTMENT_COURSE int(11) default NULL,
  KEY_DEGREE_CURRICULAR_PLAN int(11) NOT NULL default '0',
  CREDITS double default NULL,
  THEORETICAL_HOURS double default NULL,
  PRATICAL_HOURS double default NULL,
  THEO_PRAT_HOURS double default NULL,
  LAB_HOURS double default NULL,
  NAME varchar(100) NOT NULL default '',
  CODE varchar(50) NOT NULL default '',
  TYPE int(11) default NULL,
  EXECUTION_SCOPE int(11) default NULL,
  MANDATORY tinyint(1) default NULL,
  KEY_CURRICULAR_COURSE_ENROLMENT_INFO int(11) default NULL,
  KEY_UNIVERSITY int(11) default NULL,
  BASIC tinyint(1) default '0',
  KEY_SCIENTIFIC_AREA int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  MAX_INCREMENT_NAC int(11) default '2',
  MIN_INCREMENT_NAC int(11) default '1',
  ENROLLMENT_WEIGTH int(11) default '1',
  ECTS_CREDITS double default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (CODE,NAME,KEY_DEGREE_CURRICULAR_PLAN)
) TYPE=InnoDB;

--
-- Table structure for table `CURRICULAR_COURSE_CURRICULAR_COURSE_GROUP`
--

CREATE TABLE CURRICULAR_COURSE_CURRICULAR_COURSE_GROUP (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE_GROUP int(11) NOT NULL default '0',
  KEY_CURRICULAR_COURSE int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CURRICULAR_COURSE_GROUP,KEY_CURRICULAR_COURSE),
  KEY CURRICULAR_COURSE_CURRICULAR_COURSE_GROUP_INDEX_2 (KEY_CURRICULAR_COURSE),
  KEY CURRICULAR_COURSE_CURRICULAR_COURSE_GROUP_INDEX_3 (KEY_CURRICULAR_COURSE_GROUP)
) TYPE=InnoDB;

--
-- Table structure for table `CURRICULAR_COURSE_ENROLMENT_INFO`
--

CREATE TABLE CURRICULAR_COURSE_ENROLMENT_INFO (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  MAX_INCREMENT_NAC int(11) NOT NULL default '0',
  MIN_INCREMENT_NAC int(11) NOT NULL default '0',
  WEIGTH int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (MAX_INCREMENT_NAC,MIN_INCREMENT_NAC,WEIGTH)
) TYPE=InnoDB;

--
-- Table structure for table `CURRICULAR_COURSE_EQUIVALENCE`
--

CREATE TABLE CURRICULAR_COURSE_EQUIVALENCE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=MyISAM;

--
-- Table structure for table `CURRICULAR_COURSE_EQUIVALENCE_RESTRICTION`
--

CREATE TABLE CURRICULAR_COURSE_EQUIVALENCE_RESTRICTION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE_EQUIVALENCE int(11) NOT NULL default '0',
  KEY_EQUIVALENT_CURRICULAR_COURSE int(11) NOT NULL default '0',
  YEAR_OF_EQUIVALENCE varchar(9) NOT NULL default '',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CURRICULAR_COURSE_EQUIVALENCE,KEY_EQUIVALENT_CURRICULAR_COURSE,YEAR_OF_EQUIVALENCE)
) TYPE=MyISAM;

--
-- Table structure for table `CURRICULAR_COURSE_EXECUTION_COURSE`
--

CREATE TABLE CURRICULAR_COURSE_EXECUTION_COURSE (
  INTERNAL_CODE int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE int(11) NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) NOT NULL default '0',
  PRIMARY KEY  (INTERNAL_CODE),
  UNIQUE KEY U1 (KEY_CURRICULAR_COURSE,KEY_EXECUTION_COURSE),
  KEY CURRICULAR_COURSE_EXECUTION_COURSE_KEY_EXECUTION_COURSE (KEY_EXECUTION_COURSE),
  KEY CURRICULAR_COURSE_EXECUTION_COURSE_KEY_CURRICULAR_COURSE (KEY_CURRICULAR_COURSE)
) TYPE=InnoDB;

--
-- Table structure for table `CURRICULAR_COURSE_GROUP`
--

CREATE TABLE CURRICULAR_COURSE_GROUP (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_BRANCH int(11) NOT NULL default '0',
  MINIMUM_CREDITS int(11) NOT NULL default '0',
  MAXIMUM_CREDITS int(11) NOT NULL default '0',
  AREA_TYPE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_BRANCH,ID_INTERNAL),
  KEY CURRICULAR_COURSE_GROUP_INDEX_1 (KEY_BRANCH),
  KEY CURRICULAR_COURSE_GROUP_INDEX_2 (KEY_BRANCH,AREA_TYPE)
) TYPE=InnoDB;

--
-- Table structure for table `CURRICULAR_COURSE_SCOPE`
--

CREATE TABLE CURRICULAR_COURSE_SCOPE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_SEMESTER int(11) NOT NULL default '0',
  KEY_CURRICULAR_COURSE int(11) NOT NULL default '0',
  KEY_BRANCH int(11) NOT NULL default '0',
  THEORETICAL_HOURS double default '0',
  PRATICAL_HOURS double default '0',
  THEO_PRAT_HOURS double default '0',
  LAB_HOURS double default '0',
  SCOPE_TYPE int(11) NOT NULL default '0',
  MAX_INCREMENT_NAC int(11) default NULL,
  MIN_INCREMENT_NAC int(11) default NULL,
  WEIGTH int(11) default NULL,
  CREDITS double default NULL,
  ECTS_CREDITS double default NULL,
  BEGIN_DATE date NOT NULL default '2003-10-24',
  END_DATE date default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CURRICULAR_SEMESTER,KEY_CURRICULAR_COURSE,KEY_BRANCH,BEGIN_DATE),
  KEY CURRICULAR_COURSE_SCOPE_KEY_CURRICULAR_COURSE (KEY_CURRICULAR_COURSE),
  KEY CURRICULAR_COURSE_SCOPE_KEY_CURRICULAR_SEMESTER (KEY_CURRICULAR_SEMESTER)
) TYPE=InnoDB;

--
-- Table structure for table `CURRICULAR_SEMESTER`
--

CREATE TABLE CURRICULAR_SEMESTER (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_YEAR int(11) NOT NULL default '0',
  SEMESTER int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (SEMESTER,KEY_CURRICULAR_YEAR),
  KEY CURRICULAR_SEMESTER_KEY_CURRICULAR_YEAR (KEY_CURRICULAR_YEAR)
) TYPE=InnoDB COMMENT='InnoDB free: 368640 kB';

--
-- Table structure for table `CURRICULAR_YEAR`
--

CREATE TABLE CURRICULAR_YEAR (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  YEAR int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (YEAR)
) TYPE=InnoDB;

--
-- Table structure for table `CURRICULUM`
--

CREATE TABLE CURRICULUM (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE int(11) default NULL,
  GENERAL_OBJECTIVES text,
  OPERACIONAL_OBJECTIVES text,
  PROGRAM text,
  OPERACIONAL_OBJECTIVES_EN text,
  GENERAL_OBJECTIVES_EN text,
  PROGRAM_EN text,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  KEY_PERSON_WHO_ALTERED int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB; InnoDB free: 372736 kB';

--
-- Table structure for table `CURRICULUM_BACKUP`
--

CREATE TABLE CURRICULUM_BACKUP (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE int(11) default NULL,
  GENERAL_OBJECTIVES text,
  OPERACIONAL_OBJECTIVES text,
  PROGRAM text,
  OPERACIONAL_OBJECTIVES_EN text,
  GENERAL_OBJECTIVES_EN text,
  PROGRAM_EN text,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  KEY_PERSON_WHO_ALTERED int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `Concelho`
--

CREATE TABLE Concelho (
  Chave int(11) NOT NULL default '0',
  chaveDistrito int(11) NOT NULL default '0',
  Designacao varchar(35) NOT NULL default ''
) TYPE=MyISAM;

--
-- Table structure for table `DEGREE`
--

CREATE TABLE DEGREE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CODE varchar(100) NOT NULL default '',
  NAME varchar(100) NOT NULL default '',
  TYPE_DEGREE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U2 (NAME,TYPE_DEGREE),
  UNIQUE KEY U1 (CODE)
) TYPE=InnoDB;

--
-- Table structure for table `DEGREE_CURRICULAR_PLAN`
--

CREATE TABLE DEGREE_CURRICULAR_PLAN (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  KEY_DEGREE int(11) NOT NULL default '0',
  STATE int(11) default NULL,
  INITIAL_DATE date NOT NULL default '0000-00-00',
  END_DATE date default NULL,
  DEGREE_DURATION int(11) NOT NULL default '5',
  MINIMAL_YEAR_FOR_OPTIONAL_COURSES int(11) NOT NULL default '3',
  MARK_TYPE int(11) default NULL,
  NEEDED_CREDITS float(11,2) default NULL,
  NUMERUS_CLAUSUS int(11) unsigned default NULL,
  DESCRIPTION text,
  DESCRIPTION_EN text,
  ENROLLMENT_STRATEGY_CLASS_NAME varchar(255) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,KEY_DEGREE),
  KEY DEGREE_CURRICULAR_PLAN_KEY_DEGREE (KEY_DEGREE)
) TYPE=InnoDB;

--
-- Table structure for table `DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO`
--

CREATE TABLE DEGREE_CURRICULAR_PLAN_ENROLMENT_INFO (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_DEGREE_CURRICULAR_PLAN int(11) NOT NULL default '0',
  DEGREE_DURATION int(11) NOT NULL default '0',
  MINIMAL_YEAR_FOR_OPTIONAL_COURSES int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_DEGREE_CURRICULAR_PLAN)
) TYPE=InnoDB;

--
-- Table structure for table `DEGREE_INFO`
--

CREATE TABLE DEGREE_INFO (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_DEGREE int(11) NOT NULL default '0',
  DESCRIPTION text,
  OBJECTIVES text,
  HISTORY text,
  PROFESSIONAL_EXITS text,
  ADDITIONAL_INFO text,
  LINKS text,
  TEST_INGRESSION text,
  DRIFTS_INITIAL int(11) default NULL,
  DRIFTS_FIRST int(11) default NULL,
  DRIFTS_SECOND int(11) default NULL,
  CLASSIFICATIONS text,
  MARK_MIN float(10,2) default NULL,
  MARK_MAX float(10,2) default NULL,
  MARK_AVERAGE float(10,2) default NULL,
  DESCRIPTION_EN text,
  OBJECTIVES_EN text,
  HISTORY_EN text,
  PROFESSIONAL_EXITS_EN text,
  ADDITIONAL_INFO_EN text,
  LINKS_EN text,
  TEST_INGRESSION_EN text,
  CLASSIFICATIONS_EN text,
  LAST_MODIFICATION_DATE datetime default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_DEGREE,LAST_MODIFICATION_DATE)
) TYPE=InnoDB;

--
-- Table structure for table `DEGREE_OBJECTIVES`
--

CREATE TABLE DEGREE_OBJECTIVES (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_DEGREE int(11) unsigned NOT NULL default '0',
  GENERAL_OBJECTIVES text,
  OPERACIONAL_OBJECTIVES text,
  STARTING_DATE date default NULL,
  END_DATE date default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_DEGREE,STARTING_DATE)
) TYPE=MyISAM;

--
-- Table structure for table `DELEGATE`
--

CREATE TABLE DELEGATE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_DEGREE int(11) NOT NULL default '0',
  KEY_STUDENT int(11) NOT NULL default '0',
  KEY_EXECUTION_YEAR int(11) NOT NULL default '0',
  YEAR_TYPE int(11) NOT NULL default '0',
  TYPE tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U2 (KEY_EXECUTION_YEAR,KEY_STUDENT,TYPE),
  UNIQUE KEY U1 (KEY_EXECUTION_YEAR,KEY_STUDENT,YEAR_TYPE)
) TYPE=InnoDB;

--
-- Table structure for table `DEPARTMENT`
--

CREATE TABLE DEPARTMENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  CODE varchar(50) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME),
  UNIQUE KEY U2 (CODE)
) TYPE=InnoDB;

--
-- Table structure for table `DEPARTMENT_COURSE`
--

CREATE TABLE DEPARTMENT_COURSE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  CODE varchar(50) NOT NULL default '',
  KEY_DEPARTMENT int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,CODE)
) TYPE=InnoDB;

--
-- Table structure for table `DISTRIBUTED_TESTS`
--

CREATE TABLE DISTRIBUTED_TESTS (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  TITLE text NOT NULL,
  TEST_INFORMATION text,
  TEST_BEGIN_DATE date default NULL,
  TEST_BEGIN_HOUR time default NULL,
  TEST_END_DATE date default NULL,
  TEST_END_HOUR time default NULL,
  TEST_TYPE int(1) NOT NULL default '0',
  CORRECTION_AVAILABILITY int(1) NOT NULL default '0',
  STUDENT_FEEDBACK int(1) NOT NULL default '0',
  NUMBER_OF_QUESTIONS int(2) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  KEY_TEST_SCOPE int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `DISTRIBUTED_TEST_ADVISORY`
--

CREATE TABLE DISTRIBUTED_TEST_ADVISORY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_DISTRIBUTED_TEST int(11) unsigned NOT NULL default '0',
  KEY_ADVISORY int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `Disciplinas`
--

CREATE TABLE Disciplinas (
  Sigla varchar(8) default NULL,
  Nome varchar(70) default NULL,
  Aulas_Teoricas tinyint(3) default NULL,
  Aulas_Praticas tinyint(3) default NULL,
  Tipo_Sala_Teorica varchar(8) default NULL,
  Tipo_Sala_Pratica varchar(8) default NULL,
  Tipo_Sala_Teorica_Pratica varchar(8) default NULL,
  Tipo_Sala_Laboratorio varchar(8) default NULL,
  Epoca_Inicio smallint(5) default NULL,
  Epoca_Fim smallint(5) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  UNIQUE KEY Disciplinas (Sigla),
  KEY IndexEpoca_Fim (Epoca_Fim),
  KEY IndexEpoca_Inicio (Epoca_Inicio)
) TYPE=InnoDB;

--
-- Table structure for table `Distrito`
--

CREATE TABLE Distrito (
  Chave int(11) NOT NULL default '0',
  Designacao varchar(35) NOT NULL default ''
) TYPE=MyISAM;

--
-- Table structure for table `ENROLMENT`
--

CREATE TABLE ENROLMENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT_CURRICULAR_PLAN int(11) NOT NULL default '0',
  KEY_CURRICULAR_COURSE int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  STATE int(11) NOT NULL default '0',
  CLASS_NAME varchar(255) NOT NULL default '',
  KEY_CURRICULAR_COURSE_FOR_OPTION int(11) default NULL,
  EVALUATION_TYPE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT_CURRICULAR_PLAN,KEY_CURRICULAR_COURSE,KEY_EXECUTION_PERIOD),
  KEY ENROLMENT_KEY_STUDENT_CURRICULAR_PLAN (KEY_STUDENT_CURRICULAR_PLAN),
  KEY ENROLMENT_KEY_CURRICULAR_COURSE_SCOPE (KEY_CURRICULAR_COURSE)
) TYPE=InnoDB;

--
-- Table structure for table `ENROLMENT_EQUIVALENCE`
--

CREATE TABLE ENROLMENT_EQUIVALENCE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_ENROLMENT int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_ENROLMENT)
) TYPE=InnoDB;

--
-- Table structure for table `ENROLMENT_EVALUATION`
--

CREATE TABLE ENROLMENT_EVALUATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  GRADE varchar(11) default NULL,
  EVALUATION_TYPE int(11) NOT NULL default '0',
  EXAM_DATE date default NULL,
  GRADE_AVAILABLE_DATE date default NULL,
  RESPONSIBLE_PERSON int(11) default NULL,
  STATE int(11) NOT NULL default '0',
  KEY_ENROLMENT int(11) NOT NULL default '0',
  KEY_EMPLOYEE int(11) default NULL,
  WHEN_ALTER datetime default NULL,
  CHECKSUM varchar(50) default NULL,
  OBSERVATION varchar(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  KEY KEY_ENROLMENT_EVALUATION_INDEX (KEY_ENROLMENT),
  KEY ENROLMENT_EVALUATION_KEY_ENROLMENT (KEY_ENROLMENT)
) TYPE=InnoDB;

--
-- Table structure for table `ENROLMENT_PERIOD`
--

CREATE TABLE ENROLMENT_PERIOD (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_DEGREE_CURRICULAR_PLAN int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  START_DATE date NOT NULL default '0000-00-00',
  END_DATE date NOT NULL default '0000-00-00',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_DEGREE_CURRICULAR_PLAN,KEY_EXECUTION_PERIOD)
) TYPE=InnoDB;

--
-- Table structure for table `EQUIVALENCE`
--

CREATE TABLE EQUIVALENCE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_EQUIVALENT_ENROLMENT int(11) NOT NULL default '0',
  KEY_ENROLMENT int(11) NOT NULL default '0',
  EQUIVALENCE_TYPE int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EQUIVALENT_ENROLMENT,KEY_ENROLMENT)
) TYPE=InnoDB;

--
-- Table structure for table `EQUIVALENCY_THEME`
--

CREATE TABLE EQUIVALENCY_THEME (
  theme_id int(11) NOT NULL default '0',
  equivalency_id int(11) NOT NULL default '0',
  PRIMARY KEY  (equivalency_id,theme_id)
) TYPE=InnoDB;

--
-- Table structure for table `EQUIVALENT_ENROLMENT_FOR_ENROLMENT_EQUIVALENCE`
--

CREATE TABLE EQUIVALENT_ENROLMENT_FOR_ENROLMENT_EQUIVALENCE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_ENROLMENT_EQUIVALENCE int(11) NOT NULL default '0',
  KEY_EQUIVALENT_ENROLMENT int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_ENROLMENT_EQUIVALENCE,KEY_EQUIVALENT_ENROLMENT)
) TYPE=InnoDB;

--
-- Table structure for table `EVALUATION`
--

CREATE TABLE EVALUATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL default '',
  DAY date default NULL,
  BEGINNING time default NULL,
  END time default NULL,
  SEASON int(11) NOT NULL default '0',
  ENROLLMENT_BEGIN_DAY date default NULL,
  ENROLLMENT_BEGIN_TIME time default NULL,
  ENROLLMENT_END_DAY date default NULL,
  ENROLLMENT_END_TIME time default NULL,
  PUBLISHMENT_MESSAGE varchar(120) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_DISTRIBUTED_TEST int(11) unsigned default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `EVALUATION_METHOD`
--

CREATE TABLE EVALUATION_METHOD (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_EXECUTION_COURSE int(11) default NULL,
  EVALUATION_ELEMENTS text,
  EVALUATION_ELEMENTS_EN text,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_COURSE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB; InnoDB free: 365568 kB';

--
-- Table structure for table `EXAM_EXECUTION_COURSE`
--

CREATE TABLE EXAM_EXECUTION_COURSE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_EVALUATION int(11) NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EVALUATION,KEY_EXECUTION_COURSE),
  KEY EXAM_EXECUTION_COURSE_KEY_EXECUTION_COURSE (KEY_EXECUTION_COURSE)
) TYPE=InnoDB;

--
-- Table structure for table `EXAM_ROOM`
--

CREATE TABLE EXAM_ROOM (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_EVALUATION int(11) NOT NULL default '0',
  KEY_ROOM int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EVALUATION,KEY_ROOM)
) TYPE=InnoDB;

--
-- Table structure for table `EXAM_STUDENT`
--

CREATE TABLE EXAM_STUDENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_EVALUATION int(11) NOT NULL default '0',
  KEY_STUDENT int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EVALUATION,KEY_STUDENT)
) TYPE=InnoDB;

--
-- Table structure for table `EXAM_STUDENT_ROOM`
--

CREATE TABLE EXAM_STUDENT_ROOM (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_EVALUATION int(11) NOT NULL default '0',
  KEY_STUDENT int(11) NOT NULL default '0',
  KEY_ROOM int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EVALUATION,KEY_STUDENT,KEY_ROOM)
) TYPE=InnoDB;

--
-- Table structure for table `EXECUTION_COURSE`
--

CREATE TABLE EXECUTION_COURSE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(100) default NULL,
  CODE varchar(50) NOT NULL default '',
  THEORETICAL_HOURS double default NULL,
  PRATICAL_HOURS double default NULL,
  THEO_PRAT_HOURS double default NULL,
  LAB_HOURS double default NULL,
  SEMESTER int(11) default '2',
  KEY_EXECUTION_PERIOD int(11) unsigned NOT NULL default '0',
  COMMENT text NOT NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U (CODE,KEY_EXECUTION_PERIOD),
  KEY U1 (KEY_EXECUTION_PERIOD,CODE),
  KEY EXECUTION_COURSE_KEY_EXECUTION_PERIOD (KEY_EXECUTION_PERIOD)
) TYPE=InnoDB;

--
-- Table structure for table `EXECUTION_DEGREE`
--

CREATE TABLE EXECUTION_DEGREE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACADEMIC_YEAR int(11) NOT NULL default '0',
  KEY_DEGREE_CURRICULAR_PLAN int(11) NOT NULL default '0',
  TEMPORARY_EXAM_MAP tinyint(1) default '1',
  KEY_CAMPUS int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (ACADEMIC_YEAR,KEY_DEGREE_CURRICULAR_PLAN,KEY_CAMPUS),
  KEY EXECUTION_DEGREE_KEY_EXECUTION_YEAR (ACADEMIC_YEAR),
  KEY EXECUTION_DEGREE_KEY_DEGREE_CURRICULAR_PLAN (KEY_DEGREE_CURRICULAR_PLAN),
  KEY EXECUTION_DEGREE_KEY_ACADEMIC_YEAR (ACADEMIC_YEAR)
) TYPE=InnoDB;

--
-- Table structure for table `EXECUTION_PERIOD`
--

CREATE TABLE EXECUTION_PERIOD (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  KEY_EXECUTION_YEAR int(11) NOT NULL default '0',
  STATE char(3) NOT NULL default 'NO',
  SEMESTER int(11) NOT NULL default '0',
  BEGIN_DATE date NOT NULL default '0000-00-00',
  END_DATE date NOT NULL default '0000-00-00',
  KEY_PREVIOUS_EXECUTION_PERIOD int (11),
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,KEY_EXECUTION_YEAR)
) TYPE=InnoDB COMMENT='InnoDB free: 373760 kB; InnoDB free: 368640 kB';

--
-- Table structure for table `EXECUTION_YEAR`
--

CREATE TABLE EXECUTION_YEAR (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  YEAR varchar(9) NOT NULL default '',
  STATE char(3) NOT NULL default 'NO',
  BEGIN_DATE date NOT NULL default '0000-00-00',
  END_DATE date NOT NULL default '0000-00-00',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (YEAR)
) TYPE=InnoDB COMMENT='InnoDB free: 373760 kB';

--
-- Table structure for table `EXTERNAL_ACTIVITY`
--

CREATE TABLE EXTERNAL_ACTIVITY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACTIVITY text,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `EXTERNAL_PERSON`
--

CREATE TABLE EXTERNAL_PERSON (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  KEY_WORK_LOCATION int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `FINAL_DEGREE_WORK_PROPOSAL`
--

CREATE TABLE FINAL_DEGREE_WORK_PROPOSAL (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PROPOSAL_NUMBER int(11) NOT NULL default '0',
  KEY_EXECUTION_DEGREE int(11) NOT NULL default '0',
  TITLE varchar(100) NOT NULL default '',
  KEY_ORIENTATOR int(11) NOT NULL default '0',
  KEY_COORIENTATOR int(11) default NULL,
  ORIENTATORS_CREDITS_PERCENTAGE int(11) NOT NULL default '0',
  COORIENTATORS_CREDITS_PERCENTAGE int(11) NOT NULL default '0',
  COMPANION_NAME varchar(100) default NULL,
  COMPANION_MAIL varchar(100) default NULL,
  COMPANION_PHONE int(11) default NULL,
  FRAMING text NOT NULL,
  OBJECTIVES text NOT NULL,
  DESCRIPTION text NOT NULL,
  REQUIREMENTS text,
  DELIVERAVLE text,
  URL varchar(100) default NULL,
  MINIMUM_NUMBER_OF_GROUP_ELEMENTS int(2) default NULL,
  MAXIMUM_NUMBER_OF_GROUP_ELEMENTS int(2) default NULL,
  LOCATION varchar(100) default NULL,
  DEGREE_TYPE int(11) default NULL,
  OBSERVATIONS text,
  COMPANY_NAME varchar(100) default NULL,
  COMPANY_ADRESS varchar(100) default NULL,
  STATUS int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  KEY INDEX_KEY_EXECUTION_DEGREE (KEY_EXECUTION_DEGREE),
  KEY INDEX_KEY_ORIENTATOR (KEY_ORIENTATOR),
  KEY INDEX_KEY_COORIENTATOR (KEY_COORIENTATOR)
) TYPE=InnoDB;

--
-- Table structure for table `FINAL_DEGREE_WORK_PROPOSAL_BRANCH`
--

CREATE TABLE FINAL_DEGREE_WORK_PROPOSAL_BRANCH (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_FINAL_DEGREE_WORK_PROPOSAL int(11) NOT NULL default '0',
  KEY_BRANCH int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  KEY INDEX_KEY_FINAL_DEGREE_WORK_PROPOSAL (KEY_FINAL_DEGREE_WORK_PROPOSAL),
  KEY INDEX_KEY_BRANCH (KEY_BRANCH)
) TYPE=InnoDB;

--
-- Table structure for table `FINAL_DEGREE_WORK_SCHEDULEING`
--

CREATE TABLE FINAL_DEGREE_WORK_SCHEDULEING (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  KEY_EXECUTION_DEGREE int(11) NOT NULL default '0',
  START_OF_PROPOSAL_PERIOD timestamp(12) NOT NULL,
  END_OF_PROPOSAL_PERIOD timestamp(12) NOT NULL,
  CURRENT_PROPOSAL_NUMBER int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_DEGREE),
  KEY INDEX_KEY_EXECUTION_DEGREE (KEY_EXECUTION_DEGREE)
) TYPE=InnoDB;

--
-- Table structure for table `Freguesia`
--

CREATE TABLE Freguesia (
  Chave int(11) NOT NULL default '0',
  chaveDistrito int(11) NOT NULL default '0',
  chaveConcelho int(11) NOT NULL default '0',
  Designacao varchar(35) NOT NULL default ''
) TYPE=MyISAM;

--
-- Table structure for table `GAUGING_TEST_RESULT`
--

CREATE TABLE GAUGING_TEST_RESULT (
  ID_INTERNAL int(10) unsigned NOT NULL auto_increment,
  KEY_STUDENT int(11) default NULL,
  TEST int(11) default NULL,
  P1 varchar(5) default NULL,
  P2 varchar(5) default NULL,
  P3 varchar(5) default NULL,
  P4 varchar(5) default NULL,
  P5 varchar(5) default NULL,
  P6 varchar(5) default NULL,
  P7 varchar(5) default NULL,
  P8 varchar(5) default NULL,
  P9 varchar(5) default NULL,
  P10 varchar(5) default NULL,
  P11 varchar(5) default NULL,
  P12 varchar(5) default NULL,
  P13 varchar(5) default NULL,
  P14 varchar(5) default NULL,
  P15 varchar(5) default NULL,
  P16 varchar(5) default NULL,
  P17 varchar(5) default NULL,
  P18 varchar(5) default NULL,
  P19 varchar(5) default NULL,
  P20 varchar(5) default NULL,
  P21 varchar(5) default NULL,
  P22 varchar(5) default NULL,
  P23 varchar(5) default NULL,
  P24 varchar(5) default NULL,
  P25 varchar(5) default NULL,
  P26 varchar(5) default NULL,
  P27 varchar(5) default NULL,
  P28 varchar(5) default NULL,
  UNANSWERED int(11) default NULL,
  CORRECT int(11) default NULL,
  WRONG int(11) default NULL,
  CF varchar(255) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `GRANT_CONTRACT`
--

CREATE TABLE GRANT_CONTRACT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NUMBER int(11) unsigned NOT NULL default '0',
  DATE_BEGIN_CONTRACT date default NULL,
  DATE_END_CONTRACT date default NULL,
  END_CONTRACT_MOTIVE varchar(255) default NULL,
  KEY_GRANT_OWNER int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TYPE int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (NUMBER,KEY_GRANT_OWNER)
) TYPE=MyISAM;

--
-- Table structure for table `GRANT_ORIENTATION_TEACHER`
--

CREATE TABLE GRANT_ORIENTATION_TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  DATE_BEGIN date NOT NULL default '0000-00-00',
  DATE_END date default NULL,
  KEY_GRANT_CONTRACT int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TEACHER int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (DATE_BEGIN,DATE_END,KEY_GRANT_CONTRACT,KEY_GRANT_TEACHER)
) TYPE=MyISAM;

--
-- Table structure for table `GRANT_OWNER`
--

CREATE TABLE GRANT_OWNER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  NUMBER int(11) unsigned NOT NULL default '0',
  DATE_SEND_CGD date default NULL,
  CARD_COPY_NUMBER int(11) unsigned default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (NUMBER,KEY_PERSON)
) TYPE=MyISAM;

--
-- Table structure for table `GRANT_RESPONSIBLE_TEACHER`
--

CREATE TABLE GRANT_RESPONSIBLE_TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  DATE_BEGIN date NOT NULL default '0000-00-00',
  DATE_END date default NULL,
  KEY_GRANT_CONTRACT int(11) unsigned NOT NULL default '0',
  KEY_GRANT_TEACHER int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (DATE_BEGIN,DATE_END,KEY_GRANT_CONTRACT,KEY_GRANT_TEACHER)
) TYPE=MyISAM;

--
-- Table structure for table `GRANT_TYPE`
--

CREATE TABLE GRANT_TYPE (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  SIGLA varchar(50) NOT NULL default '',
  MIN_PERIOD_DAYS int(2) unsigned NOT NULL default '0',
  MAX_PERIOD_DAYS int(2) unsigned NOT NULL default '0',
  INDICATIVE_VALUE double(10,2) NOT NULL default '0.00',
  SOURCE varchar(10) NOT NULL default '',
  STATE date default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY UNIQUE1 (SIGLA)
) TYPE=MyISAM;

--
-- Table structure for table `GRATUITY`
--

CREATE TABLE GRATUITY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT_CURRICULAR_PLAN int(11) NOT NULL default '0',
  STATE int(11) default NULL,
  GRATUITY_STATE int(11) default NULL,
  DATE date default NULL,
  REMARKS text,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `GRATUITY_SITUATION`
--

CREATE TABLE GRATUITY_SITUATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  PAYED_VALUE float default NULL,
  REMAINING_VALUE float default NULL,
  EXEMPTION_PERCENTAGE int(11) default NULL,
  EXEMPTION_TYPE int(11) default NULL,
  EXEMPTION_DESCRIPTION varchar(50) default NULL,
  KEY_GRATUITY int(11) default NULL,
  KEY_STUDENT_CURRICULAR_PLAN int(11) default NULL,
  KEY_EMPLOYEE int(11) unsigned default NULL,
  WHEN_ALTER timestamp(14) NOT NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT_CURRICULAR_PLAN,KEY_GRATUITY),
  KEY INDEX1 (KEY_STUDENT_CURRICULAR_PLAN,KEY_GRATUITY)
) TYPE=InnoDB;

--
-- Table structure for table `GRATUITY_VALUES`
--

CREATE TABLE GRATUITY_VALUES (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ANUAL_VALUE float default NULL,
  SCHOLARSHIP_VALUE float default NULL,
  FINAL_PROOF_VALUE float default NULL,
  COURSE_VALUE float default NULL,
  CREDIT_VALUE float default NULL,
  PROOF_REQUEST_PAYMENT tinyint(1) default NULL,
  START_PAYMENT date default NULL,
  END_PAYMENT date default NULL,
  KEY_EXECUTION_DEGREE int(11) default NULL,
  KEY_EMPLOYEE int(11) unsigned default NULL,
  WHEN_ALTER timestamp(14) NOT NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_DEGREE),
  KEY INDEX1 (KEY_EXECUTION_DEGREE)
) TYPE=InnoDB;

--
-- Table structure for table `GROUP_PROPERTIES`
--

CREATE TABLE GROUP_PROPERTIES (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  MAXIMUM_CAPACITY int(11) default NULL,
  MINIMUM_CAPACITY int(11) default NULL,
  IDEAL_CAPACITY int(11) default NULL,
  ENROLMENT_POLICY int(11) default NULL,
  GROUP_MAXIMUM_NUMBER int(11) default NULL,
  NAME varchar(50) NOT NULL default '',
  SHIFT_TYPE int(11) default NULL,
  ENROLMENT_BEGIN_DAY date default NULL,
  ENROLMENT_END_DAY date default NULL,
  PROJECT_DESCRIPTION text,
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY ID_INTERNAL (NAME,KEY_EXECUTION_COURSE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `GUIDE`
--

CREATE TABLE GUIDE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NUMBER int(11) NOT NULL default '0',
  YEAR int(4) NOT NULL default '0',
  KEY_CONTRIBUTOR int(11) NOT NULL default '0',
  KEY_PERSON int(11) NOT NULL default '0',
  TOTAL float(11,2) NOT NULL default '0.00',
  REMARKS text,
  GUIDE_REQUESTER int(11) NOT NULL default '0',
  KEY_EXECUTION_DEGREE int(11) NOT NULL default '0',
  PAYMENT_TYPE int(11) default NULL,
  CREATION_DATE date NOT NULL default '0000-00-00',
  VERSION int(11) NOT NULL default '1',
  PAYMENT_DATE date default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY u1 (NUMBER,YEAR,VERSION)
) TYPE=InnoDB;

--
-- Table structure for table `GUIDE_ENTRY`
--

CREATE TABLE GUIDE_ENTRY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_GUIDE int(11) NOT NULL default '0',
  GRADUATION_TYPE int(11) default NULL,
  DOCUMENT_TYPE int(11) NOT NULL default '0',
  DESCRIPTION varchar(200) default NULL,
  PRICE float(10,2) NOT NULL default '0.00',
  QUANTITY int(4) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY u1 (KEY_GUIDE,GRADUATION_TYPE,DOCUMENT_TYPE,DESCRIPTION)
) TYPE=InnoDB;

--
-- Table structure for table `GUIDE_SITUATION`
--

CREATE TABLE GUIDE_SITUATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_GUIDE int(11) NOT NULL default '0',
  SITUATION int(11) NOT NULL default '0',
  DATE date NOT NULL default '0001-01-01',
  REMARKS text,
  STATE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY u1 (KEY_GUIDE,SITUATION)
) TYPE=InnoDB;

--
-- Table structure for table `ITEM`
--

CREATE TABLE ITEM (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NAME varchar(100) default NULL,
  ITEM_ORDER int(11) unsigned default NULL,
  INFORMATION text,
  URGENT int(11) unsigned default NULL,
  KEY_SECTION int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_SECTION,NAME)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB; InnoDB free: 372736 kB';

--
-- Table structure for table `LESSON`
--

CREATE TABLE LESSON (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  WEEKDAY int(11) NOT NULL default '0',
  START_TIME time NOT NULL default '00:00:00',
  END_TIME time NOT NULL default '00:00:00',
  KEY_ROOM int(11) NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) NOT NULL default '0',
  TYPE int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (WEEKDAY,START_TIME,END_TIME,KEY_ROOM,KEY_EXECUTION_PERIOD)
) TYPE=InnoDB;

--
-- Table structure for table `MARK`
--

CREATE TABLE MARK (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  MARK varchar(4) default NULL,
  PUBLISHED_MARK varchar(4) default NULL,
  KEY_EVALUATION int(11) NOT NULL default '0',
  KEY_ATTEND int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EVALUATION,KEY_ATTEND)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_CANDIDATE`
--

CREATE TABLE MASTER_DEGREE_CANDIDATE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  PERSON_KEY int(11) NOT NULL default '0',
  EXECUTION_DEGREE_KEY int(11) NOT NULL default '0',
  CANDIDATE_NUMBER int(20) default NULL,
  SPECIALIZATION int(11) NOT NULL default '0',
  MAJOR_DEGREE varchar(100) default NULL,
  MAJOR_DEGREE_SCHOOL varchar(100) default NULL,
  MAJOR_DEGREE_YEAR int(11) default '0',
  AVERAGE float default NULL,
  SPECIALIZATION_AREA varchar(255) default NULL,
  SUBSTITUTE_ORDER int(11) default NULL,
  GIVEN_CREDITS float default NULL,
  GIVEN_CREDITS_REMARKS text,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY u1 (PERSON_KEY,EXECUTION_DEGREE_KEY,SPECIALIZATION),
  UNIQUE KEY u2 (CANDIDATE_NUMBER,EXECUTION_DEGREE_KEY,SPECIALIZATION)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_PROOF_VERSION`
--

CREATE TABLE MASTER_DEGREE_PROOF_VERSION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS int(11) NOT NULL default '0',
  KEY_EMPLOYEE int(11) NOT NULL default '0',
  PROOF_DATE date default NULL,
  THESIS_DELIVERY_DATE date default NULL,
  FINAL_RESULT int(11) NOT NULL default '0',
  ATTACHED_COPIES_NUMBER int(3) default NULL,
  LAST_MODIFICATION timestamp(14) NOT NULL,
  CURRENT_STATE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_PROOF_VERSION_EXTERNAL_JURY`
--

CREATE TABLE MASTER_DEGREE_PROOF_VERSION_EXTERNAL_JURY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_PROOF_VERSION int(11) NOT NULL default '0',
  KEY_EXTERNAL_PERSON int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_PROOF_VERSION,KEY_EXTERNAL_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_PROOF_VERSION_JURY`
--

CREATE TABLE MASTER_DEGREE_PROOF_VERSION_JURY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_PROOF_VERSION int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_PROOF_VERSION,KEY_TEACHER)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_THESIS`
--

CREATE TABLE MASTER_DEGREE_THESIS (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_STUDENT_CURRICULAR_PLAN int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT_CURRICULAR_PLAN)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_THESIS_DATA_VERSION`
--

CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS int(11) NOT NULL default '0',
  KEY_EMPLOYEE int(11) NOT NULL default '0',
  DISSERTATION_TITLE varchar(255) NOT NULL default '',
  LAST_MODIFICATION timestamp(14) NOT NULL,
  CURRENT_STATE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_THESIS_DATA_VERSION_ASSISTENT_GUIDER`
--

CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_ASSISTENT_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_TEACHER)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_ASSISTENT_GUIDER`
--

CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_ASSISTENT_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) NOT NULL default '0',
  KEY_EXTERNAL_PERSON int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_EXTERNAL_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_GUIDER`
--

CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) NOT NULL default '0',
  KEY_EXTERNAL_PERSON int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_EXTERNAL_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `MASTER_DEGREE_THESIS_DATA_VERSION_GUIDER`
--

CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_TEACHER)
) TYPE=InnoDB;

--
-- Table structure for table `METADATA`
--

CREATE TABLE METADATA (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  METADATA_FILE text,
  VISIBILITY tinyint(1) NOT NULL default '1',
  ACK_OPT_LOCK int(11) default NULL,
  DESCRIPTION text,
  DIFFICULTY text,
  LEARNINGTIME time default NULL,
  LEVEL text,
  MAIN_SUBJECT text,
  SECONDARY_SUBJECT text,
  AUTHOR text,
  NUMBER_OF_MEMBERS int(5) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  KEY index1 (KEY_EXECUTION_COURSE,VISIBILITY)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DLIST`
--

CREATE TABLE OJB_DLIST (
  ID int(11) NOT NULL default '0',
  SIZE_ int(11) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DLIST_ENTRIES`
--

CREATE TABLE OJB_DLIST_ENTRIES (
  ID int(11) NOT NULL default '0',
  DLIST_ID int(11) NOT NULL default '0',
  POSITION_ int(11) default NULL,
  OID_ longblob,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DMAP`
--

CREATE TABLE OJB_DMAP (
  ID int(11) NOT NULL default '0',
  SIZE_ int(11) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DMAP_ENTRIES`
--

CREATE TABLE OJB_DMAP_ENTRIES (
  ID int(11) NOT NULL default '0',
  DMAP_ID int(11) NOT NULL default '0',
  KEY_OID longblob,
  VALUE_OID longblob,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DSET`
--

CREATE TABLE OJB_DSET (
  ID int(11) NOT NULL default '0',
  SIZE_ int(11) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DSET_ENTRIES`
--

CREATE TABLE OJB_DSET_ENTRIES (
  ID int(11) NOT NULL default '0',
  DLIST_ID int(11) NOT NULL default '0',
  POSITION_ int(11) default NULL,
  OID_ longblob,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_HL_SEQ`
--

CREATE TABLE OJB_HL_SEQ (
  TABLENAME varchar(175) NOT NULL default '',
  FIELDNAME varchar(70) NOT NULL default '',
  MAX_KEY int(11) default NULL,
  GRAB_SIZE int(11) default NULL,
  VERSION int(11) default NULL,
  PRIMARY KEY  (TABLENAME,FIELDNAME)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_LOCKENTRY`
--

CREATE TABLE OJB_LOCKENTRY (
  OID_ varchar(250) NOT NULL default '',
  TX_ID varchar(50) NOT NULL default '',
  TIMESTAMP_ decimal(10,0) default NULL,
  ISOLATIONLEVEL int(11) default NULL,
  LOCKTYPE int(11) default NULL,
  PRIMARY KEY  (OID_,TX_ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_NRM`
--

CREATE TABLE OJB_NRM (
  NAME varchar(250) NOT NULL default '',
  OID_ longblob,
  PRIMARY KEY  (NAME)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_SEQ`
--

CREATE TABLE OJB_SEQ (
  TABLENAME varchar(175) NOT NULL default '',
  FIELDNAME varchar(70) NOT NULL default '',
  LAST_NUM int(11) default NULL,
  PRIMARY KEY  (TABLENAME,FIELDNAME)
) TYPE=InnoDB;

--
-- Table structure for table `OLD_PUBLICATION`
--

CREATE TABLE OLD_PUBLICATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  OLD_PUBLICATION_TYPE int(10) default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  PUBLICATION text,
  KEY_TEACHER int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `ORIENTATION`
--

CREATE TABLE ORIENTATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ORIENTATION_TYPE int(10) default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  NUMBER_OF_STUDENTS int(10) default NULL,
  DESCRIPTION text,
  KEY_TEACHER int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_TEACHER,ORIENTATION_TYPE)
) TYPE=InnoDB;

--
-- Table structure for table `PAYMENT_PHASE`
--

CREATE TABLE PAYMENT_PHASE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  START_DATE date default NULL,
  END_DATE date default NULL,
  VALUE float default NULL,
  DESCRIPTION varchar(50) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_GRATUITY int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `PERIOD`
--

CREATE TABLE PERIOD (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  START_DATE date NOT NULL default '0000-00-00',
  END_DATE date NOT NULL default '0000-00-00',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=MyISAM;

--
-- Table structure for table `PERSON`
--

CREATE TABLE PERSON (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  DOCUMENT_ID_NUMBER varchar(50) NOT NULL default '',
  EMISSION_LOCATION_OF_DOCUMENT_ID varchar(100) default NULL,
  EMISSION_DATE_OF_DOCUMENT_ID date default NULL,
  EXPERATION_DATE_OF_DOCUMENT_ID date default NULL,
  NAME varchar(100) default NULL,
  DATE_OF_BIRTH date default NULL,
  NAME_OF_FATHER varchar(100) default NULL,
  NAME_OF_MOTHER varchar(100) default NULL,
  NATIONALITY varchar(50) default NULL,
  PARISH_OF_BIRTH varchar(100) default NULL,
  DISTRICT_SUBDIVISION_OF_BIRTH varchar(100) default NULL,
  DISTRICT_OF_BIRTH varchar(100) default NULL,
  ADDRESS varchar(100) default NULL,
  AREA varchar(100) default NULL,
  AREA_CODE varchar(8) default NULL,
  AREA_OF_AREA_CODE varchar(100) default NULL,
  PARISH_OF_RESIDENCE varchar(100) default NULL,
  DISTRICT_SUBDIVISION_OF_RESIDENCE varchar(100) default NULL,
  DISTRICT_OF_RESIDENCE varchar(100) default NULL,
  PHONE varchar(50) default NULL,
  MOBILE varchar(50) default NULL,
  EMAIL varchar(100) default NULL,
  WEB_ADRDRESS varchar(200) default NULL,
  SOCIAL_SECURITY_NUMBER varchar(50) default NULL,
  PROFESSION varchar(100) default NULL,
  USERNAME varchar(50) NOT NULL default '',
  PASSWD varchar(40) default NULL,
  KEY_COUNTRY int(11) default NULL,
  FISCAL_CODE varchar(50) default NULL,
  TYPE_ID_DOCUMENT int(11) NOT NULL default '0',
  SEX int(11) default NULL,
  MARITAL_STATUS int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (DOCUMENT_ID_NUMBER,TYPE_ID_DOCUMENT),
  UNIQUE KEY U2 (USERNAME)
) TYPE=InnoDB;

--
-- Table structure for table `PERSON_ADVISORY`
--

CREATE TABLE PERSON_ADVISORY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PERSON int(11) NOT NULL default '0',
  KEY_ADVISORY int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_PERSON,KEY_ADVISORY)
) TYPE=InnoDB;

--
-- Table structure for table `PERSON_ROLE`
--

CREATE TABLE PERSON_ROLE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_ROLE int(11) NOT NULL default '0',
  KEY_PERSON int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) unsigned NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_ROLE,KEY_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE`
--

CREATE TABLE POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_POSSIBLE_CURRICULAR_COURSE int(11) NOT NULL default '0',
  KEY_OPTIONAL_CURRICULAR_COURSE int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_POSSIBLE_CURRICULAR_COURSE,KEY_OPTIONAL_CURRICULAR_COURSE)
) TYPE=MyISAM;

--
-- Table structure for table `PRECEDENCE`
--

CREATE TABLE PRECEDENCE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE int(11) NOT NULL default '0',
  SCOPE_TO_APPLY char(3) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  KEY PRECEDENCE_INDEX_1 (KEY_CURRICULAR_COURSE)
) TYPE=InnoDB;

--
-- Table structure for table `PRICE`
--

CREATE TABLE PRICE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  GRADUATION_TYPE int(11) NOT NULL default '0',
  DOCUMENT_TYPE int(11) NOT NULL default '0',
  DESCRIPTION varchar(200) NOT NULL default '',
  PRICE float(10,2) NOT NULL default '0.00',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY u1 (GRADUATION_TYPE,DOCUMENT_TYPE,DESCRIPTION)
) TYPE=InnoDB;

--
-- Table structure for table `PROFESSORSHIPS`
--

CREATE TABLE PROFESSORSHIPS (
  INTERNAL_CODE int(11) NOT NULL auto_increment,
  KEY_TEACHER int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  CREDITS float(11,2) default '0.00',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (INTERNAL_CODE),
  UNIQUE KEY U1 (KEY_TEACHER,KEY_EXECUTION_COURSE),
  KEY PROFESSORSHIPS_KEY_EXECUTION_COURSE (KEY_EXECUTION_COURSE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `PUBLICATION`
--

CREATE TABLE PUBLICATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL default '',
  CONFERENCE varchar(100) NOT NULL default '',
  JOURNAL varchar(100) NOT NULL default '',
  LOCATION varchar(100) NOT NULL default '',
  MONTH int(2) NOT NULL default '0',
  NUMBER int(10) NOT NULL default '0',
  PAGES varchar(10) NOT NULL default '',
  PUBLISHER varchar(100) NOT NULL default '',
  SERIES varchar(100) NOT NULL default '',
  TITLE varchar(100) NOT NULL default '',
  TRNUMBER int(5) NOT NULL default '0',
  UNIVERSITY varchar(100) NOT NULL default '',
  VOLUME int(5) NOT NULL default '0',
  YEAR int(4) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=MyISAM;

--
-- Table structure for table `PUBLICATIONS_NUMBER`
--

CREATE TABLE PUBLICATIONS_NUMBER (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  PUBLICATION_TYPE int(10) default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  NATIONAL int(10) default NULL,
  INTERNATIONAL int(10) default NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_TEACHER,PUBLICATION_TYPE)
) TYPE=InnoDB;

--
-- Table structure for table `PaisNaturalidade`
--

CREATE TABLE PaisNaturalidade (
  Cod char(2) NOT NULL default '',
  Origem varchar(30) NOT NULL default ''
) TYPE=MyISAM;

--
-- Table structure for table `QUALIFICATION`
--

CREATE TABLE QUALIFICATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PERSON int(11) NOT NULL default '0',
  MARK varchar(200) default NULL,
  SCHOOL varchar(200) default NULL,
  TITLE varchar(200) default NULL,
  DEGREE varchar(200) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  BRANCH varchar(100) default NULL,
  SPECIALIZATION_AREA varchar(200) default NULL,
  DEGREE_RECOGNITION varchar(200) default NULL,
  EQUIVALENCE_DATE date default NULL,
  EQUIVALENCE_SCHOOL varchar(200) default NULL,
  KEY_COUNTRY int(11) default NULL,
  DATE date NOT NULL default '0000-00-00',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (DATE,SCHOOL,TITLE,KEY_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `REIMBURSEMENT_GUIDE`
--

CREATE TABLE REIMBURSEMENT_GUIDE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NUMBER int(11) NOT NULL default '0',
  VALUE float(11,2) NOT NULL default '0.00',
  JUSTIFICATION text,
  KEY_GUIDE int(11) NOT NULL default '0',
  CREATION_DATE date NOT NULL default '0000-00-00',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NUMBER)
) TYPE=InnoDB;

--
-- Table structure for table `REIMBURSEMENT_GUIDE_SITUATION`
--

CREATE TABLE REIMBURSEMENT_GUIDE_SITUATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  STATE int(11) NOT NULL default '0',
  KEY_REIMBURSEMENT_GUIDE int(11) NOT NULL default '0',
  REMARKS text,
  KEY_EMPLOYEE int(11) NOT NULL default '0',
  MODIFICATION_DATE date NOT NULL default '0000-00-00',
  REIMBURSEMENT_GUIDE_STATE int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `RESPONSIBLEFOR`
--

CREATE TABLE RESPONSIBLEFOR (
  INTERNAL_CODE int(11) NOT NULL auto_increment,
  KEY_TEACHER int(11) unsigned NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (INTERNAL_CODE),
  UNIQUE KEY U1 (KEY_TEACHER,KEY_EXECUTION_COURSE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `RESTRICTION`
--

CREATE TABLE RESTRICTION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL default '',
  KEY_PRECEDENCE int(11) NOT NULL default '0',
  KEY_CURRICULAR_COURSE int(11) default NULL,
  NUMBER_OF_CURRICULAR_COURSE_DONE int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PERIOD_TO_APPLY int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  KEY RESTRICTION_INDEX_1 (KEY_PRECEDENCE)
) TYPE=InnoDB;

--
-- Table structure for table `ROLE`
--

CREATE TABLE ROLE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ROLE_TYPE int(11) NOT NULL default '0',
  PORTAL_SUB_APPLICATION varchar(100) default NULL,
  PAGE varchar(100) default NULL,
  PAGE_NAME_PROPERTY varchar(100) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (ROLE_TYPE)
) TYPE=InnoDB;

--
-- Table structure for table `ROOM`
--

CREATE TABLE ROOM (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(100) NOT NULL default '',
  BUILDING varchar(50) NOT NULL default '',
  FLOOR int(11) NOT NULL default '0',
  TYPE int(11) NOT NULL default '0',
  NORMAL_CAPACITY int(11) NOT NULL default '0',
  EXAM_CAPACITY int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME)
) TYPE=InnoDB;

--
-- Table structure for table `ROOM_OCCUPATION`
--

CREATE TABLE ROOM_OCCUPATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  START_TIME time NOT NULL default '00:00:00',
  END_TIME time NOT NULL default '00:00:00',
  WEEKDAY int(11) NOT NULL default '0',
  KEY_ROOM int(11) NOT NULL default '0',
  KEY_PERIOD int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=MyISAM;

--
-- Table structure for table `SCIENTIFIC_AREA`
--

CREATE TABLE SCIENTIFIC_AREA (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(255) NOT NULL default '',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME)
) TYPE=InnoDB;

--
-- Table structure for table `SCIENTIFIC_AREA_CURRICULAR_COURSE_GROUP`
--

CREATE TABLE SCIENTIFIC_AREA_CURRICULAR_COURSE_GROUP (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_CURRICULAR_COURSE_GROUP int(11) NOT NULL default '0',
  KEY_SCIENTIFIC_AREA int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CURRICULAR_COURSE_GROUP,KEY_SCIENTIFIC_AREA),
  KEY SCIENTIFIC_AREA_CURRICULAR_COURSE_GROUP_INDEX_2 (KEY_SCIENTIFIC_AREA),
  KEY SCIENTIFIC_AREA_CURRICULAR_COURSE_GROUP_INDEX_3 (KEY_CURRICULAR_COURSE_GROUP)
) TYPE=InnoDB;

--
-- Table structure for table `SECTION`
--

CREATE TABLE SECTION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NAME varchar(100) default NULL,
  SECTION_ORDER int(11) unsigned default NULL,
  KEY_SITE int(10) unsigned NOT NULL default '0',
  KEY_SUPERIOR_SECTION int(10) unsigned default NULL,
  LAST_MODIFIED_DATE date default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,KEY_SITE,KEY_SUPERIOR_SECTION)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB; InnoDB free: 372736 kB';

--
-- Table structure for table `SEMINARY`
--

CREATE TABLE SEMINARY (
  id_internal int(11) NOT NULL default '0',
  name varchar(255) NOT NULL default '',
  description text,
  allowed_candidacies_per_student int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  enrollment_begin_time time default NULL,
  enrollment_begin_date date default NULL,
  enrollment_end_time time default NULL,
  enrollment_end_date date default NULL,
  has_theme int(11) default NULL,
  has_case_study int(11) default NULL,
  PRIMARY KEY  (id_internal),
  UNIQUE KEY name (name)
) TYPE=InnoDB;

--
-- Table structure for table `SEMINARYCANDIDACY_SEMINARYCASESTUDY`
--

CREATE TABLE SEMINARYCANDIDACY_SEMINARYCASESTUDY (
  candidacy_id_internal int(11) NOT NULL default '0',
  casestudy_id_internal int(11) NOT NULL default '0',
  preference_order int(3) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (candidacy_id_internal,casestudy_id_internal),
  UNIQUE KEY candidacy_id_internal (candidacy_id_internal,preference_order)
) TYPE=InnoDB;

--
-- Table structure for table `SEMINARY_CANDIDACY`
--

CREATE TABLE SEMINARY_CANDIDACY (
  id_internal int(11) NOT NULL auto_increment,
  student_id_internal int(11) NOT NULL default '0',
  curricular_course_id_internal int(11) NOT NULL default '0',
  seminary_id_internal int(11) NOT NULL default '0',
  seminary_theme_id_internal int(11) default NULL,
  seminary_modality_id_internal int(11) NOT NULL default '0',
  motivation text NOT NULL,
  APPROVED tinyint(1) default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (id_internal),
  UNIQUE KEY U1 (seminary_id_internal,student_id_internal)
) TYPE=InnoDB;

--
-- Table structure for table `SEMINARY_CASESTUDY`
--

CREATE TABLE SEMINARY_CASESTUDY (
  id_internal int(11) NOT NULL default '0',
  name varchar(255) NOT NULL default '',
  description text,
  code varchar(32) NOT NULL default '',
  seminary_theme_Id_Internal int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (id_internal),
  UNIQUE KEY name (name,seminary_theme_Id_Internal),
  UNIQUE KEY code (code,seminary_theme_Id_Internal)
) TYPE=InnoDB;

--
-- Table structure for table `SEMINARY_CURRICULARCOURSE`
--

CREATE TABLE SEMINARY_CURRICULARCOURSE (
  id_internal int(11) NOT NULL default '0',
  seminary_id_internal int(11) NOT NULL default '0',
  curricular_course_id_internal int(11) NOT NULL default '0',
  modality_id_internal int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (id_internal),
  UNIQUE KEY seminary_id_internal (seminary_id_internal,curricular_course_id_internal,modality_id_internal)
) TYPE=InnoDB;

--
-- Table structure for table `SEMINARY_MODALITY`
--

CREATE TABLE SEMINARY_MODALITY (
  id_internal int(11) NOT NULL default '0',
  name varchar(255) NOT NULL default '',
  description text,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (id_internal)
) TYPE=InnoDB;

--
-- Table structure for table `SEMINARY_THEME`
--

CREATE TABLE SEMINARY_THEME (
  id_internal int(11) NOT NULL default '0',
  name varchar(255) NOT NULL default '',
  short_name text,
  description text,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (id_internal),
  UNIQUE KEY name (name)
) TYPE=InnoDB;

--
-- Table structure for table `SERVICE_PROVIDER_REGIME`
--

CREATE TABLE SERVICE_PROVIDER_REGIME (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  PROVIDER_REGIME_TYPE int(10) default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_TEACHER)
) TYPE=InnoDB;

--
-- Table structure for table `SHIFT`
--

CREATE TABLE SHIFT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  NAME varchar(50) NOT NULL default '',
  KEY_EXECUTION_COURSE int(11) NOT NULL default '0',
  TYPE int(11) NOT NULL default '0',
  CAPACITY int(11) NOT NULL default '0',
  AVAILLABILITY_FINAL int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,KEY_EXECUTION_COURSE),
  KEY SHIFT_KEY_EXECUTION_COURSE (KEY_EXECUTION_COURSE)
) TYPE=InnoDB;

--
-- Table structure for table `SHIFT_LESSON`
--

CREATE TABLE SHIFT_LESSON (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_SHIFT int(11) unsigned NOT NULL default '0',
  KEY_LESSON int(11) unsigned NOT NULL default '0',
  ack_opt_lock int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_SHIFT,KEY_LESSON),
  KEY SHIFT_LESSON_KEY_SHIFT (KEY_SHIFT),
  KEY SHIFT_LESSON_KEY_LESSON (KEY_LESSON)
) TYPE=InnoDB;

--
-- Table structure for table `SHIFT_PROFESSORSHIP`
--

CREATE TABLE SHIFT_PROFESSORSHIP (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PROFESSOR_SHIP int(11) NOT NULL default '0',
  KEY_SHIFT int(11) NOT NULL default '0',
  PERCENTAGE float NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_PROFESSOR_SHIP,KEY_SHIFT)
) TYPE=InnoDB;

--
-- Table structure for table `SHIFT_STUDENT`
--

CREATE TABLE SHIFT_STUDENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_SHIFT int(11) NOT NULL default '0',
  KEY_STUDENT int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_SHIFT,KEY_STUDENT)
) TYPE=InnoDB;

--
-- Table structure for table `SITE`
--

CREATE TABLE SITE (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  KEY_INITIAL_SECTION int(11) default NULL,
  ALTERNATIVE_SITE varchar(255) default NULL,
  MAIL varchar(50) default NULL,
  INITIAL_STATEMENT text,
  INTRODUCTION text,
  STYLE varchar(255) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY ID_INTERNAL (ID_INTERNAL,KEY_EXECUTION_COURSE),
  KEY SITE_KEY_EXECUTION_COURSE (KEY_EXECUTION_COURSE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `STUDENT`
--

CREATE TABLE STUDENT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NUMBER smallint(10) unsigned NOT NULL default '0',
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  STATE int(11) unsigned NOT NULL default '1',
  DEGREE_TYPE int(11) NOT NULL default '0',
  KEY_STUDENT_KIND int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NUMBER,DEGREE_TYPE),
  UNIQUE KEY U2 (DEGREE_TYPE,KEY_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `STUDENT_COURSE_REPORT`
--

CREATE TABLE STUDENT_COURSE_REPORT (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_CURRICULAR_COURSE int(11) unsigned NOT NULL default '0',
  STUDENT_REPORT text,
  STRONG_POINTS text,
  WEAK_POINTS text,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_CURRICULAR_COURSE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `STUDENT_CURRICULAR_PLAN`
--

CREATE TABLE STUDENT_CURRICULAR_PLAN (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT int(11) NOT NULL default '0',
  KEY_DEGREE_CURRICULAR_PLAN int(11) NOT NULL default '0',
  CURRENT_STATE int(11) NOT NULL default '0',
  START_DATE date NOT NULL default '0000-00-00',
  KEY_SPECIALIZATION_BRANCH int(11) default NULL,
  SPECIALIZATION int(11) default NULL,
  GIVEN_CREDITS float(10,2) default NULL,
  COMPLETED_COURSES int(11) default '0',
  ENROLLED_COURSES int(11) default '0',
  CLASSIFICATION float(10,2) default '0.00',
  OBSERVATIONS text,
  KEY_EMPLOYEE int(11) unsigned default NULL,
  WHEN_ALTER timestamp(14) NOT NULL,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_SECUNDARY_BRANCH int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT,KEY_DEGREE_CURRICULAR_PLAN,CURRENT_STATE),
  KEY STUDENT_CURRICULAR_PLAN_KEY_STUDENT (KEY_STUDENT),
  KEY STUDENT_CURRICULAR_PLAN_KEY_DEGREE_CURRICULAR_PLAN (KEY_DEGREE_CURRICULAR_PLAN)
) TYPE=InnoDB;

--
-- Table structure for table `STUDENT_GROUP`
--

CREATE TABLE STUDENT_GROUP (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  GROUP_NUMBER int(11) unsigned NOT NULL default '0',
  KEY_SHIFT int(11) unsigned NOT NULL default '0',
  KEY_GROUP_PROPERTIES int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY ID_INTERNAL (GROUP_NUMBER,KEY_GROUP_PROPERTIES)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `STUDENT_GROUP_ATTEND`
--

CREATE TABLE STUDENT_GROUP_ATTEND (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_ATTEND int(11) unsigned NOT NULL default '0',
  KEY_STUDENT_GROUP int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY ID_INTERNAL (KEY_ATTEND,KEY_STUDENT_GROUP)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `STUDENT_GROUP_INFO`
--

CREATE TABLE STUDENT_GROUP_INFO (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  STUDENT_TYPE int(11) NOT NULL default '0',
  MIN_COURSES_TO_ENROL int(11) NOT NULL default '0',
  MAX_COURSES_TO_ENROL int(11) NOT NULL default '0',
  MAX_NAC_TO_ENROL int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (STUDENT_TYPE)
) TYPE=InnoDB;

--
-- Table structure for table `STUDENT_KIND`
--

CREATE TABLE STUDENT_KIND (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  STUDENT_TYPE int(11) NOT NULL default '0',
  MIN_COURSES_TO_ENROL int(11) NOT NULL default '0',
  MAX_COURSES_TO_ENROL int(11) NOT NULL default '0',
  MAX_NAC_TO_ENROL int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (STUDENT_TYPE)
) TYPE=InnoDB;

--
-- Table structure for table `STUDENT_TEST_LOG`
--

CREATE TABLE STUDENT_TEST_LOG (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_DISTRIBUTED_TEST int(11) unsigned NOT NULL default '0',
  DATE timestamp(14) NOT NULL,
  EVENT text,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  KEY INDEX1 (KEY_STUDENT,KEY_DISTRIBUTED_TEST,DATE)
) TYPE=InnoDB;

--
-- Table structure for table `STUDENT_TEST_QUESTION`
--

CREATE TABLE STUDENT_TEST_QUESTION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_DISTRIBUTED_TEST int(11) unsigned NOT NULL default '0',
  KEY_XML_DOCUMENT int(11) unsigned NOT NULL default '0',
  TEST_QUESTION_ORDER int(2) NOT NULL default '1',
  TEST_QUESTION_VALUE int(2) NOT NULL default '0',
  TEST_QUESTION_MARK double NOT NULL default '0',
  RESPONSE int(11) NOT NULL default '0',
  OPTION_SHUFFLE text,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT,KEY_DISTRIBUTED_TEST,KEY_XML_DOCUMENT)
) TYPE=InnoDB;

--
-- Table structure for table `SUMMARY`
--

CREATE TABLE SUMMARY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_EXECUTION_COURSE int(11) unsigned NOT NULL default '0',
  TITLE text,
  SUMMARY_DATE date default NULL,
  SUMMARY_HOUR time default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  SUMMARY_TEXT text,
  SUMMARY_TYPE int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `SUPPORT_LESSON`
--

CREATE TABLE SUPPORT_LESSON (
  ID_INTERNAL int(50) NOT NULL auto_increment,
  WEEKDAY varchar(50) NOT NULL default '',
  START_TIME time NOT NULL default '00:00:00',
  END_TIME time NOT NULL default '00:00:00',
  PLACE varchar(50) NOT NULL default '',
  KEY_PROFESSORSHIP int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_PROFESSORSHIP,WEEKDAY,START_TIME,END_TIME)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `TEACHER`
--

CREATE TABLE TEACHER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  TEACHER_NUMBER int(10) unsigned default NULL,
  KEY_PERSON int(11) unsigned NOT NULL default '0',
  KEY_CATEGORY int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (TEACHER_NUMBER,KEY_PERSON)
) TYPE=InnoDB;

--
-- Table structure for table `TEACHER_DEGREE_FINAL_PROJECT_STUDENT`
--

CREATE TABLE TEACHER_DEGREE_FINAL_PROJECT_STUDENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  PERCENTAGE float NOT NULL default '100',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT,KEY_TEACHER,KEY_EXECUTION_PERIOD)
) TYPE=MyISAM;

--
-- Table structure for table `TEACHER_INSTITUTION_WORK_TIME`
--

CREATE TABLE TEACHER_INSTITUTION_WORK_TIME (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_TEACHER int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  WEEKDAY int(11) NOT NULL default '0',
  START_TIME time default NULL,
  END_TIME time default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_TEACHER,KEY_EXECUTION_PERIOD,WEEKDAY,START_TIME)
) TYPE=MyISAM;

--
-- Table structure for table `TESTS`
--

CREATE TABLE TESTS (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  TITLE text NOT NULL,
  INFORMATION text,
  NUMBER_OF_QUESTIONS int(2) NOT NULL default '0',
  CREATION_DATE timestamp(14) NOT NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_TEST_SCOPE int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `TEST_QUESTION`
--

CREATE TABLE TEST_QUESTION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  TEST_QUESTION_ORDER int(2) NOT NULL default '1',
  TEST_QUESTION_VALUE int(2) NOT NULL default '0',
  KEY_XML_DOCUMENT int(11) unsigned NOT NULL default '0',
  KEY_TEST int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_XML_DOCUMENT,KEY_TEST)
) TYPE=InnoDB;

--
-- Table structure for table `TEST_SCOPE`
--

CREATE TABLE TEST_SCOPE (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL default '',
  KEY_CLASS int(11) unsigned default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `TUTOR_SHIP`
--

CREATE TABLE TUTOR_SHIP (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_TEACHER int(11) NOT NULL default '0',
  KEY_STUDENT int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT)
) TYPE=InnoDB;

--
-- Table structure for table `TopWords`
--

CREATE TABLE TopWords (
  Palavra varchar(50) NOT NULL default ''
) TYPE=MyISAM;

--
-- Table structure for table `Turmas`
--

CREATE TABLE Turmas (
  Sigla varchar(255) default NULL,
  Titulo varchar(255) default NULL,
  Epoca_Inicio double default NULL,
  Epoca_Fim varchar(255) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  UNIQUE KEY PrimaryKey (Sigla)
) TYPE=InnoDB;

--
-- Table structure for table `UNIVERSITY`
--

CREATE TABLE UNIVERSITY (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CODE varchar(10) NOT NULL default '',
  NAME varchar(150) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (CODE,NAME)
) TYPE=InnoDB;

--
-- Table structure for table `WEBSITE`
--

CREATE TABLE WEBSITE (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NAME varchar(100) NOT NULL default '',
  MAIL varchar(50) default NULL,
  STYLE varchar(255) default NULL,
  CLASS_NAME varchar(250) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `WEBSITE_ITEM`
--

CREATE TABLE WEBSITE_ITEM (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  TITLE varchar(100) NOT NULL default '',
  MAIN_ENTRY_TEXT text NOT NULL,
  EXCERPT text,
  PUBLISHED int(11) NOT NULL default '0',
  CREATION_DATE datetime NOT NULL default '0000-00-00 00:00:00',
  KEY_EDITOR int(11) NOT NULL default '0',
  AUTHOR_NAME varchar(100) NOT NULL default '',
  AUTHOR_EMAIL varchar(100) NOT NULL default '',
  KEYWORDS text NOT NULL,
  ONLINE_BEGIN_DAY date default NULL,
  ONLINE_END_DAY date default NULL,
  ITEM_BEGIN_DAY date default NULL,
  ITEM_END_DAY date default NULL,
  KEY_WEBSITE_SECTION int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `WEBSITE_SECTION`
--

CREATE TABLE WEBSITE_SECTION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NAME varchar(100) NOT NULL default '',
  SIZE int(11) unsigned NOT NULL default '0',
  SORTING_ORDER enum('ascendent','descendent') NOT NULL default 'ascendent',
  WHAT_TO_SORT enum('CREATION_DATE','ITEM_BEGIN_DAY','ITEM_END_DAY') NOT NULL default 'CREATION_DATE',
  EXCERPT_SIZE int(11) default NULL,
  KEY_WEBSITE int(11) unsigned NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (NAME,KEY_WEBSITE)
) TYPE=InnoDB COMMENT='InnoDB free: 372736 kB';

--
-- Table structure for table `WEEKLY_OCUPATION`
--

CREATE TABLE WEEKLY_OCUPATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  RESEARCH int(10) default NULL,
  MANAGEMENT int(10) default NULL,
  LECTURE int(10) default NULL,
  SUPPORT int(10) default NULL,
  OTHER int(10) default NULL,
  LAST_MODIFICATION_DATE timestamp(14) NOT NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_TEACHER)
) TYPE=InnoDB;

--
-- Table structure for table `WORK_LOCATION`
--

CREATE TABLE WORK_LOCATION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NAME varchar(255) NOT NULL default '',
  ACK_OPT_LOCK int(11) unsigned NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY NAME (NAME)
) TYPE=InnoDB;

--
-- Table structure for table `WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE`
--

CREATE TABLE WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_WRITTEN_EVALUATION int(11) NOT NULL default '0',
  KEY_CURRICULAR_COURSE_SCOPE int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_WRITTEN_EVALUATION,KEY_CURRICULAR_COURSE_SCOPE)
) TYPE=MyISAM;

--
-- Table structure for table `WRITTEN_EVALUATION_ROOM_OCCUPATION`
--

CREATE TABLE WRITTEN_EVALUATION_ROOM_OCCUPATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_WRITTEN_EVALUATION int(11) NOT NULL default '0',
  KEY_ROOM_OCCUPATION int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_ROOM_OCCUPATION,KEY_WRITTEN_EVALUATION)
) TYPE=MyISAM;

--
-- Table structure for table `XML_DOCUMENTS`
--

CREATE TABLE XML_DOCUMENTS (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  XML_FILE text NOT NULL,
  XML_FILE_NAME varchar(100) NOT NULL default '',
  KEY_METADATA int(11) unsigned NOT NULL default '0',
  VISIBILITY tinyint(1) NOT NULL default '1',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_METADATA,XML_FILE_NAME)
) TYPE=InnoDB;

--
-- Table structure for table `almeida_aluno`
--

CREATE TABLE almeida_aluno (
  numero int(11) NOT NULL default '0',
  nome varchar(200) default NULL,
  nascimento date default NULL,
  bi varchar(15) default NULL,
  curso int(11) default NULL,
  ramo int(11) default NULL,
  sexo char(1) default NULL,
  nacionalidade varchar(11) default NULL,
  freguesia varchar(50) default NULL,
  concelho varchar(50) default NULL,
  distrito varchar(50) default NULL,
  nomePai varchar(50) default NULL,
  nomeMae varchar(50) default NULL,
  morada varchar(50) default NULL,
  localidadeMorada varchar(50) default NULL,
  cp varchar(50) default NULL,
  localidadeCP varchar(50) default NULL,
  telefone varchar(50) default NULL,
  email varchar(50) default NULL,
  PRIMARY KEY  (numero)
) TYPE=MyISAM;

--
-- Table structure for table `almeida_coddisc`
--

CREATE TABLE almeida_coddisc (
  codint int(11) NOT NULL auto_increment,
  coddis varchar(200) default NULL,
  nomedis varchar(200) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codint)
) TYPE=InnoDB;

--
-- Table structure for table `almeida_curram`
--

CREATE TABLE almeida_curram (
  codInt int(11) NOT NULL auto_increment,
  codCur int(11) default NULL,
  codRam int(11) default NULL,
  codOrien int(11) default NULL,
  descri varchar(200) default NULL,
  PRIMARY KEY  (codInt)
) TYPE=InnoDB;

--
-- Table structure for table `almeida_disc`
--

CREATE TABLE almeida_disc (
  codInt int(11) NOT NULL auto_increment,
  codCur int(11) default NULL,
  codRam int(11) default NULL,
  anoDis int(11) default NULL,
  semDis int(11) default NULL,
  codDis varchar(200) default NULL,
  tipo int(11) default NULL,
  teo double(11,2) default NULL,
  pra double(11,2) default NULL,
  lab double(11,2) default NULL,
  teoPra double(11,2) default NULL,
  nomeDis varchar(200) default NULL,
  PRIMARY KEY  (codInt)
) TYPE=InnoDB;

--
-- Table structure for table `almeida_inscricoes`
--

CREATE TABLE almeida_inscricoes (
  codInt int(11) NOT NULL auto_increment,
  numero int(11) default NULL,
  ano int(11) default NULL,
  semestre int(11) default NULL,
  codDis varchar(200) default NULL,
  anoInscricao int(11) default NULL,
  curso int(11) default NULL,
  ramo varchar(200) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codInt)
) TYPE=InnoDB;

--
-- Table structure for table `almeida_nacionalidade`
--

CREATE TABLE almeida_nacionalidade (
  numero int(11) NOT NULL default '0',
  nome varchar(200) default NULL,
  PRIMARY KEY  (numero)
) TYPE=MyISAM;

--
-- Table structure for table `ass_CARGO`
--

CREATE TABLE ass_CARGO (
  chaveCargo int(11) NOT NULL default '0',
  cargo enum('ALUNO','FUNCDOCENTE','FUNCNAODOCENTE','ADMINISTRATOR','GestaoAssiduidade','ConsultaAssiduidade') NOT NULL default 'ALUNO',
  PRIMARY KEY  (chaveCargo)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_CARGO_URL`
--

CREATE TABLE ass_CARGO_URL (
  chaveCargo int(11) NOT NULL default '0',
  chaveUrl int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveCargo,chaveUrl)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_CARTAO`
--

CREATE TABLE ass_CARTAO (
  codigoInterno int(11) NOT NULL auto_increment,
  numCartao int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  dataInicio datetime NOT NULL default '0000-00-00 00:00:00',
  dataFim datetime default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  estado varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_CENTRO_CUSTO`
--

CREATE TABLE ass_CENTRO_CUSTO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(50) NOT NULL default '',
  departamento varchar(50) default NULL,
  seccao1 varchar(50) default NULL,
  seccao2 varchar(50) default NULL,
  ACK_OPT_LOCK int(11) unsigned NOT NULL default '1',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY sigla (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_FERIADO`
--

CREATE TABLE ass_FERIADO (
  codigoInterno int(11) NOT NULL auto_increment,
  tipoFeriado varchar(15) NOT NULL default '',
  descricao varchar(50) default NULL,
  data date default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (tipoFeriado,data)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_FERIAS`
--

CREATE TABLE ass_FERIAS (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  anoCorrente int(11) NOT NULL default '0',
  diasNormais int(11) NOT NULL default '0',
  diasEpocaBaixa int(11) NOT NULL default '0',
  diasHorasExtras int(11) NOT NULL default '0',
  diasAntiguidade int(11) NOT NULL default '0',
  diasMeioDia int(11) NOT NULL default '0',
  diasDispensaServico int(11) NOT NULL default '0',
  diasTolerancia int(11) NOT NULL default '0',
  diasTransferidos int(11) NOT NULL default '0',
  diasTransHorasExtras int(11) NOT NULL default '0',
  diasTransAntiguidade int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_FUNCIONARIO`
--

CREATE TABLE ass_FUNCIONARIO (
  codigoInterno int(11) NOT NULL auto_increment,
  chavePessoa int(11) NOT NULL default '0',
  numeroMecanografico int(11) NOT NULL default '0',
  chaveHorarioActual int(11) NOT NULL default '0',
  antiguidade date NOT NULL default '0000-00-00',
  ACTIVE tinyint(1) NOT NULL default '1',
  ACK_OPT_LOCK int(11) unsigned NOT NULL default '1',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (numeroMecanografico),
  UNIQUE KEY u2 (chavePessoa)
) TYPE=InnoDB;

--
-- Table structure for table `ass_FUNCIONARIO_HISTORICO`
--

CREATE TABLE ass_FUNCIONARIO_HISTORICO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  chaveFuncResponsavel int(11) default '0',
  chaveCCLocalTrabalho int(11) default '0',
  chaveCCCorrespondencia int(11) default '0',
  chaveCCVencimento int(11) default '0',
  calendario varchar(50) default '',
  chaveStatus int(11) default '0',
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  ACK_OPT_LOCK int(11) unsigned NOT NULL default '1',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;

--
-- Table structure for table `ass_FUNC_NAO_DOCENTE`
--

CREATE TABLE ass_FUNC_NAO_DOCENTE (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) unsigned NOT NULL default '1',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (chaveFuncionario)
) TYPE=InnoDB;

--
-- Table structure for table `ass_HORARIO`
--

CREATE TABLE ass_HORARIO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveHorarioTipo int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  sigla varchar(50) default NULL,
  modalidade varchar(50) default NULL,
  duracaoSemanal float default NULL,
  inicioPF1 datetime default NULL,
  fimPF1 datetime default NULL,
  inicioPF2 datetime default NULL,
  fimPF2 datetime default NULL,
  inicioHN1 datetime default NULL,
  fimHN1 datetime default NULL,
  inicioHN2 datetime default NULL,
  fimHN2 datetime default NULL,
  inicioRefeicao datetime default NULL,
  fimRefeicao datetime default NULL,
  descontoObrigatorio time default NULL,
  descontoMinimo time default NULL,
  inicioExpediente datetime default NULL,
  fimExpediente datetime default NULL,
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  numDias int(10) default NULL,
  posicao int(10) default NULL,
  trabalhoConsecutivo time default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';

--
-- Table structure for table `ass_HORARIOEXCEPCAO_REGIME`
--

CREATE TABLE ass_HORARIOEXCEPCAO_REGIME (
  chaveHorario int(11) NOT NULL default '0',
  chaveRegime int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveHorario,chaveRegime)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_HORARIOTIPO_REGIME`
--

CREATE TABLE ass_HORARIOTIPO_REGIME (
  chaveHorarioTipo int(11) NOT NULL default '0',
  chaveRegime int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveHorarioTipo,chaveRegime)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_HORARIO_EXCEPCAO`
--

CREATE TABLE ass_HORARIO_EXCEPCAO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveHorarioTipo int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  sigla varchar(50) default NULL,
  modalidade varchar(50) default NULL,
  duracaoSemanal float default NULL,
  inicioPF1 datetime default NULL,
  fimPF1 datetime default NULL,
  inicioPF2 datetime default NULL,
  fimPF2 datetime default NULL,
  inicioHN1 datetime default NULL,
  fimHN1 datetime default NULL,
  inicioHN2 datetime default NULL,
  fimHN2 datetime default NULL,
  inicioRefeicao datetime default NULL,
  fimRefeicao datetime default NULL,
  descontoObrigatorio time default NULL,
  descontoMinimo time default NULL,
  inicioExpediente datetime default NULL,
  fimExpediente datetime default NULL,
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  numDias int(10) default NULL,
  posicao int(10) default NULL,
  trabalhoConsecutivo time default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';

--
-- Table structure for table `ass_HORARIO_REGIME`
--

CREATE TABLE ass_HORARIO_REGIME (
  chaveHorario int(11) NOT NULL default '0',
  chaveRegime int(11) NOT NULL default '0',
  PRIMARY KEY  (chaveHorario,chaveRegime)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_HORARIO_TIPO`
--

CREATE TABLE ass_HORARIO_TIPO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(50) NOT NULL default '',
  modalidade varchar(50) NOT NULL default '',
  duracaoSemanal float NOT NULL default '0',
  inicioPF1 datetime default NULL,
  fimPF1 datetime default NULL,
  inicioPF2 datetime default NULL,
  fimPF2 datetime default NULL,
  inicioHN1 datetime NOT NULL default '0000-00-00 00:00:00',
  fimHN1 datetime NOT NULL default '0000-00-00 00:00:00',
  inicioHN2 datetime default NULL,
  fimHN2 datetime default NULL,
  inicioRefeicao datetime default NULL,
  fimRefeicao datetime default NULL,
  descontoObrigatorio time default NULL,
  descontoMinimo time default NULL,
  inicioExpediente datetime default NULL,
  fimExpediente datetime default NULL,
  trabalhoConsecutivo time default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';

--
-- Table structure for table `ass_JUSTIFICACAO`
--

CREATE TABLE ass_JUSTIFICACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveParamJustificacao int(11) NOT NULL default '0',
  chaveFuncionario int(11) NOT NULL default '0',
  diaInicio date NOT NULL default '0000-00-00',
  horaInicio time default NULL,
  diaFim date default NULL,
  horaFim time default NULL,
  observacao varchar(50) default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  KEY indexFuncionario (chaveFuncionario),
  KEY indexDatas (diaInicio,diaFim)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_MARCACAO_PONTO`
--

CREATE TABLE ass_MARCACAO_PONTO (
  codigoInterno int(11) NOT NULL auto_increment,
  unidade int(11) default NULL,
  siglaUnidade varchar(50) default NULL,
  dataMarcacao datetime default NULL,
  numCartao int(11) default NULL,
  numFuncionario int(11) default NULL,
  estado varchar(50) default NULL,
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_MODALIDADE`
--

CREATE TABLE ass_MODALIDADE (
  codigoInterno int(11) NOT NULL auto_increment,
  designacao varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (designacao)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_PARAM_FERIAS`
--

CREATE TABLE ass_PARAM_FERIAS (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(10) NOT NULL default '',
  designacao varchar(75) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_PARAM_JUSTIFICACAO`
--

CREATE TABLE ass_PARAM_JUSTIFICACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(15) NOT NULL default '',
  descricao varchar(50) NOT NULL default '',
  tipo varchar(4) NOT NULL default '',
  tipoDias char(1) NOT NULL default '',
  grupo varchar(10) default NULL,
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB; InnoDB free:';

--
-- Table structure for table `ass_PARAM_REGULARIZACAO`
--

CREATE TABLE ass_PARAM_REGULARIZACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(7) NOT NULL default '',
  descricao varchar(50) NOT NULL default '',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (sigla)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_PERIODO_FERIAS`
--

CREATE TABLE ass_PERIODO_FERIAS (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveFuncionario int(11) NOT NULL default '0',
  dataInicio date NOT NULL default '0000-00-00',
  dataFim date default NULL,
  numDiasUteis int(11) NOT NULL default '0',
  tipoFerias int(11) NOT NULL default '1',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_PESSOA_CARGO`
--

CREATE TABLE ass_PESSOA_CARGO (
  chavePessoa int(11) NOT NULL default '0',
  chaveCargo int(11) NOT NULL default '0',
  PRIMARY KEY  (chavePessoa,chaveCargo)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_REGIME`
--

CREATE TABLE ass_REGIME (
  codigoInterno int(11) NOT NULL auto_increment,
  designacao varchar(50) NOT NULL default '',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (designacao)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_REGULARIZACAO_MARCACAO`
--

CREATE TABLE ass_REGULARIZACAO_MARCACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  chaveMarcacaoPonto int(11) NOT NULL default '0',
  chaveParamRegularizacao varchar(50) NOT NULL default '',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (chaveMarcacaoPonto)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_STATUS`
--

CREATE TABLE ass_STATUS (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(10) NOT NULL default '',
  designacao varchar(50) NOT NULL default '',
  estado enum('activo','inactivo','pendente') NOT NULL default 'activo',
  assiduidade enum('true','false') NOT NULL default 'true',
  quem int(11) NOT NULL default '0',
  quando datetime NOT NULL default '0000-00-00 00:00:00',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (designacao)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_UNIDADE_MARCACAO`
--

CREATE TABLE ass_UNIDADE_MARCACAO (
  codigoInterno int(11) NOT NULL auto_increment,
  sigla varchar(20) NOT NULL default '',
  descricao varchar(50) NOT NULL default '',
  id int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `ass_URL`
--

CREATE TABLE ass_URL (
  chaveUrl int(11) NOT NULL default '0',
  url varchar(50) NOT NULL default '',
  PRIMARY KEY  (chaveUrl)
) TYPE=InnoDB COMMENT='InnoDB free: 378880 kB; InnoDB free: 378880 kB';

--
-- Table structure for table `aux_smn_students`
--

CREATE TABLE aux_smn_students (
  number int(11) default NULL,
  degree varchar(20) default NULL,
  key_modality int(11) default NULL,
  key_theme int(11) default NULL
) TYPE=InnoDB;

--
-- Table structure for table `branch_20032004`
--

CREATE TABLE branch_20032004 (
  ID_INTERNAL int(11) NOT NULL default '0',
  BRANCH_CODE char(50) NOT NULL default '',
  BRANCH_NAME char(255) NOT NULL default '',
  KEY_DEGREE_CURRICULAR_PLAN int(11) NOT NULL default '0'
) TYPE=InnoDB;

--
-- Table structure for table `concelhoExpansao`
--

CREATE TABLE concelhoExpansao (
  descr1 varchar(50) default NULL,
  descr2 varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `conversaoCidadePais`
--

CREATE TABLE conversaoCidadePais (
  descr1 varchar(50) default NULL,
  descr2 varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `conversaoLocalConcelho`
--

CREATE TABLE conversaoLocalConcelho (
  localidade varchar(50) default NULL,
  concelho varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `conversaoLocalDistrito`
--

CREATE TABLE conversaoLocalDistrito (
  localidade varchar(50) default NULL,
  distrito varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `crd_CREDITS`
--

CREATE TABLE crd_CREDITS (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  SUPPORT_LESSONS float default NULL,
  INSTITUTION_WORK_TIME float default NULL,
  LESSONS float default NULL,
  DEGREE_FINAL_PROJECT_STUDENTS float default NULL,
  OTHER_TYPE_CREDITS float default NULL,
  CONTAINS_MANAGEMENT_POSITIONS tinyint(1) default NULL,
  CONTAINS_SERVICE_EXEMPTIONS_SITUATIONS tinyint(1) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_EXECUTION_PERIOD,KEY_TEACHER)
) TYPE=InnoDB;

--
-- Table structure for table `crd_CREDITS_MANAGER_DEPARTMENT`
--

CREATE TABLE crd_CREDITS_MANAGER_DEPARTMENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PERSON int(11) NOT NULL default '0',
  KEY_DEPARTMENT int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_PERSON,KEY_DEPARTMENT)
) TYPE=InnoDB;

--
-- Table structure for table `crd_MANAGEMENT_POSITION_CREDIT_LINE`
--

CREATE TABLE crd_MANAGEMENT_POSITION_CREDIT_LINE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  POSITION text NOT NULL,
  START_DATE date NOT NULL default '0000-00-00',
  END_DATE date NOT NULL default '0000-00-00',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `crd_OTHER_TYPE_CREDIT_LINE`
--

CREATE TABLE crd_OTHER_TYPE_CREDIT_LINE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  CREDITS float NOT NULL default '0',
  REASON text NOT NULL,
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `crd_SERVICE_EXEMPTION_CREDIT_LINE`
--

CREATE TABLE crd_SERVICE_EXEMPTION_CREDIT_LINE (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  KEY_TEACHER int(11) NOT NULL default '0',
  TYPE int(11) NOT NULL default '0',
  START_DATE date NOT NULL default '0000-00-00',
  END_DATE date NOT NULL default '0000-00-00',
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;

--
-- Table structure for table `crd_TEACHER_DEGREE_FINAL_PROJECT_STUDENT`
--

CREATE TABLE crd_TEACHER_DEGREE_FINAL_PROJECT_STUDENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_STUDENT int(11) NOT NULL default '0',
  KEY_TEACHER int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  PERCENTAGE float NOT NULL default '100',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_STUDENT,KEY_TEACHER,KEY_EXECUTION_PERIOD)
) TYPE=InnoDB;

--
-- Table structure for table `crd_TEACHER_INSTITUTION_WORK_TIME`
--

CREATE TABLE crd_TEACHER_INSTITUTION_WORK_TIME (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_TEACHER int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0',
  WEEKDAY int(11) NOT NULL default '0',
  START_TIME time default NULL,
  END_TIME time default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_TEACHER,KEY_EXECUTION_PERIOD,WEEKDAY,START_TIME)
) TYPE=InnoDB;

--
-- Table structure for table `folha2`
--

CREATE TABLE folha2 (
  ID int(10) unsigned NOT NULL auto_increment,
  N int(11) default NULL,
  cod_curso int(11) default NULL,
  situacao varchar(255) default NULL,
  n_aluno int(11) default NULL,
  ano int(11) default NULL,
  n_contribuinte int(11) default NULL,
  tipo_aluno varchar(255) default NULL,
  data varchar(10) default NULL,
  candidatura_euro varchar(255) default NULL,
  inscricao_euro varchar(255) default NULL,
  propinas_inscr_euro varchar(255) default NULL,
  taxa_inscr_euro varchar(255) default NULL,
  emolumento_euro varchar(255) default NULL,
  selo_fiscal_euro varchar(255) default NULL,
  seguro_euro varchar(255) default NULL,
  outros_euro varchar(255) default NULL,
  total_euro varchar(255) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `folha3`
--

CREATE TABLE folha3 (
  ID int(10) unsigned NOT NULL auto_increment,
  n_contribuinte int(11) default NULL,
  nome_contr varchar(255) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `folha4`
--

CREATE TABLE folha4 (
  ID int(10) unsigned NOT NULL auto_increment,
  codigo_mestrado int(11) default NULL,
  nome_mestrado varchar(255) default NULL,
  cod_gradu int(11) default NULL,
  codigo_depart int(11) default NULL,
  tipo_media varchar(255) default NULL,
  internal_id int(11) unsigned default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `freguesiaExpansao`
--

CREATE TABLE freguesiaExpansao (
  descr1 varchar(50) default NULL,
  descr2 varchar(50) default NULL
) TYPE=MyISAM;

--
-- Table structure for table `guide_aux`
--

CREATE TABLE guide_aux (
  internal_id int(10) unsigned NOT NULL auto_increment,
  id int(10) unsigned NOT NULL default '0',
  n int(11) default '0',
  cod_curso int(11) default '0',
  situacao varchar(255) default '0',
  n_aluno int(11) default '0',
  ano int(11) default '0',
  n_contribuinte varchar(255) default NULL,
  tipo_aluno varchar(255) default '0',
  data datetime default NULL,
  total_euro varchar(255) default NULL,
  item float(11,2) default '0.00',
  document_type int(11) default NULL,
  PRIMARY KEY  (internal_id)
) TYPE=InnoDB;

--
-- Table structure for table `ler126`
--

CREATE TABLE ler126 (
  ID_INTERNAL int(11) unsigned NOT NULL default '0',
  KEY_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_DISTRIBUTED_TEST int(11) unsigned NOT NULL default '0',
  DATE timestamp(14) NOT NULL,
  EVENT text
) TYPE=InnoDB;

--
-- Table structure for table `lessons_update`
--

CREATE TABLE lessons_update (
  ID_INTERNAL int(11) NOT NULL default '0',
  WEEKDAY int(11) NOT NULL default '0',
  START_TIME time NOT NULL default '00:00:00',
  END_TIME time NOT NULL default '00:00:00',
  KEY_ROOM int(11) NOT NULL default '0',
  KEY_EXECUTION_COURSE int(11) NOT NULL default '0',
  TYPE int(11) NOT NULL default '0',
  KEY_EXECUTION_PERIOD int(11) NOT NULL default '0'
) TYPE=InnoDB;

--
-- Table structure for table `mw_ALUNO_temp`
--

CREATE TABLE mw_ALUNO_temp (
  documentIdNumber varchar(250) NOT NULL default '',
  number int(50) default NULL,
  degreeCode int(20) default NULL,
  branchCode int(30) default NULL,
  year int(30) default NULL,
  ISTUniversity varchar(30) default NULL,
  worker varchar(30) default NULL,
  gratuitySituation varchar(30) default NULL,
  trash varchar(10) default NULL,
  answer1 char(1) default NULL,
  answer2 char(1) default NULL,
  answer3 char(1) default NULL,
  answer4 char(1) default NULL,
  answer5 char(1) default NULL,
  answer6 char(1) default NULL,
  answer7 char(1) default NULL,
  idInternal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (idInternal)
) TYPE=InnoDB;

--
-- Table structure for table `mw_AREAS_CIENTIFICAS_ILEEC`
--

CREATE TABLE mw_AREAS_CIENTIFICAS_ILEEC (
  id_area_cientifica int(11) NOT NULL default '0',
  nome varchar(255) NOT NULL default '',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_area_cientifica)
) TYPE=InnoDB;

--
-- Table structure for table `mw_AREAS_ESPECIALIZACAO_ILEEC`
--

CREATE TABLE mw_AREAS_ESPECIALIZACAO_ILEEC (
  id_area_especializacao int(11) NOT NULL default '0',
  nome varchar(255) NOT NULL default '',
  max_creditos int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_area_especializacao)
) TYPE=InnoDB;

--
-- Table structure for table `mw_AREAS_SECUNDARIAS_ILEEC`
--

CREATE TABLE mw_AREAS_SECUNDARIAS_ILEEC (
  id_area_secundaria int(11) NOT NULL default '0',
  id_area_especializacao int(11) NOT NULL default '0',
  nome varchar(255) NOT NULL default '',
  max_creditos int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_area_secundaria)
) TYPE=InnoDB;

--
-- Table structure for table `mw_AVERAGE`
--

CREATE TABLE mw_AVERAGE (
  number int(10) NOT NULL default '0',
  numberOfCoursesEnrolled int(10) NOT NULL default '0',
  numberOfCoursesApproved int(10) default NULL,
  sum int(10) default NULL,
  average float default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1'
) TYPE=InnoDB;

--
-- Table structure for table `mw_BRANCH`
--

CREATE TABLE mw_BRANCH (
  degreeCode int(10) default NULL,
  branchCode int(10) default NULL,
  orientationCode int(10) default NULL,
  description varchar(250) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  id_internal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (id_internal)
) TYPE=InnoDB;

--
-- Table structure for table `mw_COURSE`
--

CREATE TABLE mw_COURSE (
  courseCode varchar(10) NOT NULL default '',
  courseName varchar(250) default NULL,
  universityCode varchar(10) default NULL
) TYPE=InnoDB;

--
-- Table structure for table `mw_CURRICULAR_COURSE`
--

CREATE TABLE mw_CURRICULAR_COURSE (
  courseCode varchar(10) NOT NULL default '',
  courseName varchar(255) NOT NULL default '',
  universityCode varchar(10) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (courseCode)
) TYPE=InnoDB;

--
-- Table structure for table `mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE`
--

CREATE TABLE mw_CURRICULAR_COURSE_OUTSIDE_STUDENT_DEGREE (
  courseCode varchar(10) NOT NULL default '',
  degreeCode int(20) NOT NULL default '0',
  keyCurricularCourse int(11) NOT NULL default '0',
  idInternal int(11) NOT NULL auto_increment,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (idInternal)
) TYPE=InnoDB;

--
-- Table structure for table `mw_CURRICULAR_COURSE_SCOPE`
--

CREATE TABLE mw_CURRICULAR_COURSE_SCOPE (
  executionYear int(11) NOT NULL default '0',
  courseCode char(10) NOT NULL default '',
  degreeCode int(11) NOT NULL default '0',
  branchCode int(11) NOT NULL default '0',
  curricularYear int(11) default NULL,
  curricularSemester int(11) default NULL,
  courseType int(11) default NULL,
  theoreticalHours float default NULL,
  praticaHours float default NULL,
  labHours float default NULL,
  theoPratHours float default NULL,
  credits float default NULL,
  orientation char(255) default NULL,
  idInternal int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) default NULL
) TYPE=InnoDB;

--
-- Table structure for table `mw_CURRICULAR_COURSE_SCOPE_AUXILIARY_TABLE`
--

CREATE TABLE mw_CURRICULAR_COURSE_SCOPE_AUXILIARY_TABLE (
  executionYear int(11) NOT NULL default '0',
  courseCode varchar(10) NOT NULL default '',
  degreeCode int(11) NOT NULL default '0',
  branchCode int(11) NOT NULL default '0',
  curricularYear int(11) default NULL,
  curricularSemester int(11) default NULL,
  courseType int(11) default NULL,
  theoreticalHours float default NULL,
  praticaHours float default NULL,
  labHours float default NULL,
  theoPratHours float default NULL,
  credits float default NULL,
  orientation varchar(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idInternal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (idInternal)
) TYPE=InnoDB;

--
-- Table structure for table `mw_CURRICULAR_COURSE_SCOPE_temp`
--

CREATE TABLE mw_CURRICULAR_COURSE_SCOPE_temp (
  executionYear int(11) NOT NULL default '0',
  courseCode varchar(10) NOT NULL default '',
  degreeCode int(11) NOT NULL default '0',
  branchCode int(11) NOT NULL default '0',
  curricularYear int(11) default NULL,
  curricularSemester int(11) default NULL,
  courseType int(11) default NULL,
  theoreticalHours float default NULL,
  praticaHours float default NULL,
  labHours float default NULL,
  theoPratHours float default NULL,
  credits float default NULL,
  orientation varchar(255) default NULL,
  idInternal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (idInternal)
) TYPE=InnoDB;

--
-- Table structure for table `mw_DEGREE_TRANSLATION`
--

CREATE TABLE mw_DEGREE_TRANSLATION (
  ADM_DEGREE_CODE int(11) default NULL,
  KEY_DEGREE int(11) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1'
) TYPE=InnoDB;

--
-- Table structure for table `mw_DISCIPLINAS_ILEEC`
--

CREATE TABLE mw_DISCIPLINAS_ILEEC (
  id_disciplina int(11) NOT NULL default '0',
  codigo_disciplina varchar(255) NOT NULL default '',
  id_ano_curricular int(11) NOT NULL default '0',
  nome varchar(255) NOT NULL default '',
  numero_creditos int(11) NOT NULL default '0',
  id_area_cientifica int(11) NOT NULL default '0',
  obrigatoria int(11) NOT NULL default '0',
  id_semestre int(11) NOT NULL default '0',
  tipo_precedencia int(11) NOT NULL default '0',
  insc_obrigatoria int(11) NOT NULL default '0',
  funciona int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_disciplina)
) TYPE=InnoDB;

--
-- Table structure for table `mw_DISCIPLINA_GRUPO_ILEEC`
--

CREATE TABLE mw_DISCIPLINA_GRUPO_ILEEC (
  id_disciplina int(11) NOT NULL default '0',
  id_grupo int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_disciplina,id_grupo)
) TYPE=InnoDB;

--
-- Table structure for table `mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER`
--

CREATE TABLE mw_DISTINCT_ENROLLMENT_YEAR_AND_SEMESTER (
  enrolmentYear int(11) default NULL,
  curricularCourseSemester int(11) default NULL
) TYPE=InnoDB;

--
-- Table structure for table `mw_ENROLMENT`
--

CREATE TABLE mw_ENROLMENT (
  number int(11) default NULL,
  enrolmentYear int(11) default NULL,
  curricularCourseYear int(11) default NULL,
  curricularCourseSemester int(11) default NULL,
  season int(11) default NULL,
  courseCode char(11) default NULL,
  degreeCode int(11) default NULL,
  branchCode int(11) default NULL,
  grade char(10) default NULL,
  teacherNumber int(11) default NULL,
  examDate date default NULL,
  universityCode char(10) default NULL,
  remarks char(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idinternal int(11) NOT NULL default '0',
  KEY mw_ENROLMENT_INDEX_1 (number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks),
  KEY mw_ENROLMENT_INDEX_2 (number)
) TYPE=MyISAM;

--
-- Table structure for table `mw_ENROLMENT_AUXILIARY_TABLE_1`
--

CREATE TABLE mw_ENROLMENT_AUXILIARY_TABLE_1 (
  number int(11) default NULL,
  enrolmentYear int(11) default NULL,
  curricularCourseYear int(11) default NULL,
  curricularCourseSemester int(11) default NULL,
  season int(11) default NULL,
  courseCode varchar(11) default NULL,
  degreeCode int(11) default NULL,
  branchCode int(11) default NULL,
  grade varchar(10) default NULL,
  teacherNumber int(11) default NULL,
  examDate date default NULL,
  universityCode varchar(10) default NULL,
  remarks varchar(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idinternal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (idinternal),
  KEY mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_1 (number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks),
  KEY mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_2 (enrolmentYear,degreeCode),
  KEY mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_3 (number),
  KEY mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_4 (enrolmentYear)
) TYPE=InnoDB;

--
-- Table structure for table `mw_ENROLMENT_AUXILIARY_TABLE_2`
--

CREATE TABLE mw_ENROLMENT_AUXILIARY_TABLE_2 (
  number int(11) default NULL,
  enrolmentYear int(11) default NULL,
  curricularCourseYear int(11) default NULL,
  curricularCourseSemester int(11) default NULL,
  season int(11) default NULL,
  courseCode char(11) default NULL,
  degreeCode int(11) default NULL,
  branchCode int(11) default NULL,
  grade char(10) default NULL,
  teacherNumber int(11) default NULL,
  examDate date default NULL,
  universityCode char(10) default NULL,
  remarks char(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idinternal int(11) NOT NULL default '0',
  KEY mw_ENROLMENT_AUXILIARY_TABLE_2_INDEX_1 (number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks),
  KEY mw_ENROLMENT_AUXILIARY_TABLE_2_INDEX_2 (enrolmentYear,degreeCode),
  KEY mw_ENROLMENT_AUXILIARY_TABLE_2_INDEX_3 (number),
  KEY mw_ENROLMENT_AUXILIARY_TABLE_2_INDEX_4 (enrolmentYear)
) TYPE=MyISAM;

--
-- Table structure for table `mw_ENROLMENT_temp`
--

CREATE TABLE mw_ENROLMENT_temp (
  number int(11) default NULL,
  enrolmentYear int(11) default NULL,
  curricularCourseYear int(11) default NULL,
  curricularCourseSemester int(11) default NULL,
  season int(11) default NULL,
  courseCode char(11) default NULL,
  degreeCode int(11) default NULL,
  branchCode int(11) default NULL,
  grade char(10) default NULL,
  teacherNumber int(11) default NULL,
  examDate date default NULL,
  universityCode char(10) default NULL,
  remarks char(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idinternal int(11) NOT NULL default '0'
) TYPE=MyISAM;

--
-- Table structure for table `mw_EQUIVALENCIAS_ILEEC`
--

CREATE TABLE mw_EQUIVALENCIAS_ILEEC (
  id_equivalencia int(11) NOT NULL default '0',
  codigo_disciplina_curriculo_antigo varchar(255) NOT NULL default '',
  codigo_disciplina_curriculo_actual varchar(255) NOT NULL default '',
  tipo_equivalencia int(11) NOT NULL default '0',
  id_area_cientifica int(11) NOT NULL default '0',
  id_area_secundaria int(11) NOT NULL default '0',
  id_area_especializacao int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_equivalencia)
) TYPE=InnoDB;

--
-- Table structure for table `mw_GRUPOS_ILEEC`
--

CREATE TABLE mw_GRUPOS_ILEEC (
  id_grupo int(11) NOT NULL default '0',
  id_area_especializacao int(11) NOT NULL default '0',
  id_area_secundaria int(11) NOT NULL default '0',
  max_creditos int(11) NOT NULL default '0',
  min_creditos int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_grupo)
) TYPE=InnoDB;

--
-- Table structure for table `mw_PERSON`
--

CREATE TABLE mw_PERSON (
  documentIdNumber varchar(250) NOT NULL default '',
  documentIdType varchar(50) default NULL,
  documentIdPlace varchar(20) default NULL,
  documentIdDate date default NULL,
  name varchar(250) default NULL,
  sex varchar(30) default NULL,
  maritalStatus varchar(30) default NULL,
  dateOfBirth date default NULL,
  fatherName varchar(250) default NULL,
  motherName varchar(250) default NULL,
  countryCode int(10) default NULL,
  parishOfBirth varchar(250) default NULL,
  municipalityOfBirth varchar(250) default NULL,
  districtOfBirth varchar(250) default NULL,
  address varchar(250) default NULL,
  addressAreaCode varchar(250) default NULL,
  zipCode varchar(250) default NULL,
  zipAreaCode varchar(250) default NULL,
  parishOfAddress varchar(250) default NULL,
  municipalityOfAddress varchar(250) default NULL,
  districtOfAddress varchar(250) default NULL,
  phone varchar(250) default NULL,
  mobilePhone varchar(250) default NULL,
  email varchar(250) default NULL,
  homePage varchar(250) default NULL,
  contributorNumber int(11) default NULL,
  job varchar(250) default NULL,
  username varchar(250) default NULL,
  password varchar(250) default NULL,
  fiscalCode varchar(250) default NULL,
  documentIdValidation date default NULL,
  financialRepCode varchar(250) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idInternal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (idInternal)
) TYPE=InnoDB;

--
-- Table structure for table `mw_PERSON_WITH_DUPLICATE_ID`
--

CREATE TABLE mw_PERSON_WITH_DUPLICATE_ID (
  documentidnumber char(250) NOT NULL default '',
  maxidinternal bigint(20) default NULL,
  total bigint(21) NOT NULL default '0'
) TYPE=MyISAM;

--
-- Table structure for table `mw_PESSOA`
--

CREATE TABLE mw_PESSOA (
  documentIdNumber varchar(250) NOT NULL default '',
  documentIdType varchar(50) default NULL,
  documentIdPlace varchar(20) default NULL,
  documentIdDate date default NULL,
  name varchar(250) default NULL,
  sex varchar(30) default NULL,
  maritalStatus varchar(30) default NULL,
  dateOfBirth date default NULL,
  fatherName varchar(250) default NULL,
  motherName varchar(250) default NULL,
  countryCode int(10) default NULL,
  parishOfBirth varchar(250) default NULL,
  municipalityOfBirth varchar(250) default NULL,
  districtOfBirth varchar(250) default NULL,
  address varchar(250) default NULL,
  addressAreaCode varchar(250) default NULL,
  zipCode varchar(250) default NULL,
  zipAreaCode varchar(250) default NULL,
  parishOfAddress varchar(250) default NULL,
  municipalityOfAddress varchar(250) default NULL,
  districtOfAddress varchar(250) default NULL,
  phone varchar(250) default NULL,
  mobilePhone varchar(250) default NULL,
  email varchar(250) default NULL,
  homePage varchar(250) default NULL,
  contributorNumber int(11) default NULL,
  job varchar(250) default NULL,
  username varchar(250) default NULL,
  password varchar(250) default NULL,
  fiscalCode varchar(250) default NULL,
  documentIdValidation date default NULL,
  financialRepCode varchar(250) default NULL,
  idInternal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (idInternal)
) TYPE=InnoDB;

--
-- Table structure for table `mw_PRECEDENCIAS_DISCIPLINA_DISCIPLINA_ILEEC`
--

CREATE TABLE mw_PRECEDENCIAS_DISCIPLINA_DISCIPLINA_ILEEC (
  id_disciplina int(11) NOT NULL default '0',
  id_precedente int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_disciplina,id_precedente)
) TYPE=InnoDB;

--
-- Table structure for table `mw_PRECEDENCIAS_NUMERO_DISCIPLINAS_ILEEC`
--

CREATE TABLE mw_PRECEDENCIAS_NUMERO_DISCIPLINAS_ILEEC (
  id_disciplina int(11) NOT NULL default '0',
  numero_disciplinas int(11) NOT NULL default '0',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (id_disciplina,numero_disciplinas)
) TYPE=InnoDB;

--
-- Table structure for table `mw_STUDENT`
--

CREATE TABLE mw_STUDENT (
  documentIdNumber char(250) NOT NULL default '',
  number int(50) default NULL,
  degreeCode int(20) default NULL,
  branchCode int(30) default NULL,
  year int(30) default NULL,
  ISTUniversity char(30) default NULL,
  worker char(30) default NULL,
  gratuitySituation char(30) default NULL,
  trash char(10) default NULL,
  answer1 char(1) default NULL,
  answer2 char(1) default NULL,
  answer3 char(1) default NULL,
  answer4 char(1) default NULL,
  answer5 char(1) default NULL,
  answer6 char(1) default NULL,
  answer7 char(1) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idInternal int(11) NOT NULL default '0',
  KEY mw_STUDENT_INDEX_1 (number)
) TYPE=MyISAM;

--
-- Table structure for table `mw_STUDENTS_WITH_VARIOUS_NUMBERS`
--

CREATE TABLE mw_STUDENTS_WITH_VARIOUS_NUMBERS (
  documentidnumber char(250) NOT NULL default '',
  number int(50) default NULL,
  maxidinternal bigint(20) default NULL,
  total bigint(21) NOT NULL default '0'
) TYPE=MyISAM;

--
-- Table structure for table `mw_STUDENT_AUXILIARY_TABLE`
--

CREATE TABLE mw_STUDENT_AUXILIARY_TABLE (
  documentIdNumber varchar(250) NOT NULL default '',
  number int(50) default NULL,
  degreeCode int(20) default NULL,
  branchCode int(30) default NULL,
  year int(30) default NULL,
  ISTUniversity varchar(30) default NULL,
  worker varchar(30) default NULL,
  gratuitySituation varchar(30) default NULL,
  trash varchar(10) default NULL,
  answer1 char(1) default NULL,
  answer2 char(1) default NULL,
  answer3 char(1) default NULL,
  answer4 char(1) default NULL,
  answer5 char(1) default NULL,
  answer6 char(1) default NULL,
  answer7 char(1) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idInternal int(11) NOT NULL auto_increment,
  PRIMARY KEY  (idInternal),
  KEY mw_STUDENT_AUXILIARY_TABLE_INDEX_1 (number)
) TYPE=InnoDB;

--
-- Table structure for table `mw_STUDENT_CLASS`
--

CREATE TABLE mw_STUDENT_CLASS (
  STUDENT_NUMBER varchar(20) default NULL,
  STUDENT_NAME varchar(255) default NULL,
  DEGREE_CODE int(11) default NULL,
  AVERAGE float(10,2) default NULL,
  CLASS_NAME varchar(20) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1'
) TYPE=InnoDB;

--
-- Table structure for table `mw_TIPOS_EQUIVALENCIA_ILEEC`
--

CREATE TABLE mw_TIPOS_EQUIVALENCIA_ILEEC (
  tipo_equivalencia int(11) NOT NULL default '0',
  descricao varchar(255) NOT NULL default '',
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  PRIMARY KEY  (tipo_equivalencia)
) TYPE=InnoDB;

--
-- Table structure for table `mw_TIPOS_PRECENDENCIA_ILEEC`
--

CREATE TABLE mw_TIPOS_PRECENDENCIA_ILEEC (
  tipo_precedencia int(11) NOT NULL default '0',
  descricao varchar(255) NOT NULL default '',
  PRIMARY KEY  (tipo_precedencia)
) TYPE=InnoDB;

--
-- Table structure for table `mw_TREATED_ENROLLMENT`
--

CREATE TABLE mw_TREATED_ENROLLMENT (
  idinternal int(11) default NULL,
  number int(11) default NULL,
  enrolmentYear int(11) default NULL,
  curricularCourseYear int(11) default NULL,
  curricularCourseSemester int(11) default NULL,
  season int(11) default NULL,
  courseCode varchar(11) default NULL,
  degreeCode int(11) default NULL,
  branchCode int(11) default NULL,
  grade varchar(10) default NULL,
  teacherNumber int(11) default NULL,
  examDate date default NULL,
  universityCode varchar(10) default NULL,
  remarks varchar(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  ID_INTERNAL int(11) NOT NULL auto_increment,
  PRIMARY KEY  (ID_INTERNAL),
  KEY mw_TREATED_ENROLLMENT_INDEX_1 (number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks),
  KEY mw_TREATED_ENROLLMENT_INDEX_2 (enrolmentYear,degreeCode),
  KEY mw_TREATED_ENROLLMENT_INDEX_3 (number),
  KEY mw_TREATED_ENROLLMENT_INDEX_4 (enrolmentYear)
) TYPE=InnoDB;

--
-- Table structure for table `mw_UNIVERSITY`
--

CREATE TABLE mw_UNIVERSITY (
  universityCode varchar(10) default NULL,
  universityName varchar(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1'
) TYPE=InnoDB;

--
-- Table structure for table `mw_UNTREATED_ENROLLMENT`
--

CREATE TABLE mw_UNTREATED_ENROLLMENT (
  number int(11) default NULL,
  enrolmentYear int(11) default NULL,
  curricularCourseYear int(11) default NULL,
  curricularCourseSemester int(11) default NULL,
  season int(11) default NULL,
  courseCode char(11) default NULL,
  degreeCode int(11) default NULL,
  branchCode int(11) default NULL,
  grade char(10) default NULL,
  teacherNumber int(11) default NULL,
  examDate date default NULL,
  universityCode char(10) default NULL,
  remarks char(255) default NULL,
  ACK_OPT_LOCK int(11) NOT NULL default '1',
  idinternal int(11) NOT NULL default '0',
  KEY mw_UNTREATED_ENROLLMENT_INDEX_1 (number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks),
  KEY mw_UNTREATED_ENROLLMENT_INDEX_2 (number)
) TYPE=MyISAM;

--
-- Table structure for table `paisExpansao`
--

CREATE TABLE paisExpansao (
  descr1 varchar(50) NOT NULL default '',
  descr2 varchar(50) NOT NULL default ''
) TYPE=MyISAM;

--
-- Table structure for table `plano_curricular_2003final`
--

CREATE TABLE plano_curricular_2003final (
  Id_Internal int(10) unsigned NOT NULL auto_increment,
  Ano_Lectivo int(11) default NULL,
  Codigo_Lic int(11) default NULL,
  Codigo_Ramo text,
  Ano_Curricular text,
  Semestre text,
  Codigo_Disc text,
  Nome_Disc text,
  ciencia_base enum('True','False') default NULL,
  ender_web text,
  Objectivos text,
  Progr_Res text,
  bib_princ_1 text,
  bib_princ_2 text,
  bib_princ_3 text,
  bib_sec_1 text,
  bib_sec_2 text,
  bib_sec_3 text,
  Crit_Av text,
  data_act text,
  Ingles_Nome_Disc text,
  Ingles_Objectivos text,
  Ingles_Progr_Res text,
  Ingles_Crit_Av text,
  Ingles_relatorio text,
  Ingles_data_act datetime default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (Id_Internal)
) TYPE=InnoDB;

--
-- Table structure for table `posgrad_aluno_mestrado`
--

CREATE TABLE posgrad_aluno_mestrado (
  codigoInterno int(11) NOT NULL auto_increment,
  numero int(10) NOT NULL default '0',
  codigoPessoa int(11) NOT NULL default '0',
  codigoCursoMestrado int(11) NOT NULL default '0',
  especializacao enum('Mestrado','Integrado','Especialização') NOT NULL default 'Mestrado',
  anoCandidatura int(11) NOT NULL default '0',
  escolaLicenciatura varchar(100) default NULL,
  anoLicenciatura int(11) default '0',
  media float default '0',
  creditos varchar(4) default NULL,
  observacoes varchar(255) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (numero),
  UNIQUE KEY u2 (codigoPessoa,codigoCursoMestrado)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_area_cientifica`
--

CREATE TABLE posgrad_area_cientifica (
  codigoInterno int(11) NOT NULL auto_increment,
  nome varchar(100) NOT NULL default '',
  codigoCursoMestrado int(11) NOT NULL default '0',
  anoLectivo varchar(50) NOT NULL default '',
  codigoInternoRamo int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY U1 (nome,codigoCursoMestrado,anoLectivo)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_candidato_mestrado`
--

CREATE TABLE posgrad_candidato_mestrado (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroDocumentoIdentificacao varchar(50) default NULL,
  tipoDocumentoIdentificacao enum('BILHETE DE IDENTIDADE','PASSAPORTE','OUTRO') default 'BILHETE DE IDENTIDADE',
  localEmissaoDocumentoIdentificacao varchar(100) default NULL,
  dataEmissaoDocumentoIdentificacao date default '0001-01-01',
  nome varchar(100) default NULL,
  sexo enum('masculino','feminino') default 'masculino',
  estadoCivil enum('SOLTEIRO','CASADO','DIVORCIADO','VIÚVO','SEPARADO','UNIÃO DE FACTO') default 'SOLTEIRO',
  nascimento date default '0001-01-01',
  nomePai varchar(100) default NULL,
  nomeMae varchar(100) default NULL,
  nacionalidade varchar(50) default NULL,
  freguesiaNaturalidade varchar(100) default NULL,
  concelhoNaturalidade varchar(100) default NULL,
  distritoNaturalidade varchar(100) default NULL,
  morada varchar(100) default NULL,
  localidade varchar(100) default NULL,
  codigoPostal varchar(8) default NULL,
  freguesiaMorada varchar(100) default NULL,
  concelhoMorada varchar(100) default NULL,
  distritoMorada varchar(100) default NULL,
  telefone varchar(50) default NULL,
  telemovel varchar(50) default NULL,
  email varchar(50) default NULL,
  enderecoWeb varchar(200) default NULL,
  numContribuinte varchar(50) default NULL,
  profissao varchar(100) default NULL,
  licenciatura varchar(100) default NULL,
  username varchar(50) default NULL,
  password varchar(50) default NULL,
  codigoCursoMestrado int(11) default NULL,
  numeroCandidato int(20) default NULL,
  anoCandidatura int(20) default NULL,
  especializacao enum('Mestrado','Integrado','Especialização') default 'Mestrado',
  escolaLicenciatura varchar(100) default NULL,
  anoLicenciatura int(11) default '0',
  media float default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (numeroDocumentoIdentificacao,tipoDocumentoIdentificacao,codigoCursoMestrado),
  UNIQUE KEY u2 (numeroCandidato,codigoCursoMestrado,anoCandidatura),
  UNIQUE KEY u3 (username)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_candidato_situacao`
--

CREATE TABLE posgrad_candidato_situacao (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoCandidato int(11) NOT NULL default '0',
  codigoSituacao int(11) NOT NULL default '1',
  data date NOT NULL default '2002-10-18',
  observacoes text,
  valido tinyint(4) NOT NULL default '1',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_cargo`
--

CREATE TABLE posgrad_cargo (
  codigoInterno int(11) NOT NULL auto_increment,
  cargoNome enum('POS_GRADUACAO','CANDIDATO_AUXILIAR','CANDIDATO','ALUNO_MESTRADO') NOT NULL default 'POS_GRADUACAO',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_curso_mestrado`
--

CREATE TABLE posgrad_curso_mestrado (
  codigoInterno int(11) NOT NULL auto_increment,
  nomeMestrado varchar(100) NOT NULL default '',
  anoLectivo varchar(50) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u2 (nomeMestrado)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_disc_area`
--

CREATE TABLE posgrad_disc_area (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoAreaCientifica int(11) NOT NULL default '0',
  codigoDisciplina int(11) NOT NULL default '0',
  anoLectivo varchar(50) NOT NULL default '',
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_disc_area_aluno`
--

CREATE TABLE posgrad_disc_area_aluno (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoAluno int(11) NOT NULL default '0',
  codigoAreaDisciplina int(11) NOT NULL default '0',
  dataExame date default NULL,
  dataLancamento date default NULL,
  nota varchar(4) default NULL,
  equivalencia varchar(15) default NULL,
  creditos varchar(4) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (codigoAluno,codigoAreaDisciplina)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_disciplina`
--

CREATE TABLE posgrad_disciplina (
  codigoInterno int(11) NOT NULL auto_increment,
  nome varchar(200) NOT NULL default '',
  codigoCursoMestrado int(11) NOT NULL default '0',
  creditos float default NULL,
  sigla varchar(255) default NULL,
  tipo varchar(10) default NULL,
  semestre int(1) default NULL,
  optativa varchar(5) default NULL,
  codigoCurricularCourse int(11) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (nome,codigoCursoMestrado)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_docente`
--

CREATE TABLE posgrad_docente (
  codigoInterno int(11) NOT NULL auto_increment,
  cat varchar(4) default NULL,
  numdec int(5) NOT NULL default '0',
  bi varchar(12) default NULL,
  cont varchar(15) default NULL,
  nome varchar(100) NOT NULL default '',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM;

--
-- Table structure for table `posgrad_guia`
--

CREATE TABLE posgrad_guia (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroGuia int(50) NOT NULL default '0',
  dataEmissao date NOT NULL default '0001-01-01',
  anoGuia int(11) NOT NULL default '0',
  situacao enum('PAGA','NAO_PAGA','ANULADA') NOT NULL default 'PAGA',
  entidadeContribuinte int(11) NOT NULL default '0',
  entidadeNome varchar(100) NOT NULL default '',
  entidadeMorada varchar(100) NOT NULL default '',
  numeroAluno int(11) NOT NULL default '0',
  tipoAluno enum('CANDIDATO','MESTRADO','ESPECIALIZAÇÃO','DOUTORAMENTO') NOT NULL default 'CANDIDATO',
  codigoCursoMestrado int(11) NOT NULL default '0',
  total float(11,2) NOT NULL default '0.00',
  observacoes text,
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_guia_tabela`
--

CREATE TABLE posgrad_guia_tabela (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoGuia int(11) NOT NULL default '0',
  codigoTabela int(11) NOT NULL default '0',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 379904 kB; InnoDB free:';

--
-- Table structure for table `posgrad_pagamento_guia`
--

CREATE TABLE posgrad_pagamento_guia (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoGuia int(11) NOT NULL default '0',
  dataPagamento date NOT NULL default '0001-01-01',
  montante float(10,2) NOT NULL default '0.00',
  numeroPrestacao int(11) NOT NULL default '1',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_pessoa`
--

CREATE TABLE posgrad_pessoa (
  codigoInterno int(11) NOT NULL auto_increment,
  numeroDocumentoIdentificacao varchar(50) default NULL,
  tipoDocumentoIdentificacao enum('BILHETE DE IDENTIDADE','PASSAPORTE','OUTRO') default 'BILHETE DE IDENTIDADE',
  localEmissaoDocumentoIdentificacao varchar(100) default NULL,
  dataEmissaoDocumentoIdentificacao date default '2002-01-01',
  nome varchar(100) default NULL,
  sexo enum('masculino','feminino') default 'masculino',
  estadoCivil enum('SOLTEIRO','CASADO','DIVORCIADO','VIÚVO','SEPARADO','UNIÃO DE FACTO') default 'SOLTEIRO',
  nascimento date default '2002-01-01',
  nomePai varchar(100) default NULL,
  nomeMae varchar(100) default NULL,
  nacionalidade varchar(50) default NULL,
  freguesiaNaturalidade varchar(100) default NULL,
  concelhoNaturalidade varchar(100) default NULL,
  distritoNaturalidade varchar(100) default NULL,
  morada varchar(100) default NULL,
  localidade varchar(100) default NULL,
  codigoPostal varchar(8) default NULL,
  freguesiaMorada varchar(100) default NULL,
  concelhoMorada varchar(100) default NULL,
  distritoMorada varchar(100) default NULL,
  telefone varchar(50) default NULL,
  telemovel varchar(50) default NULL,
  email varchar(100) default NULL,
  enderecoWeb varchar(200) default NULL,
  numContribuinte varchar(50) default NULL,
  profissao varchar(100) default NULL,
  username varchar(50) default NULL,
  password varchar(50) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u2 (numeroDocumentoIdentificacao,tipoDocumentoIdentificacao),
  UNIQUE KEY u1 (username)
) TYPE=InnoDB COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_pessoa_cargo`
--

CREATE TABLE posgrad_pessoa_cargo (
  codigoInterno int(11) NOT NULL auto_increment,
  codigoPessoa int(11) NOT NULL default '0',
  codigoCargo int(11) NOT NULL default '0',
  data date NOT NULL default '0001-01-01',
  PRIMARY KEY  (codigoInterno),
  UNIQUE KEY u1 (codigoPessoa,codigoCargo)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `posgrad_situacao`
--

CREATE TABLE posgrad_situacao (
  codigoInterno int(11) NOT NULL auto_increment,
  situacao enum('PENDENTE','ADMITIDO','SUPLENTE','NAO_ACEITE','DESISTIU','FALTA_CERTIFICADO') NOT NULL default 'PENDENTE',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM;

--
-- Table structure for table `posgrad_tabela_precos`
--

CREATE TABLE posgrad_tabela_precos (
  codigoInterno int(11) NOT NULL auto_increment,
  tipo varchar(200) NOT NULL default '',
  designacao varchar(200) NOT NULL default '',
  montante float(10,2) NOT NULL default '0.00',
  valido int(11) NOT NULL default '1',
  PRIMARY KEY  (codigoInterno)
) TYPE=MyISAM COMMENT='InnoDB free: 380928 kB; InnoDB free: 380928 kB; InnoDB free:';

--
-- Table structure for table `responsavel`
--

CREATE TABLE responsavel (
  Id_Internal int(10) unsigned NOT NULL auto_increment,
  Ano_Lectivo float default NULL,
  Cod_Curso float default NULL,
  Ano_Curr float default NULL,
  Semestre float default NULL,
  Cod_Ramo float default NULL,
  Cod_Disc varchar(255) default NULL,
  No_Mec float default NULL,
  Data_act datetime default NULL,
  Observacoes varchar(255) default NULL,
  depto varchar(255) default NULL,
  ACK_OPT_LOCK int(11) default NULL,
  PRIMARY KEY  (Id_Internal)
) TYPE=InnoDB;

--
-- Table structure for table `submeter126`
--

CREATE TABLE submeter126 (
  ID_INTERNAL int(11) unsigned NOT NULL default '0',
  KEY_STUDENT int(11) unsigned NOT NULL default '0',
  KEY_DISTRIBUTED_TEST int(11) unsigned NOT NULL default '0',
  DATE timestamp(14) NOT NULL,
  EVENT text
) TYPE=InnoDB;

--
-- Table structure for table `tmp_DOCENTE`
--

CREATE TABLE tmp_DOCENTE (
  codigoInterno int(11) NOT NULL auto_increment,
  cat varchar(4) NOT NULL default '',
  numdec int(5) NOT NULL default '0',
  bi varchar(12) NOT NULL default '',
  cont varchar(15) NOT NULL default '',
  nome varchar(100) NOT NULL default '',
  PRIMARY KEY  (codigoInterno)
) TYPE=InnoDB;

--
-- Table structure for table `tmp_GRATUITY_LETTERS_2002`
--

CREATE TABLE tmp_GRATUITY_LETTERS_2002 (
  NAME varchar(100) default NULL,
  ADDRESS varchar(100) default NULL,
  AREA varchar(100) default NULL,
  AREA_CODE varchar(8) default NULL,
  AREA_OF_AREA_CODE varchar(100) default NULL,
  MASTER_DEGREE_NAME varchar(100) default NULL,
  PAYMENT_REFERENCE varchar(100) default NULL,
  GRATUITY_VALUE varchar(100) default NULL,
  INSURANCE_VALUE varchar(100) default NULL,
  TOTAL_VALUE varchar(100) default NULL
) TYPE=InnoDB;

--
-- Table structure for table `tmp_GRATUITY_LETTERS_2002_2003`
--

CREATE TABLE tmp_GRATUITY_LETTERS_2002_2003 (
  NAME varchar(100) default NULL,
  ADDRESS varchar(100) default NULL,
  AREA varchar(100) default NULL,
  AREA_CODE varchar(8) default NULL,
  AREA_OF_AREA_CODE varchar(100) default NULL,
  MASTER_DEGREE_NAME varchar(100) default NULL,
  PAYMENT_REFERENCE_2002 varchar(100) default NULL,
  GRATUITY_VALUE_2002 varchar(100) default NULL,
  INSURANCE_VALUE_2002 varchar(100) default NULL,
  TOTAL_VALUE_2002 varchar(100) default NULL,
  PAYMENT_REFERENCE_2003 varchar(100) default NULL,
  GRATUITY_VALUE_2003 varchar(100) default NULL,
  INSURANCE_VALUE_2003 varchar(100) default NULL,
  TOTAL_VALUE_2003 varchar(100) default NULL
) TYPE=InnoDB;

--
-- Table structure for table `tmp_GRATUITY_LETTERS_2003`
--

CREATE TABLE tmp_GRATUITY_LETTERS_2003 (
  NAME varchar(100) default NULL,
  ADDRESS varchar(100) default NULL,
  AREA varchar(100) default NULL,
  AREA_CODE varchar(8) default NULL,
  AREA_OF_AREA_CODE varchar(100) default NULL,
  MASTER_DEGREE_NAME varchar(100) default NULL,
  PAYMENT_REFERENCE varchar(100) default NULL,
  GRATUITY_VALUE varchar(100) default NULL,
  INSURANCE_VALUE varchar(100) default NULL,
  TOTAL_VALUE varchar(100) default NULL
) TYPE=InnoDB;

