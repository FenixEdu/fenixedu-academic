-- Non Existing
drop table if exists mw_aluno;
create table mw_aluno
select a.* from mw_aluno_temp a left join STUDENT s on s.number = a.number where s.id_internal is null;