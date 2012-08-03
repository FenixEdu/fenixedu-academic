


-- Inserted at 2010-06-08T11:58:52.752+01:00

alter table `STUDENT_STATUTE` add column `CREATION_DATE` datetime NULL default NULL;

update STUDENT_STATUTE set CREATION_DATE = now();

alter table `STUDENT_STATUTE` change column `CREATION_DATE` `CREATION_DATE` datetime NOT NULL;

