alter table SPACE_INFORMATION add column VALID_UNTIL varchar(10) default null;
alter table SPACE_INFORMATION add column KEY_SPACE int(11) default null;
alter table SPACE_INFORMATION add column NAME varchar(255) default null;
alter table SPACE_INFORMATION add column LEVEL int(11) default null;