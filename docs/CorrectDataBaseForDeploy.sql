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

alter table CURRICULAR_COURSE
 add column  BASIC bit default '0' after KEY_UNIVERSITY
, type=InnoDB;


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
   primary key (ID_INTERNAL),
   unique u1 (KEY_STUDENT_CURRICULAR_PLAN, GRATUITY_STATE))
   type=InnoDB;
