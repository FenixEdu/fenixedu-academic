
-- ----------------------------
--  Table structure for COORDINATOR
-- ----------------------------
drop table if exists COORDINATOR;
create table COORDINATOR (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_EXECUTION_DEGREE int(11) not null,
   KEY_TEACHER int(11) not null,
   RESPONSIBLE bit not null default '0',
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_DEGREE,KEY_TEACHER)
) type=InnoDB;



