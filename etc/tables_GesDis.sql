/*
Mascon Dump
Source Host:           localhost
Source Server Version: 3.23.53-max
Source Database:       ciapl
Date:                  2003-03-11 16:50:53
*/

#----------------------------
# Table structure for announcement
#----------------------------
drop table if exists ANNOUNCEMENT;
create table ANNOUNCEMENT (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   TITLE varchar(100),
   CREATION_DATE timestamp,
   LAST_MODIFICATION_DATE timestamp,
   INFORMATION varchar(100),
   KEY_SITE int(11) unsigned not null default '0',
   primary key (ID_INTERNAL),
   UNIQUE KEY U1(TITLE, KEY_SITE, CREATION_DATE))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for curriculum
#----------------------------
drop table if exists CURRICULUM;
create table CURRICULUM (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_COURSE int(11) not null default '0',
   GENERAL_OBJECTIVES varchar(50),
   OPERACIONAL_OBJECTIVES varchar(50),
   PROGRAM varchar(50),
   primary key (ID_INTERNAL),
   UNIQUE KEY U1(KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for item
#----------------------------
drop table if exists ITEM;
create table ITEM (
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
# Table structure for responsiblefor
#----------------------------
drop table if exists RESPONSIBLEFOR;
create table RESPONSIBLEFOR (
  INTERNAL_CODE integer(11) not null auto_increment,
   KEY_TEACHER int(11) unsigned not null default '0',
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   primary key (INTERNAL_CODE),
    UNIQUE KEY U1(KEY_TEACHER, KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for professorships
#----------------------------
drop table if exists PROFESSORSHIPS;
create table PROFESSORSHIPS (
   INTERNAL_CODE integer(11) not null auto_increment,
   KEY_TEACHER int(11) unsigned not null default '0',
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   primary key (INTERNAL_CODE),
    UNIQUE KEY U1(KEY_TEACHER, KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for section
#----------------------------
drop table if exists SECTION;
create table SECTION (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   NAME varchar(100),
   SECTION_ORDER int(11) unsigned,
   KEY_SITE int(10) unsigned not null default '0',
   KEY_SUPERIOR_SECTION int(10) unsigned,
   LAST_MODIFIED_DATE date,
   primary key (ID_INTERNAL),
   UNIQUE KEY U1(NAME, KEY_SITE, KEY_SUPERIOR_SECTION))
   type=InnoDB comment="InnoDB free: 372736 kB; InnoDB free: 372736 kB";

#----------------------------
# Table structure for site
#----------------------------
drop table if exists SITE;
create table SITE (
   ID_INTERNAL int(11) unsigned not null auto_increment,   
   KEY_EXECUTION_COURSE int(11) unsigned not null default '0',
   KEY_INITIAL_SECTION int(11),
   ALTERNATIVE_SITE varchar(50),
   MAIL varchar(50),
   primary key (ID_INTERNAL),
   unique ID_INTERNAL (ID_INTERNAL, KEY_EXECUTION_COURSE))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for bibliographic_reference
#----------------------------
drop table if exists BIBLIOGRAPHIC_REFERENCE;
create table BIBLIOGRAPHIC_REFERENCE (
   ID_INTERNAL int(50) not null auto_increment,
   TITLE varchar(50) not null,
   AUTHORS varchar(50) not null,
   REFERENCE varchar(50) not null,
   `YEAR` varchar(50) not null,
   OPTIONAL int(11) not null default '0',
   KEY_EXECUTION_COURSE int(11) not null default '0',
   primary key (ID_INTERNAL),
   unique U1 (TITLE, AUTHORS, REFERENCE, `YEAR`))
   type=InnoDB comment="InnoDB free: 372736 kB";


