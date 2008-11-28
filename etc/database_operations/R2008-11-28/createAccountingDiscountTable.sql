create table DISCOUNT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_EVENT int(11),
  KEY_ROOT_DOMAIN_OBJECT int(11),
  AMOUNT varchar(100),
  USERNAME varchar(100),
  WHEN_CREATED datetime NULL default NULL,
  primary key (ID_INTERNAL),
  index (KEY_EVENT),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;
