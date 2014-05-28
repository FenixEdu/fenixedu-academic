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
package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.model.Calendar;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.ICalStudentTimeTable;

import org.apache.commons.lang.CharEncoding;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/iCalendarSync", module = "external")
public class ICalendarSyncPoint extends FenixDispatchAction {

    private Calendar getClassCalendar(User user, DateTime validity, HttpServletRequest request) {

        List<EventBean> allEvents = getClasses(user);
        String url = CoreConfiguration.getConfiguration().applicationUrl() + "/privado";
        EventBean event =
                new EventBean("Renovar a chave do calendario.", validity.minusMinutes(30), validity.plusMinutes(30), false, null,
                        url, "A sua chave de sincronização do calendario vai expirar. Diriga-se ao Fénix para gerar nova chave");

        allEvents.add(event);

        return CalendarFactory.createCalendar(allEvents);

    }

    public List<EventBean> getClasses(User user) {

        List<EventBean> allEvents = new ArrayList<EventBean>();
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();

        for (Registration registration : user.getPerson().getStudent().getRegistrations()) {
            for (Shift shift : registration.getShiftsForCurrentExecutionPeriod()) {
                for (Lesson lesson : shift.getAssociatedLessons()) {
                    allEvents.addAll(lesson.getAllLessonsEvents());
                }
            }

            for (Shift shift : registration.getShiftsFor(currentExecutionSemester.getPreviousExecutionPeriod())) {
                for (Lesson lesson : shift.getAssociatedLessons()) {
                    allEvents.addAll(lesson.getAllLessonsEvents());
                }
            }
        }
        return allEvents;
    }

    public List<EventBean> getTeachingClasses(User user) {

        List<EventBean> allEvents = new ArrayList<EventBean>();

        for (Professorship professorShip : user.getPerson().getProfessorships()) {
            ExecutionCourse executionCourse = professorShip.getExecutionCourse();
            for (Lesson lesson : executionCourse.getLessons()) {
                allEvents.addAll(lesson.getAllLessonsEvents());
            }
        }
        return allEvents;
    }

    private Calendar getExamsCalendar(User user, DateTime validity, HttpServletRequest request) {

        List<EventBean> allEvents = getExams(user);

        String url = CoreConfiguration.getConfiguration().applicationUrl() + "/privado";
        EventBean event =
                new EventBean("Renovar a chave do calendario.", validity.minusMinutes(30), validity.plusMinutes(30), false, null,
                        url, "A sua chave de sincronização do calendario vai expirar. Diriga-se ao Fénix para gerar nova chave");

        allEvents.add(event);

        return CalendarFactory.createCalendar(allEvents);

    }

    public List<EventBean> getExams(User user) {
        List<EventBean> allEvents = new ArrayList<EventBean>();
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();

        for (Registration registration : user.getPerson().getStudent().getRegistrations()) {
            for (WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(currentExecutionSemester)) {
                allEvents.addAll(writtenEvaluation.getAllEvents(registration));
            }

            for (Attends attends : registration.getAttendsForExecutionPeriod(currentExecutionSemester)) {
                for (Project project : attends.getExecutionCourse().getAssociatedProjects()) {
                    allEvents.addAll(project.getAllEvents(attends.getExecutionCourse()));
                }
            }

            for (WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(currentExecutionSemester
                    .getPreviousExecutionPeriod())) {
                allEvents.addAll(writtenEvaluation.getAllEvents(registration));
            }

            for (Attends attends : registration.getAttendsForExecutionPeriod(currentExecutionSemester
                    .getPreviousExecutionPeriod())) {
                for (Project project : attends.getExecutionCourse().getAssociatedProjects()) {
                    allEvents.addAll(project.getAllEvents(attends.getExecutionCourse()));
                }
            }
        }
        return allEvents;
    }

    public List<EventBean> getTeachingExams(User user) {

        List<EventBean> allEvents = new ArrayList<EventBean>();

        for (Professorship professorShip : user.getPerson().getProfessorships()) {
            ExecutionCourse executionCourse = professorShip.getExecutionCourse();

            for (WrittenEvaluation writtenEvaluation : executionCourse.getWrittenEvaluations()) {
                allEvents.addAll(writtenEvaluation.getAllEvents(null));
            }

            for (Project project : executionCourse.getAssociatedProjects()) {
                allEvents.addAll(project.getAllEvents(executionCourse));
            }

        }
        return allEvents;
    }

    private Calendar getCalendar(String method, User user, DateTime validity, HttpServletRequest request)
            throws FenixActionException {
        if (method == "syncClasses") {
            return getClassCalendar(user, validity, request);
        } else if (method == "syncExams") {
            return getExamsCalendar(user, validity, request);
        } else {
            throw new FenixActionException("unexpected.syncing.method");
        }
    }

    private void sync(HttpServletRequest request, final HttpServletResponse httpServletResponse, String method)
            throws FenixActionException, Exception {
        String userId = request.getParameter("user");
        String payload = request.getParameter("payload");
        String regId = request.getParameter("registrationID");

        if (userId == null || payload == null || regId == null) {
            throw new FenixActionException("error.expecting.parameter.not.found");
        }

        final DomainObject object = FenixFramework.getDomainObject(regId);
        if (object instanceof Registration) {
            User user = User.findByUsername(userId);
            Registration registration = (Registration) object;

            if (user.getPrivateKey() != null && user.getPrivateKey().getPrivateKeyValidity() != null) {
                if (payload.equals(ICalStudentTimeTable.calculatePayload(method, registration, user))) {
                    if (user.getPrivateKey().getPrivateKeyValidity().isBeforeNow()) {
                        returnError(httpServletResponse, "private.key.validity.expired");
                    } else {
                        if (user.getPerson().hasRole(RoleType.STUDENT)) {

                            encodeAndTransmitResponse(httpServletResponse,
                                    getCalendar(method, user, user.getPrivateKey().getPrivateKeyValidity(), request));

                        } else {
                            returnError(httpServletResponse, "user.is.not.student");
                        }
                    }
                } else {
                    returnError(httpServletResponse, "payload.checksum.doesnt.match");
                }
            } else {
                returnError(httpServletResponse, "key.not.found");
            }
        } else {
            returnError(httpServletResponse, "invalid.request");
        }
    }

    public ActionForward syncExams(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            final HttpServletResponse httpServletResponse) throws Exception {
        sync(request, httpServletResponse, "syncExams");
        return null;
    }

    public ActionForward syncClasses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            final HttpServletResponse httpServletResponse) throws Exception {
        sync(request, httpServletResponse, "syncClasses");
        return null;
    }

    @SuppressWarnings("deprecation")
    private void returnError(HttpServletResponse httpServletResponse, String error) throws IOException {
        httpServletResponse.setStatus(500, error);
        httpServletResponse.getWriter().write("");
    }

    private void encodeAndTransmitResponse(HttpServletResponse httpServletResponse, Calendar calendar) throws Exception {
        httpServletResponse.setHeader("Content-Type", "text/calendar; charset=" + CharEncoding.UTF_8);

        final OutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(calendar.toString().getBytes(CharEncoding.UTF_8));
        outputStream.close();
    }
}
