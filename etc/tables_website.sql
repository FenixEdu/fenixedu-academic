#----------------------------
# Table structure for WEBSITE
#----------------------------
drop table if exists WEBSITE;
create table WEBSITE (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   NAME varchar(100) not null,
   MAIL varchar(50),
   STYLE varchar(255),
   CLASS_NAME varchar(250),
   primary key (ID_INTERNAL),
   UNIQUE KEY U1( NAME))
   type=InnoDB comment="InnoDB free: 372736 kB";

#----------------------------
# Table structure for WEBSITE_SECTION
#----------------------------
drop table if exists WEBSITE_SECTION;
create table WEBSITE_SECTION (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   NAME varchar(100) not null,
   FTP_NAME varchar(100) not null,
   SIZE int(11) unsigned not null,
   SORTING_ORDER enum('ascendent', 'descendent'),
   EXCERPT_SIZE int(11),
   KEY_WEBSITE int(11) unsigned not null default '0',
   primary key (ID_INTERNAL),
   UNIQUE KEY U1( NAME, KEY_WEBSITE))
   type=InnoDB comment="InnoDB free: 372736 kB";
   
#----------------------------
# Table structure for WEBSITE_ITEM
#----------------------------
drop table if exists WEBSITE_ITEM;
create table WEBSITE_ITEM (
   ID_INTERNAL int(11) unsigned not null auto_increment,
   ACK_OPT_LOCK int(11),
   TITLE varchar(100) not null,
   MAIN_ENTRY_TEXT text not null,
   EXCERPT text,
   PUBLISHED int(11) not null ,
   CREATION_DATE datetime not null,
   KEY_EDITOR int(11) not null,
   AUTHOR_NAME varchar(100) not null,
   AUTHOR_EMAIL varchar(100) not null,
   KEYWORDS text not null,
   ONLINE_BEGIN_DAY date not null,
   ONLINE_END_DAY date not null,
   ITEM_BEGIN_DAY date,
   ITEM_END_DAY date,
   KEY_WEBSITE_SECTION int(11) unsigned not null,
   primary key (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB";
   