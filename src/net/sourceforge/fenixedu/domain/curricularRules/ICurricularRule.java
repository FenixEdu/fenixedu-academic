package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public interface ICurricularRule {

    static final public Comparator<ICurricularRule> COMPARATOR_BY_BEGIN = new Comparator<ICurricularRule>() {
        public int compare(ICurricularRule o1, ICurricularRule o2) {
            return o1.getBegin().compareTo(o2.getBegin());
        }
    };

    public List<GenericPair<Object, Boolean>> getLabel();

    public DegreeModule getDegreeModuleToApplyRule();

    public CourseGroup getContextCourseGroup();

    public CompositeRule getParentCompositeRule();

    public CurricularRuleType getCurricularRuleType();

    public ExecutionSemester getBegin();

    public ExecutionSemester getEnd();

    public boolean appliesToContext(final Context context);

    public boolean appliesToCourseGroup(final CourseGroup courseGroup);

    public boolean hasContextCourseGroup();

    public boolean isCompositeRule();

    public boolean isValid(ExecutionSemester executionSemester);

    public boolean isValid(ExecutionYear executionYear);

    public boolean isVisible();
    
    public boolean isActive();

    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext);

    public RuleResult verify(final VerifyRuleLevel verifyRuleLevel, final EnrolmentContext enrolmentContext,
	    final DegreeModule degreeModuleToVerify, final CourseGroup parentCourseGroup);

    public VerifyRuleExecutor createVerifyRuleExecutor();
}
