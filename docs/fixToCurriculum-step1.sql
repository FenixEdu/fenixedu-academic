use ciapl;

alter table CURRICULUM
 add column  KEY_CURRICULAR_COURSE int after ID_INTERNAL
,  drop index U1
, type=InnoDB;