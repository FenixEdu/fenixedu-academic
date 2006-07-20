alter table GUIDE add column KEY_CONTRIBUTOR_PARTY int(11) NOT NULL;
alter table GUIDE change column KEY_CONTRIBUTOR KEY_CONTRIBUTOR int(11) unsigned default NULL;

alter table RECEIPT add column KEY_CONTRIBUTOR_PARTY int(11) NOT NULL;
alter table RECEIPT change column KEY_CONTRIBUTOR KEY_CONTRIBUTOR int(11) unsigned default NULL;

alter table EXTERNAL_PERSON change column KEY_INSTITUTION_UNIT KEY_INSTITUTION_UNIT int(11) unsigned default NULL;
