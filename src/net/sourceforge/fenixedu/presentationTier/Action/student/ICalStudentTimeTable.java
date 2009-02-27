package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.model.Calendar;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.icalendar.CalendarFactory;
import net.sourceforge.fenixedu.domain.util.icalendar.EventBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.util.ByteArray;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.HexDump;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateTime;
import org.w3c.tools.codec.Base64Encoder;

import com.sun.faces.util.Base64;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/ICalTimeTable", module = "messaging")
@Forwards( { @Forward(name = "viewOptions", path = "icalendar-view-options"),
	@Forward(name = "chooseRegistration", path = "icalendar-choose-registration") })
public class ICalStudentTimeTable extends FenixDispatchAction {

    private Registration getRegistration(final HttpServletRequest request) {
	Integer registrationId = Integer.valueOf(request.getParameter("registrationId"));
	return rootDomainObject.readRegistrationByOID(registrationId);
    }

    private Registration getRegistration(final ActionForm form, final HttpServletRequest request) {
	Integer registrationId = null;
	if (form != null) {
	    registrationId = (Integer) ((DynaActionForm) form).get("registrationId");
	}
	if (registrationId == null && !StringUtils.isEmpty(request.getParameter("registrationId"))) {
	    registrationId = Integer.valueOf(request.getParameter("registrationId"));
	}
	return rootDomainObject.readRegistrationByOID(registrationId);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	List<Registration> registrations = getUserView(request).getPerson().getStudent().getActiveRegistrations();
	if (registrations.size() == 1) {
	    return forwardToShow(registrations.get(0), mapping, request);
	} else {
	    request.setAttribute("registrations", registrations);
	    return mapping.findForward("chooseRegistration");
	}
    }

    public static String calculatePayload(String to, Registration reg, User user) throws Exception {

	Cipher cipher = Cipher.getInstance("AES");

	SecretKeySpec skeySpec = new SecretKeySpec(user.getPrivateKey().getBytes(), "AES");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

	byte[] encrypted = cipher.doFinal(("This is for " + to + " calendar ##" + "1.6180339##Sistema Fenix##"
		+ user.getPrivateKeyCreation().toString() + "##" + reg.getIdInternal() + "##"+ user.getPrivateKeyValidity().toString() + "##"
		+ "## This is for " + to + " calendar").getBytes());

	return DigestUtils.shaHex(encrypted);
    }

    private String getUrl(String to, Registration registration, HttpServletRequest request) throws Exception {
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	int serverPort = request.getServerPort();
	String url = scheme + "://" + serverName + ((serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort)
		+ request.getContextPath();
	url += "/external/iCalendarSync.do?method=" + to + "&user=" + AccessControl.getPerson().getUser().getUserUId() + ""
		+ "&registrationID=" + registration.getIdInternal() + "&payload="
		+ calculatePayload(to, registration, AccessControl.getPerson().getUser());
	return url;
    }

    public ActionForward generateKey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {
	try {
	    AccessControl.getPerson().getUser().generateNewKey();
	} catch (Exception E) {
	    throw new DomainException("error.impossible.to.generate.sha256.key");
	}

	return this.redirect("/ICalTimeTable.do?method=show&registrationId=" + request.getParameter("registrationId"), request);
    }

    private ActionForward forwardToShow(Registration registration, ActionMapping mapping, HttpServletRequest request)
	    throws Exception {
	request.setAttribute("registrationId", registration.getIdInternal());

	if (AccessControl.getPerson().getUser().getPrivateKeyValidity() != null
		&& AccessControl.getPerson().getUser().getPrivateKeyValidity().isAfter(new DateTime())) {
	    if (AccessControl.getPerson().getUser().getPrivateKeyValidity() != null) {
		request.setAttribute("expirationDate", AccessControl.getPerson().getUser().getPrivateKeyValidity().toString(
			"dd/MM/yyyy HH:mm"));
		request.setAttribute("user", AccessControl.getPerson().getUser().getUserUId());
		request.setAttribute("classURL", getUrl("syncClasses", registration, request));
		request.setAttribute("examsURL", getUrl("syncExams", registration, request));
	    }
	    request.setAttribute("stillValid", true);
	} else {
	    request.setAttribute("stillValid", false);
	    request.setAttribute("payload", "");
	}
	return mapping.findForward("viewOptions");
    }

    public ActionForward show(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return forwardToShow(getRegistration(actionForm, request), mapping, request);
    }

    public ActionForward getStudentClassTimeTable(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    final HttpServletResponse httpServletResponse) throws IOException {
	Registration registration = getRegistration(request);
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

	Calendar calendar = CalendarFactory.createCalendar(allEvents);
	httpServletResponse.setContentType("text/calendar");
	final OutputStream outputStream = httpServletResponse.getOutputStream();
	outputStream.write(calendar.toString().getBytes());
	outputStream.close();

	return null;
    }
}
