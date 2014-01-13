package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.struts.action.ActionForward;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class OAuthUtils {

    public static final <T extends DomainObject> T getDomainObject(final String externalId, final Class<T> clazz) {
        try {
            final T domainObject = FenixFramework.getDomainObject(externalId);
            if (FenixFramework.isDomainObjectValid(domainObject)) {
                return domainObject;
            }
            return null;
        } catch (Exception nfe) {
            return null;
        }
    }

    public static OAuthResponse getOAuthProblemResponse(Integer httpStatus, String error, String errorDescription)
            throws OAuthSystemException {
        OAuthProblemException ex = OAuthProblemException.error(error, errorDescription);
        OAuthResponse r = getOAuthResponse(httpStatus, ex);
        return r;
    }

    public static OAuthResponse getOAuthResponse(Integer httpStatus, OAuthProblemException ex) throws OAuthSystemException {
        return OAuthASResponse.errorResponse(httpStatus).error(ex).buildJSONMessage();
    }

    public static ActionForward sendOAuthResponse(HttpServletResponse response, OAuthResponse r) throws IOException {
        response.setStatus(r.getResponseStatus());
        PrintWriter pw = response.getWriter();
        pw.print(r.getBody());
        pw.flush();
        pw.close();
        return null;
    }
}
