-- ==================================================================================
--  ZONA DEPARTAMENTAL
-- ==================================================================================

-- ----------------------------
--  Table structure for DEPARTMENT
-- ----------------------------
drop table if exists DEPARTMENT;
create table DEPARTMENT (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME),
   unique U2 (CODE)
) type=InnoDB;

-- ----------------------------
--  Table structure for DEPARTMENT_COURSE
-- ----------------------------
drop table if exists DEPARTMENT_COURSE;
create table DEPARTMENT_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   CODE varchar(50) not null,
   KEY_DEPARTMENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, CODE)
)type=InnoDB;


-- ==================================================================================
--  ZONA CURRICULAR
-- ==================================================================================

-- ----------------------------
--  Table structure for DEGREE
-- ----------------------------
drop table if exists DEGREE;
create table DEGREE (
   ID_INTERNAL int(11) not null auto_increment,
   CODE varchar(100) not null,
   NAME varchar(100) not null,
   TYPE_DEGREE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE),
   unique U2 (NAME, TYPE_DEGREE)
)type=InnoDB;

-- ----------------------------
--  Table structure for DEGREE_CURRICULAR_PLAN
-- ----------------------------
drop table if exists DEGREE_CURRICULAR_PLAN;
create table DEGREE_CURRICULAR_PLAN (
   ID_INTERNAL int(11) not null auto_increment,
   NAME varchar(50) not null,
   KEY_DEGREE int(11) not null,
   STATE int(11) not null,
   INITIAL_DATE date,
   END_DATE date,
   DEGREE_DURATION int(11) not null,
   MINIMAL_YEAR_FOR_OPTIONAL_COURSES int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (NAME, KEY_DEGREE)
)type=InnoDB;

-- ----------------------------
-- Table struture for ENROLMENT_PERIOD
-- ----------------------------
drop table if exists ENROLMENT_PERIOD;
create table ENROLMENT_PERIOD (
	ID_INTERNAL int(11) not null auto_increment,
	KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
	KEY_EXECUTION_PERIOD int(11) not null,
	START_DATE date not null,
	END_DATE date not null,
	primary key (ID_INTERNAL),
	unique U1 (KEY_DEGREE_CURRICULAR_PLAN, KEY_EXECUTION_PERIOD)
)type=InnoDB;

-- ----------------------------
--  Table structure for CURRICULAR_COURSE
-- ----------------------------
drop table if exists CURRICULAR_COURSE;
create table CURRICULAR_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_DEPARTMENT_COURSE int(11),
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null default '0',
   CREDITS double,
   THEORETICAL_HOURS double,
   PRATICAL_HOURS double,
   THEO_PRAT_HOURS double,
   LAB_HOURS double,
   NAME varchar(100) not null,
   CODE varchar(50) not null,
   TYPE int(11),
   EXECUTION_SCOPE int(11),
   MANDATORY bit,
   PRIMARY KEY  (ID_INTERNAL),
   UNIQUE KEY U1 (CODE, NAME, KEY_DEGREE_CURRICULAR_PLAN)
)type=InnoDB;

-- ----------------------------
--  Table structure for STUDENT_CURRICULAR_PLAN
-- ----------------------------
drop table if exists STUDENT_CURRICULAR_PLAN;
create table STUDENT_CURRICULAR_PLAN (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT int(11) not null,
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
   CURRENT_STATE int(11) not null,
   START_DATE date not null,
   KEY_BRANCH int(11) not null,
   SPECIALIZATION int(11),
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT, KEY_DEGREE_CURRICULAR_PLAN, CURRENT_STATE, KEY_BRANCH)
)type=InnoDB;

-- ==================================================================================
--  ZONA INSCRICAO
-- ==================================================================================

-- ----------------------------
--  Table structure for ENROLMENT
-- ----------------------------
drop table if exists ENROLMENT;
create table ENROLMENT (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_STUDENT_CURRICULAR_PLAN int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   KEY_EXECUTION_PERIOD int(11) not null,
   STATE int(11) not null,
   CLASS_NAME varchar(255) not null,
   KEY_CURRICULAR_COURSE_FOR_OPTION int (11),
   EVALUATION_TYPE int(11) not null,
   UNIVERSITY_CODE varchar(255) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_STUDENT_CURRICULAR_PLAN, KEY_CURRICULAR_COURSE, KEY_EXECUTION_PERIOD)
)type=InnoDB;

-- ----------------------------
--  Table structure for ENROLMENT_EVALUATION
-- ----------------------------
drop table if exists ENROLMENT_EVALUATION;
create table ENROLMENT_EVALUATION (
   ID_INTERNAL int(11) not null auto_increment,
   GRADE varchar(11),
   EVALUATION_TYPE int(11) not null,
   EXAM_DATE date,
   GRADE_AVAILABLE_DATE date,
   RESPONSIBLE_PERSON int(11) not null,
   STATE int(11) not null,
   KEY_ENROLMENT int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_ENROLMENT, EVALUATION_TYPE)
)type=InnoDB;

