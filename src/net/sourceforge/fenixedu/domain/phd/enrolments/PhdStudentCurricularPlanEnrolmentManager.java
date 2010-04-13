package net.sourceforge.fenixedu.domain.phd.enrolments;

import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
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

	final PhdIndividualProgramProcess process = getRegistration().getPhdIndividualProgramProcess();
	return process.hasStudyPlan() && !process.getStudyPlan().isExempted()
		&& process.getStudyPlan().isToEnrolInCurricularCourses();

    }

}
