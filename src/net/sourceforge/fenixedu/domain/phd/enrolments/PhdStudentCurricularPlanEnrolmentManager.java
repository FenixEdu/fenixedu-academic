package net.sourceforge.fenixedu.domain.phd.enrolments;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolmentManager;

public class PhdStudentCurricularPlanEnrolmentManager extends StudentCurricularPlanEnrolmentManager {

    public PhdStudentCurricularPlanEnrolmentManager(final EnrolmentContext enrolmentContext) {
	super(enrolmentContext);
    }

    @Override
    protected void addRuntimeRules(final Set<ICurricularRule> curricularRules, final CurricularCourse curricularCourse) {
	super.addRuntimeRules(curricularRules, curricularCourse);

	if (mustValidateCurricularCourses()) {
	    curricularRules.add(new PhdValidCurricularCoursesRule(curricularCourse));
	}
    }

    private boolean mustValidateCurricularCourses() {

	if (!getRegistration().hasPhdIndividualProgramProcess()) {
	    return false;
	}

	return getRegistration().getPhdIndividualProgramProcess().hasCurricularCoursesToEnrol();
    }

    @Override
    protected RuleResult evaluateExtraRules(final RuleResult actualResult) {
	/*
	 * Override to avoid running previous years rule, because student must
	 * enrol in available courses already defined in study plan
	 */
	return actualResult;
    }

    @Override
    protected EnrollmentCondition getEnrolmentCondition(final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry) {
	/*
	 * This code can be changed in future, but for now student enrolments
	 * stay temporary until validation by coordinator or academic admin
	 * office
	 */
	return isResponsiblePersonStudent() ? EnrollmentCondition.TEMPORARY : super.getEnrolmentCondition(entry);
    }

}
