drop table if exists mw_CURRICULAR_COURSE_SCOPE;
create table mw_CURRICULAR_COURSE_SCOPE
select * from mw_CURRICULAR_COURSE_SCOPE_AUXILIARY_TABLE;

-- drop table if exists mw_ENROLMENT;
-- create table mw_ENROLMENT
-- select * from mw_ENROLMENT_AUXILIARY_TABLE_1 use index (I2) where enrolmentYear <> 2003;

-- drop table if exists mw_STUDENT;
-- create table mw_STUDENT
-- select mwa.* from mw_STUDENT_AUXILIARY_TABLE mwa inner join mw_ENROLMENT mwe on mwa.number = mwe.number;

----------------------------------------------------------------------------------------------------------------------
-- Especificações do ficheiro 'curriculo.txt' que vem do Almeida:
-- Anodis: Ano da disciplina
-- Semdis: Semestre da inscrição (pode não ser aquele em que a disciplina é dada)
-- Codcur: Curso do aluno
-- Codram: Pode ser o ramo do aluno ou da disciplina (se a disciplina não tem ramo então é o do aluno)
----------------------------------------------------------------------------------------------------------------------
