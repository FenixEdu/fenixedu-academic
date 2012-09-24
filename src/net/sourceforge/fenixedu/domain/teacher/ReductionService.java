package net.sourceforge.fenixedu.domain.teacher;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.evaluation.ApprovedTeacherEvaluationProcessMark;
import net.sourceforge.fenixedu.domain.teacher.evaluation.FacultyEvaluationProcessYear;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationMark;
import net.sourceforge.fenixedu.domain.teacher.evaluation.TeacherEvaluationProcess;

import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;

public class ReductionService extends ReductionService_Base {

    private static final BigDecimal MAX_CREDITS_REDUCTION = new BigDecimal(3);

    public ReductionService(final TeacherService teacherService, final BigDecimal creditsReduction) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	if (teacherService == null) {
	    throw new DomainException("arguments can't be null");
	}
	setTeacherService(teacherService);
	setCreditsReduction(creditsReduction);
    }

    @Override
    public void setCreditsReduction(BigDecimal creditsReduction) {
	if (creditsReduction == null || creditsReduction.compareTo(BigDecimal.ZERO) < 0) {
	    creditsReduction = BigDecimal.ZERO;
	}
	if (!getTeacherService().getTeacher().isTeacherProfessorCategory(getTeacherService().getExecutionPeriod())) {
	    throw new DomainException("label.creditsReduction.invalidCategory");
	}
	if (creditsReduction.compareTo(MAX_CREDITS_REDUCTION) > 0) {
	    throw new DomainException("label.creditsReduction.exceededMaxAllowed");
	}

	BigDecimal maxCreditsFromEvaluation = getTeacherEvaluationMark();
	BigDecimal maxCreditsFromAge = getTeacherMaxCreditsFromAge();

	BigDecimal maxCreditsFromEvaluationAndAge = maxCreditsFromEvaluation.add(maxCreditsFromAge);
	if (creditsReduction.compareTo(maxCreditsFromEvaluationAndAge) > 0) {
	    throw new DomainException("label.creditsReduction.exceededMaxAllowed.evaluationAndAge",
		    maxCreditsFromEvaluationAndAge.toString());
	}

	super.setCreditsReduction(creditsReduction);
    }

    private BigDecimal getTeacherMaxCreditsFromAge() {
	YearMonthDay dateOfBirthYearMonthDay = getTeacherService().getTeacher().getPerson().getDateOfBirthYearMonthDay();
	if (dateOfBirthYearMonthDay != null) {
	    Interval interval = new Interval(dateOfBirthYearMonthDay.toLocalDate().toDateTimeAtStartOfDay(), getTeacherService()
		    .getExecutionPeriod().getEndDateYearMonthDay().plusDays(1).toLocalDate().toDateTimeAtStartOfDay());
	    int age = interval.toPeriod(PeriodType.years()).getYears();
	    if (age >= 65) {
		return BigDecimal.ONE;
	    }
	}
	return BigDecimal.ZERO;
    }

    private BigDecimal getTeacherEvaluationMark() {
	FacultyEvaluationProcessYear lastFacultyEvaluationProcessYear = null;
	for (final FacultyEvaluationProcessYear facultyEvaluationProcessYear : RootDomainObject.getInstance()
		.getFacultyEvaluationProcessYearSet()) {
	    if (lastFacultyEvaluationProcessYear == null
		    || facultyEvaluationProcessYear.getYear().compareTo(lastFacultyEvaluationProcessYear.getYear()) > 0) {
		lastFacultyEvaluationProcessYear = facultyEvaluationProcessYear;
	    }
	}
	TeacherEvaluationProcess lastTeacherEvaluationProcess = null;
	for (TeacherEvaluationProcess teacherEvaluationProcess : getTeacherService().getTeacher().getPerson()
		.getTeacherEvaluationProcessFromEvaluee()) {
	    if (teacherEvaluationProcess.getFacultyEvaluationProcess().equals(
		    lastFacultyEvaluationProcessYear.getFacultyEvaluationProcess())) {
		lastTeacherEvaluationProcess = teacherEvaluationProcess;
		break;
	    }
	}
	TeacherEvaluationMark approvedEvaluationMark = null;
	if (lastTeacherEvaluationProcess != null) {
	    for (ApprovedTeacherEvaluationProcessMark approvedTeacherEvaluationProcessMark : lastTeacherEvaluationProcess
		    .getApprovedTeacherEvaluationProcessMark()) {
		if (approvedTeacherEvaluationProcessMark.getFacultyEvaluationProcessYear().equals(
			lastFacultyEvaluationProcessYear)) {
		    approvedEvaluationMark = approvedTeacherEvaluationProcessMark.getApprovedEvaluationMark();
		    if (approvedEvaluationMark != null) {
			switch (approvedEvaluationMark) {
			case EXCELLENT:
			    return MAX_CREDITS_REDUCTION;
			case VERY_GOOD:
			    return new BigDecimal(2);
			case GOOD:
			    return BigDecimal.ONE;
			default:
			    return BigDecimal.ZERO;
			}
		    } else {
			return BigDecimal.ZERO;
		    }
		}
	    }
	}
	return BigDecimal.ZERO;
    }
}
