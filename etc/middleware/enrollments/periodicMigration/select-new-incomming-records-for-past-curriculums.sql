DROP TABLE IF EXISTS mw_ENROLMENT;
CREATE TABLE mw_ENROLMENT
SELECT mwe1.* FROM mw_ENROLMENT_AUXILIARY_TABLE_1 mwe1 LEFT JOIN mw_ENROLMENT_AUXILIARY_TABLE_2 mwe2 ON
mwe1.number = mwe2.number AND
mwe1.enrolmentYear = mwe2.enrolmentYear AND
mwe1.curricularCourseYear = mwe2.curricularCourseYear AND
mwe1.curricularCourseSemester = mwe2.curricularCourseSemester AND
mwe1.season = mwe2.season AND
mwe1.courseCode = mwe2.courseCode AND
mwe1.degreeCode = mwe2.degreeCode AND
mwe1.branchCode = mwe2.branchCode AND
mwe1.grade = mwe2.grade AND
mwe1.teacherNumber = mwe2.teacherNumber AND
mwe1.examDate = mwe2.examDate AND
mwe1.universityCode = mwe2.universityCode AND
mwe1.remarks = mwe2.remarks
WHERE mwe2.number IS NULL AND mwe1.enrolmentYear <> 2003;

CREATE INDEX mw_ENROLMENT_INDEX_1 ON mw_ENROLMENT(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_INDEX_2 ON mw_ENROLMENT(number);

DROP TABLE IF EXISTS mw_STUDENT;
CREATE TABLE mw_STUDENT
SELECT DISTINCT mwa.* FROM mw_STUDENT_AUXILIARY_TABLE mwa INNER JOIN mw_ENROLMENT mwe ON mwa.number = mwe.number;

CREATE INDEX mw_STUDENT_INDEX_1 ON mw_STUDENT(number);
