-- Load All Students with Enrolment in 2003

drop table if exists mw_ALUNO;
create table mw_ALUNO
select mwa.* from mw_ALUNO_temp mwa inner join mw_ENROLMENT mwe on mwe.number = mwa.number group by mwa.number;