#----------------------------
# Table structure for advisory
#----------------------------
drop table if exists ADVISORY;
create table ADVISORY (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   SENDER varchar(100) not null,
   CREATED TIMESTAMP(8) not null,
   SUBJECT varchar(200) not null,
   MESSAGE text not null,
   EXPIRES date not null,
   ONLY_SHOW_ONCE bit not null default 0,
   primary key (ID_INTERNAL))
   type=InnoDB;

#----------------------------
# Table structure for person_advisory
#----------------------------
drop table if exists PERSON_ADVISORY;
create table PERSON_ADVISORY (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_PERSON int(11) not null,
   KEY_ADVISORY int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_PERSON, KEY_ADVISORY))
   type=InnoDB;
