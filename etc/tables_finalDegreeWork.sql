------------------------------
-- Table structure for FINAL_DEGREE_WORK_PROPOSAL
------------------------------
drop table if exists FINAL_DEGREE_WORK_PROPOSAL;
create table FINAL_DEGREE_WORK_PROPOSAL (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11) not null default '1',
   KEY_DEGREE_CURRICULAR_PLAN int(11) not null,
   TITLE varchar(100) not null,
   KEY_ORIENTATOR int(11) not null,
   KEY_COORIENTATOR int(11),
   ORIENTATORS_CREDITS_PERCENTAGE int(11) not null,
   COORIENTATORS_CREDITS_PERCENTAGE int(11) not null,
   OBJECTIVES text not null,
   DESCRIPTION text not null,
   REQUIREMENTS text,
   URL varchar(100),
   NUMBER_OF_GROUP_ELEMENTS int(2),
   DEGREE_TYPE int(11),
   PART_A text,
   PART_B text,
   OBSERVATIONS text,
   COMPANY_NAME varchar(100),
   COMPANY_LINK_RESPONSABLE varchar(100),
   COMPANY_CONTACT varchar(100),
   primary key (ID_INTERNAL),
   index INDEX_KEY_DEGREE_CURRICULAR_PLAN (KEY_DEGREE_CURRICULAR_PLAN),
   index INDEX_KEY_ORIENTATOR (KEY_ORIENTATOR),
   index INDEX_KEY_COORIENTATOR (KEY_COORIENTATOR)
) type=InnoDB;
