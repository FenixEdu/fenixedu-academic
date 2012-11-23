package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.DataOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "publico", path = "/files")
public class FileDownload extends FenixAction {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) throws Exception {
	final String oid = request.getParameter("oid");
	final File file = AbstractDomainObject.fromExternalId(oid);
	if (file == null) {
	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
		return sendLoginRedirect(request);
		// redirect
	    } else {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	    }
	}
	return null;
    }

    private ActionForward sendLoginRedirect(final HttpServletRequest request) {
	boolean isCasEnabled = PropertiesManager.getBooleanProperty("cas.enabled");
	final String loginPage;
	if (isCasEnabled) {
	    String port = request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort();
	    String casValue = request.getScheme() + "://" + request.getServerName() + port + request.getContextPath()
		    + request.getRequestURI();
	    String urlSuffix = "?service=" + casValue;
	    loginPage = PropertiesManager.getProperty("cas.loginUrl") + urlSuffix;
	} else {
	    String urlSuffix = "?service=" + request.getRequestURI();
	    loginPage = PropertiesManager.getProperty("login.page") + urlSuffix;
	}
	return new ActionForward(loginPage, true);
    }

}
