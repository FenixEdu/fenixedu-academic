-- OK Students
drop table if exists mw_ALUNO;
create table mw_ALUNO
select a.* from mw_ALUNO_temp a inner join STUDENT s on s.number = a.number inner join PERSON p on p.id_internal = s.key_person where p.document_id_number = a.documentidnumber;
