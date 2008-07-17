create table EDUCATION_AREA (
  ID_INTERNAL INT(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null,
  KEY_PARENT_AREA INT(11) null,
  CODE varchar(20) null,
  DESCRIPTION varchar(255) not null,
  PRIMARY KEY (ID_INTERNAL),
  KEY KEY_ROOT_DOMAIN_OBJECT (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;


create table BUSINESS_AREA (
  ID_INTERNAL INT(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null,
  KEY_PARENT_AREA INT(11) null,
  LEVEL int(11) not null,
  CODE varchar(20) null,
  DESCRIPTION varchar(255) not null,
  PRIMARY KEY (ID_INTERNAL),
  KEY KEY_ROOT_DOMAIN_OBJECT (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;


create table ALUMNI (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null,
  KEY_STUDENT int(11) not null,
  URL_REQUEST_TOKEN VARCHAR(255) null,
  REGISTERED tinyint(1) not null DEFAULT '0',
  PRIMARY KEY (ID_INTERNAL),
  KEY KEY_ROOT_DOMAIN_OBJECT (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;


create table ALUMNI_IDENTITY_CHECK_REQUEST (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null,
  KEY_ALUMNI int(11) null,
  KEY_OPERATOR int(11) null,
  CONTACT_EMAIL varchar(50) not null, 
  FULL_NAME varchar(255) null,
  DOCUMENT_ID_NUMBER varchar(50) not null,
  DATE_OF_BIRTH_YEAR_MONTH_DAY date null,
  PARISH_OF_BIRTH varchar(100) null,
  DISTRICT_OF_BIRTH varchar(100) null,
  DISTRICT_SUBDIVISION_OF_BIRTH varchar(100) null,
  SOCIAL_SECURITY_NUMBER varchar(50) null,
  NAME_OF_FATHER varchar(100) null,
  NAME_OF_MOTHER varchar(100) null,
  REQUEST_TYPE varchar(100) not null,  
  CREATION_DATE_TIME datetime not null,
  APPROVED tinyint(1) null,
  DECISION_DATE_TIME datetime null,
  PRIMARY KEY (ID_INTERNAL),
  KEY KEY_ROOT_DOMAIN_OBJECT (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;
