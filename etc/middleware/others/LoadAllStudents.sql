-- All Students
drop table if exists mw_STUDENT;
create table mw_STUDENT
select * from mw_STUDENT_AUXILIARY_TABLE;