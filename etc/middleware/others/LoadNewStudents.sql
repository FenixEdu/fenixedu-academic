-- Non Existing
drop table if exists mw_STUDENT;
create table mw_STUDENT
select a.* from mw_STUDENT_AUXILIARY_TABLE a left join STUDENT s on s.number = a.number where s.id_internal is null;