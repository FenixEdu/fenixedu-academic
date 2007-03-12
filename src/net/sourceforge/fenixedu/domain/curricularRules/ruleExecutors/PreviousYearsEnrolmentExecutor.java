package net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.GenericTrio;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;

public class PreviousYearsEnrolmentExecutor extends CurricularRuleExecutor {

    /**
         * Temporary version
         */

    @Override
    protected RuleResult executeEnrolmentWithRules(final ICurricularRule curricularRule,
	    IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation = groupEnrolmentInformationByYear(
		enrolmentContext, false);
	final GenericPair<Boolean, Integer> result = isValidEnrolment(enrolmentInformation,
		enrolmentContext);
	return result.getLeft() ? RuleResult.createTrue() : RuleResult.createFalse(
		"curricularRules.ruleExecutors.PreviousYearsEnrolmentExecutor", String.valueOf(result
			.getRight()));
    }

    @Override
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(
	    final ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation = groupEnrolmentInformationByYear(
		enrolmentContext, true);
	final GenericPair<Boolean, Integer> result = isValidEnrolment(enrolmentInformation,
		enrolmentContext);
	if (!result.getLeft()) {
	    return RuleResult.createFalse(
		    "curricularRules.ruleExecutors.PreviousYearsEnrolmentExecutor", String
			    .valueOf(result.getRight()));
	}
	return isTemporaryEnrolment(enrolmentInformation, enrolmentContext) ? RuleResult
		.createTrue(EnrolmentResultType.TEMPORARY) : RuleResult.createTrue();
    }

    private Map<Integer, GenericTrio<Integer, Integer, Integer>> groupEnrolmentInformationByYear(
	    final EnrolmentContext enrolmentContext, boolean countTemporary) {

	final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation = new HashMap<Integer, GenericTrio<Integer, Integer, Integer>>();

	final DegreeCurricularPlan degreeCurricularPlan = enrolmentContext.getStudentCurricularPlan()
		.getDegreeCurricularPlan();
	final ExecutionPeriod executionPeriod = enrolmentContext.getExecutionPeriod();

	final int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration()
		.intValue();
	final int semester = enrolmentContext.getExecutionPeriod().getSemester().intValue();

	for (int year = 1; year <= numberOfYears; year++) {

	    final CurricularPeriod curricularPeriod = degreeCurricularPlan.getCurricularPeriodFor(year,
		    semester);
	    final List<Context> contexts = curricularPeriod
		    .getContextsWithCurricularCourses(executionPeriod);

	    int totalCurricularCourses = contexts.size();
	    int numberOfMissingCurricularCoursesToEnrol = totalCurricularCourses;
	    int numberOfEnroledCurricularCoursesInPreviousSemester = 0;

	    for (final Context context : contexts) {

		final CurricularCourse curricularCourse = (CurricularCourse) context
			.getChildDegreeModule();

		if (isApproved(enrolmentContext, curricularCourse)
			|| isEnroled(enrolmentContext, curricularCourse)
			|| isEnrolling(enrolmentContext, curricularCourse)) {

		    numberOfMissingCurricularCoursesToEnrol--;

		} else if (countTemporary
			&& executionPeriod.hasPreviousExecutionPeriod()
			&& hasEnrolmentWithEnroledState(enrolmentContext, curricularCourse,
				executionPeriod.getPreviousExecutionPeriod())) {
		    numberOfMissingCurricularCoursesToEnrol--;
		    numberOfEnroledCurricularCoursesInPreviousSemester++;
		}
	    }
	    addInformationToResult(enrolmentInformation, year, numberOfMissingCurricularCoursesToEnrol,
		    totalCurricularCourses, numberOfEnroledCurricularCoursesInPreviousSemester);
	}
	return enrolmentInformation;
    }

    private void addInformationToResult(
	    final Map<Integer, GenericTrio<Integer, Integer, Integer>> result, final int year,
	    final int numberOfMissingCurricularCoursesToEnrol, final int totalCurricularCourses,
	    final int numberOfEnroledCurricularCoursesInPreviousSemester) {

	result.put(year, new GenericTrio<Integer, Integer, Integer>(Integer
		.valueOf(numberOfMissingCurricularCoursesToEnrol), Integer
		.valueOf(totalCurricularCourses), Integer
		.valueOf(numberOfEnroledCurricularCoursesInPreviousSemester)));
    }

    private boolean isTemporaryEnrolment(
	    final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation,
	    final EnrolmentContext enrolmentContext) {
	int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration().intValue();
	for (int year = numberOfYears; year > 1; year--) {
	    final GenericTrio<Integer, Integer, Integer> values = enrolmentInformation.get(year);

	    final int numberOfMissingCurricularCoursesToEnrol = values.getFirst().intValue();
	    final int totalCurricularCourses = values.getSecond().intValue();

	    if (numberOfMissingCurricularCoursesToEnrol != totalCurricularCourses) {
		if (previousYearsHaveTemporaryEnrolment(year - 1, enrolmentInformation)) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean previousYearsHaveTemporaryEnrolment(int year,
	    final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation) {
	while (year > 0) {
	    final GenericTrio<Integer, Integer, Integer> values = enrolmentInformation.get(year);
	    int numberOfEnroledCurricularCoursesInPreviousSemester = values.getThird().intValue();
	    if (numberOfEnroledCurricularCoursesInPreviousSemester > 0) {
		return true;
	    }
	    year--;
	}
	return false;
    }

    private boolean previousYearsHaveMissingCurricularCoursesToEnrol(int year,
	    final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation) {
	while (year > 0) {
	    final GenericTrio<Integer, Integer, Integer> values = enrolmentInformation.get(year);

	    final int numberOfMissingCurricularCoursesToEnrol = values.getFirst().intValue();
	    final int totalCurricularCourses = values.getSecond().intValue();

	    if (numberOfMissingCurricularCoursesToEnrol > 0
		    && numberOfMissingCurricularCoursesToEnrol != totalCurricularCourses) {
		return true;
	    }
	    year--;
	}
	return false;
    }

    private GenericPair<Boolean, Integer> isValidEnrolment(
	    final Map<Integer, GenericTrio<Integer, Integer, Integer>> enrolmentInformation,
	    final EnrolmentContext enrolmentContext) {
	int numberOfYears = enrolmentContext.getStudentCurricularPlan().getDegreeDuration();

	for (int year = numberOfYears; year > 1; year--) {
	    final GenericTrio<Integer, Integer, Integer> values = enrolmentInformation.get(year);

	    final int numberOfMissingCurricularCoursesToEnrol = values.getFirst().intValue();
	    final int totalCurricularCourses = values.getSecond().intValue();

	    if (numberOfMissingCurricularCoursesToEnrol != totalCurricularCourses) {
		if (previousYearsHaveMissingCurricularCoursesToEnrol(year - 1, enrolmentInformation)) {
		    return new GenericPair<Boolean, Integer>(Boolean.FALSE, year);
		}
	    }
	}
	return new GenericPair<Boolean, Integer>(Boolean.TRUE, null);
    }

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule, final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return RuleResult.createNA();
    }

}
