package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class MobilityExtraEnrolmentBean extends StudentExtraEnrolmentBean {

	public MobilityExtraEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
		super(studentCurricularPlan, executionSemester);
	}

	@Override
	public NoCourseGroupCurriculumGroupType getGroupType() {
		return NoCourseGroupCurriculumGroupType.STANDALONE;
	}

}
