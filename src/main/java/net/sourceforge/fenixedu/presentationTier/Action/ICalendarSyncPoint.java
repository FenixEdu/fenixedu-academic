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
import net.sourceforge.fenixedu.domain.Login;
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
import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.User;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/iCalendarSync", module = "external")
public class ICalendarSyncPoint extends FenixDispatchAction {

    private Calendar getClassCalendar(User user, DateTime validity, HttpServletRequest request) {

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        List<EventBean> allEvents = getClasses(user, scheme, serverName, serverPort);
        String url = scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort) + "/privado";
        EventBean event =
                new EventBean("Renovar a chave do calendario.", validity.minusMinutes(30), validity.plusMinutes(30), false,
                        "Portal Fénix", url,
                        "A sua chave de sincronização do calendario vai expirar. Diriga-se ao Portal Fénix para gerar nova chave");

        allEvents.add(event);

        return CalendarFactory.createCalendar(allEvents);

    }

    public List<EventBean> getClasses(User user, String scheme, String serverName, int serverPort) {

        List<EventBean> allEvents = new ArrayList<EventBean>();
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();

        for (Registration registration : user.getPerson().getStudent().getRegistrations()) {
            for (Shift shift : registration.getShiftsForCurrentExecutionPeriod()) {
                for (Lesson lesson : shift.getAssociatedLessons()) {
                    allEvents.addAll(lesson.getAllLessonsEvents(scheme, serverName, serverPort));
                }
            }

            for (Shift shift : registration.getShiftsFor(currentExecutionSemester.getPreviousExecutionPeriod())) {
                for (Lesson lesson : shift.getAssociatedLessons()) {
                    allEvents.addAll(lesson.getAllLessonsEvents(scheme, serverName, serverPort));
                }
            }
        }
        return allEvents;
    }

    public List<EventBean> getTeachingClasses(User user, String scheme, String serverName, int serverPort) {

        List<EventBean> allEvents = new ArrayList<EventBean>();

        for (Professorship professorShip : user.getPerson().getProfessorships()) {
            ExecutionCourse executionCourse = professorShip.getExecutionCourse();
            for (Lesson lesson : executionCourse.getLessons()) {
                allEvents.addAll(lesson.getAllLessonsEvents(scheme, serverName, serverPort));
            }
        }
        return allEvents;
    }

    private Calendar getExamsCalendar(User user, DateTime validity, HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        List<EventBean> allEvents = getExams(user, scheme, serverName, serverPort);

        String url = scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort) + "/privado";
        EventBean event =
                new EventBean("Renovar a chave do calendario.", validity.minusMinutes(30), validity.plusMinutes(30), false,
                        "Portal Fénix", url,
                        "A sua chave de sincronização do calendario vai expirar. Diriga-se ao Portal Fénix para gerar nova chave");

        allEvents.add(event);

        return CalendarFactory.createCalendar(allEvents);

    }

    public List<EventBean> getExams(User user, String scheme, String serverName, int serverPort) {
        List<EventBean> allEvents = new ArrayList<EventBean>();
        ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();

        for (Registration registration : user.getPerson().getStudent().getRegistrations()) {
            for (WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(currentExecutionSemester)) {
                allEvents.addAll(writtenEvaluation.getAllEvents(registration, scheme, serverName, serverPort));
            }

            for (Attends attends : registration.getAttendsForExecutionPeriod(currentExecutionSemester)) {
                for (Project project : attends.getExecutionCourse().getAssociatedProjects()) {
                    allEvents.addAll(project.getAllEvents(attends.getExecutionCourse(), scheme, serverName, serverPort));
                }
            }

            for (WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(currentExecutionSemester
                    .getPreviousExecutionPeriod())) {
                allEvents.addAll(writtenEvaluation.getAllEvents(registration, scheme, serverName, serverPort));
            }

            for (Attends attends : registration.getAttendsForExecutionPeriod(currentExecutionSemester
                    .getPreviousExecutionPeriod())) {
                for (Project project : attends.getExecutionCourse().getAssociatedProjects()) {
                    allEvents.addAll(project.getAllEvents(attends.getExecutionCourse(), scheme, serverName, serverPort));
                }
            }
        }
        return allEvents;
    }

    public List<EventBean> getTeachingExams(User user, String scheme, String serverName, int serverPort) {

        List<EventBean> allEvents = new ArrayList<EventBean>();

        for (Professorship professorShip : user.getPerson().getProfessorships()) {
            ExecutionCourse executionCourse = professorShip.getExecutionCourse();

            for (WrittenEvaluation writtenEvaluation : executionCourse.getWrittenEvaluations()) {
                allEvents.addAll(writtenEvaluation.getAllEvents(null, scheme, serverName, serverPort));
            }

            for (Project project : executionCourse.getAssociatedProjects()) {
                allEvents.addAll(project.getAllEvents(executionCourse, scheme, serverName, serverPort));
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
            User user = Login.readUserByUserUId(userId);
            Registration registration = (Registration) object;

            if (user.getPrivateKeyValidity() != null) {
                if (payload.equals(ICalStudentTimeTable.calculatePayload(method, registration, user))) {
                    if (user.getPrivateKeyValidity().isBeforeNow()) {
                        returnError(httpServletResponse, "private.key.validity.expired");
                    } else {
                        if (user.getPerson().hasRole(RoleType.STUDENT)) {

                            encodeAndTransmitResponse(httpServletResponse,
                                    getCalendar(method, user, user.getPrivateKeyValidity(), request));

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
