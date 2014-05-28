/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

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

            if (!FenixFramework.isDomainObjectValid(domainObject) || !clazz.isAssignableFrom(domainObject.getClass())) {
                return null;
            }
            return domainObject;
        } catch (Exception nfe) {
            return null;
        }
    }

    public static OAuthResponse getOAuthProblemResponse(Integer httpStatus, String error, String errorDescription) {
        OAuthProblemException ex = OAuthProblemException.error(error, errorDescription);
        OAuthResponse r = getOAuthResponse(httpStatus, ex);
        return r;
    }

    public static OAuthResponse getOAuthResponse(Integer httpStatus, OAuthProblemException ex) {
        try {
            return OAuthASResponse.errorResponse(httpStatus).error(ex).buildJSONMessage();
        } catch (OAuthSystemException e) {
            throw new WebApplicationException(e);
        }
    }

    public static ActionForward sendOAuthResponse(HttpServletResponse response, OAuthResponse r) {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(r.getResponseStatus());
        PrintWriter pw;
        try {
            pw = response.getWriter();
            pw.print(r.getBody());
            pw.flush();
            pw.close();
            return null;
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }

    }

}
