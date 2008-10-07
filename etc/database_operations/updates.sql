


-- Inserted at 2008-10-03T17:10:33.996+01:00

create table UTIL_EMAIL_MESSAGE_UTIL_EMAIL_RECIPIENT (KEY_MESSAGE int(11) not null, KEY_RECIPIENT int(11) not null,  primary key (KEY_MESSAGE, KEY_RECIPIENT), key(KEY_MESSAGE), key(KEY_RECIPIENT)) type=InnoDB;
create table MESSAGE (
  `BODY` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_PERSON` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_SENDER` int(11),
  `SUBJECT` text,
  primary key (ID_INTERNAL),
  index (KEY_PERSON),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_SENDER)
) type=InnoDB ;

create table RECIPIENT (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `MEMBERS` text,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

create table SENDER (
  `FROM_ADDRESS` text,
  `FROM_NAME` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `MEMBERS` text,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;




-- Inserted at 2008-10-06T16:02:27.115+01:00

create table UTIL_EMAIL_SENDER_UTIL_EMAIL_RECIPIENT (KEY_SENDER int(11) not null, KEY_RECIPIENT int(11) not null,  primary key (KEY_SENDER, KEY_RECIPIENT), key(KEY_SENDER), key(KEY_RECIPIENT)) type=InnoDB;
alter table RECIPIENT add column TO_NAME text;
alter table MESSAGE add column CREATED timestamp NULL default NULL;
alter table MESSAGE add column SENT timestamp NULL default NULL;
alter table MESSAGE add column BCCS text;
alter table MESSAGE add column KEY_ROOT_DOMAIN_OBJECT_FROM_PENDING_RELATION int(11);
alter table MESSAGE add index (KEY_ROOT_DOMAIN_OBJECT_FROM_PENDING_RELATION);

