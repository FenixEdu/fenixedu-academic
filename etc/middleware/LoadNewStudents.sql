drop table if exists mw_aluno;
create table mw_aluno
select a.* from mw_aluno_temp a inner join student s on s.number = a.number inner join person p on p.id_internal = s.key_person where p.document_id_number <> a.documentidnumber;
