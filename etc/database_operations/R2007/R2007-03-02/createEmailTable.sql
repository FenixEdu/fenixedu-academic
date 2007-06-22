create table EMAIL (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  FROM_NAME text,
  FROM_ADDRESS text,
  REPLY_TOS longtext,
  TO_ADDRESSES longtext,
  CC_ADDRESSES longtext,
  BCC_ADDRESSES longtext,
  SUBJECT text,
  BODY longtext,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;
