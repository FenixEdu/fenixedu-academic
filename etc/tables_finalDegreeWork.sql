------------------------------
-- Table structure for FINAL_DEGREE_WORK_PROPOSAL
------------------------------
drop table if exists FINAL_DEGREE_WORK_PROPOSAL;
create table FINAL_DEGREE_WORK_PROPOSAL (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11) not null default '1',
   PROPOSAL_NUMBER int(11) not null,
   KEY_EXECUTION_DEGREE int(11) not null,
   TITLE varchar(100) not null,
   KEY_ORIENTATOR int(11) not null,
   KEY_COORIENTATOR int(11),
   ORIENTATORS_CREDITS_PERCENTAGE int(11) not null,
   COORIENTATORS_CREDITS_PERCENTAGE int(11) not null,
   COMPANION_NAME varchar(100),
   COMPANION_MAIL varchar(100),
   COMPANION_PHONE int(11),
   FRAMING text not null,
   OBJECTIVES text not null,
   DESCRIPTION text not null,
   REQUIREMENTS text,
   DELIVERAVLE text,
   URL varchar(100),
   MINIMUM_NUMBER_OF_GROUP_ELEMENTS int(2),
   MAXIMUM_NUMBER_OF_GROUP_ELEMENTS int(2),
   LOCATION varchar(100),
   DEGREE_TYPE int(11),
   OBSERVATIONS text,
   COMPANY_NAME varchar(100),
   COMPANY_ADRESS varchar(100),
   primary key (ID_INTERNAL),
   index INDEX_KEY_EXECUTION_DEGREE (KEY_EXECUTION_DEGREE),
   index INDEX_KEY_ORIENTATOR (KEY_ORIENTATOR),
   index INDEX_KEY_COORIENTATOR (KEY_COORIENTATOR)
) type=InnoDB;

------------------------------
-- Table structure for FINAL_DEGREE_WORK_SCHEDULEING
------------------------------
drop table if exists FINAL_DEGREE_WORK_SCHEDULEING;
create table FINAL_DEGREE_WORK_SCHEDULEING (
   ID_INTERNAL int(11) not null auto_increment,
   ACK_OPT_LOCK int(11) not null default '1',
   KEY_EXECUTION_DEGREE int(11) not null,
   START_OF_PROPOSAL_PERIOD_DATE date default null,
   START_OF_PROPOSAL_PERIOD_TIME time default null,
   END_OF_PROPOSAL_PERIOD_DATE date default null,
   END_OF_PROPOSAL_PERIOD_TIME time default null,
   START_OF_CANDIDACY_PERIOD_DATE date default null,
   START_OF_CANDIDACY_PERIOD_TIME time default null,
   END_OF_CANDIDACY_PERIOD_DATE date default null,
   END_OF_CANDIDACY_PERIOD_TIME time default null,
   CURRENT_PROPOSAL_NUMBER int (11) not null default '0',
   primary key (ID_INTERNAL),
   unique U1 (KEY_EXECUTION_DEGREE),
   index INDEX_KEY_EXECUTION_DEGREE (KEY_EXECUTION_DEGREE)
) type=InnoDB;

------------------------------
-- Table structure for FINAL_DEGREE_WORK_PROPOSAL_BRANCH
------------------------------
drop table if exists FINAL_DEGREE_WORK_PROPOSAL_BRANCH;
create table FINAL_DEGREE_WORK_PROPOSAL_BRANCH (
   ID_INTERNAL int(11) not null auto_increment,
   KEY_FINAL_DEGREE_WORK_PROPOSAL int(11) not null,
   KEY_BRANCH int(11) not null,
   primary key (ID_INTERNAL),
   index INDEX_KEY_FINAL_DEGREE_WORK_PROPOSAL (KEY_FINAL_DEGREE_WORK_PROPOSAL),
   index INDEX_KEY_BRANCH (KEY_BRANCH)
) type=InnoDB;