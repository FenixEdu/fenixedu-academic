alter table PARTY add column KEY_CAMPUS int(11) default NULL;
alter table PARTY add key KEY_CAMPUS (KEY_CAMPUS);