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
package org.fenixedu.academic.ui.struts.action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.service.filter.enrollment.ClassEnrollmentAuthorizationFilter;
import org.fenixedu.academic.service.services.enrollment.shift.ReadClassTimeTableByStudent;
import org.fenixedu.academic.service.services.enrollment.shift.WriteStudentAttendingCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.student.ReadStudentTimeTable;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixTransactionException;
import org.fenixedu.academic.ui.struts.config.FenixErrorExceptionHandler;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "student", path = "/studentShiftEnrollmentManagerLookup",
        input = "/studentShiftEnrollmentManager.do?method=prepare", formBean = "studentShiftEnrollmentForm", validate = false,
        functionality = ShiftStudentEnrollmentManagerDispatchAction.class)
@Forwards(value = {
        @Forward(name = "prepareShiftEnrollment",
                path = "/student/studentShiftEnrollmentManager.do?method=prepareShiftEnrollment"),
        @Forward(name = "prepareEnrollmentViewWarning",
                path = "/student/studentShiftEnrollmentManager.do?method=prepareStartViewWarning"),
        @Forward(name = "showShiftsToEnroll", path = "/student/enrollment/showShiftsToEnroll.jsp"),
        @Forward(name = "studentFirstPage", path = "/student/index.do"),
        @Forward(name = "beginTransaction", path = "/student/studentShiftEnrollmentManager.do?method=start&firstTime=true") })
