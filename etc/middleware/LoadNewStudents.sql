-- Non Existing
drop table if exists mw_ALUNO;
create table mw_ALUNO
select a.* from mw_ALUNO_temp a left join STUDENT s on s.number = a.number where s.id_internal is null;