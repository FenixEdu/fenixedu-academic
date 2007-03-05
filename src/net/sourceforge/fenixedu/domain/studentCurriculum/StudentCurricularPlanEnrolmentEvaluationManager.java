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
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanEnrolmentEvaluationManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanEnrolmentEvaluationManager(StudentCurricularPlan plan, EnrolmentContext enrolmentContext, Person responsiblePerson) {
	super(plan, enrolmentContext, responsiblePerson);
    }

    @Override
    protected void unEnrol() {
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (curriculumModule.isLeaf()) {
		// find enrolment evaluation that is an improvement / special season and delete it
	    } else {
		// throw exception, error in render
	    }
	}
    }
    
    @Override
    protected void addEnroled() {
	return;
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();
	
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModuleToEvaluate()) {
	    
	    if (degreeModuleToEvaluate.canCollectRules() && degreeModuleToEvaluate.isLeaf()) {
		final CurricularCourse curricularCourse = (CurricularCourse) degreeModuleToEvaluate.getDegreeModule();
		final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		
		if (curricularRuleLevel == CurricularRuleLevel.IMPROVEMENT_ENROLMENT) {
		    curricularRules.add(null /* TODO new rule for improvements */);
		} else if (curricularRuleLevel == CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT) {
		    curricularRules.add(null /* TODO new rule for special season */);
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
			// create enrolment evaluation
		    }
		} else {
		    throw new DomainException("shit, hell break loose");
		}
	    }
	}
    }

}
