alter table SHIFT_STUDENT change column KEY_STUDENT KEY_REGISTRATION int(11) NOT NULL default '0';
rename table STUDENT to REGISTRATION;

alter table PARTY add column KEY_STUDENT int(11) NULL;

drop table if exists STUDENT;
create table STUDENT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL,
  NUMBER int(11) NOT NULL,
  KEY_PERSON int(11) NULL,
  primary key (ID_INTERNAL)
) type=InnoDB;

alter table REGISTRATION add column KEY_STUDENT int(11) NULL;