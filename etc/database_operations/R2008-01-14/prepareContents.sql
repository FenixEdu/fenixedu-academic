alter table CONTENT add column OLD_ID_INTERNAL int(11);

alter table CONTENT add index(OLD_ID_INTERNAL);

alter table CONTENT add column KEY_FORUM_EXECUTION_COURSE int(11);
alter table CONTENT add index(KEY_FORUM_EXECUTION_COURSE);

