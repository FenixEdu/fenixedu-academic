create table UNIT_NAME (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  NAME varchar(255),
  KEY_UNIT int(11) not null,
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_UNIT)
) Type=InnoDB;

insert into UNIT_NAME (ID_INTERNAL, NAME, KEY_UNIT) select PARTY.ID_INTERNAL, PARTY.NAME, PARTY.ID_INTERNAL from PARTY where PARTY.OJB_CONCRETE_CLASS like '%Unit';
alter table PARTY add column KEY_UNIT_NAME  int(11) default null;
alter table PARTY add index (KEY_UNIT_NAME);
update PARTY set PARTY.KEY_UNIT_NAME = PARTY.ID_INTERNAL;

create table UNIT_NAME_PART (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  NAME_PART varchar(255),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  unique (NAME_PART)
) Type=InnoDB;

create table UNIT_NAME_UNIT_NAME_PART (
  KEY_UNIT_NAME int(11) not null,
  KEY_UNIT_NAME_PART int(11) not null,
  primary key (KEY_UNIT_NAME, KEY_UNIT_NAME_PART),
  index (KEY_UNIT_NAME),
  index (KEY_UNIT_NAME_PART)
) Type=InnoDB;

alter table UNIT_NAME add column IS_EXTERNAL_UNIT tinyint(1);