ALTER TABLE MASTER_DEGREE_CANDIDATE ADD GIVEN_CREDITS FLOAT;
ALTER TABLE MASTER_DEGREE_CANDIDATE ADD GIVEN_CREDITS_REMARKS TEXT;

#----------------------------
# Table structure for CANDIDATE_ENROLMENT
#----------------------------
drop table if exists CANDIDATE_ENROLMENT;
create table CANDIDATE_ENROLMENT(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_MASTER_DEGREE_CANDIDATE integer(11) not null ,
   KEY_CURRICULAR_COURSE_SCOPE integer(11) not null ,
   primary key (ID_INTERNAL),
   unique u1 (KEY_MASTER_DEGREE_CANDIDATE, KEY_CURRICULAR_COURSE_SCOPE))
   type=InnoDB;


drop table if exists QUALIFICATION;
CREATE TABLE QUALIFICATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PERSON int(11) NOT NULL,
  YEAR int(11) default NULL,
  MARK varchar(200) default NULL,
  SCHOOL varchar(200) default NULL,
  TITLE varchar(200) default NULL,
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;drop table if exists GRATUITY;

drop table if exists GRATUITY;
create table GRATUITY(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_STUDENT_CURRICULAR_PLAN integer(11) not null ,
   STATE integer(11),
   GRATUITY_STATE integer(11),
   DATE date, 
   REMARKS text,
   primary key (ID_INTERNAL))
   type=InnoDB;

#----------------------------
# Table structure for advisory
#----------------------------
drop table if exists ADVISORY;
create table ADVISORY (
   ID_INTERNAL int(11) not null auto_increment,
   SENDER varchar(100) not null,
   CREATED TIMESTAMP(8) not null,
   SUBJECT varchar(200) not null,
   MESSAGE text not null,
   EXPIRES date not null,
   ONLY_SHOW_ONCE bit not null default 0,
   primary key (ID_INTERNAL))
   type=InnoDB;

#----------------------------
# Table structure for person_advisory
#----------------------------
drop table if exists PERSON_ADVISORY;
create table PERSON_ADVISORY (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_PERSON int(11) not null,
   KEY_ADVISORY int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_PERSON, KEY_ADVISORY))
   type=InnoDB;
   