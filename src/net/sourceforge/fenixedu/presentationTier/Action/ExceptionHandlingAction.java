/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.support.SupportRequestPriority;
import net.sourceforge.fenixedu.domain.support.SupportRequestType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;

import pt.utl.ist.fenix.tools.util.EMail;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author João Mota
 */
public class ExceptionHandlingAction extends FenixDispatchAction {

    public ActionForward sendEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm emailForm = (DynaActionForm) form;
	String formEmail = (String) emailForm.get("email");
	String formSubject = (String) emailForm.get("subject");
	String formBody = (String) emailForm.get("body");
	String exceptionInfo = (String) emailForm.get("exceptionInfo");

	String sender = "Sender: " + formEmail;
	String subject = " - " + formSubject;

	String mailBody = "Error Report\n\n";
	mailBody += sender + "\n\n";
	mailBody += "User Comment: \n" + formBody + "\n\n";
	mailBody += exceptionInfo;

	final ActionForward actionForward;
	actionForward = new ActionForward();
	actionForward.setContextRelative(false);
	actionForward.setRedirect(true);
	actionForward.setPath("/showErrorPageRegistered.do");

	EMail email = null;
	try {
	    if (!request.getServerName().equals("localhost")) {
		email = new EMail("mail.adm", "erro@dot.ist.utl.pt");
		email.send("suporte@dot.ist.utl.pt", "Fenix Error Report" + subject, mailBody);
	    } else {
		email = new EMail("mail.rnl.ist.utl.pt", "erro@dot.ist.utl.pt");
		email.send("suporte@dot.ist.utl.pt", "Localhost Error Report", mailBody);
	    }
	} catch (Throwable t) {
	    t.printStackTrace();
	    throw new Error(t);
	}

	sessionRemover(request);

	return actionForward;
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	ActionMapping originalMapping = (ActionMapping) request.getSession().getAttribute(SessionConstants.ORIGINAL_MAPPING_KEY);
	sessionRemover(request);

	ActionForward actionForward = originalMapping.getInputForward();

	RequestUtils.selectModule(originalMapping.getModuleConfig().getPrefix(), request, this.servlet.getServletContext());
	if (actionForward.getPath() == null) {
	    actionForward.setPath("/");
	}

