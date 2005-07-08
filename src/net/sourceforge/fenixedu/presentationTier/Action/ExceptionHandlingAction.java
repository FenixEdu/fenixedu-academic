/*
 * Created on 25/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.EMail;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.RequestUtils;

/**
 * @author João Mota
 */
public class ExceptionHandlingAction extends FenixDispatchAction {

    public ActionForward sendEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        ActionMapping originalMapping = (ActionMapping) session
                .getAttribute(SessionConstants.ORIGINAL_MAPPING_KEY);

        DynaActionForm emailForm = (DynaActionForm) form;
        String formEmail = (String) emailForm.get("email");
        String formSubject = (String) emailForm.get("subject");
        String formBody = (String) emailForm.get("body");

        StackTraceElement[] stackTrace = (StackTraceElement[]) session
                .getAttribute(SessionConstants.EXCEPTION_STACK_TRACE);

        String requestContext = (String) session.getAttribute(SessionConstants.REQUEST_CONTEXT);

        String sender = "Sender: " + formEmail;
        String subject = " - " + formSubject;

        String mailBody = "Error Report\n\n";
        mailBody += sender + "\n\n";
        mailBody += "User Comment: \n" + formBody + "\n\n";
        mailBody += "Error Origin: \n";
        mailBody += "Exception: \n" + session.getAttribute(Globals.EXCEPTION_KEY) + "\n\n";
        mailBody += "RequestContext: \n" + requestContext + "\n\n\n";
        mailBody += "SessionContext: \n" + sessionContextGetter(request) + "\n\n\n";
        mailBody += "Path: " + originalMapping.getPath() + "\n";
        mailBody += "Name: " + originalMapping.getName() + "\n";

        IUserView userView = (IUserView) session.getAttribute("UserView");
        if (userView != null) {
            mailBody += "UserLogedIn: " + userView.getUtilizador() + "\n";
        } else {
            mailBody += "No user logged in.";
        }

        mailBody += stackTrace2String(stackTrace);

        EMail email = null;

        if (!request.getServerName().equals("localhost")) {
            email = new EMail("mail.adm", "erro@dot.ist.utl.pt");
            email.send("suporte@dot.ist.utl.pt", "Fenix Error Report" + subject, mailBody);
        }

        else {
            email = new EMail("mail.rnl.ist.utl.pt", "erro@dot.ist.utl.pt");
            email.send("suporte@dot.ist.utl.pt", "Localhost Error Report", mailBody);
        }

        //		email.send("j.mota@netcabo.pt", subject, mailBody);
        //		email.send("lepc@mega.ist.utl.pt", subject, mailBody);
        //		removes from session all the un-needed info
        sessionRemover(request);

        ActionForward actionForward = originalMapping.getInputForward();
        RequestUtils.selectModule(originalMapping.getModuleConfig().getPrefix(), request, this.servlet
                .getServletContext());

        actionForward.setPath(actionForward.getPath());

        return actionForward;
    }

    public ActionForward goBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        ActionMapping originalMapping = (ActionMapping) session
                .getAttribute(SessionConstants.ORIGINAL_MAPPING_KEY);
        sessionRemover(request);

        ActionForward actionForward = originalMapping.getInputForward();

        RequestUtils.selectModule(originalMapping.getModuleConfig().getPrefix(), request, this.servlet
                .getServletContext());
        if (actionForward.getPath() == null) {
            actionForward.setPath("/");
        }

        return actionForward;
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

}