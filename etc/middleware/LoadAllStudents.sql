-- All Students
drop table if exists mw_ALUNO;
create table mw_ALUNO
select * from mw_ALUNO_temp;