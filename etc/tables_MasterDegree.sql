#----------------------------
# Table structure for MASTER_DEGREE_CANDIDATE
#----------------------------
drop table if exists MASTER_DEGREE_CANDIDATE;
create table MASTER_DEGREE_CANDIDATE (
   ID_INTERNAL integer(11) not null auto_increment,
   PERSON_KEY integer(11) not null,
   EXECUTION_DEGREE_KEY integer(11) not null default '0',
   CANDIDATE_NUMBER int(20),
   SPECIALIZATION int(11) not null, 
   MAJOR_DEGREE varchar(100),
   MAJOR_DEGREE_SCHOOL varchar(100),
   MAJOR_DEGREE_YEAR int(11) default '0',
   AVERAGE float,
   primary key (ID_INTERNAL),
   unique u1 (PERSON_KEY, EXECUTION_DEGREE_KEY, SPECIALIZATION),
   unique u2 (CANDIDATE_NUMBER, EXECUTION_DEGREE_KEY, SPECIALIZATION))
   type=InnoDB;



#----------------------------
# Table structure for CANDIDATE_SITUATION
#----------------------------
drop table if exists CANDIDATE_SITUATION;
create table CANDIDATE_SITUATION (
   ID_INTERNAL integer(11) not null auto_increment,
   DATE date not null default '0001-01-01',
   REMARKS text,
   VALIDATION tinyint(4) not null default '1',
   CANDIDATE_KEY integer(11) not null,
   SITUATION integer(11) not null, 
   primary key (ID_INTERNAL)) type=InnoDB;

#----------------------------
# Table structure for PRICE
#----------------------------

drop table if exists PRICE; 
create table PRICE (
   ID_INTERNAL integer(11) not null auto_increment,
   GRADUATION_TYPE integer(11) not null, 
   DOCUMENT_TYPE integer(11) not null,
   DESCRIPTION varchar(200) not null,
   PRICE float(10,2) not null default '0.00',
   primary key (ID_INTERNAL),
   unique u1 (GRADUATION_TYPE,DOCUMENT_TYPE,DESCRIPTION))
   type=InnoDB;
   
#----------------------------
# Table structure for GUIDE
#----------------------------
drop table if exists GUIDE;
create table GUIDE (

   ID_INTERNAL integer(11) not null auto_increment,
   NUMBER integer(11) not null default '0',
   YEAR integer(4) not null default '0',
   KEY_CONTRIBUTOR integer(11) not null default '0',
   KEY_PERSON integer(11) not null default '0',
   TOTAL float(11,2) not null default '0.00',
   REMARKS text,
   primary key (ID_INTERNAL),
   unique u1 (NUMBER,YEAR))
   type=InnoDB;
   
#----------------------------
# Table structure for GUIDE_ENTRY
#----------------------------
drop table if exists GUIDE_ENTRY;
create table GUIDE_ENTRY(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_GUIDE integer(11) not null default '0',
   GRADUATION_TYPE integer(11), 
   DOCUMENT_TYPE integer(11) not null,
   DESCRIPTION varchar(200),
   PRICE float(10,2) not null default '0.00',
   QUANTITY integer(4) not null default '0',
   primary key (ID_INTERNAL),
   unique u1 (KEY_GUIDE,GRADUATION_TYPE,DOCUMENT_TYPE,DESCRIPTION))
   type=InnoDB;


#----------------------------
# Table structure for GUIDE_SITUATION
#----------------------------
drop table if exists GUIDE_SITUATION;
create table GUIDE_SITUATION(
   ID_INTERNAL integer(11) not null auto_increment,
   KEY_GUIDE integer(11) not null default '0',
   SITUATION integer(11) not null,
   DATE date not null default '0001-01-01',   
   REMARKS text,
   STATE integer(11) not null,
   primary key (ID_INTERNAL),
   unique u1 (KEY_GUIDE,SITUATION))
   type=InnoDB;

   