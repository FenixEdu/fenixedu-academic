alter table EXTERNAL_PERSON drop key `KEY_UNIT`;
alter table EXTERNAL_PERSON drop key `KEY_INSTITUTION`;
alter table EXTERNAL_PERSON change column `KEY_INSTITUTION` `KEY_INSTITUTION` int(11) unsigned default '0';
alter table EXTERNAL_PERSON change column `KEY_UNIT` `KEY_UNIT` int(11) unsigned NOT NULL default '0';