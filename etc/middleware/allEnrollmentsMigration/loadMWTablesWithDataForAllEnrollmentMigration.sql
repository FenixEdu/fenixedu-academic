drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_temp use index (I2) where enrolmentYear <> 2003;

drop table if exists mw_CURRICULAR_COURSE_SCOPE;
create table mw_CURRICULAR_COURSE_SCOPE
select * from mw_CURRICULAR_COURSE_SCOPE_temp;

drop table if exists mw_ALUNO;
create table mw_ALUNO
select mwa.* from mw_ALUNO_temp mwa inner join mw_ENROLMENT mwe on mwe.number = mwa.number group by mwa.number;

----------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------

drop table if exists UNIVERSITY;
create table UNIVERSITY (
   ID_INTERNAL int(11) not null auto_increment,
   CODE varchar(10) not null,
   NAME varchar(150) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE,NAME)
)type=InnoDB;

----------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------

DELETE FROM BRANCH WHERE ID_INTERNAL=93;
UPDATE BRANCH SET BRANCH_CODE='' WHERE BRANCH_NAME='';
UPDATE STUDENT_CURRICULAR_PLAN SET KEY_BRANCH=8272 WHERE KEY_BRANCH=93;

----------------------------------------------------------------------------------------------------------------------
-- Especificações do ficheiro 'curriculo.txt' que vem do Almeida:
-- Anodis: Ano da disciplina
-- Semdis: Semestre da inscrição (pode não ser aquele em que a disciplina é dada)
-- Codcur: Curso do aluno
-- Codram: Pode ser o ramo do aluno ou da disciplina (se a disciplina não tem ramo então é o do aluno)
----------------------------------------------------------------------------------------------------------------------
