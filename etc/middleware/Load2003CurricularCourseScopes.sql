-- 2003 Enrolments
drop table if exists mw_CURRICULAR_COURSE_SCOPE;
create table mw_CURRICULAR_COURSE_SCOPE
select * from mw_CURRICULAR_COURSE_SCOPE_temp where executionyear = 2003;