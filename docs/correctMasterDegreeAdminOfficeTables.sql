ALTER TABLE EXTERNAL_PERSON DROP COLUMN WORK_LOCATION;
ALTER TABLE EXTERNAL_PERSON ADD COLUMN KEY_WORK_LOCATION int(11) unsigned not null;

#------------------------------------------------------------
# Table structure for table work location
#------------------------------------------------------------
DROP TABLE IF EXISTS WORK_LOCATION;
CREATE TABLE WORK_LOCATION (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  NAME varchar(255) not null,
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE (NAME)
) TYPE=InnoDB;
