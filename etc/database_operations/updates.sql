


-- Inserted at 2008-11-14T18:37:33.033Z

create table UTIL_EMAIL_REPLY_TO_UTIL_EMAIL_MESSAGE (KEY_MESSAGE int(11) not null, KEY_REPLY_TO int(11) not null,  primary key (KEY_MESSAGE, KEY_REPLY_TO), key(KEY_MESSAGE), key(KEY_REPLY_TO)) type=InnoDB;

create table REPLY_TO (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_SENDER` int(11),
  `REPLY_TO_ADDRESS` text,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_SENDER)
) type=InnoDB ;
