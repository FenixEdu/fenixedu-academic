/*
 * Created on 25/Fev/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package ServidorApresentacao.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.EMail;

/**
 * @author jmota
 */
public class ExceptionHandlingAction extends FenixAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		ActionMapping originalMapping =
			(ActionMapping) request.getSession().getAttribute(
				SessionConstants.ORIGINAL_MAPPING_KEY);

		StackTraceElement[] stackTrace =
			(StackTraceElement[]) request.getSession().getAttribute(
				SessionConstants.EXCEPTION_STACK_TRACE);
		String mailBody="Error Report\n\n Error Origin: \n";
		mailBody +="Path: " + originalMapping.getPath()+ "\n";
		mailBody +="Name: " + originalMapping.getName()+ "\n";
		mailBody += stackTrace2String(stackTrace);
		//TODO : we still have to do all the exception treatment, including the e-mail and so on
		//		EMail email = new EMail("localhost", "erro@dot.ist.utl.pt");
		EMail email = new EMail("mail.rnl.ist.utl.pt", "erro@dot.ist.utl.pt");
		//email.send("suporte@dot.ist.utl.pt","Error Report",stackTrace.toString());
		email.send("j.mota@netcabo.pt", "Error Report", mailBody);

		//		removes from session all the un-needed info
		HttpSession session = request.getSession();
		session.removeAttribute(SessionConstants.ORIGINAL_MAPPING_KEY);
		session.removeAttribute(Globals.ERROR_KEY);
		session.removeAttribute(SessionConstants.EXCEPTION_STACK_TRACE);

		return originalMapping.getInputForward();
	}
	private String stackTrace2String(StackTraceElement[] stackTrace) {
		String result = "StackTrace: \n ";
		int i=0;
		while (i<stackTrace.length) {
		result +=stackTrace[i]+ "\n";
		i++;
		}
		return result;
	}

}
