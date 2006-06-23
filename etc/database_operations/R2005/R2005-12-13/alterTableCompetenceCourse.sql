alter table COMPETENCE_COURSE add column KEY_UNIT int(11) default NULL;
alter table COMPETENCE_COURSE change column NAME NAME varchar(100) default NULL;
alter table COMPETENCE_COURSE change column CODE CODE varchar(50) default NULL;
