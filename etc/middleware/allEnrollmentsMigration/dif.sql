CREATE INDEX mw_ENROLMENT_INDEX_1 on mw_ENROLMENT_1(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_INDEX_1 on mw_ENROLMENT_2(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);

select * from mw_ENROLMENT_1 mwe1 left join mw_ENROLMENT_2 mwe2 on
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
where
mwe2.number is null;



-- RENAME TABLE mw_ENROLMENT_temp TO mw_ENROLMENT_AUXILIARY_TABLE_1;
-- RENAME TABLE mw_ALUNO_temp TO mw_STUDENT_AUXILIARY_TABLE;
-- RENAME TABLE mw_ALUNO TO mw_STUDENT;
-- RENAME TABLE mw_CURRICULAR_COURSE_SCOPE_temp TO mw_CURRICULAR_COURSE_SCOPE_AUXILIARY_TABLE;
-- RENAME TABLE mw_PESSOA TO mw_PERSON;

drop table if exists mw_ENROLMENT_AUXILIARY_TABLE_2;
create table mw_ENROLMENT_AUXILIARY_TABLE_2
select * from mw_ENROLMENT_AUXILIARY_TABLE_1 where enrolmentYear <> 2003;

CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_2_INDEX_1 on mw_ENROLMENT_AUXILIARY_TABLE_2(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);

CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_1 on mw_ENROLMENT_AUXILIARY_TABLE_1(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_2 on mw_ENROLMENT_AUXILIARY_TABLE_1(enrolmentYear,degreeCode);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_3 on mw_ENROLMENT_AUXILIARY_TABLE_1(number);

CREATE INDEX mw_STUDENT_AUXILIARY_TABLE_INDEX_1 on mw_STUDENT_AUXILIARY_TABLE(number);

drop table if exists mw_ENROLMENT;
create table mw_ENROLMENT
select * from mw_ENROLMENT_AUXILIARY_TABLE_1 where degreeCode = 1 and enrolmentYear <> 2003;

drop table if exists mw_STUDENT;
create table mw_STUDENT
select mwa.* from mw_STUDENT_AUXILIARY_TABLE mwa inner join mw_ENROLMENT mwe on mwa.number = mwe.number;

CREATE INDEX mw_ENROLMENT_INDEX_1 on mw_ENROLMENT(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_STUDENT_INDEX_1 on mw_STUDENT(number);
