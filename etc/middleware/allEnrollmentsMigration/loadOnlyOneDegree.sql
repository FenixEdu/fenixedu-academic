-- **************************************************
-- *** ATENTION! ************************************
-- *** The contents of this file is used only for ***
-- *** testing. *************************************
-- **************************************************

-- delete from mw_ENROLMENT where degreeCode <> 1;
-- delete from mw_ALUNO where degreeCode <> 1;

drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_temp where degreeCode = 3;

drop table if exists mw_ALUNO;
create table mw_ALUNO
select * from mw_ALUNO_temp where degreeCode = 3;
