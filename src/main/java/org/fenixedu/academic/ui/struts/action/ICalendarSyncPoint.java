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
package org.fenixedu.academic.ui.struts.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.CharEncoding;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.util.icalendar.CalendarFactory;
import org.fenixedu.academic.domain.util.icalendar.EventBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.ui.struts.action.student.ICalStudentTimeTable;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.DateTime;

import net.fortuna.ical4j.model.Calendar;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/iCalendarSync", module = "external")
public class ICalendarSyncPoint extends FenixDispatchAction {

    private Calendar getClassCalendar(User user, DateTime validity, HttpServletRequest request) {

        List<EventBean> allEvents = getClasses(user);
        String url = CoreConfiguration.getConfiguration().applicationUrl() + "/login";
        EventBean event =
                new EventBean("Renovar a chave do calendario.", validity.minusMinutes(30), validity.plusMinutes(30), false, null,
                        url, "A sua chave de sincronização do calendario vai expirar. Diriga-se ao Fénix para gerar nova chave");

        allEvents.add(event);

        return CalendarFactory.createCalendar(allEvents);

    }

    public List<EventBean> getClasses(User user) {

        List<EventBean> allEvents = new ArrayList<EventBean>();
        ExecutionInterval currentExecutionInterval = ExecutionInterval.findFirstCurrentChild(null);

        if (user.getPerson().getStudent() != null) {

            for (Registration registration : user.getPerson().getStudent().getRegistrationsSet()) {
                for (Shift shift : registration.getShiftsForCurrentExecutionPeriod()) {
                    for (Lesson lesson : shift.getAssociatedLessonsSet()) {
                        allEvents.addAll(lesson.getAllLessonsEvents());
                    }
                }

                for (Shift shift : registration.getShiftsFor(currentExecutionInterval.getPrevious())) {
                    for (Lesson lesson : shift.getAssociatedLessonsSet()) {
                        allEvents.addAll(lesson.getAllLessonsEvents());
                    }
                }
            }
        }
        return allEvents;
    }

    public List<EventBean> getTeachingClasses(User user) {

        List<EventBean> allEvents = new ArrayList<EventBean>();

        for (Professorship professorShip : user.getPerson().getProfessorshipsSet()) {
            ExecutionCourse executionCourse = professorShip.getExecutionCourse();
            executionCourse.getAssociatedShifts().stream().flatMap(s -> s.getAssociatedLessonsSet().stream())
                    .forEach(lesson -> allEvents.addAll(lesson.getAllLessonsEvents()));
        }
        return allEvents;
    }

    private Calendar getCalendar(String method, User user, DateTime validity, HttpServletRequest request)
            throws FenixActionException {
        if (method == "syncClasses") {
            return getClassCalendar(user, validity, request);
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
                        if (RoleType.STUDENT.isMember(user.getPerson().getUser())) {

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

    public ActionForward syncClasses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            final HttpServletResponse httpServletResponse) throws Exception {
        sync(request, httpServletResponse, "syncClasses");
        return null;
    }

    @SuppressWarnings("deprecation")
    private void returnError(HttpServletResponse httpServletResponse, String error) throws IOException {
        httpServletResponse.setStatus(500);
        httpServletResponse.getWriter().write(error);
    }

    private void encodeAndTransmitResponse(HttpServletResponse httpServletResponse, Calendar calendar) throws Exception {
        httpServletResponse.setHeader("Content-Type", "text/calendar; charset=" + CharEncoding.UTF_8);

        final OutputStream outputStream = httpServletResponse.getOutputStream();
        outputStream.write(calendar.toString().getBytes(CharEncoding.UTF_8));
        outputStream.close();
    }
}
