alter table CREDITS add OTHER_TYPE_CREDITS float;
drop table if exists crd_OTHER_TYPE_CREDIT_LINE;
create table crd_OTHER_TYPE_CREDIT_LINE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_TEACHER int(11) not null,
   CREDITS float not null,
   REASON TEXT not null,
   KEY_EXECUTION_PERIOD	int(11) not null,
   primary key (ID_INTERNAL)
)type=InnoDB;   
