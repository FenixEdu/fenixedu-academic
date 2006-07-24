alter table RESULT drop column TITLE_OLD;
alter table RESULT drop column DESCRIPTION_OLD;
alter table RESULT drop column PUBLICATION_STRING;
alter table RESULT drop column PRODUCT_INTERNAL_ID;

alter table RESULT drop column PATENT_NUMBER;
alter table RESULT drop column STATUS;
alter table RESULT drop column KEY_COUNTRY;
alter table RESULT drop column REGISTRATION_DATE;
alter table RESULT drop column APPROVAL_DATE;
alter table RESULT drop column OJB_CONCRETE_CLASS;
alter table RESULT drop column FORMAT_TYPE;
alter table RESULT drop column PATENT_TYPE;

alter table RESULT change column TITLE TITLE_OLD longtext;
alter table RESULT add column TITLE longtext;

alter table RESULT change column DESCRIPTION OBSERVATION_OLD longtext;
alter table RESULT add column OBSERVATION longtext;

alter table AUTHORSHIP change column KEY_RESULT KEY_PUBLICATION int(11) NOT NULL;


update RESULT set TITLE = SUBSTRING(TITLE_OLD, POSITION(':' IN TITLE_OLD)+1);
update RESULT set OBSERVATION = SUBSTRING(OBSERVATION_OLD, POSITION(':' IN OBSERVATION_OLD)+1);

alter table RESULT rename PUBLICATION_DATA;

create table RESULT (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
  OJB_CONCRETE_CLASS varchar(255),
  TITLE longtext,
  NOTE longtext,
  URL varchar(250),
  LAST_MODIFICATION_DATE TIMESTAMP,
  MODIFYED_BY varchar(250),
  PATENT_NUMBER varchar(250),
  PATENT_TYPE varchar(50),
  PATENT_STATUS varchar(50),
  REGISTRATION_DATE	blob,
  APPROVAL_DATE blob,
  KEY_COUNTRY int(11),
  LANGUAGE varchar(250),
  NUMBER_PAGES int(11),
  LOCAL varchar(250),
  YEAR int(11),
  PRIMARY KEY (ID_INTERNAL)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

create table RESULT_PARTICIPATION (
  ID_INTERNAL int(11) NOT NULL auto_increment,
  KEY_PERSON int(11) NOT NULL,
  KEY_RESULT int(11) NOT NULL,
  PERSON_ORDER int(11),
  KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',
  PRIMARY KEY  (ID_INTERNAL),
  KEY (KEY_PERSON),
  KEY (KEY_RESULT),
  KEY (KEY_ROOT_DOMAIN_OBJECT)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
