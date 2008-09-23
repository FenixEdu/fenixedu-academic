-- Inserted at 2008-09-17T15:18:52.594+01:00
alter table CONTENT add column KEY_THESIS int(11);
alter table CONTENT add index (KEY_THESIS);
alter table THESIS add column KEY_SITE int(11);
alter table THESIS add index (KEY_SITE);
