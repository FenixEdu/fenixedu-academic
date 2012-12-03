package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.struts.action.ActionServlet;

import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class FileDownloadServlet extends ActionServlet {

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
	    throws IOException, ServletException {
	final String uri = request.getRequestURI();
	final int lastSlash = uri.lastIndexOf('/');
	if (lastSlash < 0 || lastSlash == uri.length() - 1) {
	    sendBadRequest(response);
	} else {
	    final String oid = uri.substring(lastSlash + 1);
	    final File file = AbstractDomainObject.fromExternalId(oid);
	    if (file == null) {
		sendBadRequest(response);
	    } else {
		final Person person = AccessControl.getPerson();
		if (!file.isPrivate() || file.isPersonAllowedToAccess(person)) {
		    response.setContentType(file.getMimeType());
		    response.addHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
		    response.setContentLength(file.getSize());
		    final DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		    dos.write(file.getContents());
		    dos.close();
		} else if (file.isPrivate() && person == null) {
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.sendRedirect(sendLoginRedirect(request, file));
		} else {
		    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		    response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_FORBIDDEN));
		    response.getWriter().close();
		}
	    }
	}
    }

    private String sendLoginRedirect(final HttpServletRequest request, final File file) {
	final boolean isCasEnabled = PropertiesManager.getBooleanProperty("cas.enabled");
	final String loginLinkPropery = isCasEnabled ? "cas.loginUrl" : "login.page";
	final String serviceLink = isCasEnabled ?
		PropertiesManager.getProperty("file.download.url.local.content") + file.getExternalId()
		: request.getRequestURI();
	return PropertiesManager.getProperty(loginLinkPropery) + "?service=" + serviceLink;
    }

    private void sendBadRequest(final HttpServletResponse response) throws IOException {
	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
	response.getWriter().close();
    }

}
