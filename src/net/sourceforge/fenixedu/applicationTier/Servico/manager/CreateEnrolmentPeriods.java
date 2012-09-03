package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesFlunkedSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ReingressionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodManagementBean;
import net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateEnrolmentPeriods {

    @Service
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    public static void run(EnrolmentPeriodManagementBean bean) throws FenixServiceException {

	final ExecutionSemester executionSemester = bean.getExecutionSemester();
	DegreeType degreeType = bean.getDegreeType();
	EnrolmentPeriodType enrolmentPeriodType = bean.getType();
	final Date startDate = bean.getBegin().toDate();
	final Date endDate = bean.getEnd().toDate();

	/*
	 * Allow pre-bolonha degrees to create reingression periods
	 */
	if (!degreeType.isBolonhaType() && enrolmentPeriodType.isReingressionPeriod()) {
	    createReingressionPeriodsForPreBolonhaDegrees(executionSemester, degreeType, startDate, endDate,
		    bean.getDegreeCurricularPlanList());
	} else if (degreeType.isEmpty()) {
	    createEnrolmentPeriodsForEmptyDegree(executionSemester, enrolmentPeriodType, startDate, endDate);
	} else {
	    createEnrolmentPeriodsForBolonhaDegrees(executionSemester, degreeType, enrolmentPeriodType, startDate, endDate,
		    bean.getDegreeCurricularPlanList());
	}
    }

    private static void createReingressionPeriodsForPreBolonhaDegrees(final ExecutionSemester executionSemester,
	    final DegreeType degreeType, final Date startDate, final Date endDate, final List<DegreeCurricularPlan> dcpList) {
	for (final DegreeCurricularPlan degreeCurricularPlan : dcpList) {
	    new ReingressionPeriod(degreeCurricularPlan, executionSemester, startDate, endDate);
	}
    }

    private static void createEnrolmentPeriodsForEmptyDegree(ExecutionSemester executionSemester,
	    EnrolmentPeriodType enrolmentPeriodType, Date startDate, Date endDate) throws FenixServiceException {
	createPeriod(enrolmentPeriodType, startDate, endDate, executionSemester,
		DegreeCurricularPlan.readEmptyDegreeCurricularPlan());
    }

    private static void createEnrolmentPeriodsForBolonhaDegrees(final ExecutionSemester executionSemester,
	    final DegreeType degreeType, final EnrolmentPeriodType enrolmentPeriodType, final Date startDate, final Date endDate,
	    final List<DegreeCurricularPlan> dcpList) throws FenixServiceException {

	for (final DegreeCurricularPlan degreeCurricularPlan : dcpList) {

	    if (degreeType == null || degreeType == degreeCurricularPlan.getDegree().getDegreeType()) {
		createPeriod(enrolmentPeriodType, startDate, endDate, executionSemester, degreeCurricularPlan);
	    }
	}
    }

    private static void createPeriod(EnrolmentPeriodType enrolmentPeriodType, final Date startDate, final Date endDate,
	    final ExecutionSemester executionSemester, final DegreeCurricularPlan degreeCurricularPlan)
	    throws FenixServiceException {

	if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CLASSES.equals(enrolmentPeriodType)) {

	    new EnrolmentPeriodInClasses(degreeCurricularPlan, executionSemester, startDate, endDate);

	} else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CURRICULAR_COURSES.equals(enrolmentPeriodType)) {

	    new EnrolmentPeriodInCurricularCourses(degreeCurricularPlan, executionSemester, startDate, endDate);

	} else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_SPECIAL_SEASON_EVALUATIONS.equals(enrolmentPeriodType)) {

	    new EnrolmentPeriodInSpecialSeasonEvaluations(degreeCurricularPlan, executionSemester, startDate, endDate);

	} else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CURRICULAR_COURSES_SPECIAL_SEASON.equals(enrolmentPeriodType)) {

	    new EnrolmentPeriodInCurricularCoursesSpecialSeason(degreeCurricularPlan, executionSemester, startDate, endDate);

	} else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_CURRICULAR_COURSES_FLUNKED_SEASON.equals(enrolmentPeriodType)) {

	    new EnrolmentPeriodInCurricularCoursesFlunkedSeason(degreeCurricularPlan, executionSemester, startDate, endDate);

	} else if (EnrolmentPeriodType.ENROLMENT_PERIOD_IN_IMPROVEMENT_OF_APPROVED_ENROLMENT.equals(enrolmentPeriodType)) {

	    new EnrolmentPeriodInImprovementOfApprovedEnrolment(degreeCurricularPlan, executionSemester, startDate, endDate);

	} else if (enrolmentPeriodType.isReingressionPeriod()) {

	    new ReingressionPeriod(degreeCurricularPlan, executionSemester, startDate, endDate);

	} else {
	    throw new FenixServiceException("error.invalid.enrolment.period.class.name");
	}
    }

}