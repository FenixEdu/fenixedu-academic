# ----------------------------
#  Table structure for DELEGATE
# ----------------------------
drop table if exists DELEGATE;
create table DELEGATE (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_DEGREE int(11) not null,
   KEY_STUDENT int(11) not null,
   KEY_EXECUTION_YEAR int(11) not null,
   YEAR_TYPE int(11) not null,
   TYPE bit not null,
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_YEAR, KEY_STUDENT, YEAR_TYPE),
   unique U2 (KEY_EXECUTION_YEAR, KEY_STUDENT, TYPE)
) type=InnoDB;
