-- INSERT INTO mw_ENROLMENT_AUXILIARY_TABLE_1 SELECT * FROM mw_ENROLMENT;
INSERT INTO mw_ENROLMENT_AUXILIARY_TABLE_1 (number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks) SELECT number,enrolmentYear,curricularCourseYear,curricularCourseSemester,season,courseCode,degreeCode,branchCode,grade,teacherNumber,examDate,universityCode,remarks FROM mw_ENROLMENT;
