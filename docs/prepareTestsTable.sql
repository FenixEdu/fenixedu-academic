ALTER TABLE METADATA add INDEX index1(KEY_EXECUTION_COURSE, VISIBILITY);

drop table if exists DISTRIBUTED_TEST_ADVISORY;
CREATE TABLE DISTRIBUTED_TEST_ADVISORY(
  ID_INTERNAL int(11) unsigned not null auto_increment,
  KEY_DISTRIBUTED_TEST int(11) unsigned not null,
  KEY_ADVISORY int(11) unsigned not null,
  ACK_OPT_LOCK int(11),
  PRIMARY KEY  (ID_INTERNAL)
) TYPE=InnoDB;