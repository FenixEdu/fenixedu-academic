package net.sourceforge.fenixedu.domain.curricularRules;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public interface ICurricularRule {
    
    public DegreeModule getDegreeModuleToApplyRule();
    public CourseGroup getContextCourseGroup();
    public CompositeRule getParentCompositeRule();
    
    public CurricularRuleType getCurricularRuleType();
    
    public ExecutionPeriod getBegin();
    public ExecutionPeriod getEnd();
    
    public boolean appliesToContext(final Context context);
    public boolean appliesToCourseGroup(final CourseGroup courseGroup);
    public boolean hasContextCourseGroup();
    
    public boolean isCompositeRule();
    public boolean isValid(ExecutionPeriod executionPeriod);
    public boolean isValid(ExecutionYear executionYear);
    
    public RuleResult evaluate(final EnrolmentContext enrolmentContext);
    public RuleResult evaluate(final EnrolmentContext enrolmentContext, final CurricularRuleLevel level);

}
