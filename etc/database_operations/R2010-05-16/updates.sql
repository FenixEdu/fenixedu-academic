-- Inserted at 2010-05-16T18:33:54.376+01:00
alter table `FACULTY_EVALUATION_PROCESS` add column `SUFFIX` text;
update FACULTY_EVALUATION_PROCESS set SUFFIX = 'suffix';
