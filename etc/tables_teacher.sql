#----------------------------
# Table structure for CAREER
#----------------------------
drop table if exists CAREER;
create table CAREER (
   ID_INTERNAL integer(11) not null auto_increment,
   CLASS_NAME varchar(250) not null,
   BEGIN_YEAR integer(10),
   END_YEAR integer(10),
   KEY_TEACHER integer(11) not null,
   ENTITY varchar(50),
   FUNCTION varchar(50),
   COURSE_OR_POSITION varchar(100),
   KEY_CATEGORY integer(11),
   primary key (ID_INTERNAL))
   type=InnoDB;
