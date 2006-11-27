alter table REGISTRATION add column KEY_DEGREE int(11) default NULL;
alter table REGISTRATION add key KEY_DEGREE (KEY_DEGREE);