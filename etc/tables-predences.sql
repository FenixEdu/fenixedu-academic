#----------------------------
# Table structure for PRECEDENCE
#----------------------------
drop table if exists PRECEDENCE;
create table PRECEDENCE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_COURSE int(11) not null,
   ENROLMENT_STEP enum ('online','offline') not null,
   primary key (ID_INTERNAL))
   type=InnoDB;

#----------------------------
# Table structure for RESTRICTION
#----------------------------
DROP TABLE IF EXISTS RESTRICTION;
CREATE TABLE RESTRICTION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  CLASS_NAME varchar(250) NOT NULL,
  KEY_PRECEDENCE int(11) not null,
  KEY_CURRICULAR_COURSE int (11),
  NUMBER_OF_CURRICULAR_COURSE_DONE int(11),
  primary key (ID_INTERNAL)) TYPE=InnoDB;


