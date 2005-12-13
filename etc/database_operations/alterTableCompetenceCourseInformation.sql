alter table COMPETENCE_COURSE_INFORMATION drop column PRATICAL_HOURS;
alter table COMPETENCE_COURSE_INFORMATION drop column THEO_PRAT_HOURS;

alter table COMPETENCE_COURSE_INFORMATION add column CODE varchar(50) NOT NULL default '';
alter table COMPETENCE_COURSE_INFORMATION add column NAME varchar(100) NOT NULL default '';
alter table COMPETENCE_COURSE_INFORMATION add column PROBLEMS_HOURS double default 0;
alter table COMPETENCE_COURSE_INFORMATION add column PROJECT_HOURS double default 0;
alter table COMPETENCE_COURSE_INFORMATION add column SEMINARY_HOURS double default 0;
alter table COMPETENCE_COURSE_INFORMATION add column REGIME varchar(40) NOT NULL default 'SEMESTER';

alter table COMPETENCE_COURSE_INFORMATION change column THEORETICAL_HOURS THEORETICAL_HOURS double default 0;
alter table COMPETENCE_COURSE_INFORMATION change column LAB_HOURS LAB_HOURS double default 0;
