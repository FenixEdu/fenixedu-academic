drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_temp use index (I2) where enrolmentYear <> 2003 and degreeCode = 1 order by number;

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

UPDATE DEGREE_CURRICULAR_PLAN SET STATE = 2 WHERE NAME LIKE "L%" AND NAME NOT LIKE "%2003/2004";
UPDATE DEGREE_CURRICULAR_PLAN SET STATE = 1 WHERE NAME LIKE "L%2003/2004";

UPDATE BRANCH SET BRANCH_NAME = '' WHERE BRANCH_NAME LIKE "CURSO DE%";

----------------------------------------------------------------------------------------------------------------------
-- Especificações do ficheiro 'curriculo.txt' que vem do Almeida:
-- Anodis: Ano da disciplina
-- Semdis: Semestre da inscrição (pode não ser aquele em que a disciplina é dada)
-- Codcur: Curso do aluno
-- Codram: Pode ser o ramo do aluno ou da disciplina (se a disciplina não tem ramo então é o do aluno)
----------------------------------------------------------------------------------------------------------------------
