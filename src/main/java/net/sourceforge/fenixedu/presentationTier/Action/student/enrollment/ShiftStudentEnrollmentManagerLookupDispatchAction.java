/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.student.enrollment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.ClassEnrollmentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.ReadClassTimeTableByStudent;
import net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift.WriteStudentAttendingCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException;
import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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

        try {
            WriteStudentAttendingCourse.runWriteStudentAttendingCourse(registration, executionCourseId);

        } catch (NotAuthorizedException exception) {
            addActionMessage(request, "error.attend.curricularCourse.impossibleToEnroll");
            return mapping.getInputForward();

        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            return mapping.findForward("prepareShiftEnrollment");

        } catch (FenixServiceException exception) {
            addActionMessage(request, "errors.impossible.operation");
            return mapping.getInputForward();
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

        final List<InfoShowOccupation> infoClasslessons =
                ReadClassTimeTableByStudent.runReadClassTimeTableByStudent(registration, schoolClass, executionCourse);

        request.setAttribute("infoClasslessons", infoClasslessons);
        request.setAttribute("infoClasslessonsEndTime", Integer.valueOf(getEndTime(infoClasslessons)));

        final List<InfoShowOccupation> infoLessons = ReadStudentTimeTable.run(registration, null);
        request.setAttribute("person", registration.getPerson());
        request.setAttribute("infoLessons", infoLessons);
        request.setAttribute("infoLessonsEndTime", Integer.valueOf(getEndTime(infoLessons)));

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