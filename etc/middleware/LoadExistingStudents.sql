-- OK Students
drop table if exists mw_ALUNO;
create table mw_ALUNO
select a.* from mw_ALUNO_temp a inner join student s on s.number = a.number inner join person p on p.id_internal = s.key_person where p.document_id_number = a.documentidnumber;
