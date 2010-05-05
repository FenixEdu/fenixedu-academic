-- Inserted at 2010-05-04T16:35:02.771+01:00

alter table `TEACHER_EVALUATION` add column `CREATED_DATE` timestamp NULL default NULL;
alter table `TEACHER_EVALUATION` add index (`OID_TEACHER_EVALUATION_PROCESS`);


