------------------------------
-- Table structure for PRECEDENCE
-- Column :SCOPE_TO_APPLY tells in which step of the enrolment this precedence is to be applied
--			"SP" means that precedence is to be applied in calculation
--			"ENR" means that precedence is to be applied during student enrolment
------------------------------
drop table if exists PRECEDENCE;
create table PRECEDENCE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_CURRICULAR_COURSE int(11) not null,
   SCOPE_TO_APPLY varchar(3) not null,
   primary key (ID_INTERNAL))
   type=InnoDB;

------------------------------
-- Table structure for RESTRICTION
------------------------------
DROP TABLE IF EXISTS RESTRICTION;
CREATE TABLE RESTRICTION (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  ACK_OPT_LOCK int(11),
  CLASS_NAME varchar(250) NOT NULL,
  KEY_PRECEDENCE int(11) not null,
  KEY_CURRICULAR_COURSE int (11),
  NUMBER_OF_CURRICULAR_COURSE_DONE int(11),
  primary key (ID_INTERNAL)) TYPE=InnoDB;


