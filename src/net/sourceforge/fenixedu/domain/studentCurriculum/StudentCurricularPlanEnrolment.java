package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.curricularRules.AssertUniqueApprovalInCurricularCourseContexts;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.EnrolmentResultType;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.CurriculumModuleEnroledWrapper;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class StudentCurricularPlanEnrolment {

    private final Map<ICurricularRule, RuleResult> cachedRuleResults = new HashMap<ICurricularRule, RuleResult>();

    public StudentCurricularPlanEnrolment() {
    }

    public void enrol(final Person person, final EnrolmentContext enrolmentContext) {
	checkRulesToEnrol(person, enrolmentContext);
	unEnrolFromCurriculumModules(enrolmentContext);
	performEnrolmentInCurriculumModules(person, enrolmentContext, prepareCurriculumModulesEnrolmentInformation(enrolmentContext));
    }

    private Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> prepareCurriculumModulesEnrolmentInformation(final EnrolmentContext enrolmentContext) {

	final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap = new HashMap<EnrolmentResultType, List<IDegreeModuleToEvaluate>>();
	final List<RuleResult> falseResults = evaluateDegreeModules(enrolmentContext, degreeModulesEnrolMap);
	if (!falseResults.isEmpty()) {
	    throw new EnrollmentDomainException(falseResults);
	}
	return degreeModulesEnrolMap;
    }

    private void checkRulesToEnrol(final Person person, final EnrolmentContext enrolmentContext) {

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModuleToEvaluate()) {
	    if (enrolmentContext.getCurriculumModulesToRemove().contains(degreeModuleToEvaluate.getCurriculumGroup())) {
		throw new DomainException(
			"error.StudentCurricularPlan.cannot.remove.enrollment.on.curriculum.group.because.other.enrollments.depend.on.it",
			degreeModuleToEvaluate.getCurriculumGroup().getName().getContent());
	    }
	}

	final Registration registration = enrolmentContext.getStudentCurricularPlan().getRegistration();
	if (!registration.isActive()) {
	    throw new DomainException(
		    "error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
	}
	
	if (!registration.getPayedTuition()) {
	    throw new DomainException(
		    "error.StudentCurricularPlan.cannot.enrol.with.gratuity.debts.for.previous.execution.years");
	}

	final DegreeCurricularPlan degreeCurricularPlan = enrolmentContext.getStudentCurricularPlan()
		.getDegreeCurricularPlan();
	if (person.hasRole(RoleType.STUDENT)
		&& !degreeCurricularPlan.hasOpenEnrolmentPeriodInCurricularCoursesFor(enrolmentContext
			.getExecutionPeriod())) {
	    throw new DomainException(
		    "error.StudentCurricularPlan.students.can.only.perform.curricular.course.enrollment.inside.established.periods");
	}
    }

    private void unEnrolFromCurriculumModules(final EnrolmentContext enrolmentContext) {
	// First remove Enrolments
	for (final CurriculumModule curriculumModule : enrolmentContext.getCurriculumModulesToRemove()) {
	    if (curriculumModule.isLeaf()) {
		curriculumModule.delete();
	    }
	}
	// After, remove CurriculumGroups
	for (final CurriculumModule curriculumModule : enrolmentContext.getCurriculumModulesToRemove()) {
	    if (!curriculumModule.isLeaf()) {
		curriculumModule.delete();
	    }
	}
    }

    private RuleResult evaluateRules(final EnrolmentContext enrolmentContext, final Set<ICurricularRule> curricularRules) {
	RuleResult ruleResult = RuleResult.createTrue();

	for (final ICurricularRule rule : curricularRules) {
	    RuleResult cachedResult;
	    boolean copyMessages = true;
	    if (getCachedRuleResults().containsKey(rule)) {
		cachedResult = getCachedRuleResults().get(rule);
		copyMessages = false;
	    } else {
		getCachedRuleResults().put(rule, cachedResult = rule.evaluate(enrolmentContext));
	    }
	    ruleResult = ruleResult.and(cachedResult, copyMessages);
	}
	return ruleResult;
    }

    private Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> getRulesToEvaluate(final EnrolmentContext enrolmentContext) {
	final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> curricularRulesByDegreeModule = new HashMap<IDegreeModuleToEvaluate, Set<ICurricularRule>>();
	addCurricularRulesFromDegreeModuleToEnrol(enrolmentContext, curricularRulesByDegreeModule);
	addCurricularRulesFromExistingEnrolmentsInStudentCurricularPlan(enrolmentContext, curricularRulesByDegreeModule);
	return curricularRulesByDegreeModule;
    }

    private void addCurricularRulesFromExistingEnrolmentsInStudentCurricularPlan(final EnrolmentContext enrolmentContext, final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> curricularRulesByDegreeModule) {
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getStudentCurricularPlan().getDegreeModulesToEvaluate(executionPeriod)) {
	    
	    if (degreeModuleToEvaluate.canCollectRules()) {
		final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		curricularRules.addAll(degreeModuleToEvaluate.getDegreeModule().getCurricularRules(executionPeriod));
		curricularRules.addAll(degreeModuleToEvaluate.getCurriculumGroup().getCurricularRules(executionPeriod));
		curricularRulesByDegreeModule.put(degreeModuleToEvaluate, curricularRules);
		
		if (degreeModuleToEvaluate.isLeaf()) {
		    curricularRules.add(new AssertUniqueApprovalInCurricularCourseContexts((CurricularCourse) degreeModuleToEvaluate.getDegreeModule()));
		}
	    }

	    enrolmentContext.addDegreeModuleToEvaluate(degreeModuleToEvaluate);
	}
    }

    private void addCurricularRulesFromDegreeModuleToEnrol(final EnrolmentContext enrolmentContext,
	    final Map<IDegreeModuleToEvaluate, Set<ICurricularRule>> curricularRulesByDegreeModule) {
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	
	for (final IDegreeModuleToEvaluate degreeModuleToEvaluate : enrolmentContext.getDegreeModuleToEvaluate()) {

	    if (!degreeModuleToEvaluate.isEnroled()) {

		final Set<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
		final DegreeModule degreeModule = degreeModuleToEvaluate.getDegreeModule();

		curricularRules.addAll(degreeModule.getCurricularRules(executionPeriod));
		curricularRules.addAll(degreeModuleToEvaluate.getCurriculumGroup().getCurricularRules(executionPeriod));

		if (degreeModule instanceof CurricularCourse) {
		    curricularRules.add(new AssertUniqueApprovalInCurricularCourseContexts((CurricularCourse) degreeModule));
		}
		curricularRulesByDegreeModule.put(degreeModuleToEvaluate, curricularRules);
	    }
	}
    }

    private List<RuleResult> evaluateDegreeModules(final EnrolmentContext enrolmentContext, final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {

	final List<RuleResult> falseRuleResults = new ArrayList<RuleResult>();
	for (final Entry<IDegreeModuleToEvaluate, Set<ICurricularRule>> entry : getRulesToEvaluate(enrolmentContext).entrySet()) {

	    final RuleResult ruleResult = evaluateRules(enrolmentContext, entry.getValue());
	    if (ruleResult.isFalse()) {
		falseRuleResults.add(ruleResult);

	    } else if (falseRuleResults.isEmpty() && ruleResult.isTrue()) {
		addDegreeModuleToEvaluateToMap(degreeModulesEnrolMap, ruleResult.getEnrolmentResultType(), entry.getKey());
	    }
	}
	return falseRuleResults;
    }

    private void performEnrolmentInCurriculumModules(final Person person,
	    final EnrolmentContext enrolmentContext,
	    final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> degreeModulesEnrolMap) {

	final String createdBy = person.getIstUsername();
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
			    createOptionalEnrolmentFor(enrolmentContext, enrollmentCondition, degreeModuleToEvaluate,
				    curriculumGroup);

			} else {
			    new Enrolment(enrolmentContext.getStudentCurricularPlan(), curriculumGroup,
				    (CurricularCourse) degreeModule, enrolmentContext
					    .getExecutionPeriod(), enrollmentCondition, createdBy);
			}
		    } else {
			new CurriculumGroup(degreeModuleToEvaluate.getCurriculumGroup(),
				(CourseGroup) degreeModuleToEvaluate.getDegreeModule(), enrolmentContext.getExecutionPeriod());
		    }

		}

	    }

	}
    }

    private void addDegreeModuleToEvaluateToMap(final Map<EnrolmentResultType, List<IDegreeModuleToEvaluate>> result,
	    final EnrolmentResultType enrolmentResultType, final IDegreeModuleToEvaluate degreeModuleToEnrol) {
	
	List<IDegreeModuleToEvaluate> information = result.get(enrolmentResultType);
	if (information == null) {
	    result.put(enrolmentResultType, information = new ArrayList<IDegreeModuleToEvaluate>());
	}
	information.add(degreeModuleToEnrol);
    }

    private void createOptionalEnrolmentFor(final EnrolmentContext enrolmentContext,
	    final EnrollmentCondition enrollmentCondition, final IDegreeModuleToEvaluate ixpto,
	    final CurriculumGroup curriculumGroup) {

	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = (OptionalDegreeModuleToEnrol) ixpto;
	final OptionalCurricularCourse optionalCurricularCourse = (OptionalCurricularCourse) optionalDegreeModuleToEnrol
		.getDegreeModule();
	final CurricularCourse curricularCourse = optionalDegreeModuleToEnrol.getCurricularCourse();

	enrolmentContext.getStudentCurricularPlan().createOptionalEnrolment(curriculumGroup,
		enrolmentContext.getExecutionPeriod(), optionalCurricularCourse, curricularCourse,
		enrollmentCondition);
    }

    private Map<ICurricularRule, RuleResult> getCachedRuleResults() {
	return cachedRuleResults;
    }

}
