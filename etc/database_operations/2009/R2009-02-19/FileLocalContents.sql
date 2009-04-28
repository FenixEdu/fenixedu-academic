alter table FILE add column KEY_LOCAL_CONTENT int(11);
alter table FILE add index (KEY_LOCAL_CONTENT);
alter table FILE change column MIME_TYPE MIME_TYPE varchar(48) NULL;
alter table FILE change column CHECKSUM CHECKSUM varchar(64) NULL;
alter table FILE change column CHECKSUM_ALGORITHM CHECKSUM_ALGORITHM varchar(32) NULL;
alter table FILE change column EXTERNAL_STORAGE_IDENTIFICATION EXTERNAL_STORAGE_IDENTIFICATION varchar(255) NULL UNIQUE;

create table FILE_LOCAL_CONTENT (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `CONTENT` longblob NOT NULL,
  `PATH` text,
  `KEY_FILE` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  primary key (ID_INTERNAL),
  index (KEY_FILE),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

create table FILE_LOCAL_CONTENT_METADATA (
  `ENTRY` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_CONTENT` int(11),
  primary key (ID_INTERNAL),
  index (KEY_CONTENT)
) type=InnoDB ;

