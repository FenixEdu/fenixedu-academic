alter table HOMEPAGE drop column MY_URL;
alter table HOMEPAGE add column ACTIVATED tinyint(1) NOT NULL default '0';
alter table HOMEPAGE add column NAME varchar(255) NOT NULL;
alter table HOMEPAGE add column SHOW_UNIT tinyint(1) NOT NULL default '0';
alter table HOMEPAGE add column SHOW_PHOTO tinyint(1) NOT NULL default '0';
alter table HOMEPAGE add column SHOW_EMAIL tinyint(1) NOT NULL default '0';
alter table HOMEPAGE add column SHOW_TELEPHONE tinyint(1) NOT NULL default '0';
alter table HOMEPAGE add column SHOW_ALTERNATIVE_HOMEPAGE tinyint(1) NOT NULL default '0';
alter table HOMEPAGE add column SHOW_RESEARCH_UNIT_HOMEPAGE tinyint(1) NOT NULL default '0';
