alter table CONVERSATION_THREAD add (KEY_ROOT_DOMAIN_OBJECT int not null);
alter table CONVERSATION_MESSAGE add (KEY_ROOT_DOMAIN_OBJECT int not null);
alter table FORUM drop READERS_GROUP;
alter table FORUM drop WRITERS_GROUP;
