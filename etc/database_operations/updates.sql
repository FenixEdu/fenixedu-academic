create table CARD_GENERATION_PROBLEM (
  `ARG` text,
  `DESCRIPTION_KEY` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_CARD_GENERATION_BATCH` int(11),
  `KEY_ROOT_DOMAIN_OBJECT` int(11),
  primary key (ID_INTERNAL),
  index (KEY_CARD_GENERATION_BATCH),
  index (KEY_ROOT_DOMAIN_OBJECT)
) type=InnoDB ;

alter table CARD_GENERATION_BATCH add column PEOPLE_FOR_ENTRY_CREATION longtext;
