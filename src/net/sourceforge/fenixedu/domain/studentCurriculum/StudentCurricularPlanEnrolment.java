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
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;

abstract public class StudentCurricularPlanEnrolment {

    protected StudentCurricularPlan studentCurricularPlan;
    protected EnrolmentContext enrolmentContext;
    protected ExecutionPeriod executionPeriod;
    protected CurricularRuleLevel curricularRuleLevel;
    protected Person responsiblePerson;
    private final Map<ICurricularRule, RuleResult> cachedRuleResults = new HashMap<ICurricularRule, RuleResult>();
    
    public StudentCurricularPlanEnrolment(final StudentCurricularPlan studentCurricularPlan,
	    final EnrolmentContext enrolmentContext, final Person responsiblePerson) {

	this.studentCurricularPlan = studentCurricularPlan;
	this.enrolmentContext = enrolmentContext;
	this.executionPeriod = enrolmentContext.getExecutionPeriod();
	this.curricularRuleLevel = enrolmentContext.getCurricularRuleLevel();
	this.responsiblePerson = responsiblePerson;
    }

    final public List<RuleResult> manage() {
	unEnrol();
	addEnroled();
	
	final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap = new HashMap<EnrolmentResultType, List<IDegreeModuleToEvaluate>>();
	final List<RuleResult> ruleResults = evaluateDegreeModules(degreeModulesToEnrolMap);
	performEnrolments(degreeModulesToEnrolMap);

	return ruleResults;
    }

    abstract protected void unEnrol();
    
    abstract protected void addEnroled();
    
    private List<RuleResult> evaluateDegreeModules(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {
	final List<RuleResult> falseRuleResults = new ArrayList<RuleResult>();
	final List<RuleResult> ruleResults = new ArrayList<RuleResult>();

	for (final Entry<IDegreeModuleToEvaluate, Set<ICurricularRule>> entry : getRulesToEvaluate().entrySet()) {
	    final RuleResult ruleResult = evaluateRules(entry.getKey(), entry.getValue());

	    if (ruleResult.isFalse()) {
		falseRuleResults.add(ruleResult);
	    } else if (falseRuleResults.isEmpty()) {
		addDegreeModuleToEvaluateToMap(degreeModulesEnrolMap, ruleResult.getEnrolmentResultType(), entry.getKey());
		ruleResults.add(ruleResult);
	    }
	}

	if (!falseRuleResults.isEmpty()) {
	    throw new EnrollmentDomainException(falseRuleResults);
	}

	return ruleResults;
    }

    abstract protected Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate();
    
    private RuleResult evaluateRules(final IDegreeModuleToEvaluate degreeModuleToEvaluate, final Set<ICurricularRule> curricularRules) {
	RuleResult ruleResult = RuleResult.createTrue();
	
	for (final ICurricularRule rule : curricularRules) {
	    RuleResult cachedResult;
	    boolean copyMessages = true;
	    if (cachedRuleResults.containsKey(rule)) {
		cachedResult = cachedRuleResults.get(rule);
		copyMessages = false;
	    } else {
		cachedRuleResults.put(rule, cachedResult = rule.evaluate(degreeModuleToEvaluate, enrolmentContext));
	    }
	    ruleResult = ruleResult.and(cachedResult, copyMessages);
	}
	return ruleResult;
    }
    
    private void addDegreeModuleToEvaluateToMap(
	    final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> result,
	    final EnrolmentResultType enrolmentResultType,
	    final IDegreeModuleToEvaluate degreeModuleToEnrol) {

	List<IDegreeModuleToEvaluate> information = result.get(enrolmentResultType);
	if (information == null) {
	    result.put(enrolmentResultType, information = new ArrayList<IDegreeModuleToEvaluate>());
	}
	information.add(degreeModuleToEnrol);
    }

    abstract protected void performEnrolments(Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesToEnrolMap);
    
}
