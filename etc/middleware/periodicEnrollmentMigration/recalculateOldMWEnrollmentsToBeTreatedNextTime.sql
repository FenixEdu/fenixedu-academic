drop table if exists mw_UNTREATED_ENROLLMENT;
create table mw_UNTREATED_ENROLLMENT
select mwe1.* from mw_ENROLMENT mwe1 left join mw_TREATED_ENROLLMENT mwe2 on
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
where mwe2.number is null;

CREATE INDEX mw_UNTREATED_ENROLLMENT_INDEX_1 on mw_UNTREATED_ENROLLMENT(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_UNTREATED_ENROLLMENT_INDEX_2 on mw_UNTREATED_ENROLLMENT(number);

drop table if exists mw_ENROLMENT_temp;
create table mw_ENROLMENT_temp
select * from mw_ENROLMENT_AUXILIARY_TABLE_1;

drop table if exists mw_ENROLMENT_AUXILIARY_TABLE_1;
create table mw_ENROLMENT_AUXILIARY_TABLE_1
select * from mw_ENROLMENT_temp;
INSERT INTO mw_ENROLMENT_AUXILIARY_TABLE_1 select * from mw_UNTREATED_ENROLLMENT;

CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_1 on mw_ENROLMENT_AUXILIARY_TABLE_1(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_2 on mw_ENROLMENT_AUXILIARY_TABLE_1(enrolmentYear,degreeCode);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_3 on mw_ENROLMENT_AUXILIARY_TABLE_1(number);
CREATE INDEX mw_ENROLMENT_AUXILIARY_TABLE_1_INDEX_4 on mw_ENROLMENT_AUXILIARY_TABLE_1(enrolmentYear);

drop table if exists mw_ENROLMENT_temp;
drop table if exists mw_UNTREATED_ENROLLMENT;

delete from mw_TREATED_ENROLLMENT;
