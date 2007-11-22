create table LIBRARY_CARD (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_PERSON` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11) NOT NULL default '1',
  `USER_NAME` VARCHAR(40) default '',
  `UNIT_NAME` VARCHAR(40) default '',
  `ROLE` VARCHAR(50) default '',
  `PIN` int(5),
  `VALID_UNTIL` VARCHAR(20) default '',
  primary key (ID_INTERNAL)
) type=InnoDB;

ALTER TABLE PARTY ADD COLUMN KEY_LIBRARY_CARD int(11);