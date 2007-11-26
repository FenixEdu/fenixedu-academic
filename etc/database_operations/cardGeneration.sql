
create table CARD_GENERATION_BATCH (
  `CREATED` timestamp NULL default NULL,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_EXECUTION_YEAR` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `SENT` timestamp NULL default NULL,
  `UPDATED` timestamp NULL default NULL,
  primary key (ID_INTERNAL),
  index (KEY_EXECUTION_YEAR),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

create table CARD_GENERATION_ENTRY (
  `CREATED` timestamp NULL default NULL,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_CARD_GENERATION_BATCH` int(11),
  `KEY_PERSON` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  `LINE` text,
  primary key (ID_INTERNAL),
  index (KEY_CARD_GENERATION_BATCH),
  index (KEY_PERSON),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

alter table DEGREE add column MINISTRY_CODE text default null;
alter table DEGREE add column ID_CARD_NAME text default null;
update DEGREE set ID_CARD_NAME = upper(NOME);