	return actionForward;
    }

    private String stackTrace2String(StackTraceElement[] stackTrace) {
	String result = "StackTrace: \n ";
	int i = 0;
	if (stackTrace != null) {
	    while (i < stackTrace.length) {
		result += stackTrace[i] + "\n";
		i++;
	    }
	}
	return result;
    }

    private void sessionRemover(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	session.removeAttribute(Globals.ERROR_KEY);
	session.removeAttribute(SessionConstants.REQUEST_CONTEXT);
    }

    private String sessionContextGetter(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	Enumeration sessionContents = session.getAttributeNames();
	String context = "";
	while (sessionContents.hasMoreElements()) {
	    String sessionElement = sessionContents.nextElement().toString();
	    context += "Element:" + sessionElement + "\n";
	    context += "Element Value:" + session.getAttribute(sessionElement) + "\n";
	}

	return context;
    }

    public final ActionForward prepareSupportHelp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	SupportRequestBean supportRequestBean = new SupportRequestBean();
	supportRequestBean.setResponseEmail(getLoggedPerson(request).getEmail());

	String contextId = request.getParameter("contextId");
	supportRequestBean.setRequestContext(RootDomainObject.getInstance().readContentByOID(Integer.valueOf(contextId)));
	request.setAttribute("requestBean", supportRequestBean);

	return mapping.findForward("supportHelpInquiry");
    }

    public final ActionForward processException(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm emailForm = (DynaActionForm) form;
	String formEmail = (String) emailForm.get("email");
	String formSubject = (String) emailForm.get("subject");
	String formBody = (String) emailForm.get("body");

	SupportRequestBean requestBean = new SupportRequestBean(SupportRequestType.EXCEPTION, SupportRequestPriority.IMPEDIMENT);
	requestBean.setResponseEmail(formEmail);
	requestBean.setSubject(formSubject);
	requestBean.setMessage(formBody);

	return sendSupportEmail(mapping, form, request, response, requestBean);
    }

    public final ActionForward supportHelpError(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("requestBean", (SupportRequestBean) getObjectFromViewState("requestBean"));
	return mapping.findForward("supportHelpInquiry");
    }

    public final ActionForward supportHelpException(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("exceptionInfo", request.getParameter("exceptionInfo"));
	request.setAttribute("requestBean", (SupportRequestBean) getObjectFromViewState("requestBean"));
	return mapping.findForward("supportHelpInquiry");
    }

    public final ActionForward processSupportRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return sendSupportEmail(mapping, form, request, response, (SupportRequestBean) getObjectFromViewState("requestBean"));
    }

    protected final ActionForward sendSupportEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, SupportRequestBean requestBean) throws Exception {

	StringBuilder builder = new StringBuilder();
	List<String> roles;
	if (getLoggedPerson(request) != null) {
	    roles = getLoggedPerson(request).getMainRoles();
	} else {
	    roles = new ArrayList<String>(0);
	}
	String mailSubject = generateEmailSubject(request, requestBean, roles, builder);
	builder.setLength(0);
	String mailBody = generateEmailBody(request, requestBean, roles, builder);

	final ActionForward actionForward = new ActionForward();
	actionForward.setContextRelative(false);
	actionForward.setRedirect(true);
	actionForward.setPath("/showErrorPageRegistered.do");

	try {
	    executeService("CreateSupportRequest", getLoggedPerson(request), requestBean);
	} catch (DomainException e) {
	    // fail silently
	    // a mail must be always sent, no need to give error feedback
	}

	EMail email = null;
	try {
	    if (!request.getServerName().equals("localhost")) {
		email = new EMail("mail.adm", "erro@dot.ist.utl.pt");
	    } else {
		email = new EMail("mail.rnl.ist.utl.pt", "erro@dot.ist.utl.pt");
	    }
	    final ResourceBundle gBundle = ResourceBundle.getBundle("resources.GlobalResources", Language.getLocale());
	    email.send(gBundle.getString("suporte.mail"), mailSubject, mailBody);

	    System.out.println(mailSubject);
	    System.out.println(mailBody);

	} catch (Throwable t) {
	    t.printStackTrace();
	    throw new Error(t);
	}

	sessionRemover(request);
	return actionForward;
    }

    private String generateEmailSubject(HttpServletRequest request, SupportRequestBean requestBean, List<String> roles,
	    StringBuilder builder) {

	builder.append(request.getServerName().equals("localhost") ? "Localhost " : "");
	builder.append("[").append(roles.size() > 0 ? roles.get(0) : "").append("] ");
	builder.append("[").append(requestBean.getRequestType()).append("] ");
	builder.append("[").append(requestBean.getRequestPriority().getName()).append("] ");

	if (!StringUtils.isEmpty(requestBean.getSubject())) {
	    builder.append(requestBean.getSubject());
	}
	return builder.toString();
    }

    private String generateEmailBody(HttpServletRequest request, SupportRequestBean requestBean, List<String> roles,
	    StringBuilder builder) {

	builder.append("Support Report").append("\n\n");
	builder.append("Sender: ").append(requestBean.getResponseEmail()).append("\n\n");

	builder.append("Roles: ");
	if (roles.size() > 0) {
	    for (String role : roles) {
		builder.append(role).append(", ");
	    }
	    builder.setLength(builder.length() - 2);
	} else {
	    final ResourceBundle aBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
	    builder.append(aBundle.getString("support.mail.roles.error"));
	}
	builder.append("\n\n");

	if (requestBean.getRequestContext() != null) {
	    builder.append("Portal: ").append(requestBean.getRequestContext().getName()).append("\n\n");
	}

	builder.append("Type: ").append(requestBean.getRequestType().getName()).append("\n\n");
	builder.append("Priority: ").append(requestBean.getRequestPriority().getName()).append("\n\n");

	builder.append("User Agent: ").append(
		StringUtils.isEmpty(request.getParameter("userAgent")) ? "" : request.getParameter("userAgent")).append("\n\n");

	builder.append("User Comment: \n").append(requestBean.getMessage()).append("\n\n");
	builder.append(request.getParameter("exceptionInfo") != null ? request.getParameter("exceptionInfo").toString() : "");

	return builder.toString();
    }

}