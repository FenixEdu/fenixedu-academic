package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class StudentPropaeudeuticEnrolmentsBean extends StudentOptionalEnrolmentBean implements NoCourseGroupEnrolmentBean {

    public StudentPropaeudeuticEnrolmentsBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionSemester);
    }

    @Override
    public NoCourseGroupCurriculumGroupType getGroupType() {
	return NoCourseGroupCurriculumGroupType.PROPAEDEUTICS;
    }

    @Override
    public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup() {
	return getStudentCurricularPlan().getNoCourseGroupCurriculumGroup(getGroupType());
    }

}
