alter table STUDENT add column FLUNKED tinyint(1) NOT NULL default '0';
alter table STUDENT add column REQUESTED_CHANGE_DEGREE tinyint(1) NOT NULL default '0';
alter table STUDENT add column REQUESTED_CHANGE_BRANCH tinyint(1) NOT NULL default '0';

-- Ricardo Rodrigues
alter table STUDENT add column KEY_STUDENT_PERSONAL_DATA_AUTHORIZATION int(11) unsigned;
