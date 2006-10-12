alter table EXECUTION_COURSE change column NOME NOME varchar(100) NOT NULL default '';
alter table EXECUTION_COURSE drop column SEMESTER;
alter table EXECUTION_COURSE change column COMMENT COMMENT text;
alter table EXECUTION_COURSE drop column ACK_OPT_LOCK;
alter table EXECUTION_COURSE change column KEY_BOARD KEY_BOARD int(11) NOT NULL;
