#----------------------------
# Table structure for room occupation
#----------------------------
drop table if exists ROOM_OCCUPATION;
create table ROOM_OCCUPATION (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   START_TIME time not null,
   END_TIME time not null,
   WEEKDAY int(11) not null,
   KEY_ROOM int(11) not null,
   KEY_PERIOD int(11) not null,
   primary key (ID_INTERNAL)
) type=InnoDB;

#----------------------------
# Table structure for period
#----------------------------
drop table if exists PERIOD;
create table PERIOD (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   START_DATE date not null,
   END_DATE date not null,
   primary key (ID_INTERNAL)
) type=InnoDB;

#----------------------------
# Table structure for writtenEvaluation_roomOccupation
#----------------------------
drop table if exists WRITTEN_EVALUATION_ROOM_OCCUPATION;
create table WRITTEN_EVALUATION_ROOM_OCCUPATION (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_WRITTEN_EVALUATION int(11) not null,
   KEY_ROOM_OCCUPATION int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_ROOM_OCCUPATION, KEY_WRITTEN_EVALUATION))
   type=InnoDB;
   
   #----------------------------
# Table structure for writtenEvaluation_curricularCourseScope
#----------------------------
drop table if exists WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE;
create table WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_WRITTEN_EVALUATION int(11) not null,
   KEY_CURRICULAR_COURSE_SCOPE int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_WRITTEN_EVALUATION, KEY_CURRICULAR_COURSE_SCOPE))
   type=InnoDB;
   
