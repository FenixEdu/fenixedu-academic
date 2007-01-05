alter table SPACE_OCCUPATION add column `KEY_UNIT` int(11) default NULL;
alter table SPACE_OCCUPATION add key `KEY_UNIT` (`KEY_UNIT`);
