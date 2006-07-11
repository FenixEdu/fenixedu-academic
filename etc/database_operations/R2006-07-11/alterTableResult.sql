drop table RESULT_PARTICIPATION;
create table RESULT_PARTICIPATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PERSON int(11) NOT NULL,
  KEY_RESULT int(11) NOT NULL,
  PERSON_ORDER int(11),
  RESULT_PARTICIPATION_ROLE varchar(50) default NULL,
  KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  UNIQUE(KEY_PERSON, KEY_RESULT, RESULT_PARTICIPATION_ROLE)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table RESULT add column KEY_PUBLISHER int(11) default NULL;
alter table RESULT add column KEY_ORGANIZATION int(11) default NULL;
alter table RESULT add column ADDRESS varchar(250) default NULL;
alter table RESULT add column VOLUME varchar(250) default NULL;
alter table RESULT add column SERIES varchar(250) default NULL;
alter table RESULT add column EDITION varchar(250) default NULL;
alter table RESULT add column ISBN int(11) default NULL;
alter table RESULT add column ISSN int(11) default NULL;
alter table RESULT add column SCOPE varchar(50) default NULL;
alter table RESULT add column BOOK_PART_TYPE varchar(50) default NULL;
alter table RESULT add column CHAPTER_SECTION varchar(250) default NULL;
alter table RESULT add column FIRST_PAGE int(11) default NULL;
alter table RESULT add column LAST_PAGE int(11) default NULL;
alter table RESULT add column JOURNAL_NAME varchar(250) default NULL;
alter table RESULT add column NUMBER varchar(50) default NULL;
alter table RESULT add column BOOK_TITLE varchar(250) default NULL;
alter table RESULT add column THESIS_TYPE varchar(50) default NULL;
alter table RESULT add column TYPE varchar(250) default NULL;
alter table RESULT add column HOW_PUBLISHED varchar(250) default NULL;
alter table RESULT add column OTHER_PUBLICATION_TYPE varchar(250) default NULL;
alter table RESULT add column MONTH varchar(50) default NULL;