-- ==================================================================================
--  ZONA TABELAS NOVAS
-- ==================================================================================

-- ----------------------------
--  Table structure for CURRICULAR_YEAR
-- ----------------------------
drop table if exists CURRICULAR_YEAR;
create table CURRICULAR_YEAR (
   ID_INTERNAL int(11) not null auto_increment,
   YEAR int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (YEAR)
)type=InnoDB;

-- ----------------------------
--  Table structure for CURRICULAR_SEMESTER
-- ----------------------------
drop table if exists CURRICULAR_SEMESTER;
create table CURRICULAR_SEMESTER (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_YEAR int(11) not null,
   SEMESTER int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (SEMESTER, KEY_CURRICULAR_YEAR)
)type=InnoDB;

-- ----------------------------
--  Table structure for BRANCH
-- ----------------------------
drop table if exists BRANCH;
create table BRANCH (
   ID_INTERNAL int(11) not null auto_increment,
   BRANCH_CODE varchar(50) not null,
   BRANCH_NAME varchar(255),
   KEY_DEGREE_CURRICULAR_PLAN integer(11),
   primary key (ID_INTERNAL),
   unique U1 (BRANCH_NAME, BRANCH_CODE, KEY_DEGREE_CURRICULAR_PLAN)
)type=InnoDB;

-- ==================================================================================
--  ZONA TABELAS DE RELACAO MUITOS PARA MUITOS
-- ==================================================================================

-- ----------------------------
--  Table structure for CURRICULAR_COURSE_SCOPE
-- ----------------------------
drop table if exists CURRICULAR_COURSE_SCOPE;
create table CURRICULAR_COURSE_SCOPE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_SEMESTER int(11) not null,
   KEY_CURRICULAR_COURSE int(11) not null,
   KEY_BRANCH int(11) not null,
   THEORETICAL_HOURS double default '0',
   PRATICAL_HOURS double default '0',
   THEO_PRAT_HOURS double default '0',
   LAB_HOURS double default '0',
   MAX_INCREMENT_NAC int(11) not null,
   MIN_INCREMENT_NAC int(11) not null,
   WEIGTH int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_CURRICULAR_SEMESTER, KEY_CURRICULAR_COURSE, KEY_BRANCH)
)type=InnoDB;


-- ----------------------------
--  Table structure for EQUIVALENCE
-- ----------------------------
drop table if exists EQUIVALENCE;
create table EQUIVALENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EQUIVALENT_ENROLMENT int(11) not null,
   KEY_ENROLMENT int(11) not null,
   EQUIVALENCE_TYPE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EQUIVALENT_ENROLMENT, KEY_ENROLMENT)
)type=InnoDB;

-- ----------------------------
--  Table structure for POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE
-- ----------------------------
drop table if exists POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE;
create table POSSIBLE_CURRICULAR_COURSE_FOR_OPTIONAL_CURRICULAR_COURSE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_POSSIBLE_CURRICULAR_COURSE int(11) not null,
   KEY_OPTIONAL_CURRICULAR_COURSE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_POSSIBLE_CURRICULAR_COURSE, KEY_OPTIONAL_CURRICULAR_COURSE)
)type=InnoDB;

-- --------------------------------------------
--  Table structure for STUDENT_KIND
-- --------------------------------------------
drop table if exists STUDENT_KIND;
create table STUDENT_KIND (
   ID_INTERNAL int(11) not null auto_increment,
   STUDENT_TYPE int(11) not null,
   MIN_COURSES_TO_ENROL int(11) not null,
   MAX_COURSES_TO_ENROL int(11) not null,
   MAX_NAC_TO_ENROL int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (STUDENT_TYPE)
)type=InnoDB;

-- ==================================================================================
--  ZONA TABELAS REPETIDAS
-- ==================================================================================

-- ----------------------------
--  Table structure for PRECEDENCE
-- ----------------------------
drop table if exists PRECEDENCE;
create table PRECEDENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_COURSE int(11) not null,
   SCOPE_TO_APPLY varchar(3) not null,
   primary key (ID_INTERNAL))
   type=InnoDB;

-- ----------------------------
--  Table structure for RESTRICTION
-- ----------------------------
DROP TABLE IF EXISTS RESTRICTION;
CREATE TABLE RESTRICTION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL,
  KEY_PRECEDENCE int(11) not null,
  KEY_CURRICULAR_COURSE int (11),
  NUMBER_OF_CURRICULAR_COURSE_DONE int(11),
  primary key (ID_INTERNAL)) TYPE=InnoDB;

