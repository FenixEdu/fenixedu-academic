use ciapl;

alter table CURRICULUM
 drop column  KEY_EXECUTION_COURSE
, add unique U1(KEY_CURRICULAR_COURSE)
, type=InnoDB;      

delete from CURRICULUM where KEY_CURRICULAR_COURSE is null;