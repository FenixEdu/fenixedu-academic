drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_AUXILIARY_TABLE_1
where grade <> '' and enrolmentYear = 2003;

CREATE INDEX mw_ENROLMENT_INDEX_1 on mw_ENROLMENT(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_INDEX_2 on mw_ENROLMENT(number);

drop table if exists mw_STUDENT;
create table mw_STUDENT
select distinct mwa.* from mw_STUDENT_AUXILIARY_TABLE mwa inner join mw_ENROLMENT mwe on mwa.number = mwe.number;

CREATE INDEX mw_STUDENT_INDEX_1 on mw_STUDENT(number);
