-- Load All Students with Enrolment in 2003

drop table if exists mw_STUDENT;
create table mw_STUDENT
select mwa.* from mw_STUDENT_AUXILIARY_TABLE mwa inner join mw_ENROLMENT mwe on mwe.number = mwa.number group by mwa.number;