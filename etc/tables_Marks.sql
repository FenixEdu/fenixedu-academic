#----------------------------
# Table structure for MARK
#----------------------------
drop table if exists MARK;
create table MARK (
   ID_INTERNAL int(11) not null auto_increment,
   MARK varchar (4),
   PUBLISHED_MARK varchar(4),
   KEY_EXAM int(11) not null,
   KEY_ATTEND int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXAM, KEY_ATTEND))
   type=InnoDB;