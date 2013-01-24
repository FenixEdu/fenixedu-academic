package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanExtraEnrolmentManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanExtraEnrolmentManager(final EnrolmentContext enrolmentContext) {
	super(enrolmentContext);
    }

    @Override
    protected void assertEnrolmentPreConditions() {
	if (isResponsiblePersonManager()) {
	    return;
	}

	if (getRegistration().isRegistrationConclusionProcessed()) {
	    checkUpdateRegistrationAfterConclusion();
	}

	checkEnrolingDegreeModules();
    }

    private void checkEnrolingDegreeModules() {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (degreeModuleToEvaluate.isEnroling()) {
		if (!degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
		    throw new DomainException(
			    "error.StudentCurricularPlanExtraEnrolmentManager.can.only.enrol.in.curricularCourses");
		}
		checkIDegreeModuleToEvaluate((CurricularCourse) degreeModuleToEvaluate.getDegreeModule());
	    }
	}
    }

    private void checkIDegreeModuleToEvaluate(final CurricularCourse curricularCourse) {
	if (getStudentCurricularPlan().isApproved(curricularCourse, getExecutionSemester())) {
	    throw new DomainException("error.already.aproved", curricularCourse.getName());
	}

	if (getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, getExecutionSemester())) {
	    throw new DomainException("error.already.enroled.in.executionPeriod", curricularCourse.getName(),
		    getExecutionSemester().getQualifiedName());
	}
    }

    @Override
    protected void addEnroled() {
	// nothing to be done
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModulesToEvaluate()) {
	    if (degreeModuleToEvaluate.isEnroling() && degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
		result.put(degreeModuleToEvaluate, Collections.<ICurricularRule> emptySet());
	    }
	}
	return result;
    }

    @Override
    protected void performEnrolments(Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap) {
	for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEnrolMap.entrySet()) {
	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
		if (degreeModuleToEvaluate.isEnroling() && degreeModuleToEvaluate.getDegreeModule().isCurricularCourse()) {
		    final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();

		    checkIDegreeModuleToEvaluate(curricularCourse);
		    new Enrolment(getStudentCurricularPlan(), degreeModuleToEvaluate.getCurriculumGroup(), curricularCourse,
			    getExecutionSemester(), EnrollmentCondition.VALIDATED, getResponsiblePerson().getIstUsername());
		}
	    }
	}
    }

    @Override
    protected void unEnrol() {
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (curriculumModule.isLeaf()) {
		curriculumModule.delete();
	    }
	}
    }

}
