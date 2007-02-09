package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

abstract public class CurricularRuleExecutor {

    protected CurricularRuleExecutor() {
    }

    public RuleResult execute(final CurricularRule curricularRule, final CurricularRuleLevel level, final EnrolmentContext enrolmentContext) {
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
    
    protected DegreeModuleToEnrol searchDegreeModuleToEnrol(final EnrolmentContext enrolmentContext, final CurricularRule curricularRule) {
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
    
    protected boolean ruleWasSelectedFromAnyModuleToEnrol(final EnrolmentContext enrolmentContext, final CurricularRule curricularRule) {
	return searchDegreeModuleToEnrol(enrolmentContext, curricularRule.getDegreeModuleToApplyRule()) != null;
    }
    
    protected boolean appliesToContext(final EnrolmentContext enrolmentContext, final CurricularRule curricularRule) {
	return curricularRule.appliesToContext(searchDegreeModuleToEnrol(enrolmentContext, curricularRule).getContext());
    }
    
    protected boolean appliesToCourseGroup(final EnrolmentContext enrolmentContext, final CurricularRule curricularRule) {
	if (!curricularRule.hasContextCourseGroup() || curricularRule.getDegreeModuleToApplyRule().isRoot()) {
	    return true;
	}
	final CurriculumModule curriculumModule = searchCurriculumModule(enrolmentContext, curricularRule);
	final CourseGroup parentCourseGroup = curriculumModule.getCurriculumGroup().getDegreeModule();
	return curricularRule.appliesToCourseGroup(parentCourseGroup);
    }
    
    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
	return enrolmentContext.getStudentCurricularPlan().findCurriculumModuleFor(degreeModule);
    }
    
    protected CurriculumModule searchCurriculumModule(final EnrolmentContext enrolmentContext, final CurricularRule curricularRule) {
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

    protected boolean isEnrolling(final EnrolmentContext enrolmentContext, final DegreeModule degreeModule) {
        return searchDegreeModuleToEnrol(enrolmentContext, degreeModule) != null;
    }

    abstract protected RuleResult executeEnrolmentWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext);
    abstract protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext);
    abstract protected RuleResult executeEnrolmentWithNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext);
}
