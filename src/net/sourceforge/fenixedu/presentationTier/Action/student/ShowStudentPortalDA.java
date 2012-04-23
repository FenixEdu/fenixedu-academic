package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentPortalBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "student", path = "/showStudentPortal", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "studentPortal", path = "/student/main_bd.jsp") })
public class ShowStudentPortalDA extends FenixDispatchAction {

    private static int NUMBER_OF_DAYS_BEFORE_PERIOD_TO_WARN = 100;
    private static int NUMBER_OF_DAYS_AFTER_PERIOD_TO_WARN = 5;

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	List<StudentPortalBean> studentPortalBeans = new ArrayList<StudentPortalBean>();
	List<String> genericDegreeWarnings = new ArrayList<String>();
	List<ExecutionCourse> executionCourses;

	final Student student = getLoggedPerson(request).getStudent();
	if (student != null) {
	    for (Registration registration : student.getActiveRegistrationsIn(ExecutionSemester.readActualExecutionSemester())) {
		executionCourses = new ArrayList<ExecutionCourse>();
		DegreeCurricularPlan degreeCurricularPlan = registration.getActiveDegreeCurricularPlan();
		for (ExecutionCourse executionCourse : registration.getAttendingExecutionCoursesForCurrentExecutionPeriod()) {
		    executionCourses.add(executionCourse);
		}
		if (executionCourses.isEmpty() == false) {
		    studentPortalBeans.add(new StudentPortalBean(registration.getDegree(), student, executionCourses,
			    degreeCurricularPlan));
		}
		if (hasSpecialSeasonEnrolments(student)) {
		    genericDegreeWarnings.addAll(getEnrolmentPeriodCoursesAfterSpecialSeason(degreeCurricularPlan));
		} else {
		    genericDegreeWarnings.addAll(getEnrolmentPeriodCourses(degreeCurricularPlan));
		}
		genericDegreeWarnings.addAll(getEnrolmentPeriodInSpecialSeasonEvaluations(degreeCurricularPlan));
		genericDegreeWarnings.addAll(getEnrolmentPeriodClasses(degreeCurricularPlan));
	    }
	}

