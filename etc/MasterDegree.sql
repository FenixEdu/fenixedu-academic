#----------------------------
# Table structure for MASTER_DEGREE_CANDIDATE
#----------------------------

drop table if exists MASTER_DEGREE_CANDIDATE;
create table MASTER_DEGREE_CANDIDATE (
   INTERNAL_CODE integer(11) not null auto_increment,
   IDENTIFICATION_DOCUMENT_NUMBER varchar(50) not null,
   IDENTIFICATION_DOCUMENT_TYPE int(11) not null, 
   IDENTIFICATION_DOCUMENT_ISSUE_PLACE varchar(100),
   IDENTIFICATION_DOCUMENT_ISSUE_DATE date default '0001-01-01',
   IDENTIFICATION_DOCUMENT_EXPIRATION_DATE date default '0001-01-01',
   NAME varchar(100),
   SEX int(11),
   MARITAL_STATUS int(11),
   BIRTH date default '0001-01-01',
   FATHER_NAME varchar(100),
   MOTHER_NAME varchar(100),
   NATIONALITY_COUNTRY_KEY integer(11),
   BIRTH_PLACE_PARISH varchar(100),
   BIRTH_PLACE_MUNICIPALITY varchar(100),
   BIRTH_PLACE_DISTRICT varchar(100),
   ADDRESS varchar(100),
   PLACE varchar(100),
   POSTCODE varchar(8),
   ADDRESS_PARISH varchar(100),
   ADDRESS_MUNICIPALITY varchar(100),
   ADDRESS_DISTRICT varchar(100),
   TELEPHONE varchar(50),
   MOBILE_PHONE varchar(50),
   EMAIL varchar(50),
   WEB_SITE varchar(200),
   CONTRIBUTOR_NUMBER varchar(50),
   OCCUPATION varchar(100),
   MAJOR_DEGREE varchar(100),
   USERNAME varchar(50),
   PASSWORD varchar(50),
   CANDIDATE_NUMBER int(20),
   EXECUTION_YEAR_KEY integer(20) not null,
   SPECIALIZATION int(11) not null, 
   MAJOR_DEGREE_SCHOOL varchar(100),
   MAJOR_DEGREE_YEAR int(11) default '0',
   AVERAGE float,
   DEGREE_KEY integer(11) not null default '0',
   COUNTRY_KEY integer(11),
   primary key (INTERNAL_CODE),
   unique u1 (IDENTIFICATION_DOCUMENT_NUMBER, IDENTIFICATION_DOCUMENT_TYPE, EXECUTION_YEAR_KEY, DEGREE_KEY),
   unique u3 (USERNAME),
   unique u2 (CANDIDATE_NUMBER, DEGREE_KEY, EXECUTION_YEAR_KEY))
   type=InnoDB;



#----------------------------
# Table structure for CANDIDATE_SITUATION
#----------------------------
drop table if exists CANDIDATE_SITUATION;
create table CANDIDATE_SITUATION (
   INTERNAL_CODE integer(11) not null auto_increment,
   DATE date not null default '0001-01-01',
   REMARKS text,
   VALIDATION tinyint(4) not null default '1',
   CANDIDATE_KEY integer(11) not null,
   SITUATION integer(11) not null, 
   primary key (INTERNAL_CODE),
   unique u1 (CANDIDATE_KEY, SITUATION)) type=InnoDB;
