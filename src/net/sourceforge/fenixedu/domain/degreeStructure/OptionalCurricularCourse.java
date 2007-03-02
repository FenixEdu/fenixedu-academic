package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;

public class OptionalCurricularCourse extends OptionalCurricularCourse_Base {
    
    protected  OptionalCurricularCourse() {
        super();
    }
    
    /**
     * - This constructor is used to create a 'special' curricular course
     * that will represent any curricular course accordding to a rule
     */
    public OptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn,
	    CurricularStage curricularStage, CurricularPeriod curricularPeriod,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

	this();
	setName(name);
	setNameEn(nameEn);
	setCurricularStage(curricularStage);
	setType(CurricularCourseType.OPTIONAL_COURSE);
	new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }
    
    @Override
    public boolean isOptionalCurricularCourse() {
        return true;
    }

    @Override
    public Double getMaxEctsCredits(final ExecutionPeriod executionPeriod) {
	final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionPeriod);
	if (creditsLimitRule != null) {
	    return creditsLimitRule.getMaximumCredits();
	}
	final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionPeriod);
	if (anyCurricularCourseRule != null) {
	    return anyCurricularCourseRule.getCredits();
	}
        return Double.valueOf(0d);
    }
    
    @Override
    public Double getMinEctsCredits(ExecutionPeriod executionPeriod) {
	final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionPeriod);
	if (creditsLimitRule != null) {
	    return creditsLimitRule.getMinimumCredits();
	}
	final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionPeriod);
	if (anyCurricularCourseRule != null) {
	    return anyCurricularCourseRule.getCredits();
	}
        return Double.valueOf(0d);
    }
    
    private CreditsLimit getCreditsLimitRule(final ExecutionPeriod executionPeriod) {
	final List<ICurricularRule> result = getCurricularRule(CurricularRuleType.CREDITS_LIMIT, executionPeriod);
	return result.isEmpty() ? null : (CreditsLimit) result.get(0); 
    }
    
    private AnyCurricularCourse getAnyCurricularCourseRule(final ExecutionPeriod executionPeriod) {
	final List<ICurricularRule> result = getCurricularRule(CurricularRuleType.ANY_CURRICULAR_COURSE, executionPeriod);
	return result.isEmpty() ? null : (AnyCurricularCourse) result.get(0); 
    }

    @Override
    protected Double countAllMaxEctsCredits(ExecutionPeriod executionPeriod) {
        return getMaxEctsCredits(executionPeriod);
    }
    
    @Override
    protected Double countAllMinEctsCredits(ExecutionPeriod executionPeriod) {
        return getMinEctsCredits(executionPeriod);
    }

}
