alter table NON_AFFILIATED_TEACHER drop key `KEY_UNIT`;
alter table NON_AFFILIATED_TEACHER drop key `KEY_INSTITUTION`;
alter table NON_AFFILIATED_TEACHER drop key `U1`;
--alter table NON_AFFILIATED_TEACHER add key `U1` (`NAME`, `KEY_UNIT`);
alter table NON_AFFILIATED_TEACHER change column `KEY_INSTITUTION` `KEY_INSTITUTION` int(11) unsigned default '0';
