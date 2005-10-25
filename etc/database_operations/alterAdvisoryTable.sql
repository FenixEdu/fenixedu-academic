alter table ADVISORY drop column ONLY_SHOW_ONCE;
alter table ADVISORY change column CREATED CREATED timestamp NOT NULL default '0000-00-00 00:00:00';
alter table ADVISORY change column EXPIRES EXPIRES timestamp NOT NULL default '0000-00-00 00:00:00';