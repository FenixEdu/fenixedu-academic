/*
Mascon Dump
Source Host:           localhost
Source Server Version: 3.23.53-max
Source Database:       ciapl
Date:                  2003-03-11 16:50:53
*/

use ciapl ;
#----------------------------
# Table structure for announcement
#----------------------------
drop table if exists announcement;
create table announcement (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   TITLE varchar(100),
   CREATION_DATE date,
   LAST_MODIFICATION_DATE date,
   INFORMATION varchar(100),
   KEY_SITE int(11) unsigned not null default '0',
   primary key (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for curriculum
#----------------------------
drop table if exists curriculum;
create table curriculum (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_COURSE int(11) not null default '0',
   GENERAL_OBJECTIVES varchar(50),
   OPERACIONAL_OBJECTIVES varchar(50),
   PROGRAM varchar(50),
   primary key (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for item
#----------------------------
drop table if exists item;
create table item (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   NAME varchar(100),
   ITEM_ORDER int(11) unsigned,
   INFORMATION text,
   URGENT int(11) unsigned,
   KEY_SECTION int(11) unsigned not null default '0',
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for owns
#----------------------------
drop table if exists owns;
create table owns (
   ID_TEACHER int(11) unsigned not null default '0',
   ID_SITE int(11) unsigned not null default '0',
   primary key (ID_TEACHER, ID_SITE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for professorships
#----------------------------
drop table if exists professorships;
create table professorships (
   ID_TEACHER int(11) unsigned not null default '0',
   ID_SITE int(11) unsigned not null default '0',
   primary key (ID_TEACHER, ID_SITE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for section
#----------------------------
drop table if exists section;
create table section (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   NAME varchar(100),
   SECTION_ORDER int(11) unsigned,
   KEY_SITE int(10) unsigned not null default '0',
   KEY_SUPERIOR_SECTION int(10) unsigned not null default '0',
   LAST_MODIFIED_DATE date,
   primary key (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for site
#----------------------------
drop table if exists site;
create table site (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   KEY_INITIAL_SECTION int(11) default '0',
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (ID_INTERNAL, KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for teacher
#----------------------------
drop table if exists teacher;
create table teacher (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   USERNAME varchar(20),
   `PASSWORD` varchar(16),
   TEACHER_NUMBER int(10) unsigned,
   primary key (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB";


