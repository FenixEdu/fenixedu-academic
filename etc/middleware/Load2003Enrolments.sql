-- 2003 Enrolments
drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_temp where enrolmentYear = 2003;
