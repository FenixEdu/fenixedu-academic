  
#----------------------------
# Table structure for room occupation
#----------------------------
create table ROOM_OCCUPATION (
   ID_INTERNAL int(11) not null auto_increment,
   START_TIME time not null,
   END_TIME time not null,
   WEEKDAY int(11) not null,
   KEY_ROOM int(11) not null,
   KEY_PERIOD int(11) not null,
   ACK_OPT_LOCK int(11) not null default '1',
   primary key (ID_INTERNAL),
   unique U1 (START_TIME,END_TIME,KEY_ROOM,KEY_PERIOD))
   type=InnoDB;

#----------------------------
# Table structure for period
#----------------------------
create table PERIOD (
   ID_INTERNAL int(11) not null auto_increment,
   START_DATE date not null,
   END_DATE date not null,
   ACK_OPT_LOCK int(11) not null default '1',
   primary key (ID_INTERNAL),
   unique U1 (START_DATE,END_DATE))
   type=InnoDB;


#----------------------------
# Table structure for writtenEvaluation_roomOccupation
#----------------------------
create table WRITTEN_EVALUATION_ROOM_OCCUPATION (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_WRITTEN_EVALUATION int(11) not null,
   KEY_ROOM_OCCUPATION int(11) not null,
   ACK_OPT_LOCK int(11) not null default '1',
   primary key (ID_INTERNAL),
   unique U1 (KEY_ROOM_OCCUPATION, KEY_WRITTEN_EVALUATION))
   type=InnoDB;
   
#----------------------------
# Table structure for writtenEvaluation_curricularCourseScope
#----------------------------
create table WRITTEN_EVALUATION_CURRICULAR_COURSE_SCOPE (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_WRITTEN_EVALUATION int(11) not null,
   KEY_CURRICULAR_COURSE_SCOPE int(11) not null,
   ACK_OPT_LOCK int(11) not null default '1',
   primary key (ID_INTERNAL),
   unique U1 (KEY_WRITTEN_EVALUATION, KEY_CURRICULAR_COURSE_SCOPE))
   type=InnoDB;
   
alter table EXAM_EXECUTION_COURSE alter ACK_OPT_LOCK set default '1';