#----------------------------
# Table structure for CONTRIBUTOR
#----------------------------
drop table if exists CONTRIBUTOR;
create table CONTRIBUTOR (
   ID_INTERNAL integer(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   CONTRIBUTOR_NUMBER integer(11) not null,
   CONTRIBUTOR_NAME varchar(100) not null,
   CONTRIBUTOR_ADDRESS varchar(200) not null,
   primary key (ID_INTERNAL),
   unique u1 (CONTRIBUTOR_NUMBER))
   type=InnoDB;
