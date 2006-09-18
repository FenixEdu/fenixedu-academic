insert into ROLE set ROLE.ROLE_TYPE = 'CANDIDATE', ROLE.PORTAL_SUB_APPLICATION = '/candidate', ROLE.PAGE = '/index.do', ROLE.PAGE_NAME_PROPERTY = 'portal.candidate';
update ROLE set ROLE.PAGE_NAME_PROPERTY = 'portal.masterDegreeCandidate' where ROLE.ID_INTERNAL = 5;

alter table PARTY add column KEY_COUNTRY_OF_BIRTH int;
alter table PARTY add column KEY_COUNTRY_OF_RESIDENCE int;

create table PRECEDENT_DEGREE_INFORMATION (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  KEY_STUDENT int(11),
  KEY_INSTITUTION int(11),
  KEY_COUNTRY int(11),
  KEY_DFA_CANDIDACY int(11),
  CONCLUSION_GRADE varchar(20),
  DEGREE_DESIGNATION varchar(255),
  CONCLUSION_YEAR int(4),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_STUDENT),
  index (KEY_INSTITUTION),
  index (KEY_COUNTRY),
  index (KEY_DFA_CANDIDACY)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table STUDENT add column KEY_PRECEDENT_DEGREE_INFORMATION int;

create table CANDIDACY_DOCUMENT (
  ID_INTERNAL int(11) unsigned not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  DOCUMENT_DESCRIPTION varchar(255) not null,
  FILE_UPLOAD_TIME datetime,
  KEY_CANDIDACY int(11) not null,
  KEY_FILE int(11),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_FILE),
  index (KEY_CANDIDACY)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

alter table FILE add column KEY_CANDIDACY_DOCUMENT int;