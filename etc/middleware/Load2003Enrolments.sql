-- 2003 Enrolments
drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_temp where enrolmentYear = 2003 and degreeCode = 2;

update DEGREE_CURRICULAR_PLAN set state = 2 where name like "L%" and name not like "%2003/2004";
update DEGREE_CURRICULAR_PLAN set state = 1 where name like "L%2003/2004";