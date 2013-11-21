package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.httpclient.HttpStatus;

import pt.ist.bennu.core.util.ConfigurationManager;
import pt.ist.fenixframework.FenixFramework;

@WebServlet(urlPatterns = "/downloadFile/*")
public class FileDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 6954413451468325605L;

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {

        final String oid = getFileExternalId(request);
        if (oid == null) {
            sendBadRequest(response);
        } else {
            final File file = FenixFramework.getDomainObject(oid);
            if (file == null) {
                sendBadRequest(response);
            } else {
                final Person person = AccessControl.getPerson();
                if (!file.isPrivate() || file.isPersonAllowedToAccess(person)) {
                    response.setContentType(file.getMimeType());
                    response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
                    //response.addHeader("Content-Disposition", "attachment; filename=" + file.getFilename());
                    response.setContentLength(file.getSize().intValue());
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

    private String getFileExternalId(HttpServletRequest request) {
        Matcher match = Pattern.compile("downloadFile\\/([0-9]+)\\/").matcher(request.getRequestURI());
        if (match.matches()) {
            if (match.groupCount() == 2) {
                return match.group(1);
            }
        }
        return null;
    }

    private String sendLoginRedirect(final HttpServletRequest request, final File file) {
        final boolean isCasEnabled = ConfigurationManager.getBooleanProperty("cas.enabled", false);
        final String loginLinkPropery = isCasEnabled ? "cas.loginUrl" : "login.page";
        final String serviceLink =
                isCasEnabled ? ConfigurationManager.getProperty("file.download.url.local.content") + file.getExternalId() : request
                        .getRequestURI();
        return ConfigurationManager.getProperty(loginLinkPropery) + "?service=" + serviceLink;
    }

    private void sendBadRequest(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
        response.getWriter().close();
    }

}
