#------------------------------------------------------------
# Table structure for table Master Degree Proof Version External Jury
#------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_PROOF_VERSION_EXTERNAL_JURY;
CREATE TABLE MASTER_DEGREE_PROOF_VERSION_EXTERNAL_JURY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_PROOF_VERSION int(11) not null,
  KEY_EXTERNAL_PERSON int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_PROOF_VERSION,KEY_EXTERNAL_PERSON)
) TYPE=InnoDB;

#-------------------------------------------------------------------------------------
# Table structure for table Master Degree Thesis Data Version External Guider
#-------------------------------------------------------------------------------------
DROP TABLE IF EXISTS MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_GUIDER;
CREATE TABLE MASTER_DEGREE_THESIS_DATA_VERSION_EXTERNAL_GUIDER (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_MASTER_DEGREE_THESIS_DATA_VERSION int(11) not null,
  KEY_EXTERNAL_PERSON int(11) not null,
  PRIMARY KEY (ID_INTERNAL),
  UNIQUE KEY U1 (KEY_MASTER_DEGREE_THESIS_DATA_VERSION,KEY_EXTERNAL_PERSON)
) TYPE=InnoDB;
