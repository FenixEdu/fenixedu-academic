drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select mwe1.* from mw_ENROLMENT_AUXILIARY_TABLE_1 mwe1 left join mw_ENROLMENT_AUXILIARY_TABLE_2 mwe2 on
mwe1.number = mwe2.number and
mwe1.enrolmentYear = mwe2.enrolmentYear and
mwe1.curricularCourseYear = mwe2.curricularCourseYear and
mwe1.curricularCourseSemester = mwe2.curricularCourseSemester and
mwe1.season = mwe2.season and
mwe1.courseCode = mwe2.courseCode and
mwe1.degreeCode = mwe2.degreeCode and
mwe1.branchCode = mwe2.branchCode and
mwe1.grade = mwe2.grade and
mwe1.teacherNumber = mwe2.teacherNumber and
mwe1.examDate = mwe2.examDate and
mwe1.universityCode = mwe2.universityCode and
mwe1.remarks = mwe2.remarks
where mwe2.number is null and mwe1.enrolmentYear = 2003;

CREATE INDEX mw_ENROLMENT_INDEX_1 on mw_ENROLMENT(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_INDEX_2 on mw_ENROLMENT(number);

drop table if exists mw_STUDENT;
create table mw_STUDENT
select distinct mwa.* from mw_STUDENT_AUXILIARY_TABLE mwa inner join mw_ENROLMENT mwe on mwa.number = mwe.number;

CREATE INDEX mw_STUDENT_INDEX_1 on mw_STUDENT(number);
