package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class StudentStandaloneEnrolmentBean extends StudentOptionalEnrolmentBean implements NoCourseGroupEnrolmentBean {

    public StudentStandaloneEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionSemester);
    }

    @Override
    public NoCourseGroupCurriculumGroupType getGroupType() {
	return NoCourseGroupCurriculumGroupType.STANDALONE;
    }

    @Override
    public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup() {
	return getStudentCurricularPlan().getNoCourseGroupCurriculumGroup(getGroupType());
    }

}
