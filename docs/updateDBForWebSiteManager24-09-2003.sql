#----------------------------
# Table structure for WEBSITE
#----------------------------
drop table if exists WEBSITE;
create table WEBSITE (
   ID_INTERNAL int(11) unsigned not null auto_increment,
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
   NAME varchar(100) not null,
   SIZE int(11) unsigned not null,
   SORTING_ORDER enum('ascendent', 'descendent') not null,
   WHAT_TO_SORT enum('CREATION_DATE', 'ITEM_BEGIN_DAY', 'ITEM_END_DAY') not null,
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
   TITLE varchar(100) not null,
   MAIN_ENTRY_TEXT text not null,
   EXCERPT text,
   PUBLISHED int(11) not null ,
   CREATION_DATE datetime not null,
   KEY_EDITOR int(11) not null,
   KEYWORDS text not null,
   ONLINE_BEGIN_DAY date not null,
   ONLINE_END_DAY date not null,
   ITEM_BEGIN_DAY date,
   ITEM_END_DAY date,
   KEY_WEBSITE_SECTION int(11) unsigned not null,
   primary key (ID_INTERNAL))
   type=InnoDB comment="InnoDB free: 372736 kB";

insert into ROLE (ID_INTERNAL, ROLE_TYPE, PORTAL_SUB_APPLICATION, PAGE, PAGE_NAME_PROPERTY) values (22,21,'/webSiteManager','/index.do','portal.webSiteManager');

# website management user - Jorge Amador: numero mecanografico 3490
insert into PERSON_ROLE (ID_INTERNAL, KEY_ROLE, KEY_PERSON) values (0, 22, 2525);

insert into WEBSITE (ID_INTERNAL, NAME, MAIL, STYLE, CLASS_NAME) values(1, 'Instituto Superior Técnico', null, null, 'Dominio.SiteIST');

insert into WEBSITE_SECTION (ID_INTERNAL, NAME, SIZE, SORTING_ORDER, WHAT_TO_SORT, EXCERPT_SIZE, KEY_WEBSITE) values (1, 'Notícias', 5, 2, 1, 30, 1);
insert into WEBSITE_SECTION (ID_INTERNAL, NAME, SIZE, SORTING_ORDER, WHAT_TO_SORT, EXCERPT_SIZE, KEY_WEBSITE) values (2, 'Eventos', 5, 1, 2, 30, 1);