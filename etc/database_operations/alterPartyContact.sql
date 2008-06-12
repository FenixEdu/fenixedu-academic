alter table PARTY_CONTACT add column KEY_RESEARCHER int(11);
alter table PARTY_CONTACT add index (KEY_RESEARCHER);
