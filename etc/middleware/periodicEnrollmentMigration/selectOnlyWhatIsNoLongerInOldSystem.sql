drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select mwe2.* from mw_ENROLMENT_AUXILIARY_TABLE_2 mwe2 left join mw_ENROLMENT_AUXILIARY_TABLE_1 mwe1 on
mwe2.number = mwe1.number and
mwe2.enrolmentYear = mwe1.enrolmentYear and
mwe2.curricularCourseYear = mwe1.curricularCourseYear and
mwe2.curricularCourseSemester = mwe1.curricularCourseSemester and
mwe2.season = mwe1.season and
mwe2.courseCode = mwe1.courseCode and
mwe2.degreeCode = mwe1.degreeCode and
mwe2.branchCode = mwe1.branchCode and
mwe2.grade = mwe1.grade and
mwe2.teacherNumber = mwe1.teacherNumber and
mwe2.examDate = mwe1.examDate and
mwe2.universityCode = mwe1.universityCode and
mwe2.remarks = mwe1.remarks
where mwe1.number is null and mwe2.enrolmentYear <> 2003;

CREATE INDEX mw_ENROLMENT_INDEX_1 on mw_ENROLMENT(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_INDEX_2 on mw_ENROLMENT(number);

drop table if exists mw_STUDENT;
create table mw_STUDENT
select distinct mwa.* from mw_STUDENT_AUXILIARY_TABLE mwa inner join mw_ENROLMENT mwe on mwa.number = mwe.number;

CREATE INDEX mw_STUDENT_INDEX_1 on mw_STUDENT(number);
