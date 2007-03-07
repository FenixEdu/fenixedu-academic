create table ID_DOCUMENT_TYPE (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  VALUE varchar(255),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  unique (VALUE)
) type=InnoDB;

insert into ID_DOCUMENT_TYPE (VALUE) values ("IDENTITY_CARD");
insert into ID_DOCUMENT_TYPE (VALUE) values ("PASSPORT");
insert into ID_DOCUMENT_TYPE (VALUE) values ("FOREIGNER_IDENTITY_CARD");
insert into ID_DOCUMENT_TYPE (VALUE) values ("NATIVE_COUNTRY_IDENTITY_CARD");
insert into ID_DOCUMENT_TYPE (VALUE) values ("NAVY_IDENTITY_CARD");
insert into ID_DOCUMENT_TYPE (VALUE) values ("AIR_FORCE_IDENTITY_CARD");
insert into ID_DOCUMENT_TYPE (VALUE) values ("OTHER");
insert into ID_DOCUMENT_TYPE (VALUE) values ("MILITARY_IDENTITY_CARD");
insert into ID_DOCUMENT_TYPE (VALUE) values ("EXTERNAL");


create table ID_DOCUMENT (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  KEY_ID_DOCUMENT_TYPE int(11) not null,
  KEY_PERSON int(11) not null,
  VALUE varchar(255),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_ID_DOCUMENT_TYPE),
  index (KEY_PERSON),
  unique (VALUE, KEY_ID_DOCUMENT_TYPE)
) type=InnoDB;

insert into ID_DOCUMENT (KEY_ID_DOCUMENT_TYPE, KEY_PERSON, VALUE)
select ID_DOCUMENT_TYPE.ID_INTERNAL, PARTY.ID_INTERNAL, PARTY.DOCUMENT_ID_NUMBER
from PARTY inner join ID_DOCUMENT_TYPE on ID_DOCUMENT_TYPE.VALUE = PARTY.ID_DOCUMENT_TYPE
where PARTY.OJB_CONCRETE_CLASS like '%Person%';