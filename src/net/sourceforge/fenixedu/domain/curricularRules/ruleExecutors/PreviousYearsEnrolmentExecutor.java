package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.utl.fenix.utils.Pair;

import net.sourceforge.fenixedu.dataTransferObject.GenericTrio;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;

public class PreviousYearsEnrolmentExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation = groupEnrolmentInformationByYear(enrolmentContext);
	final Pair<Boolean, Integer> result = isValidEnrolment(enrolmentInformation, enrolmentContext);
	return result.getKey() ? RuleResult.createTrue() : RuleResult.createFalse("curricularRules.rulesExecutor.PreviousYearsEnrolmentExecutor", String.valueOf(result.getValue()));
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	
	final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation = groupEnrolmentInformationByYear(enrolmentContext);
	final Pair<Boolean, Integer> result = isValidEnrolment(enrolmentInformation, enrolmentContext);
	
	if (!result.getKey()) {
	    return RuleResult.createFalse("curricularRules.rulesExecutor.PreviousYearsEnrolmentExecutor", String.valueOf(result.getValue()));
	}
	return isTemporaryEnrolment(enrolmentInformation, enrolmentContext) ? RuleResult.createTrue(EnrolmentResultType.TEMPORARY) : RuleResult.createTrue();
    }
    
    private Pair<Boolean, Integer> isValidEnrolment(final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation, final EnrolmentContext enrolmentContext) {

	int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration();
	for (int year = numberOfYears ; year >= 1; year--) {
	    final GenericTrio<Integer, Integer, Integer> values = enrolmentInformation.get(year);
	    final int numberOfCurricularCoursesToEnrol = values.getFirst().intValue();
	    
	    if (numberOfCurricularCoursesToEnrol > 0) {
		if (previousYearsHaveNotChosenCurricularCoursesToEnrol(year - 1, enrolmentInformation)) {
		    return new Pair<Boolean, Integer>(Boolean.FALSE, year);
		}
	    }
	}
	return new Pair<Boolean, Integer>(Boolean.TRUE, null);
    }
    
    
    private Map<Integer, GenericTrio<Integer, Integer, Integer>> groupEnrolmentInformationByYear(final EnrolmentContext enrolmentContext) {
	final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation = new HashMap<Integer, GenericTrio<Integer,Integer, Integer>>();
	
	final DegreeCurricularPlan degreeCurricularPlan = enrolmentContext.getStudentCurricularPlan().getDegreeCurricularPlan();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();
	
	final int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration().intValue();
	final int semester = enrolmentContext.getExecutionPeriod().getSemester().intValue();
	
	for (int year = 1 ; year <= numberOfYears; year++) {
	    final CurricularPeriod curricularPeriod = degreeCurricularPlan.getCurricularPeriodFor(year, semester);
	    
	    int numberOfCurricularCoursesToEnrol = 0;
	    int numberOfEnroledCurricularCoursesInPreviousSemester = 0;
	    int numberOfNotChosenCurricularCoursesToEnrol = 0;
	    
	    final List<Context> contexts = curricularPeriod.getContextsWithCurricularCourses(executionPeriod);
	    for (final Context context : contexts) {
		final CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

		if (!isApproved(enrolmentContext, curricularCourse) && !isEnroled(enrolmentContext, curricularCourse)) {

		    if (isEnrolling(enrolmentContext, curricularCourse)) {
			numberOfCurricularCoursesToEnrol++;
			
		    } else if (executionPeriod.hasPreviousExecutionPeriod()
			    && isEnroled(enrolmentContext, curricularCourse, executionPeriod.getPreviousExecutionPeriod())) {
			numberOfEnroledCurricularCoursesInPreviousSemester++;
			
		    } else {
			numberOfNotChosenCurricularCoursesToEnrol++;
		    }
		}
	    }
	    addInformationToResult(enrolmentInformation, year, numberOfCurricularCoursesToEnrol, numberOfNotChosenCurricularCoursesToEnrol, numberOfEnroledCurricularCoursesInPreviousSemester);
	}
	return enrolmentInformation;
    }
    
    private boolean isTemporaryEnrolment(final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation, final EnrolmentContext enrolmentContext) {
	
	int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration().intValue();
	for (int year = numberOfYears ; year >= 1; year--) {
	    final GenericTrio<Integer, Integer, Integer> values = enrolmentInformation.get(year);
	    final int numberOfEnroledCurricularCoursesInPreviousSemester = values.getThird().intValue();
	    
	    if (numberOfEnroledCurricularCoursesInPreviousSemester > 0) {
		return true;
	    }
	}
	return false;
    }

    private boolean previousYearsHaveNotChosenCurricularCoursesToEnrol(int year, final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation) {
	while (year > 0) {
	    final GenericTrio<Integer, Integer, Integer> values = enrolmentInformation.get(year);
	    final int numberOfNotChosenCurricularCoursesToEnrol = values.getSecond().intValue();
	    if (numberOfNotChosenCurricularCoursesToEnrol > 0) {
		return true;
	    }
	    year--;
	}
	return false;
    }
    
    private void addInformationToResult(final Map<Integer, GenericTrio<Integer, Integer, Integer>> result,
	    final int year, final int numberOfCurricularCoursesToEnrol,
	    final int numberOfNotChosenCurricularCoursesToEnrol, final int numberOfEnroledCurricularCoursesInPreviousSemester) {
	
	result.put(year, new GenericTrio<Integer, Integer, Integer>(
		Integer.valueOf(numberOfCurricularCoursesToEnrol),
		Integer.valueOf(numberOfNotChosenCurricularCoursesToEnrol),
		Integer.valueOf(numberOfEnroledCurricularCoursesInPreviousSemester)));
    }
    
    @Override
    protected RuleResult executeEnrolmentWithNoRules(final ICurricularRule curricularRule, final EnrolmentContext enrolmentContext) {
	return RuleResult.createTrue();
    }

}
