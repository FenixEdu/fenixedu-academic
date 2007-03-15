package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.EnrolmentInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfEnrolmentsInSpecialSeasonEvaluation;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager extends StudentCurricularPlanEnrolment {

    public StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager(StudentCurricularPlan plan, EnrolmentContext enrolmentContext, Person responsiblePerson) {
	super(plan, enrolmentContext, responsiblePerson);
    }

    @Override
    protected void unEnrol() {
	for (final CurriculumModule curriculumModule : enrolmentContext.getToRemove()) {
	    if (curriculumModule instanceof Enrolment) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		enrolment.deleteSpecialSeasonEvaluation();
	    } else {
		throw new DomainException("StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
	    }
	}
    }
    
    @Override
    protected void addEnroled() {
	for (final Enrolment enrolment : studentCurricularPlan.getSpecialSeasonEnrolments(executionPeriod.getExecutionYear())) {
	    enrolmentContext.addDegreeModuleToEvaluate(new CurriculumModuleEnroledWrapper(enrolment, executionPeriod));
	}
    }

    @Override
    protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate() {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> result = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();
	
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModuleToEvaluate()) {
	    
	    if (degreeModuleToEvaluate.isEnroled() && degreeModuleToEvaluate.canCollectRules()) {
		final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;
		
		if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
		    final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();

		    final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		    if (!enrolment.hasSpecialSeason()) {
			curricularRules.add(new EnrolmentInSpecialSeasonEvaluation(enrolment));
		    }
		    curricularRules.add(new MaximumNumberOfEnrolmentsInSpecialSeasonEvaluation());

		    result.put(degreeModuleToEvaluate, curricularRules);
		} else {
		    throw new DomainException("StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
		}
	    }
	}
	
	return result;
    }

    @Override
    protected void performEnrolments(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEvaluate) {
	for (final Entry<EnrolmentResultType, List<IDegreeModuleToEvaluate>> entry : degreeModulesToEvaluate.entrySet()) {

	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : entry.getValue()) {
		if (degreeModuleToEvaluate.isEnroled()) {
		    final CurriculumModuleEnroledWrapper moduleEnroledWrapper = (CurriculumModuleEnroledWrapper) degreeModuleToEvaluate;

		    if (moduleEnroledWrapper.getCurriculumModule() instanceof Enrolment) {
			final Enrolment enrolment = (Enrolment) moduleEnroledWrapper.getCurriculumModule();
			
			if (!enrolment.hasSpecialSeason()) {
			    enrolment.createSpecialSeasonEvaluation(responsiblePerson.getEmployee());
			}
		    } else {
			throw new DomainException("StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager.can.only.manage.enrolment.evaluations.of.enrolments");
		    }
		}
	    }
	}
    }

}
