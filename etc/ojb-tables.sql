#----------------------------
# Table structure for ojb_dlist
#----------------------------
drop table if exists OJB_DLIST;
create table OJB_DLIST (
   ID int(11) not null default '0',
   SIZE_ int(11),
   primary key (ID))
   type=InnoDB;

#----------------------------
# Table structure for ojb_dlist_entries
#----------------------------
drop table if exists OJB_DLIST_ENTRIES;
create table OJB_DLIST_ENTRIES (
   ID int(11) not null default '0',
   DLIST_ID int(11) not null default '0',
   POSITION_ int(11),
   OID_ longblob,
   primary key (ID))
   type=InnoDB;

#----------------------------
# Table structure for ojb_dmap
#----------------------------
drop table if exists OJB_DMAP;
create table OJB_DMAP (
   ID int(11) not null default '0',
   SIZE_ int(11),
   primary key (ID))
   type=InnoDB;

#----------------------------
# Table structure for ojb_dmap_entries
#----------------------------
drop table if exists OJB_DMAP_ENTRIES;
create table OJB_DMAP_ENTRIES (
   ID int(11) not null default '0',
   DMAP_ID int(11) not null default '0',
   KEY_OID longblob,
   VALUE_OID longblob,
   primary key (ID))
   type=InnoDB;

#----------------------------
# Table structure for ojb_dset
#----------------------------
drop table if exists OJB_DSET;
create table OJB_DSET (
   ID int(11) not null default '0',
   SIZE_ int(11),
   primary key (ID))
   type=InnoDB;

#----------------------------
# Table structure for ojb_dset_entries
#----------------------------
drop table if exists OJB_DSET_ENTRIES;
create table OJB_DSET_ENTRIES (
   ID int(11) not null default '0',
   DLIST_ID int(11) not null default '0',
   POSITION_ int(11),
   OID_ longblob,
   primary key (ID))
   type=InnoDB;

#----------------------------
# Table structure for ojb_hl_seq
#----------------------------
drop table if exists OJB_HL_SEQ;
create table OJB_HL_SEQ (
   TABLENAME varchar(175) not null,
   FIELDNAME varchar(70) not null,
   MAX_KEY int(11),
   GRAB_SIZE int(11),
   primary key (TABLENAME, FIELDNAME))
   type=InnoDB;

#----------------------------
# Table structure for ojb_lockentry
#----------------------------
drop table if exists OJB_LOCKENTRY;
create table OJB_LOCKENTRY (
   OID_ varchar(250) not null,
   TX_ID varchar(50) not null,
   TIMESTAMP_ decimal(10,0),
   ISOLATIONLEVEL int(11),
   LOCKTYPE int(11),
   primary key (OID_, TX_ID))
   type=InnoDB;

#----------------------------
# Table structure for ojb_nrm
#----------------------------
drop table if exists OJB_NRM;
create table OJB_NRM (
   NAME varchar(250) not null,
   OID_ longblob,
   primary key (NAME))
   type=InnoDB;

#----------------------------
# Table structure for ojb_seq
#----------------------------
drop table if exists OJB_SEQ;
create table OJB_SEQ (
   TABLENAME varchar(175) not null,
   FIELDNAME varchar(70) not null,
   LAST_NUM int(11),
   primary key (TABLENAME, FIELDNAME))
   type=InnoDB;


