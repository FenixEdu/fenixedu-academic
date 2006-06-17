alter table ROOM_CLASSIFICATION add column KEY_PARENT_ROOM_CLASSIFICATION int(11) default null;
alter table ROOM_CLASSIFICATION change column CODE CODE int(11) not null;
alter table ROOM_CLASSIFICATION drop key CODE;
alter table ROOM_CLASSIFICATION add unique (KEY_PARENT_ROOM_CLASSIFICATION, CODE);