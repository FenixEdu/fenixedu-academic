alter table CURRICULUM
 add column  KEY_CURRICULAR_COURSE int after ID_INTERNAL
,  drop index U1
, type=InnoDB;

alter table EVALUATION_METHOD
 add column  KEY_CURRICULAR_COURSE int after ID_INTERNAL
,  drop index ID_INTERNAL
, type=InnoDB;

update EXECUTION_YEAR set state = "O" where id_internal = 1;
update EXECUTION_YEAR set state = "C" where id_internal = 2;
update EXECUTION_PERIOD set state = "O" where id_internal = 1;
update EXECUTION_PERIOD set state = "C" where id_internal = 2;