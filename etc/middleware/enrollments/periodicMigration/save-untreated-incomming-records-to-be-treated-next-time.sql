DELETE mw_ENROLMENT_AUXILIARY_TABLE_1 FROM mw_ENROLMENT_AUXILIARY_TABLE_1, mw_ENROLMENT WHERE
mw_ENROLMENT_AUXILIARY_TABLE_1.number = mw_ENROLMENT.number and
mw_ENROLMENT_AUXILIARY_TABLE_1.enrolmentYear = mw_ENROLMENT.enrolmentYear and
mw_ENROLMENT_AUXILIARY_TABLE_1.curricularCourseYear = mw_ENROLMENT.curricularCourseYear and
mw_ENROLMENT_AUXILIARY_TABLE_1.curricularCourseSemester = mw_ENROLMENT.curricularCourseSemester and
mw_ENROLMENT_AUXILIARY_TABLE_1.season = mw_ENROLMENT.season and
mw_ENROLMENT_AUXILIARY_TABLE_1.courseCode = mw_ENROLMENT.courseCode and
mw_ENROLMENT_AUXILIARY_TABLE_1.degreeCode = mw_ENROLMENT.degreeCode and
mw_ENROLMENT_AUXILIARY_TABLE_1.branchCode = mw_ENROLMENT.branchCode and
mw_ENROLMENT_AUXILIARY_TABLE_1.grade = mw_ENROLMENT.grade and
mw_ENROLMENT_AUXILIARY_TABLE_1.teacherNumber = mw_ENROLMENT.teacherNumber and
mw_ENROLMENT_AUXILIARY_TABLE_1.examDate = mw_ENROLMENT.examDate and
mw_ENROLMENT_AUXILIARY_TABLE_1.universityCode = mw_ENROLMENT.universityCode and
mw_ENROLMENT_AUXILIARY_TABLE_1.remarks = mw_ENROLMENT.remarks and
mw_ENROLMENT_AUXILIARY_TABLE_1.idinternal = mw_ENROLMENT.idinternal;
