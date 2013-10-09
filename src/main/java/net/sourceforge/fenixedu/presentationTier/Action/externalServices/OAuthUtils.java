package net.sourceforge.fenixedu.presentationTier.Action.externalServices;

import static org.apache.commons.lang.StringUtils.capitalize;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Role;
import pt.ist.fenixframework.dml.Slot;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.struts.action.ActionForward;

public class OAuthUtils {

    public static final <T extends DomainObject> T getDomainObject(final String externalId, final Class<T> clazz) {
        try {
            Long.parseLong(externalId);
            final T domainObject = FenixFramework.getDomainObject(externalId);
            // Dirty check to see if domain object still exists due fenix-framework limitations.
            // When using fromExternalId fenix-framework creates a shallow objects with that id.
            // On following requests to object's methods it will throw a VersionNotAvailableException if the object was deleted.
            if (domainObject == null) {
                return null;
            }

            String getterName = null;
            final DomainClass domainClass = FenixFramework.getDomainModel().findClass(clazz.getName());
            if (domainClass != null) {
                final List<Slot> slotsList = domainClass.getSlotsList();
                if (slotsList.isEmpty()) {
                    final List<Role> roleSlots = domainClass.getRoleSlotsList();
                    if (roleSlots.isEmpty()) {
                        return null;
                    }
                    Role role = roleSlots.get(0);
                    if (role.getMultiplicityUpper() != 1) {
                        getterName = String.format("get%sSet", capitalize(role.getName()));
                    } else {
                        getterName = String.format("get%s", capitalize(role.getName()));
                    }
                } else {
                    getterName = String.format("get%s", capitalize(slotsList.get(0).getName()));
                }
            }
            final Method method = clazz.getMethod(getterName, (Class[]) null);

            if (method == null) {
                return null;
            }

            method.invoke(domainObject, (Object[]) null);

            return domainObject;
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
