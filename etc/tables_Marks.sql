#----------------------------
# Table structure for mark
#----------------------------
drop table if exists MARK;
create table MARK (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   MARK varchar (4),
   PUBLISHED_MARK varchar(4),
   KEY_EVALUATION int(11) not null,
   KEY_ATTEND int(11) not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EVALUATION, KEY_ATTEND))
   type=InnoDB;