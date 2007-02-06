package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import pt.ist.utl.fenix.utils.Pair;

public class PreviousYearsEnrolmentExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeWithRules(final CurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final Map<Integer, Pair<Integer, Integer>> enrolmentInformation = groupCurricularCoursesEnrolmentInformationByYear(enrolmentContext);
	final int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration().intValue();

	for (int year = numberOfYears ; year >= 1; year--) {

	    final Pair<Integer, Integer> values = enrolmentInformation.get(year);
	    final int numberOfCurricularCoursesToEnrol = values.getKey().intValue();
	    
	    if (numberOfCurricularCoursesToEnrol > 0) {
		if (previousYearsHaveNotChosenCurricularCoursesToEnrol(year - 1, enrolmentInformation)) {
		    RuleResult.createFalse("curricularRules.rulesExecutor.PreviousYearsEnrolmentExecutor");
		}
	    }
	}
	
	return RuleResult.createTrue();
    }

    private boolean previousYearsHaveNotChosenCurricularCoursesToEnrol(int year, final Map<Integer, Pair<Integer, Integer>> enrolmentInformation) {
	while (year > 0) {
	    final Pair<Integer, Integer> values = enrolmentInformation.get(year);
	    final int numberOfNotChosenCurricularCoursesToEnrol = values.getValue().intValue();
	    if (numberOfNotChosenCurricularCoursesToEnrol > 0) {
		return true;
	    }
	    year--;
	}
	return false;
    }

    private Map<Integer, Pair<Integer, Integer>> groupCurricularCoursesEnrolmentInformationByYear(final EnrolmentContext enrolmentContext) {
	final Map<Integer, Pair<Integer, Integer>> result = new HashMap<Integer, Pair<Integer,Integer>>();
	
	final DegreeCurricularPlan degreeCurricularPlan = enrolmentContext.getStudentCurricularPlan().getDegreeCurricularPlan();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	
	final int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration().intValue();
	final int semester = enrolmentContext.getExecutionPeriod().getSemester().intValue();
	
	for (int year = 1 ; year <= numberOfYears; year++) {
	    final CurricularPeriod curricularPeriod = degreeCurricularPlan.getCurricularPeriodFor(year, semester);
	    
	    int numberOfCurricularCoursesToEnrol = 0;
	    int numberOfNotChosenCurricularCoursesToEnrol = 0;
	    
	    final List<Context> contexts = curricularPeriod.getContextsWithCurricularCourses(enrolmentContext.getExecutionPeriod());
	    for (final Context context : contexts) {
		final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

		if (!isApproved(enrolmentContext, curricularCourse)
			&& !isEnroled(enrolmentContext, curricularCourse) 
			&& !isEnroled(enrolmentContext, curricularCourse, executionPeriod.getPreviousExecutionPeriod())) {
		    
		    if (isEnrolling(enrolmentContext, curricularCourse)) {
			numberOfCurricularCoursesToEnrol++;
			
		    } else {
			numberOfNotChosenCurricularCoursesToEnrol++;
		    }
		}
	    }
	    addInformationToResult(result, year, numberOfCurricularCoursesToEnrol, numberOfNotChosenCurricularCoursesToEnrol);
	}
	
	return result;
    }
    
    private void addInformationToResult(final Map<Integer, Pair<Integer, Integer>> result,
	    final int year, final int numberOfCurricularCoursesToEnrol,
	    final int numberOfNotChosenCurricularCoursesToEnrol) {
	result.put(year, new Pair<Integer, Integer>(Integer.valueOf(numberOfCurricularCoursesToEnrol), Integer.valueOf(numberOfNotChosenCurricularCoursesToEnrol)));
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
