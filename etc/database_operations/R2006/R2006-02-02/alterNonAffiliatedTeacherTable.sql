alter table NON_AFFILIATED_TEACHER add column KEY_UNIT int(11) unsigned NOT NULL default '0';
alter table NON_AFFILIATED_TEACHER drop KEY U1;
-- alter table NON_AFFILIATED_TEACHER add KEY U1 (`NAME`,`KEY_INSTITUTION`);