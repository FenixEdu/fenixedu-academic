
#----------------------------
# Table structure for REIMBURSEMENT_GUIDE
#----------------------------
drop table if exists REIMBURSEMENT_GUIDE;
create table REIMBURSEMENT_GUIDE (
   ID_INTERNAL integer(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   NUMBER integer(11) not null default '0',
   KEY_GUIDE integer(11) not null,
   CREATION_DATE date not null,  
   primary key (ID_INTERNAL),
   unique U1 (NUMBER))
   type=InnoDB;
   
#----------------------------
# Table structure for REIMBURSEMENT_GUIDE_SITUATION
#----------------------------
drop table if exists REIMBURSEMENT_GUIDE_SITUATION;
create table REIMBURSEMENT_GUIDE_SITUATION (
   ID_INTERNAL integer(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   STATE integer(11) not null default '0',
   KEY_REIMBURSEMENT_GUIDE integer(11) not null,
   REMARKS text,
   KEY_EMPLOYEE integer(11) not null,
   MODIFICATION_DATE date not null, 
   OFFICIAL_DATE date not null, 
   REIMBURSEMENT_GUIDE_STATE integer(11) not null,  
   primary key (ID_INTERNAL))
   type=InnoDB;   

#----------------------------
# Table structure for REIMBURSEMENT_GUIDE_ENTRY
#----------------------------
drop table if exists REIMBURSEMENT_GUIDE_ENTRY;
create table REIMBURSEMENT_GUIDE_ENTRY(
   ID_INTERNAL integer(11) not null auto_increment,
   ACK_OPT_LOCK int(11),
   KEY_REIMBURSEMENT_GUIDE integer(11) not null default '0',
   KEY_GUIDE_ENTRY integer(11) not null default '0',
   JUSTIFICATION text,
   VALUE float(11,2) not null default '0.00',
   primary key (ID_INTERNAL))
   type=InnoDB;

