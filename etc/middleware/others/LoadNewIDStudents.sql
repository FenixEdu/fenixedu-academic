-- Existing with diferent ID's
drop table if exists mw_STUDENT;
create table mw_STUDENT
select a.* from mw_STUDENT_AUXILIARY_TABLE a inner join STUDENT s on s.number = a.number inner join PERSON p on p.id_internal = s.key_person where p.document_id_number <> a.documentidnumber;
