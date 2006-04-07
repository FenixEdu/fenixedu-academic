alter table IDENTIFICATION change column BEGIN_DATE BEGIN_DATE timestamp NULL DEFAULT NULL;
alter table IDENTIFICATION change column END_DATE END_DATE timestamp NULL DEFAULT NULL;
alter table IDENTIFICATION change column USERNAME USERNAME varchar(50) default '';

alter table USER change column CURRENT_LOGIN_DATE_TIME CURRENT_LOGIN_DATE_TIME timestamp NULL DEFAULT NULL;
alter table USER change column LAST_LOGIN_DATE_TIME LAST_LOGIN_DATE_TIME timestamp NULL DEFAULT NULL;

drop table PERSON_FUNCTION;