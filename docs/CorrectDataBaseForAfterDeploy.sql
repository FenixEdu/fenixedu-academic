#----------------------------
# Table structure for StudentGroup
#----------------------------
drop table if exists STUDENT_GROUP;
create table STUDENT_GROUP (
   ID_INTERNAL int(11) unsigned not null auto_increment,   
   GROUP_NUMBER int(11) unsigned not null,
   KEY_SHIFT int(11) unsigned not null,
   KEY_GROUP_PROPERTIES int(11) unsigned not null,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (GROUP_NUMBER, KEY_GROUP_PROPERTIES))
   type=InnoDB comment="InnoDB free: 372736 kB";
   
#----------------------------
# Table structure for StudentGroupAttend
#----------------------------
drop table if exists STUDENT_GROUP_ATTEND;
create table STUDENT_GROUP_ATTEND (
   ID_INTERNAL int(11) unsigned not null auto_increment,   
   KEY_ATTEND int(11) unsigned not null,
   KEY_STUDENT_GROUP int(11) unsigned not null,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (KEY_ATTEND, KEY_STUDENT_GROUP))
   type=InnoDB comment="InnoDB free: 372736 kB";
   
   
#----------------------------
# Table structure for GroupProperties
#----------------------------
drop table if exists GROUP_PROPERTIES;
create table GROUP_PROPERTIES (
   ID_INTERNAL int(11) unsigned not null auto_increment,   
   MAXIMUM_CAPACITY int(11),
   MINIMUM_CAPACITY int(11),
   IDEAL_CAPACITY int(11),
   ENROLMENT_POLICY int(11),
   GROUP_MAXIMUM_NUMBER int(11),
   NAME varchar(50) not null,
   SHIFT_TYPE int(11),
   ENROLMENT_BEGIN_DAY date,
   ENROLMENT_END_DAY date,
   PROJECT_DESCRIPTION text,
   KEY_EXECUTION_COURSE int(11) unsigned not null ,
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (NAME,KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";