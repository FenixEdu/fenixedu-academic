


-- Inserted at 2008-10-31T16:32:50.504Z

alter table SENDER add column KEY_UNIT int(11);
alter table SENDER add column OJB_CONCRETE_CLASS text;
alter table SENDER add index (KEY_UNIT);

