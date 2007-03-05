package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.AssertUniqueApprovalInCurricularCourseContexts;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;

public class StudentCurricularPlanEnrolmentManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanEnrolmentManager(StudentCurricularPlan plan, EnrolmentContext enrolmentContext, Person responsiblePerson) {
	super(plan, enrolmentContext, responsiblePerson);
    }

    @Override
    protected void unEnrol() {
	// First remove Enrolments
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (curriculumModule.isLeaf()) {
		curriculumModule.delete();
	    }
	}
	
	// After, remove CurriculumGroups and evaluate rules
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (!curriculumModule.isLeaf()) {
		curriculumModule.delete();
	    }
	}
    }
    
    @Override
    protected void addEnroled() {
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : studentCurricularPlan.getDegreeModulesToEvaluate(executionPeriod)) {
	    enrolmentContext.addDegreeModuleToEvaluate(degreeModuleToEvaluate);
	}
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();
	
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModuleToEvaluate()) {
	    
	    if (degreeModuleToEvaluate.canCollectRules()) {
		
		final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		curricularRules.addAll(degreeModuleToEvaluate.getCurricularRulesFromDegreeModule(executionPeriod));
		curricularRules.addAll(degreeModuleToEvaluate.getCurricularRulesFromCurriculumGroup(executionPeriod));
		
		if (degreeModuleToEvaluate.isLeaf()) {
		    final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();
		    curricularRules.add(new AssertUniqueApprovalInCurricularCourseContexts(curricularCourse));
		}
		result.put(degreeModuleToEvaluate, curricularRules);
	    }
	}
	
	return result;
    }

    @Override
    protected void performEnrolments(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {
	final String createdBy = responsiblePerson.getIstUsername();
	for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesEnrolMap.entrySet()) {

	    final EnrollmentCondition enrollmentCondition = entry.getKey().getEnrollmentCondition();
	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {

		if (degreeModuleToEvaluate.isEnroled()) {

		    final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;

		    if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
			final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();
			enrolment.setEnrolmentCondition(enrollmentCondition);
		    }
		} else {

		    final DegreeModule degreeModule = degreeModuleToEvaluate.getDegreeModule();
		    final CurriculumGroup curriculumGroup = degreeModuleToEvaluate.getCurriculumGroup();

		    if (degreeModule.isLeaf()) {
			if (degreeModuleToEvaluate.isOptional()) {
			    createOptionalEnrolmentFor(enrollmentCondition,
				    degreeModuleToEvaluate, curriculumGroup);

			} else {
			    new Enrolment(studentCurricularPlan, curriculumGroup,
				    (CurricularCourse) degreeModule, executionPeriod, enrollmentCondition, createdBy);
			}
		    } else {
			new CurriculumGroup(degreeModuleToEvaluate.getCurriculumGroup(),
				(CourseGroup) degreeModuleToEvaluate.getDegreeModule(), executionPeriod);
		    }

		}
	    }
	}
    }

    private void createOptionalEnrolmentFor(final EnrollmentCondition enrollmentCondition, final IDegreeModuleToEvaluate ixpto,
	    final CurriculumGroup curriculumGroup) {

	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = (OptionalDegreeModuleToEnrol) ixpto;
	final OptionalCurricularCourse optionalCurricularCourse = (OptionalCurricularCourse) optionalDegreeModuleToEnrol
		.getDegreeModule();
	final CurricularCourse curricularCourse = optionalDegreeModuleToEnrol.getCurricularCourse();

	enrolmentContext.getStudentCurricularPlan().createOptionalEnrolment(curriculumGroup,
		executionPeriod, optionalCurricularCourse, curricularCourse,
		enrollmentCondition);
    }

}
