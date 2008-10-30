


-- Inserted at 2008-10-30T20:50:18.098Z

alter table PARTY add column KEY_UNIT_COST_CENTER_CODE int(11);
alter table PARTY add index (KEY_UNIT_COST_CENTER_CODE);


create table UNIT_COST_CENTER_CODE (
  `COST_CENTER_CODE` int(11),
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `KEY_UNIT` int(11),
  primary key (ID_INTERNAL),
  index (KEY_ROOT_DOMAIN_OBJECT),
  index (KEY_UNIT)
) type=InnoDB ;


insert into UNIT_COST_CENTER_CODE
select PARTY.COST_CENTER_CODE, PARTY.ID_INTERNAL, 1, PARTY.ID_INTERNAL
from PARTY where PARTY.COST_CENTER_CODE is not null ;

