package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class StudentExtraEnrolmentBean extends StudentOptionalEnrolmentBean implements NoCourseGroupEnrolmentBean {

	static private final long serialVersionUID = 1L;

	public StudentExtraEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
		setStudentCurricularPlan(studentCurricularPlan);
		setExecutionPeriod(executionSemester);
	}

	@Override
	public NoCourseGroupCurriculumGroupType getGroupType() {
		return NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR;
	}

	@Override
	public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup() {
		return getStudentCurricularPlan().getNoCourseGroupCurriculumGroup(getGroupType());
	}

	@Override
	public CurricularRuleLevel getCurricularRuleLevel() {
		return super.getCurricularRuleLevel() != null ? super.getCurricularRuleLevel() : getGroupType().getCurricularRuleLevel();
	}

}
