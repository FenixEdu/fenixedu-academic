alter table CURRICULUM
 drop column  KEY_EXECUTION_COURSE
, add unique U1(KEY_CURRICULAR_COURSE)
, type=InnoDB;      

alter table EVALUATION_METHOD
 drop column  KEY_EXECUTION_COURSE
, add unique U1(KEY_CURRICULAR_COURSE)
, type=InnoDB;      

delete from CURRICULUM where KEY_CURRICULAR_COURSE is null;
delete from EVALUATION_METHOD where KEY_CURRICULAR_COURSE is null;