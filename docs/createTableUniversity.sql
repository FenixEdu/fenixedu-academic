-- ---------------------------------
--Table structure for UNIVERSITY
-- ---------------------------------
drop table if exists UNIVERSITY;
create table UNIVERSITY (
   ID_INTERNAL int(11) not null auto_increment,
   CODE varchar(10) not null,
   NAME varchar(150) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE,NAME)
)type=InnoDB;
