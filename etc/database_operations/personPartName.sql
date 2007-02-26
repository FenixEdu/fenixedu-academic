create table PERSON_NAME (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  NAME varchar(255),
  KEY_PERSON int(11) not null,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_PERSON)
) Type=InnoDB;

insert into PERSON_NAME (ID_INTERNAL, NAME, KEY_PERSON) select PARTY.ID_INTERNAL, PARTY.NAME, PARTY.ID_INTERNAL from PARTY where PARTY.OJB_CONCRETE_CLASS like '%Person';
alter table PARTY add column KEY_PERSON_NAME  int(11) default null;
alter table PARTY add index (KEY_PERSON_NAME);
update PARTY set PARTY.KEY_PERSON_NAME = PARTY.ID_INTERNAL;

create table PERSON_NAME_PART (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  NAME_PART varchar(255),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  unique (NAME_PART)
) Type=InnoDB;

create table PERSON_NAME_PERSON_NAME_PART (
  KEY_PERSON_NAME int(11) not null,
  KEY_PERSON_NAME_PART int(11) not null,
  primary key (KEY_PERSON_NAME, KEY_PERSON_NAME_PART),
  index (KEY_PERSON_NAME),
  index (KEY_PERSON_NAME_PART)
) Type=InnoDB;

alter table PERSON_NAME add column IS_EXTERNAL_PERSON tinyint(1);