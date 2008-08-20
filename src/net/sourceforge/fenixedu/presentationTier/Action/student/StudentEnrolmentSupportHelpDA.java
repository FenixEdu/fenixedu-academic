package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.support.SupportRequestBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.ExceptionHandlingAction;

import org.apache.commons.validator.EmailValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.EMail;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@Mapping(path = "/exceptionHandlingAction", module = "student")
@Forwards( { @Forward(name = "supportHelpInquiry", path = "/supportHelpInquiry.jsp", contextRelative = true, useTile = false),
	@Forward(name = "showErrorPage", path = "/exception/errorRegistered.jsp", contextRelative = true, useTile = false)

})
public class StudentEnrolmentSupportHelpDA extends ExceptionHandlingAction {

    @Override
    public final ActionForward prepareSupportHelp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final SupportRequestBean requestBean = new SupportRequestBean();
	requestBean.setResponseEmail(getLoggedPerson(request).getInstitutionalOrDefaultEmailAddressValue());
	requestBean.setRequestContext(rootDomainObject.readContentByOID(Integer.valueOf(request.getParameter("contextId"))));

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
	final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	requestBean.setSubject(bundle.getString("label.student.enrolments") + " " + executionSemester.getQualifiedName());

	request.setAttribute("requestBean", requestBean);
	return mapping.findForward("supportHelpInquiry");
    }

    @Override
    protected ActionForward sendSupportEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, SupportRequestBean requestBean) throws Exception {

	final StringBuilder builder = new StringBuilder();
	builder.setLength(0);

	final String mailSubject = generateEmailSubject(request, requestBean, getLoggedPerson(request), builder);
	final String mailBody = generateEmailBody(request, requestBean, getLoggedPerson(request), builder);

	try {
	    executeService("CreateSupportRequest", getLoggedPerson(request), requestBean);
	} catch (DomainException e) {
	    // a mail must be always sent, no need to give error feedback
	}

	try {
	    sendMail(request, requestBean, mailSubject, mailBody);
	} catch (Throwable t) {
	    t.printStackTrace();
	    throw new Error(t);
	}

	sessionRemover(request);
	return mapping.findForward("showErrorPage");
    }

    @Override
    protected void sendMail(HttpServletRequest request, SupportRequestBean requestBean, String mailSubject, String mailBody) {
	final EMail email = new EMail(!request.getServerName().equals("localhost") ? "mail.adm" : "mail.rnl.ist.utl.pt",
		isEmailValid(requestBean) ? requestBean.getResponseEmail() : "erro@dot.ist.utl.pt");
	final ResourceBundle bundle = ResourceBundle.getBundle("resources.GlobalResources", Language.getLocale());
	email.send(bundle.getString("support.enrolments.mail"), mailSubject, mailBody);
    }

    private boolean isEmailValid(final SupportRequestBean requestBean) {
	return EmailValidator.getInstance().isValid(requestBean.getResponseEmail());
    }

}
