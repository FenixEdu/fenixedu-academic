#----------------------------
# Table structure for MASTER_DEGREE_CANDIDATE
#----------------------------
drop table if exists MASTER_DEGREE_CANDIDATE;
create table MASTER_DEGREE_CANDIDATE (
   INTERNAL_CODE integer(11) not null auto_increment,
   PERSON_KEY integer(11) not null,
   EXECUTION_DEGREE_KEY integer(11) not null default '0',
   CANDIDATE_NUMBER int(20),
   SPECIALIZATION int(11) not null, 
   MAJOR_DEGREE varchar(100),
   MAJOR_DEGREE_SCHOOL varchar(100),
   MAJOR_DEGREE_YEAR int(11) default '0',
   AVERAGE float,
   primary key (INTERNAL_CODE),
   unique u1 (PERSON_KEY, EXECUTION_DEGREE_KEY, SPECIALIZATION),
   unique u2 (CANDIDATE_NUMBER, EXECUTION_DEGREE_KEY, SPECIALIZATION))
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
   primary key (INTERNAL_CODE)) type=InnoDB;
