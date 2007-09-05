alter table PARTY add index (KEY_LIBRARY_CARD);
alter table LIBRARY_CARD add index (KEY_ROOT_DOMAIN_OBJECT);
alter table LIBRARY_CARD add index (KEY_PERSON);