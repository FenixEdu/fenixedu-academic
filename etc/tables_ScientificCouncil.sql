/*
Mascon Dump
Source Host:           localhost
Source Server Version: 3.23.53-max
Source Database:       ciapl
Date:                  2003-03-11 16:50:53
*/


#----------------------------
# Table structure for DegreeObjectives
#----------------------------
drop table if exists DEGREE_OBJECTIVES;
create table DEGREE_OBJECTIVES (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACKOPTLOCK int(11),
   KEY_DEGREE int(11) unsigned not null default '0',
   GENERAL_OBJECTIVES text,
   OPERACIONAL_OBJECTIVES text,
   STARTING_DATE date,
   END_DATE date,	
   primary key (ID_INTERNAL),
   unique U1 (KEY_DEGREE,STARTING_DATE))
   type=InnoDB ;

