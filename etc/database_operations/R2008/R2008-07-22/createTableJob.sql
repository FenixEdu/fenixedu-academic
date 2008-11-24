create table JOB (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null,
  KEY_PERSON int(11) not null,
  KEY_COUNTRY int(11) not null,
  KEY_BUSINESS_AREA int(11) not null,
  EMPLOYER_NAME varchar(100) null,
  CITY varchar(100) null,
  POSITION varchar(100) null,
  BEGIN_DATE date not null,
  END_DATE date default null,
  CONTRACT_TYPE varchar(100) null,  
  PRIMARY KEY (ID_INTERNAL),
  KEY KEY_ROOT_DOMAIN_OBJECT (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB;
