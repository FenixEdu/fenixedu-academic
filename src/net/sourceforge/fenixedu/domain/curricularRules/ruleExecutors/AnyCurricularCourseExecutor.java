package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.OptionalDegreeModuleToEnrol;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class AnyCurricularCourseExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final AnyCurricularCourse rule = (AnyCurricularCourse) curricularRule;
	final OptionalDegreeModuleToEnrol moduleToEnrol = (OptionalDegreeModuleToEnrol) searchDegreeModuleToEnrol(enrolmentContext, rule);

	if (!rule.appliesToContext(moduleToEnrol.getContext())) {
	    return RuleResult.createNA();
	}
	
	final CurricularCourse curricularCourseToEnrol = moduleToEnrol.getCurricularCourse();
	final Degree degree = curricularCourseToEnrol.getDegree();
	
	 /**
         * -> if getDegree() == null 
         *      ? getBolonhaDegreeType() == null ? any degree from IST
         *      ? getBolonhaDegreeType() != null ? any degree with same DegreeType
         * -> else 
         * 	? use selected degree
         * 
         * -> if departmentUnit != null ? curricular courses from competence courses that belong to that department      
         */
	
	boolean result = true;
	
	result &= rule.hasDegree() ? rule.getDegree() == degree : rule.hasBolonhaDegreeType() ? degree
		.getDegreeType() == rule.getBolonhaDegreeType() : true;
		
	result &= rule.hasDepartmentUnit() ? rule.getDepartmentUnit().hasCompetenceCourses(
		curricularCourseToEnrol.getCompetenceCourse()) : true;
	
	return result ? RuleResult.createTrue() : RuleResult.createFalse(CurricularRuleLabelFormatter.getLabel(rule));
    }

    @Override
    protected RuleResult executeWithRulesAndTemporaryEnrolment(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return executeWithRules(curricularRule, enrolmentContext);
    }

    @Override
    protected RuleResult executeNoRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

}
