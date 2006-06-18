alter table ROOM_CLASSIFICATION add column KEY_PARENT_ROOM_CLASSIFICATION int(11) default null;
alter table ROOM_CLASSIFICATION change column CODE CODE int(11) not null;
alter table ROOM_CLASSIFICATION drop key CODE;
alter table ROOM_CLASSIFICATION add unique (KEY_PARENT_ROOM_CLASSIFICATION, CODE);
alter table SPACE_INFORMATION drop column CLASSIFICATION;
alter table SPACE_INFORMATION add column KEY_ROOM_CLASSIFICATION int(11);
alter table SPACE add column CREATED_ON varchar(10);
alter table SPACE_OCCUPATION add column KEY_SPACE int(11) default null;
alter table SPACE_OCCUPATION add column KEY_PERSON int(11) default null;
alter table SPACE_OCCUPATION add column KEY_EXTENSION int(11) default null;
alter table SPACE_OCCUPATION change column CLASS_NAME OJB_CONCRETE_CLASS varchar(250) NOT NULL;
alter table SPACE_OCCUPATION add column KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1';
alter table SPACE_OCCUPATION add column BEGIN varchar(10);
alter table SPACE_OCCUPATION add column END varchar(10);
--create table SPACE_PERSON_SPACE_OCCUPATION (
--	KEY_SPACE int(11) NOT NULL,
--	KEY_PERSON_SPACE_OCCUPATION int(11) NOT NULL,
--	unique (KEY_SPACE, KEY_PERSON_SPACE_OCCUPATION),
--	index (KEY_SPACE),
--	index (KEY_PERSON_SPACE_OCCUPATION)
--) type=InnoDB;