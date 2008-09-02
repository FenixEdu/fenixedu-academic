alter table FILE add column KEY_ADDRESSEE int(11);
alter table FILE add column KEY_SOURCE int(11);
alter table FILE add column KEY_OPERATOR int(11);
alter table FILE add column TYPE text;
alter table FILE add index (KEY_ADDRESSEE);
alter table FILE add index (KEY_OPERATOR);
alter table FILE add index (KEY_SOURCE);