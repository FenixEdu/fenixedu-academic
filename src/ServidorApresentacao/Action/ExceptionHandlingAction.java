/*
 * Created on 25/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.Action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.EMail;

/**
 * @author jmota
 */
public class ExceptionHandlingAction extends FenixDispatchAction {
	
	public ActionForward sendEmail(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		
		ActionMapping originalMapping =
			(ActionMapping) session.getAttribute(
				SessionConstants.ORIGINAL_MAPPING_KEY);

		DynaActionForm emailForm = (DynaActionForm) form;
		String formEmail = (String) emailForm.get("email");
		String formSubject = (String) emailForm.get("subject");
		String formBody = (String) emailForm.get("body");

		StackTraceElement[] stackTrace =
			(StackTraceElement[]) session.getAttribute(
				SessionConstants.EXCEPTION_STACK_TRACE);

		String requestContext= (String) session.getAttribute(SessionConstants.REQUEST_CONTEXT);

		String sender = "Sender: " + formEmail;
		String subject = "Error Report - " + formSubject;

		String mailBody = "Error Report\n\n";
		mailBody += sender +"\n\n";
		mailBody += "User Comment: \n" + formBody + "\n\n";
		mailBody += "Error Origin: \n";
		mailBody += "Exception: \n" + session.getAttribute(Globals.EXCEPTION_KEY) + "\n\n";
		mailBody += "RequestContext: \n" + requestContext + "\n\n\n";
		mailBody += "SessionContext: \n" + sessionContextGetter(request) + "\n\n\n";
		mailBody += "Path: " + originalMapping.getPath() + "\n";
		mailBody += "Name: " + originalMapping.getName() + "\n";
		mailBody += stackTrace2String(stackTrace);
		//TODO :IMPORTANT change the current mail server and e-mail to the real ones
		//		EMail email = new EMail("localhost", "erro@dot.ist.utl.pt");
		EMail email = new EMail("mail.rnl.ist.utl.pt", "erro@dot.ist.utl.pt");
		//email.send("suporte@dot.ist.utl.pt","Error Report",stackTrace.toString());
		email.send("j.mota@netcabo.pt", subject, mailBody);
		email.send("lepc@mega.ist.utl.pt", subject, mailBody);
		//		removes from session all the un-needed info
		sessionRemover(request);
		return originalMapping.getInputForward();
	}

	public ActionForward goBack(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			HttpSession session = request.getSession(false);

			ActionMapping originalMapping =
				(ActionMapping) session.getAttribute(
					SessionConstants.ORIGINAL_MAPPING_KEY);
				sessionRemover(request);
			return originalMapping.getInputForward();
		}




	private String stackTrace2String(StackTraceElement[] stackTrace) {
		String result = "StackTrace: \n ";
		int i = 0;
		while (i < stackTrace.length) {
			result += stackTrace[i] + "\n";
			i++;
		}
		return result;
	}

	private void sessionRemover(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

//		session.removeAttribute(SessionConstants.ORIGINAL_MAPPING_KEY);
		session.removeAttribute(Globals.ERROR_KEY);
		session.removeAttribute(SessionConstants.REQUEST_CONTEXT);
//		session.removeAttribute(SessionConstants.EXCEPTION_STACK_TRACE);
	}

	private String sessionContextGetter(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Enumeration sessionContents = session.getAttributeNames();
		String context = "";
		while (sessionContents.hasMoreElements()) {
			String sessionElement = sessionContents.nextElement().toString();
			context += "Element:" + sessionElement + "\n";
			context += "Element Value:"
				+ session.getAttribute(sessionElement)
				+ "\n";
		}

		return context;
	}
	
}
