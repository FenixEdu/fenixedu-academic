#----------------------------
# Table structure for StudentCourseReport
#----------------------------
drop table if exists STUDENT_COURSE_REPORT;
create table STUDENT_COURSE_REPORT (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_CURRICULAR_COURSE int(11) unsigned not null ,
   STUDENT_REPORT text,
   STRONG_POINTS text,
   WEAK_POINTS text,
   LAST_MODIFICATION_DATE timestamp(14) not null,   
   primary key (ID_INTERNAL),
   unique U1 (KEY_CURRICULAR_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";