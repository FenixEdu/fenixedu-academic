alter table BUILDING add column KEY_CAMPUS int(11) not null;
update BUILDING set KEY_CAMPUS = 1;