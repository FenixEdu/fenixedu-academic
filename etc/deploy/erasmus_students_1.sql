alter table STUDENT ADD  AGREEMENT_TYPE INT(11) NOT NULL DEFAULT 0 AFTER KEY_STUDENT_KIND;

insert into STUDENT_KIND values (null,3,0,10,10,1);
insert into STUDENT_KIND values (null,4,0,10,10,1);
insert into STUDENT_KIND values (null,5,0,10,10,1);
