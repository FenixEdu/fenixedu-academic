package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail.UserAlreadyHasEmailException;
import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.SetEmail.UserDoesNotExistException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SetEmail extends FenixDispatchAction {

    public ActionForward setEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final String host = HostAccessControl.getRemoteAddress(request);
	final String ip = request.getRemoteAddr();
	final String password = request.getParameter("password");
	final String userUId = request.getParameter("userUId");
	final String email = request.getParameter("email");

	String message = "ko";

	try {
	    final Object[] args = new Object[] { host, ip, password, userUId, email };
	    executeService(request, "SetEmail", args);
	    message = "ok";
	} catch (NotAuthorizedException ex) {
	    message = "Not authorized";
	} catch (UserAlreadyHasEmailException ex) {
	    message = "User already has email.";
	} catch (UserDoesNotExistException ex) {
	    message = "User does not exist.";
	} finally {
	    final ServletOutputStream servletOutputStream = response.getOutputStream();
	    response.setContentType("text/html");
	    servletOutputStream.print(message);
	    servletOutputStream.flush();
	    response.flushBuffer();
	}

	return null;
    }

}
