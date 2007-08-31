package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;

abstract public class StudentCurricularPlanEnrolment {

    protected StudentCurricularPlan studentCurricularPlan;
    protected EnrolmentContext enrolmentContext;
    protected ExecutionPeriod executionPeriod;
    protected CurricularRuleLevel curricularRuleLevel;
    protected Person responsiblePerson;

    protected StudentCurricularPlanEnrolment(final StudentCurricularPlan studentCurricularPlan,
	    final EnrolmentContext enrolmentContext, final Person responsiblePerson) {

	this.studentCurricularPlan = studentCurricularPlan;
	this.enrolmentContext = enrolmentContext;
	this.executionPeriod = enrolmentContext.getExecutionPeriod();
	this.curricularRuleLevel = enrolmentContext.getCurricularRuleLevel();
	this.responsiblePerson = responsiblePerson;
    }

    static public StudentCurricularPlanEnrolment createManager(final StudentCurricularPlan studentCurricularPlan,
	    final EnrolmentContext enrolmentContext, final Person responsiblePerson) {

	if (enrolmentContext.getCurricularRuleLevel().managesEnrolments()) {
	    return new StudentCurricularPlanEnrolmentManager(studentCurricularPlan, enrolmentContext, responsiblePerson);

	} else if (enrolmentContext.getCurricularRuleLevel() == CurricularRuleLevel.IMPROVEMENT_ENROLMENT) {
	    return new StudentCurricularPlanImprovementOfApprovedEnrolmentManager(studentCurricularPlan, enrolmentContext,
		    responsiblePerson);

	} else if (enrolmentContext.getCurricularRuleLevel() == CurricularRuleLevel.SPECIAL_SEASON_ENROLMENT) {
	    return new StudentCurricularPlanEnrolmentInSpecialSeasonEvaluationManager(studentCurricularPlan, enrolmentContext,
		    responsiblePerson);
	}

	throw new DomainException("StudentCurricularPlanEnrolment");
    }

    final public RuleResult manage() {
	unEnrol();
	addEnroled();

	final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap = new HashMap<EnrolmentResultType, List<IDegreeModuleToEvaluate>>();
	final RuleResult result = evaluateDegreeModules(degreeModulesToEnrolMap);
	performEnrolments(degreeModulesToEnrolMap);

	return result;
    }

    abstract protected void unEnrol();

    abstract protected void addEnroled();

    private RuleResult evaluateDegreeModules(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {

	RuleResult finalResult = RuleResult.createInitialTrue();
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> rulesToEvaluate = getRulesToEvaluate();
	for (final Entry<IDegreeModuleToEvaluate, Set<ICurricularRule>> entry : rulesToEvaluate.entrySet()) {
	    RuleResult result = evaluateRules(entry.getKey(), entry.getValue());
	    finalResult = finalResult.and(result);
	}

	finalResult = evaluateExtraRules(finalResult);

	if (!finalResult.isFalse()) {
	    for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : rulesToEvaluate.keySet()) {
		addDegreeModuleToEvaluateToMap(degreeModulesEnrolMap, finalResult
			.getEnrolmentResultTypeFor(degreeModuleToEvaluate.getDegreeModule()), degreeModuleToEvaluate);
	    }

	}

	if (finalResult.isFalse()) {
	    throw new EnrollmentDomainException(finalResult);
	}

	return finalResult;
    }

    protected RuleResult evaluateExtraRules(final RuleResult actualResult) {
	// no extra rules to be executed
	return actualResult;

    }

    abstract protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate();

    private RuleResult evaluateRules(final IDegreeModuleToEvaluate degreeModuleToEvaluate,
	    final Set<ICurricularRule> curricularRules) {
	RuleResult ruleResult = RuleResult.createTrue(degreeModuleToEvaluate.getDegreeModule());

	for (final ICurricularRule rule : curricularRules) {
	    ruleResult = ruleResult.and(rule.evaluate(degreeModuleToEvaluate, enrolmentContext));
	}

	return ruleResult;
    }

    private void addDegreeModuleToEvaluateToMap(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> result,
	    final EnrolmentResultType enrolmentResultType, final IDegreeModuleToEvaluate degreeModuleToEnrol) {

	List<IDegreeModuleToEvaluate> information = result.get(enrolmentResultType);
	if (information == null) {
	    result.put(enrolmentResultType, information = new ArrayList<IDegreeModuleToEvaluate>());
	}
	information.add(degreeModuleToEnrol);
    }

    abstract protected void performEnrolments(Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap);

}
