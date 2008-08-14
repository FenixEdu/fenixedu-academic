package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class StudentExtraEnrolmentBean extends StudentOptionalEnrolmentBean {

    private NoCourseGroupCurriculumGroupType groupType;

    public StudentExtraEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
	    NoCourseGroupCurriculumGroupType groupType) {
	setStudentCurricularPlan(studentCurricularPlan);
	setExecutionPeriod(executionSemester);
	setGroupType(groupType);
    }

    public NoCourseGroupCurriculumGroupType getGroupType() {
	return groupType;
    }

    public void setGroupType(NoCourseGroupCurriculumGroupType groupType) {
	this.groupType = groupType;
    }

}
