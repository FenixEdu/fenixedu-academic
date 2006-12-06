alter table CANDIDACY_SITUATION add column REMARKS varchar(255);
alter table CANDIDACY_SITUATION add column CANDIDACY_ORDER int;

alter table REGISTRATION change column NUMBER NUMBER int(11) unsigned NULL;

alter table REGISTRATION ADD column KEY_CANDIDACY int(11) unsigned NULL;

alter table CANDIDACY ADD column KEY_REGISTRATION int(11) unsigned NULL;

alter table REGISTRATION drop index U2;
