package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.externalServices.ConsultRoles.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.util.HostAccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ConsultRoles extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	final String host = HostAccessControl.getRemoteAddress(request);
	final String ip = request.getRemoteAddr();
	final String password = request.getParameter("password");
	final String userUId = request.getParameter("userUId");

	String message = "ko";

	try {
	    final Object[] args = new Object[] { host, ip, password, userUId };
	    final Set<Role> roles = (Set<Role>) executeService(request, "ConsultRoles", args);
	    final StringBuilder stringBuilder = new StringBuilder();
	    if (roles == null) {
		stringBuilder.append("User does not exist");
	    } else {
		stringBuilder.append("ok");
		for (final Role role : roles) {
		    stringBuilder.append('\n');
		    stringBuilder.append(role.getRoleType().getName());
		}
		stringBuilder.append('\n');
	    }
	    message = stringBuilder.toString();
	} catch (NotAuthorizedException ex) {
	    message = "Not authorized";
	} catch (Throwable ex) {
	    message = ex.getMessage();
	    ex.printStackTrace();
	} finally {
	    writeResponse(response, message);
	}

	return null;
    }

    private void writeResponse(final HttpServletResponse response, final String message) throws IOException {
	final ServletOutputStream servletOutputStream = response.getOutputStream();
	response.setContentType("text/html");
	servletOutputStream.print(message);
	servletOutputStream.flush();
	response.flushBuffer();
    }

}
