alter table `PHD_STUDY_PLAN_ENTRY` add column `CREATED_BY` varchar(255) null;
alter table `PHD_STUDY_PLAN_ENTRY` add column `WHEN_CREATED` datetime not NULL;
alter table `PHD_STUDY_PLAN` add column `CREATED_BY` varchar(255) null;
alter table `PHD_STUDY_PLAN` add column `WHEN_CREATED` datetime not null;
alter table `PROCESS` add column `QUALIFICATION_EXAMS_PERFORMED` tinyint(1) null;
alter table `PROCESS` add column `QUALIFICATION_EXAMS_REQUIRED` tinyint(1) null;


