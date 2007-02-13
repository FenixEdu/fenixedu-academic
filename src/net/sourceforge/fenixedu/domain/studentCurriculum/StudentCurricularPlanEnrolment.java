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
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrollmentDomainException;
import net.sourceforge.fenixedu.domain.exceptions.UnEnrollmentDomainException;
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

    private Map<EnrolmentResultType, List<DegreeModuleToEnrol>> prepareCurriculumModulesEnrolmentInformation(
	    final EnrolmentContext enrolmentContext) {
	
	final Map<EnrolmentResultType, List<DegreeModuleToEnrol>> degreeModulesEnrolMap = buildDegreeModulesToEnrolMap();
	final List<RuleResult> falseResults = evaluateDegreeModulesToEnrol(enrolmentContext, degreeModulesEnrolMap);
	if (!falseResults.isEmpty()) {
	    throw new EnrollmentDomainException(falseResults);
	}
	return degreeModulesEnrolMap;
    }
    
    private void checkRulesToEnrol(final Person person, final EnrolmentContext enrolmentContext) {

	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    if (enrolmentContext.getCurriculumModulesToRemove().contains(degreeModuleToEnrol.getCurriculumGroup())) {
		throw new DomainException(
			"error.StudentCurricularPlan.cannot.remove.enrollment.on.curriculum.group.because.other.enrollments.depend.on.it",
			degreeModuleToEnrol.getCurriculumGroup().getName().getContent());
	    }
	}

	final Registration registration = enrolmentContext.getStudentCurricularPlan().getRegistration();
	if (!registration.isActive()) {
	    throw new DomainException(
		    "error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
	}

	final DegreeCurricularPlan degreeCurricularPlan = enrolmentContext.getStudentCurricularPlan().getDegreeCurricularPlan();
	if (person.hasRole(RoleType.STUDENT)
		&& !degreeCurricularPlan.hasOpenEnrolmentPeriodInCurricularCoursesFor(enrolmentContext.getExecutionPeriod())) {
	    throw new DomainException(
		    "error.StudentCurricularPlan.students.can.only.perform.curricular.course.enrollment.inside.established.periods");
	}
    }
    
    private void unEnrolFromCurriculumModules(final EnrolmentContext enrolmentContext) {

	final Map<DegreeModule, Set<ICurricularRule>> collectedCurricularRules = new HashMap<DegreeModule, Set<ICurricularRule>>();
	
	// First remove Enrolments and evaluate rules 
	for (final CurriculumModule curriculumModule : enrolmentContext.getCurriculumModulesToRemove()) {
	    if (curriculumModule.isLeaf()) {
		collectedCurricularRules.put(curriculumModule.getDegreeModule(), getCurricularRulesFromCurriculumModule(enrolmentContext, curriculumModule));
		curriculumModule.delete();
	    }
	}

	// After, remove CurriculumGroups and evaluate rules
	for (final CurriculumModule curriculumModule : enrolmentContext.getCurriculumModulesToRemove()) {
	    if (!curriculumModule.isLeaf()) {
		collectedCurricularRules.put(curriculumModule.getDegreeModule(), getCurricularRulesFromCurriculumModule(enrolmentContext, curriculumModule));
		curriculumModule.delete();
		
	    }
	}
	
	final List<RuleResult> falseRuleResults = evaluateDegreeModulesToUnenrol(enrolmentContext, collectedCurricularRules);
	if (!falseRuleResults.isEmpty()) { 
	    throw new UnEnrollmentDomainException(falseRuleResults);
	}
    }

    private List<RuleResult> evaluateDegreeModulesToUnenrol(final EnrolmentContext enrolmentContext, final Map<DegreeModule, Set<ICurricularRule>> collectedCurricularRules) {
	final List<RuleResult> falseRuleResults = new ArrayList<RuleResult>();
	for (final Entry<DegreeModule, Set<ICurricularRule>> entry : collectedCurricularRules.entrySet()) {
	    final RuleResult ruleResult = evaluateRules(enrolmentContext, entry.getValue());
	    if (ruleResult.isFalse()) {
		falseRuleResults.add(ruleResult);
	    }
	}
	return falseRuleResults;
    }

    private Set<ICurricularRule> getCurricularRulesFromCurriculumModule(final EnrolmentContext enrolmentContext, CurriculumModule curriculumModule) {
	final Set<ICurricularRule> curricularRules = curriculumModule.getCurriculumGroup().getCurricularRules(enrolmentContext.getExecutionPeriod());
	curricularRules.addAll(curriculumModule.getDegreeModule().getParticipatingCurricularRules());
	return curricularRules;
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
    
    private Map<DegreeModuleToEnrol, Set<ICurricularRule>> getRulesToEvaluate(final EnrolmentContext enrolmentContext) {

	final Map<DegreeModuleToEnrol, Set<ICurricularRule>> curricularRulesByDegreeModule = new HashMap<DegreeModuleToEnrol, Set<ICurricularRule>>();

	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    final HashSet<ICurricularRule> curricularRules = new HashSet<ICurricularRule>();
	    
	    final DegreeModule degreeModule = degreeModuleToEnrol.getContext().getChildDegreeModule();
	    final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	    
	    curricularRules.addAll(degreeModule.getCurricularRules(executionPeriod));
	    if (degreeModule instanceof CurricularCourse) {
		curricularRules.add(new AssertUniqueApprovalInCurricularCourseContexts((CurricularCourse) degreeModule));
	    }
	    curricularRules.addAll(degreeModuleToEnrol.getCurriculumGroup().getCurricularRules(executionPeriod));
	    curricularRulesByDegreeModule.put(degreeModuleToEnrol, curricularRules);
	}

	return curricularRulesByDegreeModule;
    }
    
    private List<RuleResult> evaluateDegreeModulesToEnrol(final EnrolmentContext enrolmentContext,
	    final Map<EnrolmentResultType, List<DegreeModuleToEnrol>> degreeModulesEnrolMap) {
	
	final List<RuleResult> falseRuleResults = new ArrayList<RuleResult>();
	for (final Entry<DegreeModuleToEnrol, Set<ICurricularRule>> entry : getRulesToEvaluate(enrolmentContext).entrySet()) {

	    final RuleResult ruleResult = evaluateRules(enrolmentContext, entry.getValue());
	    if (ruleResult.isFalse()) {
		falseRuleResults.add(ruleResult);
		
	    } else if (falseRuleResults.isEmpty() && ruleResult.isTrue()) {
		addDegreeModelToEnrolMap(degreeModulesEnrolMap, ruleResult.getEnrolmentResultType(), entry.getKey());
	    }
	}
	return falseRuleResults;
    }

    private void performEnrolmentInCurriculumModules(final Person person, final EnrolmentContext enrolmentContext,
	    final Map<EnrolmentResultType, List<DegreeModuleToEnrol>> degreeModulesEnrolMap) {

	final String createdBy = person.getIstUsername();
	for (final Entry<EnrolmentResultType, List<DegreeModuleToEnrol>> entry : degreeModulesEnrolMap.entrySet()) {
	    
	    final EnrollmentCondition enrollmentCondition = entry.getKey().getEnrollmentCondition();
	    for (final DegreeModuleToEnrol degreeModuleToEnrol : entry.getValue()) {
		
		final DegreeModule degreeModule = degreeModuleToEnrol.getContext().getChildDegreeModule();
		final CurriculumGroup curriculumGroup = degreeModuleToEnrol.getCurriculumGroup();
		
		if (degreeModule.isLeaf()) {
		    if (degreeModuleToEnrol.isOptional()) {
			createOptionalEnrolmentFor(enrolmentContext, enrollmentCondition,
				degreeModuleToEnrol, curriculumGroup);

		    } else {
			new Enrolment(enrolmentContext.getStudentCurricularPlan(), curriculumGroup,
				(CurricularCourse) degreeModule, enrolmentContext.getExecutionPeriod(),
				enrollmentCondition, createdBy);
		    }
		} else {
		    new CurriculumGroup(degreeModuleToEnrol.getCurriculumGroup(),
			    (CourseGroup) degreeModuleToEnrol.getContext().getChildDegreeModule(),
			    enrolmentContext.getExecutionPeriod());
		}
	    }

	}
    }
    
    private Map<EnrolmentResultType, List<DegreeModuleToEnrol>> buildDegreeModulesToEnrolMap() {
	final Map<EnrolmentResultType, List<DegreeModuleToEnrol>> result = 
	    new HashMap<EnrolmentResultType, List<DegreeModuleToEnrol>>(2);
	result.put(EnrolmentResultType.FINAL, new ArrayList<DegreeModuleToEnrol>());
	result.put(EnrolmentResultType.TEMPORARY, new ArrayList<DegreeModuleToEnrol>());

	return result;
    }
    
    private void addDegreeModelToEnrolMap(
	    final Map<EnrolmentResultType, List<DegreeModuleToEnrol>> result,
	    final EnrolmentResultType enrolmentResultType, final DegreeModuleToEnrol degreeModuleToEnrol) {
	result.get(enrolmentResultType).add(degreeModuleToEnrol);
    }

    private void createOptionalEnrolmentFor(final EnrolmentContext enrolmentContext,
	    final EnrollmentCondition enrollmentCondition,
	    final DegreeModuleToEnrol degreeModuleToEnrol, final CurriculumGroup curriculumGroup) {

	final OptionalDegreeModuleToEnrol optionalDegreeModuleToEnrol = (OptionalDegreeModuleToEnrol) degreeModuleToEnrol;
	final OptionalCurricularCourse optionalCurricularCourse = (OptionalCurricularCourse) optionalDegreeModuleToEnrol
		.getContext().getChildDegreeModule();
	final CurricularCourse curricularCourse = optionalDegreeModuleToEnrol.getCurricularCourse();

	enrolmentContext.getStudentCurricularPlan().createOptionalEnrolment(curriculumGroup,
		enrolmentContext.getExecutionPeriod(), optionalCurricularCourse, curricularCourse,
		enrollmentCondition);
    }

    public Map<ICurricularRule, RuleResult> getCachedRuleResults() {
        return cachedRuleResults;
    }


}
