-- **************************************************
-- *** ATENTION! ************************************
-- *** The contents of this file is used only for ***
-- *** testing. *************************************
-- **************************************************

drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_AUXILIARY_TABLE_1 use index (I2) where degreeCode = 10 and enrolmentYear <> 2003;

drop table if exists mw_STUDENT;
create table mw_STUDENT
select * from mw_STUDENT_AUXILIARY_TABLE where degreeCode = 10;

-- drop table if exists mw_STUDENT;
-- create table mw_STUDENT
-- select mwa.* from mw_STUDENT_AUXILIARY_TABLE mwa inner join mw_ENROLMENT mwe on mwe.number = mwa.number;
