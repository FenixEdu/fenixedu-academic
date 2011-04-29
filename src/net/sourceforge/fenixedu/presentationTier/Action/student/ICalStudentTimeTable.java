package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.messaging.RegistrationsBean;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/ICalTimeTable", module = "student")
@Forwards( { @Forward(name = "viewOptions", path = "icalendar-view-options"),
	@Forward(name = "chooseRegistration", path = "icalendar-choose-registration") })
public class ICalStudentTimeTable extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	if (getRenderedObject("bean") == null) {
	    Registration registration = rootDomainObject.readRegistrationByOID(Integer.valueOf(request
		    .getParameter("registrationId")));

	    return forwardToShow(registration, mapping, request);
	} else {
	    if (((RegistrationsBean) getRenderedObject("bean")).getSelected() != null) {
		return forwardToShow(((RegistrationsBean) getRenderedObject("bean")).getSelected(), mapping, request);
	    } else {
		return prepare(mapping, form, request, response);
	    }

	}

    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	List<Registration> registrations = getUserView(request).getPerson().getStudent().getActiveRegistrations();
	if (registrations.size() == 1) {
	    return forwardToShow(registrations.get(0), mapping, request);
	} else {
	    RegistrationsBean bean = new RegistrationsBean();
	    bean.setRegistrations(registrations);
	    request.setAttribute("bean", bean);
	    return mapping.findForward("chooseRegistration");
	}
    }

    public ActionForward generateKey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
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

    public static String calculatePayload(String to, Registration reg, User user) throws Exception {

	Cipher cipher = Cipher.getInstance("AES");

	SecretKeySpec skeySpec = new SecretKeySpec(user.getPrivateKey().getBytes(), "AES");
	cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

	byte[] encrypted = cipher.doFinal(("This is for " + to + " calendar ##" + "1.6180339##Sistema Fenix##"
		+ user.getPrivateKeyCreation().toString() + "##" + reg.getIdInternal() + "##"
		+ user.getPrivateKeyValidity().toString() + "##" + "## This is for " + to + " calendar").getBytes());

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

}
