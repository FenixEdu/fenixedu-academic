DROP TABLE IF EXISTS mw_ENROLMENT;
CREATE TABLE mw_ENROLMENT
SELECT mwe2.* FROM mw_ENROLMENT_AUXILIARY_TABLE_2 mwe2 LEFT JOIN mw_ENROLMENT_AUXILIARY_TABLE_1 mwe1 ON
mwe2.number = mwe1.number AND
mwe2.enrolmentYear = mwe1.enrolmentYear AND
mwe2.curricularCourseYear = mwe1.curricularCourseYear AND
mwe2.curricularCourseSemester = mwe1.curricularCourseSemester AND
mwe2.season = mwe1.season AND
mwe2.courseCode = mwe1.courseCode AND
mwe2.degreeCode = mwe1.degreeCode AND
mwe2.branchCode = mwe1.branchCode AND
mwe2.grade = mwe1.grade AND
mwe2.teacherNumber = mwe1.teacherNumber AND
mwe2.examDate = mwe1.examDate AND
mwe2.universityCode = mwe1.universityCode AND
mwe2.remarks = mwe1.remarks
WHERE mwe1.number IS NULL AND mwe2.enrolmentYear <> 2003;

CREATE INDEX mw_ENROLMENT_INDEX_1 ON mw_ENROLMENT(number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks);
CREATE INDEX mw_ENROLMENT_INDEX_2 ON mw_ENROLMENT(number);

DROP TABLE IF EXISTS mw_STUDENT;
CREATE TABLE mw_STUDENT
SELECT DISTINCT mwa.* FROM mw_STUDENT_AUXILIARY_TABLE mwa INNER JOIN mw_ENROLMENT mwe ON mwa.number = mwe.number;

CREATE INDEX mw_STUDENT_INDEX_1 ON mw_STUDENT(number);
