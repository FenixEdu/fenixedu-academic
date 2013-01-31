package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class NoCourseGroupEnroledCurriculumModuleWrapper extends EnroledCurriculumModuleWrapper {

	private static final long serialVersionUID = 1L;

	public NoCourseGroupEnroledCurriculumModuleWrapper(final CurriculumModule curriculumModule,
			final ExecutionSemester executionSemester) {
		super(curriculumModule, executionSemester);
	}

	@Override
	public boolean canCollectRules() {
		return false;
	}

	@Override
	public Context getContext() {
		return null;
	}

	@Override
	public double getAccumulatedEctsCredits(ExecutionSemester executionSemester) {
		if (getCurriculumModule().isEnrolment()) {
			final Enrolment enrolment = (Enrolment) getCurriculumModule();

			if (!enrolment.isBolonhaDegree()) {
				return enrolment.getAccumulatedEctsCredits(executionSemester);
			} else {
				return enrolment.getStudentCurricularPlan().getAccumulatedEctsCredits(getExecutionPeriod(),
						enrolment.getCurricularCourse());
			}
		} else {
			return 0d;
		}
	}

	@Override
	public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(ExecutionSemester executionSemester) {
		return Collections.emptySet();
	}

	@Override
	public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
		return Collections.emptyList();
	}

	@Override
	public boolean isDissertation() {
		return getCurriculumModule().getDegreeModule().isDissertation();
	}

}
