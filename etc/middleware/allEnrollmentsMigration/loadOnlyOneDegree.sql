-- **************************************************
-- *** ATENTION! ************************************
-- *** The contents of this file is used only for ***
-- *** testing. *************************************
-- **************************************************

drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_temp use index (I2) where degreeCode = 1 and enrolmentYear <> 2003;

-- drop table if exists mw_ALUNO;
-- create table mw_ALUNO
-- select * from mw_ALUNO_temp where degreeCode = 11;

drop table if exists mw_ALUNO;
create table mw_ALUNO
select mwa.* from mw_ALUNO_temp mwa inner join mw_ENROLMENT mwe on mwa.number = mwe.number;
