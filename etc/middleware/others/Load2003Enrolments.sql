-- 2003 Enrolments
drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_AUXILIARY_TABLE_1 where enrolmentYear = 2003;
