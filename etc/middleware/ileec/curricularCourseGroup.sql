#----------------------------
# Table structure for 
#----------------------------
drop table if exists CURRICULAR_COURSE_GROUP;
create table CURRICULAR_COURSE_GROUP (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   KEY_BRANCH int(11) unsigned not null,
   MINIMUM_CREDITS int(11) unsigned not null,
   MAXIMUM_CREDITS int(11) unsigned not null,
   AREA_TYPE int(11) unsigned not null,
   primary key (ID_INTERNAL))
   type=InnoDB ;
   
   
#----------------------------
# Table structure for 
#----------------------------
drop table if exists CURRICULAR_COURSE_SCOPE_GROUP;
create table CURRICULAR_COURSE_SCOPE_GROUP (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_CURRICULAR_COURSE_GROUP int(11) not null,
   KEY_CURRICULAR_COURSE_SCOPE int(11) not null,
   primary key (ID_INTERNAL),
   unique KEY U1(KEY_CURRICULAR_COURSE_GROUP,KEY_CURRICULAR_COURSE_SCOPE))
   type=InnoDB ;