	request.setAttribute("genericDegreeWarnings", genericDegreeWarnings);
	request.setAttribute("studentPortalBeans", studentPortalBeans);
	request.setAttribute("executionSemester", ExecutionSemester.readActualExecutionSemester().getQualifiedName());
	return mapping.findForward("studentPortal");
    }

    private List<String> getEnrolmentPeriodCourses(DegreeCurricularPlan degreeCurricularPlan) {
	List<String> warnings = new ArrayList<String>();
	for (final EnrolmentPeriod enrolmentPeriod : degreeCurricularPlan.getEnrolmentPeriodsSet()) {
	    if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses) {
		if (isBetweenWarnPeriod(enrolmentPeriod)) {
		    warnings.add(BundleUtil.getMessageFromModuleOrApplication("Student", "message.out.degree.enrolment.period",
			    degreeCurricularPlan.getDegree().getSigla(), YearMonthDay.fromDateFields(
				    enrolmentPeriod.getStartDate()).toString(), YearMonthDay.fromDateFields(
				    enrolmentPeriod.getEndDate()).toString()));
		}
	    }
	}
	return warnings;
    }

    private List<String> getEnrolmentPeriodCoursesAfterSpecialSeason(DegreeCurricularPlan degreeCurricularPlan) {
	List<String> warnings = new ArrayList<String>();
	for (final EnrolmentPeriod enrolmentPeriod : degreeCurricularPlan.getEnrolmentPeriodsSet()) {
	    if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCoursesSpecialSeason) {
		if (isBetweenWarnPeriod(enrolmentPeriod)) {
		    warnings.add(BundleUtil.getMessageFromModuleOrApplication("Student",
			    "message.out.degree.enrolment.period.after.special.season", degreeCurricularPlan.getDegree()
				    .getSigla(), YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).toString(),
			    YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).toString()));
		}
	    }
	}
	return warnings;
    }

    private boolean hasSpecialSeasonEnrolments(Student student) {
	ExecutionSemester actualSemester = ExecutionSemester.readActualExecutionSemester();
	ExecutionSemester previousSemester = actualSemester.getPreviousExecutionPeriod();
	ExecutionSemester previousPreviousSemester = previousSemester.getPreviousExecutionPeriod();
	if (actualSemester.isFirstOfYear()) {
	    return (student.hasSpecialSeasonEnrolments(previousSemester) || student
		    .hasSpecialSeasonEnrolments(previousPreviousSemester));
	}
	return (student.hasSpecialSeasonEnrolments(actualSemester) || student.hasSpecialSeasonEnrolments(previousSemester));
    }

    private List<String> getEnrolmentPeriodInSpecialSeasonEvaluations(DegreeCurricularPlan degreeCurricularPlan) {
	HashMap<String, TreeSet<EnrolmentPeriod>> enrolmentPeriodsByDate = new HashMap<String, TreeSet<EnrolmentPeriod>>();
	List<String> warnings = new ArrayList<String>();
	for (final EnrolmentPeriod enrolmentPeriod : degreeCurricularPlan.getEnrolmentPeriodsSet()) {
	    if (enrolmentPeriod instanceof EnrolmentPeriodInSpecialSeasonEvaluations) {
		if (isBetweenWarnPeriod(enrolmentPeriod)) {
		    String dateKey = YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).toString()
			    + YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).toString();

		    if (enrolmentPeriodsByDate.get(dateKey) == null) {
			enrolmentPeriodsByDate.put(dateKey, new TreeSet<EnrolmentPeriod>(
				EnrolmentPeriod.COMPARATOR_BY_EXECUTION_SEMESTER));
		    }

		    enrolmentPeriodsByDate.get(dateKey).add(enrolmentPeriod);
		}
	    }
	}

	for (TreeSet<EnrolmentPeriod> periods : enrolmentPeriodsByDate.values()) {
	    if (periods.size() == 1) {
		EnrolmentPeriod enrolmentPeriod = periods.first();
		warnings.add(BundleUtil.getMessageFromModuleOrApplication("Student",
			"message.out.degree.enrolment.period.in.special.season.evaluations", degreeCurricularPlan.getDegree()
				.getSigla(), enrolmentPeriod.getExecutionPeriod().getSemester().toString(), enrolmentPeriod
				.getExecutionPeriod().getYear(), YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate())
				.toString(), YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).toString()));
	    } else {
		EnrolmentPeriod enrolmentPeriod = periods.first();
		warnings.add(BundleUtil.getMessageFromModuleOrApplication("Student",
			"message.out.degree.enrolment.period.in.special.season.evaluations.simple", degreeCurricularPlan
				.getDegree().getSigla(), YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).toString(),
			YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).toString()));
	    }
	}
	return warnings;
    }

    private List<String> getEnrolmentPeriodClasses(DegreeCurricularPlan degreeCurricularPlan) {
	List<String> warnings = new ArrayList<String>();
	for (final EnrolmentPeriod enrolmentPeriod : degreeCurricularPlan.getEnrolmentPeriodsSet()) {
	    if (enrolmentPeriod instanceof EnrolmentPeriodInClasses) {
		if (isBetweenWarnPeriod(enrolmentPeriod)) {
		    warnings.add(BundleUtil.getMessageFromModuleOrApplication("Student", "message.out.classes.enrolment.period",
			    degreeCurricularPlan.getDegree().getSigla(), YearMonthDay.fromDateFields(
				    enrolmentPeriod.getStartDate()).toString(), YearMonthDay.fromDateFields(
				    enrolmentPeriod.getEndDate()).toString()));
		}
	    }
	}
	return warnings;
    }

    private boolean isBetweenWarnPeriod(EnrolmentPeriod enrolmentPeriod) {
	if (enrolmentPeriod == null) {
	    return false;
	}
	YearMonthDay startWarnPeriod = YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).minusDays(
		NUMBER_OF_DAYS_BEFORE_PERIOD_TO_WARN);
	YearMonthDay endWarnPeriod = YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).plusDays(
		NUMBER_OF_DAYS_AFTER_PERIOD_TO_WARN);
	Date now = new Date();
	if (startWarnPeriod.toDateTimeAtMidnight().toDate().before(now)
		&& endWarnPeriod.toDateTimeAtMidnight().toDate().after(now)) {
	    return true;
	} else {
	    return false;
	}
    }
}
