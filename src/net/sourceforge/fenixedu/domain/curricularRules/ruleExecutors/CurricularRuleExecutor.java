package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

abstract public class CurricularRuleExecutor {

    protected CurricularRuleExecutor() {
    }

    public RuleResult execute(final ICurricularRule curricularRule, final CurricularRuleLevel level, final EnrolmentContext enrolmentContext) {
	switch (level) {
	case ENROLMENT_WITH_RULES:
	    return executeEnrolmentWithRules(curricularRule, enrolmentContext);

	case ENROLMENT_WITH_RULES_AND_TEMPORARY_ENROLMENT:
	    return executeEnrolmentWithRulesAndTemporaryEnrolment(curricularRule, enrolmentContext);

	case ENROLMENT_NO_RULES:
	    return executeEnrolmentWithNoRules(curricularRule, enrolmentContext);

	default:
	    throw new DomainException("error.curricularRules.RuleExecutor.unimplemented.rule.level");
	}
    }

    protected DegreeModuleToEnrol searchDegreeModuleToEnrol(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    if (degreeModuleToEnrol.getContext().getChildDegreeModule() == degreeModule) {
		return degreeModuleToEnrol;
	    }
	}
	return null;
    }
    
    protected DegreeModuleToEnrol searchDegreeModuleToEnrol(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	return searchDegreeModuleToEnrol(enrolmentContext, curricularRule.getDegreeModuleToApplyRule());
    }
    
    protected Collection<DegreeModuleToEnrol> collectDegreeModuleToEnrolFromCourseGroup(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
	final Collection<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();
	for (final DegreeModuleToEnrol degreeModuleToEnrol : enrolmentContext.getDegreeModuleToEnrol()) {
	    if (degreeModuleToEnrol.getContext().getParentCourseGroup() == courseGroup) {
		result.add(degreeModuleToEnrol);
	    }
	}
	return result;
    }
    
    protected boolean ruleWasSelectedFromAnyModuleToEnrol(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	return searchDegreeModuleToEnrol(enrolmentContext, curricularRule.getDegreeModuleToApplyRule()) != null;
    }
    
    protected boolean appliesToContext(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	return curricularRule.appliesToContext(searchDegreeModuleToEnrol(enrolmentContext, curricularRule).getContext());
    }
    
    /**
     *	1 - If DegreeModule from CurricularRule has no connection to Student curriculum than return false;
     *  2 - If CurricularRule has no Context CourseGroup or CurricularRule is connected to a root DegreeModule return true;
     *  3 - Else, search for parentCourseGroup and check if applies to CurricularRule. 
     */
    protected boolean appliesToCourseGroup(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, curricularRule);
	if (curriculumModule == null) {
	    return false;
	}
	if (!curricularRule.hasContextCourseGroup() || curricularRule.getDegreeModuleToApplyRule().isRoot()) {
	    return true;
	}
	final CourseGroup parentCourseGroup = curriculumModule.getCurriculumGroup().getDegreeModule();
	return curricularRule.appliesToCourseGroup(parentCourseGroup);
    }
    
    /**
     * This method check if a CurricularRule can be applied, checking it Context 
     * or CourseGroup (when rule was gathered into a collection of rules)
     * 
     * - uses ruleWasSelectedFromAnyModuleToEnrol, appliesToContext and appliesToCourseGroup methods
     * - these methods exist by their own, because some rules may apply them in different ways
     * 
     */
    protected boolean canApplyRule(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	if (ruleWasSelectedFromAnyModuleToEnrol(enrolmentContext, curricularRule)) {
	    if (!appliesToContext(enrolmentContext, curricularRule)) {
		return false;
	    }
	} else if (!appliesToCourseGroup(enrolmentContext, curricularRule)) {
	    return false;
	}
	return true;
    }
    
    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	if (degreeModule.isLeaf()) {
	    return enrolmentContext.getStudentCurricularPlan().findCurriculumLineFor((CurricularCourse) degreeModule, enrolmentContext.getExecutionPeriod());
	} else {
	    return enrolmentContext.getStudentCurricularPlan().findCurriculumGroupFor((CourseGroup) degreeModule);
	}
    }
    
    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext, final ICurricularRule curricularRule) {
	return searchCurriculumModule(enrolmentContext, curricularRule.getDegreeModuleToApplyRule());
    }
    
    protected boolean isApproved(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
	return enrolmentContext.getStudentCurricularPlan().isApproved(curricularCourse);
    }
    
    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	return degreeModule.isLeaf() ? isEnroled(enrolmentContext, (CurricularCourse) degreeModule) 
		: isEnroled(enrolmentContext, (CourseGroup) degreeModule); 
    }
    
    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse) {
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	return enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionPeriod);
    }
    
    private boolean isEnroled(final EnrolmentContext enrolmentContext, final CourseGroup courseGroup) {
	return enrolmentContext.getStudentCurricularPlan().hasDegreeModule(courseGroup);
    }
    
    protected boolean isEnroled(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return enrolmentContext.getStudentCurricularPlan().isEnroledInExecutionPeriod(curricularCourse, executionPeriod);
    }
    
    protected boolean hasEnrolmentWithEnroledState(final EnrolmentContext enrolmentContext, final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return enrolmentContext.getStudentCurricularPlan().getRoot().hasEnrolmentWithEnroledState(curricularCourse, executionPeriod);
    }

    protected boolean isEnrolling(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
        return searchDegreeModuleToEnrol(enrolmentContext, degreeModule) != null;
    }

    abstract protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext);
    abstract protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext);
    abstract protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext);
}
