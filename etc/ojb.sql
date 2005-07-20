--
-- Table structure for table `OJB_DLIST`
--

DROP TABLE IF EXISTS OJB_DLIST;
CREATE TABLE OJB_DLIST (
  ID int(11) NOT NULL default '0',
  SIZE_ int(11) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DLIST_ENTRIES`
--

DROP TABLE IF EXISTS OJB_DLIST_ENTRIES;
CREATE TABLE OJB_DLIST_ENTRIES (
  ID int(11) NOT NULL default '0',
  DLIST_ID int(11) NOT NULL default '0',
  POSITION_ int(11) default NULL,
  OID_ longblob,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DMAP`
--

DROP TABLE IF EXISTS OJB_DMAP;
CREATE TABLE OJB_DMAP (
  ID int(11) NOT NULL default '0',
  SIZE_ int(11) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DMAP_ENTRIES`
--

DROP TABLE IF EXISTS OJB_DMAP_ENTRIES;
CREATE TABLE OJB_DMAP_ENTRIES (
  ID int(11) NOT NULL default '0',
  DMAP_ID int(11) NOT NULL default '0',
  KEY_OID longblob,
  VALUE_OID longblob,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DSET`
--

DROP TABLE IF EXISTS OJB_DSET;
CREATE TABLE OJB_DSET (
  ID int(11) NOT NULL default '0',
  SIZE_ int(11) default NULL,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_DSET_ENTRIES`
--

DROP TABLE IF EXISTS OJB_DSET_ENTRIES;
CREATE TABLE OJB_DSET_ENTRIES (
  ID int(11) NOT NULL default '0',
  DLIST_ID int(11) NOT NULL default '0',
  POSITION_ int(11) default NULL,
  OID_ longblob,
  PRIMARY KEY  (ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_HL_SEQ`
--

DROP TABLE IF EXISTS OJB_HL_SEQ;
CREATE TABLE OJB_HL_SEQ (
  TABLENAME varchar(175) NOT NULL default '',
  FIELDNAME varchar(70) NOT NULL default '',
  MAX_KEY int(11) default NULL,
  GRAB_SIZE int(11) default NULL,
  VERSION int(11) default NULL,
  PRIMARY KEY  (TABLENAME,FIELDNAME)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_LOCKENTRY`
--

DROP TABLE IF EXISTS OJB_LOCKENTRY;
CREATE TABLE OJB_LOCKENTRY (
  OID_ varchar(250) NOT NULL default '',
  TX_ID varchar(50) NOT NULL default '',
  TIMESTAMP_ decimal(10,0) default NULL,
  ISOLATIONLEVEL int(11) default NULL,
  LOCKTYPE int(11) default NULL,
  PRIMARY KEY  (OID_,TX_ID)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_NRM`
--

DROP TABLE IF EXISTS OJB_NRM;
CREATE TABLE OJB_NRM (
  NAME varchar(250) NOT NULL default '',
  OID_ longblob,
  PRIMARY KEY  (NAME)
) TYPE=InnoDB;

--
-- Table structure for table `OJB_SEQ`
--

DROP TABLE IF EXISTS OJB_SEQ;
CREATE TABLE OJB_SEQ (
  TABLENAME varchar(175) NOT NULL default '',
  FIELDNAME varchar(70) NOT NULL default '',
  LAST_NUM int(11) default NULL,
  PRIMARY KEY  (TABLENAME,FIELDNAME)
) TYPE=InnoDB;
