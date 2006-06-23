alter table HOLIDAY drop index u1;
alter table HOLIDAY change column DATE DATE blob not null;