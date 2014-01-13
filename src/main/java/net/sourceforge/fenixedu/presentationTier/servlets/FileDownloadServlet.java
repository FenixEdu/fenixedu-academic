package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.servlets.startup.FenixInitializer.FenixCustomExceptionHandler;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.httpclient.HttpStatus;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@WebServlet(urlPatterns = FileDownloadServlet.SERVLET_PATH + "*")
public class FileDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 6954413451468325605L;

    static final String SERVLET_PATH = "/downloadFile/";

    private final FenixCustomExceptionHandler exceptionHandler = new FenixCustomExceptionHandler();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            super.service(request, response);
        } catch (Throwable t) {
            exceptionHandler.handle(request, response, t);
        }
    }

    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        final File file = getFileFromURL(request.getRequestURI());
        if (file == null) {
            sendBadRequest(response);
        } else {
            final Person person = AccessControl.getPerson();
            if (!file.isPrivate() || file.isPersonAllowedToAccess(person)) {
                byte[] content = file.getContent();
                response.setContentType(file.getContentType());
                response.setContentLength(file.getSize().intValue());
                try (OutputStream stream = response.getOutputStream()) {
                    stream.write(content);
                    stream.flush();
                }
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

    private String sendLoginRedirect(final HttpServletRequest request, final File file) {
        final boolean isCasEnabled = CoreConfiguration.casConfig().isCasEnabled();
        if (isCasEnabled) {
            return CoreConfiguration.casConfig().getCasLoginUrl(
                    FenixConfigurationManager.getConfiguration().getFileDownloadUrlLocalContent() + file.getExternalId());
        }
        return FenixConfigurationManager.getConfiguration().getLoginPage() + "?service=" + request.getRequestURI();
    }

    private void sendBadRequest(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(HttpStatus.getStatusText(HttpStatus.SC_BAD_REQUEST));
        response.getWriter().close();
    }

    public final static File getFileFromURL(String url) {
        // Remove trailing path, and split the tokens
        String[] parts = url.substring(url.indexOf(SERVLET_PATH)).replace(SERVLET_PATH, "").split("\\/");
        if (parts.length == 0) {
            return null;
        }
        DomainObject object = FenixFramework.getDomainObject(parts[0]);
        if (object instanceof File) {
            return (File) object;
        } else {
            return null;
        }
    }

}
