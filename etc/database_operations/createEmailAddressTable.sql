create table EMAIL_ADDRESS (
  ID_INTERNAL int(11) not null auto_increment,
  KEY_ROOT_DOMAIN_OBJECT int(11) not null default 1,
  KEY_PERSON int(11) not null default 1,
  IS_INSTITUTIONAL_EMAIL tinyint(1) not null default 0,
  VALUE varchar(255),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_PERSON)
) type=InnoDB;

insert into EMAIL_ADDRESS (KEY_PERSON, VALUE, IS_INSTITUTIONAL_EMAIL)
select PARTY.ID_INTERNAL, PARTY.EMAIL, 0
from PARTY where PARTY.OJB_CONCRETE_CLASS like '%Person%' and PARTY.EMAIL is not null and length(PARTY.EMAIL) > 0;

insert into EMAIL_ADDRESS (KEY_PERSON, VALUE, IS_INSTITUTIONAL_EMAIL)
select PARTY.ID_INTERNAL, PARTY.INSTITUTIONAL_EMAIL, 1
from PARTY where PARTY.OJB_CONCRETE_CLASS like '%Person%' and PARTY.INSTITUTIONAL_EMAIL is not null and length(PARTY.INSTITUTIONAL_EMAIL) > 0;