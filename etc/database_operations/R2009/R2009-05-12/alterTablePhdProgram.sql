
alter table PHD_PROGRAM add column NAME text;
alter table PHD_PROGRAM add column ACRONYM varchar(100);
alter table PHD_PROGRAM add unique (ACRONYM);

alter table DEGREE add column KEY_PHD_PROGRAM int(11), add index (KEY_PHD_PROGRAM);
