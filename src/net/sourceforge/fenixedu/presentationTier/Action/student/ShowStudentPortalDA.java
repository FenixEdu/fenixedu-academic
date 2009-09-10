package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.StudentPortalBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ShowStudentPortalDA extends FenixDispatchAction {

    private static int NUMBER_OF_DAYS_BEFORE_PERIOD_TO_WARN = 100;
    private static int NUMBER_OF_DAYS_AFTER_PERIOD_TO_WARN = 5;

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	List<StudentPortalBean> studentPortalBeans = new ArrayList<StudentPortalBean>();
	List<String> genericDegreeWarnings = new ArrayList<String>();
	List<ExecutionCourse> executionCourses;

	Student student = getLoggedPerson(request).getStudent();
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
	    genericDegreeWarnings.addAll(getEnrolmentPeriodCourses(degreeCurricularPlan));
	    genericDegreeWarnings.addAll(getEnrolmentPeriodClasses(degreeCurricularPlan));
	}

	request.setAttribute("genericDegreeWarnings", genericDegreeWarnings);
	request.setAttribute("studentPortalBeans", studentPortalBeans);
	request.setAttribute("executionSemester", ExecutionSemester.readActualExecutionSemester().getQualifiedName());
	return mapping.findForward("studentPortal");
    }

    private List<String> getEnrolmentPeriodCourses(DegreeCurricularPlan degreeCurricularPlan) {
	List<String> warnings = new ArrayList<String>();
	EnrolmentPeriodInCurricularCourses periodCourses;
	for (final EnrolmentPeriod enrolmentPeriod : degreeCurricularPlan.getEnrolmentPeriodsSet()) {
	    if (enrolmentPeriod instanceof EnrolmentPeriodInCurricularCourses) {
		periodCourses = (EnrolmentPeriodInCurricularCourses) enrolmentPeriod;
		if (isBetweenWarnPeriod(periodCourses)) {
		    ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		    warnings.add(degreeCurricularPlan.getDegree().getSigla() + ": "
			    + resource.getString("message.out.degree.enrolment.period") + " "
			    + YearMonthDay.fromDateFields(periodCourses.getStartDate()) + " "
			    + resource.getString("message.out.until") + " "
			    + YearMonthDay.fromDateFields(periodCourses.getEndDate()));
		}
	    }
	}
	return warnings;
    }

    private List<String> getEnrolmentPeriodClasses(DegreeCurricularPlan degreeCurricularPlan) {
	List<String> warnings = new ArrayList<String>();
	EnrolmentPeriodInClasses periodClasses;
	for (final EnrolmentPeriod enrolmentPeriod : degreeCurricularPlan.getEnrolmentPeriodsSet()) {
	    if (enrolmentPeriod instanceof EnrolmentPeriodInClasses) {
		periodClasses = (EnrolmentPeriodInClasses) enrolmentPeriod;
		if (isBetweenWarnPeriod(periodClasses)) {
		    ResourceBundle resource = ResourceBundle.getBundle("resources.StudentResources", Language.getLocale());
		    warnings.add(degreeCurricularPlan.getDegree().getSigla() + ": "
			    + resource.getString("message.out.classes.enrolment.period") + " "
			    + YearMonthDay.fromDateFields(periodClasses.getStartDate()) + " "
			    + resource.getString("message.out.until") + " "
			    + YearMonthDay.fromDateFields(periodClasses.getEndDate()));
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
