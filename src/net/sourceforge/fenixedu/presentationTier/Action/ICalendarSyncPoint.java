package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.Period;

import com.lowagie.text.DocumentException;

import dml.Role;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import net.fortuna.ical4j.model.Calendar;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ExecutionPeriodDA;
import net.sourceforge.fenixedu.presentationTier.Action.student.ICalStudentTimeTable;
import net.sourceforge.fenixedu.presentationTier.backBeans.example.ExecutionPeriods;
import net.sourceforge.fenixedu.util.PeriodState;

@Mapping(path = "/iCalendarSync", module = "external")
public class ICalendarSyncPoint extends FenixDispatchAction {

    private Calendar getClassCalendar(Registration registration, DateTime validity, HttpServletRequest request) {
	ExecutionSemester currentExecutionSemester = null;
	List<EventBean> allEvents = new ArrayList<EventBean>();
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	int serverPort = request.getServerPort();

	for (Shift shift : registration.getShiftsForCurrentExecutionPeriod()) {
	    for (Lesson lesson : shift.getAssociatedLessons()) {
		allEvents.addAll(lesson.getAllLessonsEvents(scheme, serverName, serverPort));
	    }
	    if (currentExecutionSemester == null) {
		currentExecutionSemester = shift.getExecutionPeriod();
	    }
	}

	for (Shift shift : registration.getShiftsFor(currentExecutionSemester.getPreviousExecutionPeriod())) {
	    for (Lesson lesson : shift.getAssociatedLessons()) {
		allEvents.addAll(lesson.getAllLessonsEvents(scheme, serverName, serverPort));
	    }
	}
	String url = scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort) + "/privado";
	EventBean event = new EventBean("Renovar a chave do calendario.", validity.minusMinutes(30), validity.plusMinutes(30),
		false, "Portal Fénix", url,
		"A sua chave de sincronização do calendario vai expirar. Diriga-se ao Portal Fénix para gerar nova chave");

	allEvents.add(event);

	return CalendarFactory.createCalendar(allEvents);

    }
    private Calendar getExamsCalendar(Registration registration, DateTime validity, HttpServletRequest request) {
	ExecutionSemester currentExecutionSemester = ExecutionSemester.readActualExecutionSemester();
	List<EventBean> allEvents = new ArrayList<EventBean>();
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	int serverPort = request.getServerPort();

	for(WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(currentExecutionSemester)){
	    allEvents.addAll(writtenEvaluation.getAllEvents(registration, scheme, serverName, serverPort));
	}
	
	for(Attends attends : registration.getAttendsForExecutionPeriod(currentExecutionSemester)){
	    for(Project project : attends.getExecutionCourse().getAssociatedProjects()){
		allEvents.addAll(project.getAllEvents(attends.getExecutionCourse(), scheme, serverName, serverPort));
	    }
	}
	
	for(WrittenEvaluation writtenEvaluation : registration.getWrittenEvaluations(currentExecutionSemester.getPreviousExecutionPeriod())){
	    allEvents.addAll(writtenEvaluation.getAllEvents(registration, scheme, serverName, serverPort));
	}
	
	for(Attends attends : registration.getAttendsForExecutionPeriod(currentExecutionSemester.getPreviousExecutionPeriod())){
	    for(Project project : attends.getExecutionCourse().getAssociatedProjects()){
		allEvents.addAll(project.getAllEvents(attends.getExecutionCourse(), scheme, serverName, serverPort));
	    }
	} 
	
	String url = scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort) + "/privado";
	EventBean event = new EventBean("Renovar a chave do calendario.", validity.minusMinutes(30), validity.plusMinutes(30),
		false, "Portal Fénix", url,
		"A sua chave de sincronização do calendario vai expirar. Diriga-se ao Portal Fénix para gerar nova chave");

	allEvents.add(event);

	return CalendarFactory.createCalendar(allEvents);

    }
    


    public ActionForward syncClasses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    final HttpServletResponse httpServletResponse) throws Exception {
	String userId = request.getParameter("user");
	String payload = request.getParameter("payload");
	String regId = request.getParameter("registrationID");

	if (userId == null || payload == null || regId == null) {
	    throw new DocumentException("error.expecting.parameter.not.found");
	}
	User user = User.readUserByUserUId(userId);
	Registration registration = rootDomainObject.readRegistrationByOID(Integer.valueOf(regId));

	final OutputStream outputStream = httpServletResponse.getOutputStream();

	if (payload.equals(ICalStudentTimeTable.calculatePayload("syncClasses", registration, user))) {
	    if (user.getPrivateKeyValidity().isBeforeNow()) {
		throw new DocumentException("private.key.validity.expired");
	    } else {
		if (user.getPerson().hasRole(RoleType.STUDENT)) {
		    Calendar calendar = getClassCalendar(registration,user.getPrivateKeyValidity(), request);
		    outputStream.write(calendar.toString().getBytes());
		} else {
		    throw new DocumentException("user.is.not.student");
		}
	    }
	} else {
	    throw new DocumentException("payload.checksum.doesnt.match");
	}
	return null;
    }
    
    public ActionForward syncExams(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    final HttpServletResponse httpServletResponse) throws Exception {
	String userId = request.getParameter("user");
	String payload = request.getParameter("payload");
	String regId = request.getParameter("registrationID");

	if (userId == null || payload == null || regId == null) {
	    throw new DocumentException("error.expecting.parameter.not.found");
	}
	User user = User.readUserByUserUId(userId);
	Registration registration = rootDomainObject.readRegistrationByOID(Integer.valueOf(regId));

	final OutputStream outputStream = httpServletResponse.getOutputStream();

	if (payload.equals(ICalStudentTimeTable.calculatePayload("syncExams", registration, user))) {
	    if (user.getPrivateKeyValidity().isBeforeNow()) {
		throw new DocumentException("private.key.validity.expired");
	    } else {
		if (user.getPerson().hasRole(RoleType.STUDENT)) {
		    Calendar calendar = getExamsCalendar(registration,user.getPrivateKeyValidity(), request);
		    outputStream.write(calendar.toString().getBytes());
		} else {
		    throw new DocumentException("user.is.not.student");
		}
	    }
	} else {
	    throw new DocumentException("payload.checksum.doesnt.match");
	}
	return null;
    }

    
    
}
