/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.student;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentPeriod;
import org.fenixedu.academic.domain.EnrolmentPeriodInClasses;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCourses;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import org.fenixedu.academic.domain.EnrolmentPeriodInSpecialSeasonEvaluations;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.StudentPortalBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.NotificationPlug;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsApplication;
import org.joda.time.YearMonthDay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

@StrutsApplication(bundle = "TitlesResources", path = "student", titleKey = "private.student",
        accessGroup = StudentApplication.ACCESS_GROUP, hint = StudentApplication.HINT)
@Mapping(module = "student", path = "/showStudentPortal")
@Forwards(value = { @Forward(name = "studentPortal", path = "/student/main_bd.jsp") })
public class ShowStudentPortalDA extends Action {

    private static int NUMBER_OF_DAYS_BEFORE_PERIOD_TO_WARN = 100;
    private static int NUMBER_OF_DAYS_AFTER_PERIOD_TO_WARN = 5;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final HttpSession httpSession = request.getSession(false);
        final NotificationPlug notificationPlug = NotificationPlug.PLUGS.stream()
                .filter(plug -> plug.showNotification(Authenticate.getUser(), httpSession))
                .findAny().orElse(null);
        if (notificationPlug != null) {
            return new ActionForward(notificationPlug.redirectUrl(httpSession), true);
        }

        List<StudentPortalBean> studentPortalBeans = new ArrayList<StudentPortalBean>();
        List<String> genericDegreeWarnings = new ArrayList<String>();

        final Student student = AccessControl.getPerson().getStudent();
        if (student != null) {
            for (Registration registration : student.getAllRegistrations()) {
                DegreeCurricularPlan degreeCurricularPlan = registration.getLastDegreeCurricularPlan();
                if (registration.getAttendingExecutionCoursesForCurrentExecutionPeriod().isEmpty() == false) {
                    studentPortalBeans.add(new StudentPortalBean(registration.getDegree(), student, registration
                            .getAttendingExecutionCoursesForCurrentExecutionPeriod(), degreeCurricularPlan));
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
                    warnings.add(BundleUtil.getString(Bundle.STUDENT, "message.out.degree.enrolment.period", degreeCurricularPlan
                            .getDegree().getSigla(), YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).toString(),
                            YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).toString()));
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
                    warnings.add(BundleUtil.getString(Bundle.STUDENT, "message.out.degree.enrolment.period.after.special.season",
                            degreeCurricularPlan.getDegree().getSigla(),
                            YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).toString(),
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
                    String dateKey =
                            YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).toString()
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
                warnings.add(BundleUtil.getString(Bundle.STUDENT,
                        "message.out.degree.enrolment.period.in.special.season.evaluations", degreeCurricularPlan.getDegree()
                                .getSigla(), enrolmentPeriod.getExecutionPeriod().getSemester().toString(), enrolmentPeriod
                                .getExecutionPeriod().getYear(), YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate())
                                .toString(), YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).toString()));
            } else {
                EnrolmentPeriod enrolmentPeriod = periods.first();
                warnings.add(BundleUtil.getString(Bundle.STUDENT,
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
                    warnings.add(BundleUtil.getString(Bundle.STUDENT, "message.out.classes.enrolment.period",
                            degreeCurricularPlan.getDegree().getSigla(),
                            YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).toString(),
                            YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).toString()));
                }
            }
        }
        return warnings;
    }

    private boolean isBetweenWarnPeriod(EnrolmentPeriod enrolmentPeriod) {
        if (enrolmentPeriod == null) {
            return false;
        }
        YearMonthDay startWarnPeriod =
                YearMonthDay.fromDateFields(enrolmentPeriod.getStartDate()).minusDays(NUMBER_OF_DAYS_BEFORE_PERIOD_TO_WARN);
        YearMonthDay endWarnPeriod =
                YearMonthDay.fromDateFields(enrolmentPeriod.getEndDate()).plusDays(NUMBER_OF_DAYS_AFTER_PERIOD_TO_WARN);
        Date now = new Date();
        if (startWarnPeriod.toDateTimeAtMidnight().toDate().before(now)
                && endWarnPeriod.toDateTimeAtMidnight().toDate().after(now)) {
            return true;
        } else {
            return false;
        }
    }
}