@Exceptions(value = {
        @ExceptionHandling(type = FenixTransactionException.class, key = "error.transaction.enrolment",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = NotAuthorizedException.class,
                key = "error.message.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = ClassEnrollmentAuthorizationFilter.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan.class,
                key = "error.message.CurrentClassesEnrolmentPeriodUndefinedForDegreeCurricularPlan",
                handler = FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(
                type = ClassEnrollmentAuthorizationFilter.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan.class,
                key = "error.message.OutsideOfCurrentClassesEnrolmentPeriodForDegreeCurricularPlan",
                handler = FenixErrorExceptionHandler.class, scope = "request") })
public class ShiftStudentEnrollmentManagerLookupDispatchAction extends FenixDispatchAction {

    private Registration getAndSetRegistration(final HttpServletRequest request) {
        final Registration registration = FenixFramework.getDomainObject(request.getParameter("registrationOID"));
        if (!getUserView(request).getPerson().getStudent().getRegistrationsToEnrolInShiftByStudent().contains(registration)) {
            return null;
        }

        request.setAttribute("registration", registration);
        request.setAttribute("registrationOID", registration.getExternalId().toString());
        return registration;
    }

    public ActionForward addCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixTransactionException {

        this.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        checkParameter(request);

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionCourseId = (String) form.get("wantedCourse");
        ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterID");
        request.setAttribute("executionSemesterID", executionSemester.getExternalId());

        try {
            WriteStudentAttendingCourse.runWriteStudentAttendingCourse(registration, executionCourseId, executionSemester);

        } catch (NotAuthorizedException exception) {
            addActionMessage(request, "error.attend.curricularCourse.impossibleToEnroll");
            return mapping.getInputForward();

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("prepareShiftEnrollment");

        } catch (org.fenixedu.bennu.core.domain.exceptions.DomainException de) {
            addActionMessage(request, de.getLocalizedMessage(), false);
            return mapping.findForward("prepareShiftEnrollment");

        } catch (FenixServiceException ex) {
            addActionMessage(request, ex.getMessage(), ex.getArgs());
            return mapping.findForward("prepareShiftEnrollment");
        }

        return mapping.findForward("prepareShiftEnrollment");
    }

    public ActionForward removeCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixTransactionException {

        this.validateToken(request, actionForm, mapping, "error.transaction.enrollment");

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        checkParameter(request);
        request.setAttribute("executionSemesterID", request.getParameter("executionSemesterID"));

        final DynaActionForm form = (DynaActionForm) actionForm;
        final String executionCourseId = (String) form.get("removedCourse");
        if (StringUtils.isEmpty(executionCourseId)) {
            return mapping.findForward("prepareShiftEnrollment");
        }

        try {
            registration.removeAttendFor(FenixFramework.<ExecutionCourse> getDomainObject(executionCourseId));
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return mapping.getInputForward();
        }

        return mapping.findForward("prepareShiftEnrollment");
    }

    public ActionForward proceedToShiftEnrolment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        checkParameter(request);
        final String classIdSelected = readClassSelected(request);

        final Registration registration = getAndSetRegistration(request);
        if (registration == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        }

        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final List<SchoolClass> schoolClassesToEnrol =
                readStudentSchoolClassesToEnrolUsingExecutionCourse(request, registration, executionCourse);
        request.setAttribute("schoolClassesToEnrol", schoolClassesToEnrol);

        if (schoolClassesToEnrol.isEmpty()) {
            return mapping.findForward("prepareShiftEnrollment");
        }

        final SchoolClass schoolClass = setSelectedSchoolClass(request, classIdSelected, schoolClassesToEnrol);

        final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterID");
        final List<InfoShowOccupation> infoClasslessons =
                ReadClassTimeTableByStudent.runReadClassTimeTableByStudent(registration, schoolClass, executionCourse,
                        executionSemester);

        request.setAttribute("infoClasslessons", infoClasslessons);
        request.setAttribute("infoClasslessonsEndTime", Integer.valueOf(getEndTime(infoClasslessons)));

        final List<InfoShowOccupation> infoLessons = ReadStudentTimeTable.run(registration, executionSemester);
        request.setAttribute("person", registration.getPerson());
        request.setAttribute("infoLessons", infoLessons);
        request.setAttribute("infoLessonsEndTime", Integer.valueOf(getEndTime(infoLessons)));
        request.setAttribute("executionSemesterID", executionSemester.getExternalId());

        return mapping.findForward("showShiftsToEnroll");
    }

    private SchoolClass setSelectedSchoolClass(HttpServletRequest request, final String classIdSelected,
            final List<SchoolClass> schoolClassesToEnrol) {

        final SchoolClass schoolClass =
                (classIdSelected != null) ? searchSchoolClassFrom(schoolClassesToEnrol, classIdSelected) : schoolClassesToEnrol
                        .iterator().next();
        request.setAttribute("selectedSchoolClass", schoolClass);

        return schoolClass;
    }

    private SchoolClass searchSchoolClassFrom(final List<SchoolClass> schoolClassesToEnrol, final String classId) {
        return (SchoolClass) CollectionUtils.find(schoolClassesToEnrol, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return ((SchoolClass) object).getExternalId().equals(classId);
            }
        });
    }

    private List<SchoolClass> readStudentSchoolClassesToEnrolUsingExecutionCourse(HttpServletRequest request,
            final Registration registration, final ExecutionCourse executionCourse) {

        final List<SchoolClass> schoolClassesToEnrol = new ArrayList<SchoolClass>();
        if (executionCourse != null) {
            request.setAttribute("executionCourse", executionCourse);
            schoolClassesToEnrol.addAll(registration.getSchoolClassesToEnrolBy(executionCourse));

        } else {
            schoolClassesToEnrol.addAll(registration.getSchoolClassesToEnrol());
        }

        Collections.sort(schoolClassesToEnrol, SchoolClass.COMPARATOR_BY_NAME);
        return schoolClassesToEnrol;
    }

    private ExecutionCourse getExecutionCourse(HttpServletRequest request) {
        if (!StringUtils.isEmpty(request.getParameter("executionCourseID"))) {
            return FenixFramework.getDomainObject(request.getParameter("executionCourseID"));
        } else {
            return null;
        }
    }

    private int getEndTime(List<InfoShowOccupation> infoOccupations) {
        int endTime = 0;
        for (final InfoShowOccupation occupation : infoOccupations) {
            int tempEnd = occupation.getLastHourOfDay();
            if (endTime < tempEnd) {
                endTime = tempEnd;
            }
        }
        return endTime;
    }

    private void checkParameter(HttpServletRequest request) {
        final String selectCourses = request.getParameter("selectCourses");
        if (selectCourses != null) {
            request.setAttribute("selectCourses", selectCourses);
        }
    }

    private String readClassSelected(HttpServletRequest request) {
        String classIdSelectedString = request.getParameter("classId");
        String classIdSelected = null;
        if (classIdSelectedString != null) {
            classIdSelected = classIdSelectedString;
        } else {
            classIdSelected = (String) request.getAttribute("classId");
        }
        return classIdSelected;
    }

    public ActionForward exitEnrollment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("studentFirstPage");
    }

    public ActionForward prepareStartViewWarning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("executionSemesterID", request.getParameter("executionSemesterID"));
        if (getAndSetRegistration(request) == null) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
        } else {
            return mapping.findForward("prepareEnrollmentViewWarning");
        }
    }

    protected void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping, String errorMessageKey)
            throws FenixTransactionException {

        if (!isTokenValid(request)) {
            form.reset(mapping, request);
            throw new FenixTransactionException(errorMessageKey);
        }
        generateToken(request);
        saveToken(request);

    }

}