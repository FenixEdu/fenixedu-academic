alter table FILE add column KEY_UNIT_FILE_TAG int(11);
alter table FILE add INDEX(`KEY_UNIT_FILE_TAG`);
