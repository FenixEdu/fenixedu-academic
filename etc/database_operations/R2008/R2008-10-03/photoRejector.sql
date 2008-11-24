alter table PHOTOGRAPH add column KEY_REJECTOR int(11);
alter table PHOTOGRAPH add index (KEY_REJECTOR);