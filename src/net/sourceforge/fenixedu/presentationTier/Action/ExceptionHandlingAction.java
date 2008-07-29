/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.Person;
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

    private final int INDENT = 12;

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

    private void sessionRemover(HttpServletRequest request) {
	HttpSession session = request.getSession(false);
	session.removeAttribute(Globals.ERROR_KEY);
	session.removeAttribute(SessionConstants.REQUEST_CONTEXT);
    }

    public final ActionForward prepareSupportHelp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	SupportRequestBean requestBean = new SupportRequestBean();
	requestBean.setResponseEmail(getLoggedPerson(request).getInstitutionalOrDefaultEmailAddress().getValue());
	requestBean.setRequestContext(RootDomainObject.getInstance().readContentByOID(
		Integer.valueOf(request.getParameter("contextId"))));

	request.setAttribute("requestBean", requestBean);
	return mapping.findForward("supportHelpInquiry");
    }

    public final ActionForward processExceptionLegacy(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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

    public final ActionForward supportHelpFieldValidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("exceptionInfo", request.getParameter("exceptionInfo"));
	request.setAttribute("requestBean", getObjectFromViewState("requestBean"));
	return mapping.findForward("supportHelpInquiry");
    }

    public final ActionForward processSupportRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	SupportRequestBean supportRequestBean = (SupportRequestBean) getObjectFromViewState("requestBean");
	return sendSupportEmail(mapping, form, request, response, supportRequestBean);
    }

    protected final ActionForward sendSupportEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, SupportRequestBean requestBean) throws Exception {

	StringBuilder builder = new StringBuilder();

	String mailSubject = generateEmailSubject(request, requestBean, getLoggedPerson(request), builder);
	builder.setLength(0);
	String mailBody = generateEmailBody(request, requestBean, getLoggedPerson(request), builder);

	try {
	    executeService("CreateSupportRequest", getLoggedPerson(request), requestBean);
	} catch (DomainException e) {
	    // a mail must be always sent, no need to give error feedback
	}

	System.out.println(mailSubject);
	System.out.println(mailBody);

	EMail email = null;
	try {
	    if (!request.getServerName().equals("localhost")) {
		email = new EMail("mail.adm", "erro@dot.ist.utl.pt");
	    } else {
		email = new EMail("mail.rnl.ist.utl.pt", "erro@dot.ist.utl.pt");
	    }
	    final ResourceBundle gBundle = ResourceBundle.getBundle("resources.GlobalResources", Language.getLocale());
	    email.send(gBundle.getString("suporte.mail"), mailSubject, mailBody);

	} catch (Throwable t) {
	    t.printStackTrace();
	    throw new Error(t);
	}

	sessionRemover(request);

	final ActionForward actionForward = new ActionForward();
	actionForward.setContextRelative(false);
	actionForward.setRedirect(true);
	actionForward.setPath("/showErrorPageRegistered.do");
	return actionForward;
    }

    private String generateEmailSubject(HttpServletRequest request, SupportRequestBean requestBean, Person loggedPerson,
	    StringBuilder builder) {

	builder.append(request.getServerName().equals("localhost") ? "Localhost " : "");
	builder.append("[").append(requestBean.getRequestContext() != null ? requestBean.getRequestContext().getName() : "")
		.append("] ");
	builder.append("[").append(requestBean.getRequestType().getName()).append("] ");
	builder.append("[").append(requestBean.getRequestPriority()).append("] ");
	builder.append(requestBean.getSubject());
	return builder.toString();
    }

    private String generateEmailBody(HttpServletRequest request, SupportRequestBean requestBean, Person loggedPerson,
	    StringBuilder builder) {

	appendNewLine(builder);
	builder.append("===========================================================================");
	appendNewLine(builder);
	appendRoles(builder, loggedPerson);
	appendNewLine(builder);
	appendUserInfo(builder, loggedPerson, requestBean);
	appendNewLine(builder);
	appendFormInfo(builder, requestBean);
	appendNewLine(builder);
	appendUserInfo(builder, (String) request.getParameter("userAgent"));
	appendNewLine(builder);
	builder.append("===========================================================================");
	appendNewLine(builder, 2);
	appendComments(builder, requestBean, (String) request.getParameter("exceptionInfo"));
	return builder.toString();
    }

    private void appendUserInfo(StringBuilder builder, String userAgent) {
	generateLabel(builder, "Browser/SO:").append("[").append(userAgent).append("]");
    }

    private void appendComments(StringBuilder builder, SupportRequestBean requestBean, String exceptionInfo) {

	builder.append(requestBean.getMessage());
	if (!StringUtils.isEmpty(exceptionInfo)) {
	    appendNewLine(builder, 4);
	    builder.append(exceptionInfo);
	    appendNewLine(builder);
	}
    }

    private void appendFormInfo(StringBuilder builder, SupportRequestBean requestBean) {

	generateLabel(builder, "Email:").append("[").append(requestBean.getResponseEmail()).append("]");
	appendNewLine(builder);

	generateLabel(builder, "Portal:").append("[").append(
		requestBean.getRequestContext() != null ? requestBean.getRequestContext().getName() : "").append("]");
	appendNewLine(builder);

	generateLabel(builder, "Tipo:").append("[").append(requestBean.getRequestType().getName()).append("]");
	appendNewLine(builder);

	generateLabel(builder, "Prioridade:").append("[").append(requestBean.getRequestPriority()).append("]");
    }

    private void appendRoles(StringBuilder builder, Person loggedPerson) {
	generateLabel(builder, "Roles:");
	builder.append("[");
	if (loggedPerson != null) {

	    for (String role : loggedPerson.getMainRoles()) {
		builder.append("#").append(role).append(", ");
	    }
	    builder.setLength(builder.length() - 2);
	}
	builder.append("] ");
    }

    private StringBuilder generateLabel(StringBuilder builder, String label) {
	builder.append(label);
	for (int i = label.length(); i <= 15; i++) {
	    builder.append(" ");
	}
	return builder;
    }

    private void appendUserInfo(StringBuilder builder, Person loggedPerson, SupportRequestBean requestBean) {
	generateLabel(builder, "Nome:");
	builder.append("[");
	if (loggedPerson != null) {

	    builder.append(loggedPerson.getName()).append("]");
	    appendNewLine(builder);
	    generateLabel(builder, "Username:");
	    builder.append("[").append(loggedPerson.getUsername()).append("]");
	} else {
	    final ResourceBundle aBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
	    builder.append(aBundle.getString("support.mail.session.error"));
	}
	builder.append("]");
    }

    private void appendNewLine(StringBuilder builder) {
	appendNewLine(builder, 1);
    }

    private void appendNewLine(StringBuilder builder, int number) {
	for (int i = 0; i < number; i++) {
	    builder.append("\n");
	}
    }

}